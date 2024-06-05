package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ImageAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class HotelPostingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerViewImages;
    private ImageAdapter imageAdapter;
    private EditText editTextHotelName, editTextHotelAddress, editTextPrice;
    private Spinner spinnerProv, spinnerNumRooms, spinnerMaxGuests;
    private Button btnSubmitHotel, buttonAddImage;
    private CheckBox checkBoxPool, checkBoxWifi, checkBoxTV, checkBox24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_hotel_posting);

        editTextHotelName = findViewById(R.id.etxt_hotelName);
        editTextHotelAddress = findViewById(R.id.etxt_hotelAddress);
        spinnerProv = findViewById(R.id.spinner_Prov);
        spinnerNumRooms = findViewById(R.id.spinnerNumRooms);
        spinnerMaxGuests = findViewById(R.id.spinnerMaxGuests);
        checkBoxPool = findViewById(R.id.checkBoxPool);
        checkBoxWifi = findViewById(R.id.checkBoxWifi);
        checkBoxTV = findViewById(R.id.checkBoxTV);
        checkBox24 = findViewById(R.id.checkBox24);
        btnSubmitHotel = findViewById(R.id.btn_submitHotel);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        buttonAddImage = findViewById(R.id.btn_imageSelecting);
        editTextPrice = findViewById(R.id.etxt_price);

        btnSubmitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHotelInfo();
            }
        });

        setupRecyclerView();
        loadProvinces();
        setupButtonListeners();
    }

    private void loadProvinces() {
        // Lấy danh sách các tỉnh từ cơ sở dữ liệu
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.getAllProvinceNames(new FirebaseHelper.AllProvinceNamesCallback() {
            @Override
            public void onCallback(List<String> provinceNames) {
                // Log dữ liệu trước khi load vào Spinner
                Log.d("ProvinceNames", "Before loading into Spinner: " + provinceNames.toString());

                // Tạo ArrayAdapter và gán cho Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(HotelPostingActivity.this, android.R.layout.simple_spinner_item, provinceNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProv.setAdapter(adapter);

                // Log dữ liệu sau khi load vào Spinner
                if (!provinceNames.isEmpty()) {
                    Log.d("ProvinceNames", "After loading into Spinner: " + provinceNames.get(0));
                } else {
                    Log.d("ProvinceNames", "No province loaded into Spinner");
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi khi lấy danh sách tỉnh
                Log.e("ProvinceNames", "Error loading province names: " + e.getMessage());
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(this, new ArrayList<>());
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void setupButtonListeners() {
        buttonAddImage.setOnClickListener(v -> {
            requestStoragePermission();
        });

        btnSubmitHotel.setOnClickListener(v -> {
            submitHotelInfo();
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        if (checkBoxPool.isChecked()) {
            amenities.append("Pool,");
        }
        if (checkBoxWifi.isChecked()) {
            amenities.append("Wifi,");
        }
        if (checkBoxTV.isChecked()) {
            amenities.append("TV,");
        }
        if (checkBox24.isChecked()) {
            amenities.append("24/7,");
        }
        // Add more amenities checkboxes here as needed
        return amenities.toString();
    }

    private void submitHotelInfo() {
        String hotelName = editTextHotelName.getText().toString().trim();
        String address = editTextHotelAddress.getText().toString().trim();
        int provincePosition = spinnerProv.getSelectedItemPosition() + 1;
        String provinceID = "province" + String.valueOf(provincePosition);
        String amenities = getAmenities();
        int numberOfRooms = Integer.parseInt(spinnerNumRooms.getSelectedItem().toString());
        int maxGuestsPerRoom = Integer.parseInt(spinnerMaxGuests.getSelectedItem().toString());
        int price = Integer.parseInt(editTextPrice.getText().toString());

        // Lấy danh sách các URL của hình ảnh từ adapter
        List<String> imageUrls = imageAdapter.getImageUrls();

        // Kiểm tra xem có ảnh nào để tải lên không
        if (imageUrls.isEmpty()) {
            Toast.makeText(this, "Xin hãy chọn ảnh mô tả cho khách sạn của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo một ArrayList để lưu trữ các URL của ảnh trên Firebase Storage
        List<String> firebaseImageUrls = new ArrayList<>();

        // Sử dụng ExecutorService để thực hiện các tác vụ tải ảnh đồng thời
        ExecutorService executorService = Executors.newFixedThreadPool(imageUrls.size());
        CountDownLatch latch = new CountDownLatch(imageUrls.size());

        for (String imageUrl : imageUrls) {
            executorService.submit(() -> {
                try {
                    // Tạo một StorageReference để lưu trữ ảnh trên Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotel_images/" + UUID.randomUUID().toString());

                    // Tạo Uri từ đường dẫn của ảnh
                    Uri imageUri = Uri.parse(imageUrl);

                    // Tải ảnh lên Firebase Storage
                    storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                // Nếu tải lên thành công, lấy URL của ảnh và thêm vào firebaseImageUrls
                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    synchronized (firebaseImageUrls) {
                                        firebaseImageUrls.add(uri.toString());
                                    }
                                    latch.countDown();

                                    // Nếu đã tải lên tất cả các ảnh, tiến hành thêm thông tin khách sạn vào cơ sở dữ liệu
                                    if (latch.getCount() == 0) {
                                        // Chuyển danh sách các URL thành một chuỗi để lưu trữ trong cơ sở dữ liệu
                                        String imageUrlsString = TextUtils.join(",", firebaseImageUrls);

                                        // Gọi hàm addHotel() để thêm thông tin về khách sạn vào cơ sở dữ liệu
                                        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
                                        String ownerId = currentUserManager.getUserId(); // Lấy ID của người dùng hiện tại
                                        firebaseHelper.addHotel(ownerId, hotelName, address, provinceID, amenities, imageUrlsString, numberOfRooms, maxGuestsPerRoom, price, new FirebaseHelper.HotelAddCallback() {
                                            @Override
                                            public void onSuccess(String hotelId) {
                                                // Xử lý khi thêm khách sạn thành công
                                                Log.d("AddHotel", "Khách sạn with ID: " + hotelId + " đã được thêm thành công");
                                                Intent intent = new Intent(HotelPostingActivity.this, HotelListActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                // Xử lý khi có lỗi xảy ra trong quá trình thêm khách sạn
                                                Log.e("AddHotel", "Thêm khách sạn thất bại " + e.getMessage());
                                            }
                                        });
                                    }
                                });
                            })
                            .addOnFailureListener(exception -> {
                                // Xảy ra lỗi trong quá trình tải lên
                                Toast.makeText(this, "Tải ảnh lên hệ thống thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                latch.countDown();
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    latch.countDown();
                }
            });
        }
        // Đóng ExecutorService sau khi hoàn thành
        executorService.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Quyền bị từ chối", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageAdapter.addImage(imageUri.toString());
            imageAdapter.notifyDataSetChanged();
        }
    }
}