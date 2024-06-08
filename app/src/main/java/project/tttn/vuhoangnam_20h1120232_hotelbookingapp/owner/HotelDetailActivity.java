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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ImageAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ReviewAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Review;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class HotelDetailActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerViewImages;
    private ImageAdapter imageAdapter;
    private EditText editTextHotelName, editTextHotelAddress, editTextPrice;
    private Spinner spinnerProv, spinnerNumRooms, spinnerMaxGuests;
    private Button btnEditHotel, btnAddImage, btnRemoveHotel;
    private CheckBox checkBoxPool, checkBoxWifi, checkBoxTV, checkBox24;
    private Hotel selectedHotel;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Review> reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_hotel_detail);

        editTextHotelName = findViewById(R.id.etxt_editHotelName);
        editTextHotelAddress = findViewById(R.id.etxt_editHotelAddress);
        spinnerProv = findViewById(R.id.spinner_editProv);
        spinnerNumRooms = findViewById(R.id.spinner_editNumRooms);
        spinnerMaxGuests = findViewById(R.id.spinner_editMaxGuests);
        checkBoxPool = findViewById(R.id.checkBoxeditPool);
        checkBoxWifi = findViewById(R.id.checkBoxeditWifi);
        checkBoxTV = findViewById(R.id.checkBoxeditTV);
        checkBox24 = findViewById(R.id.checkBoxedit24);
        btnEditHotel = findViewById(R.id.btn_editHotel);
        btnRemoveHotel = findViewById(R.id.btn_deleteHotel);
        recyclerViewImages = findViewById(R.id.recyclerView_editImages);
        btnAddImage = findViewById(R.id.btn_editImageSelecting);
        editTextPrice = findViewById(R.id.etxt_editPrice);
        ListView lvw_reviews = findViewById(R.id.lvw_reviews);

        Intent intent = getIntent();

        selectedHotel = new Hotel(
                intent.getStringExtra("hotelId"),
                intent.getStringExtra("ownerId"),
                intent.getStringExtra("hotelName"),
                intent.getStringExtra("hotelAddress"),
                intent.getStringExtra("hotelProvinceID"),
                intent.getStringExtra("hotelAmenities"),
                intent.getStringExtra("hotelImageUrls"),
                intent.getIntExtra("hotelNumRooms", 0),
                intent.getIntExtra("hotelNumMaxGuest", 0),
                intent.getIntExtra("hotelPrice", 0),
                intent.getIntExtra("hotelNumReviews", 0),
                intent.getIntExtra("hotelRate", 0)
        );

        int posit = Integer.parseInt(selectedHotel.getProvinceID().substring("province".length()));
        ArrayList<String> imageUrlList = new ArrayList<>(Arrays.asList(selectedHotel.getImageUrls().split(",")));

        setupButtonListeners();
        setupRecyclerView();
        loadImages(imageUrlList);
        loadProvinces(posit -1);
        spinnerNumRooms.setSelection(selectedHotel.getNumRooms() -1);
        spinnerMaxGuests.setSelection(selectedHotel.getNumMaxGuest() -1);
        setAmenities(selectedHotel.getAmenities());
        editTextHotelName.setText(selectedHotel.getName());
        editTextHotelAddress.setText(selectedHotel.getAddress());
        String priceFormat = formatCurrency(selectedHotel.getPrice());
        editTextPrice.setText(priceFormat);

        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, R.layout.item_review, reviews);
        lvw_reviews.setAdapter(reviewAdapter);
        loadReviews(selectedHotel.getId());

    }

    private void loadReviews(String hotelId){
        firebaseHelper.getReviewsByHotelId(hotelId, new FirebaseHelper.ReviewCallback() {
            @Override
            public void onSuccess(List<Review> reviewList) {
                reviews.clear();
                reviews.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("HotelDetailActivity", "Lỗi khi lấy reviews", e);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(this, new ArrayList<>());
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void loadImages(ArrayList<String> imageUrls) {
        // Tạo một adapter mới nếu cần
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(this, imageUrls);
            recyclerViewImages.setAdapter(imageAdapter);
        } else {
            // Cập nhật danh sách URL ảnh trong adapter
            imageAdapter.setImageUrls(imageUrls);
            // Thông báo cho adapter biết dữ liệu đã thay đổi
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void loadProvinces(final int pos) {
        // Lấy danh sách các tỉnh từ cơ sở dữ liệu
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.getAllProvinceNames(new FirebaseHelper.AllProvinceNamesCallback() {
            @Override
            public void onCallback(List<String> provinceNames) {
                // Log dữ liệu trước khi load vào Spinner
                Log.d("ProvinceNames", "Before loading into Spinner: " + provinceNames.toString());

                // Tạo ArrayAdapter và gán cho Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(HotelDetailActivity.this, android.R.layout.simple_spinner_item, provinceNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProv.setAdapter(adapter);

                // Thiết lập Spinner ở vị trí pos
                if (pos >= 0 && pos < provinceNames.size()) {
                    spinnerProv.setSelection(pos);
                } else {
                    Log.w("ProvinceNames", "Position out of range: " + pos);
                }

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

    private void setupButtonListeners() {
        btnAddImage.setOnClickListener(v -> {
            requestStoragePermission();
        });

        btnEditHotel.setOnClickListener(v -> {
            updateHotelInfo();
        });

        btnRemoveHotel.setOnClickListener(v -> {
            removeSelectedHotel();
        });
    }



    private void updateHotelInfo() {
        String hotelId = selectedHotel.getId();
        String hotelName = editTextHotelName.getText().toString().trim();
        String address = editTextHotelAddress.getText().toString().trim();
        int provincePosition = spinnerProv.getSelectedItemPosition() + 1;
        String provinceID = "province" + provincePosition;
        String amenities = getAmenities();
        int numberOfRooms = Integer.parseInt(spinnerNumRooms.getSelectedItem().toString());
        int maxGuestsPerRoom = Integer.parseInt(spinnerMaxGuests.getSelectedItem().toString());
        int price = parsePriceFromEditText(editTextPrice);

        // Lấy danh sách các URL của hình ảnh từ adapter
        List<String> tempUrls = imageAdapter.getImageUrls(); // Lấy danh sách tạm thời từ adapter
        List<String> newUrls = new ArrayList<>(); // Danh sách các URL ảnh mới

        // Kiểm tra và tách các ảnh mới (các ảnh chưa có URL)
        for (String imageUrl : tempUrls) {
            if (imageUrl.startsWith("file://")) {
                newUrls.add(imageUrl);
            }
        }

        // Loại bỏ các URL ảnh mới khỏi tempUrls để chỉ còn lại các URL cũ
        tempUrls.removeAll(newUrls);

        // Tạo một ArrayList để lưu trữ các URL của ảnh trên Firebase Storage
        List<String> finalImageUrls = new ArrayList<>(tempUrls);

        if (!newUrls.isEmpty()) {
            // Sử dụng ExecutorService để thực hiện các tác vụ tải ảnh đồng thời
            ExecutorService executorService = Executors.newFixedThreadPool(newUrls.size());
            CountDownLatch latch = new CountDownLatch(newUrls.size());

            for (String newImageUrl : newUrls) {
                executorService.submit(() -> {
                    try {
                        // Tạo một StorageReference để lưu trữ ảnh trên Firebase Storage
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotel_images/" + UUID.randomUUID().toString());

                        // Tạo Uri từ đường dẫn của ảnh
                        Uri imageUri = Uri.parse(newImageUrl);

                        // Tải ảnh lên Firebase Storage
                        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                            // Nếu tải lên thành công, lấy URL của ảnh và thêm vào firebaseImageUrls
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                synchronized (finalImageUrls) {
                                    finalImageUrls.add(uri.toString());
                                }
                                latch.countDown();

                                // Nếu đã tải lên tất cả các ảnh, tiến hành cập nhật thông tin khách sạn
                                if (latch.getCount() == 0) {
                                    updateHotelInfoInDatabase(hotelId, hotelName, address, provinceID, amenities, finalImageUrls, numberOfRooms, maxGuestsPerRoom, price);
                                }
                            });
                        }).addOnFailureListener(exception -> {
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
        } else {
            // Nếu không có ảnh mới để tải lên, trực tiếp cập nhật thông tin khách sạn
            updateHotelInfoInDatabase(hotelId, hotelName, address, provinceID, amenities, finalImageUrls, numberOfRooms, maxGuestsPerRoom, price);
        }
    }

    private void updateHotelInfoInDatabase(String hotelId, String hotelName, String address, String provinceID, String amenities, List<String> finalImageUrls, int numberOfRooms, int maxGuestsPerRoom, int price) {
        String imageUrlsString = TextUtils.join(",", finalImageUrls);
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        String ownerId = currentUserManager.getUserId(); // Lấy ID của người dùng hiện tại

        // Gọi hàm updateHotel() để cập nhật thông tin về khách sạn trong cơ sở dữ liệu
        firebaseHelper.updateHotel(hotelId, ownerId, hotelName, address, provinceID, amenities, imageUrlsString, numberOfRooms, maxGuestsPerRoom, price, new FirebaseHelper.HotelUpdateCallback() {
            @Override
            public void onSuccess() {
                // Xử lý khi cập nhật thông tin khách sạn thành công
                Log.d("UpdateHotel", "Thông tin của khách sạn với ID: " + hotelId + " đã được cập nhật thành công");
                Intent intent = new Intent(HotelDetailActivity.this, HotelListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra trong quá trình cập nhật thông tin khách sạn
                Log.e("UpdateHotel", "Cập nhật thông tin khách sạn thất bại " + e.getMessage());
            }
        });
    }



    private void removeSelectedHotel() {
        if (selectedHotel != null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.removeHotel(selectedHotel.getId(), new FirebaseHelper.HotelRemoveCallback() {
                @Override
                public void onSuccess() {
                    // Xoá thành công, bạn có thể thực hiện các hành động khác ở đây, ví dụ: cập nhật giao diện người dùng, thông báo thành công, vv.
                    Toast.makeText(HotelDetailActivity.this, "Khách sạn đã được xoá thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HotelDetailActivity.this, HotelListActivity.class);
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    // Xảy ra lỗi khi xoá khách sạn, bạn có thể xử lý lỗi ở đây, ví dụ: hiển thị thông báo lỗi cho người dùng, ghi log lỗi, vv.
                    Log.e("RemoveHotel", "Xảy ra lỗi khi xoá khách sạn: " + e.getMessage());
                    Toast.makeText(HotelDetailActivity.this, "Xảy ra lỗi khi xoá khách sạn", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Không có khách sạn được chọn, bạn có thể thông báo cho người dùng hoặc thực hiện các hành động khác ở đây
            Toast.makeText(HotelDetailActivity.this, "Không có khách sạn được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private int parsePriceFromEditText(EditText editTextPrice) {
        String priceText = editTextPrice.getText().toString();
        // Loại bỏ các ký tự không phải số
        String cleanPriceText = priceText.replaceAll("[^\\d]", "");
        // Chuyển đổi chuỗi thành số nguyên
        return Integer.parseInt(cleanPriceText);
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

    private void setAmenities(String amenities) {
        // Tách chuỗi tiện ích thành một mảng các tiện ích riêng lẻ
        String[] amenitiesArray = amenities.split(",");

        // Đặt trạng thái của các CheckBox dựa trên các tiện ích đã tách được
        checkBoxPool.setChecked(false);
        checkBoxWifi.setChecked(false);
        checkBoxTV.setChecked(false);
        checkBox24.setChecked(false);

        for (String amenity : amenitiesArray) {
            switch (amenity.trim().toLowerCase()) {
                case "pool":
                    checkBoxPool.setChecked(true);
                    break;
                case "wifi":
                    checkBoxWifi.setChecked(true);
                    break;
                case "tv":
                    checkBoxTV.setChecked(true);
                    break;
                case "24/7":
                    checkBox24.setChecked(true);
                    break;
            }
        }
    }

    public static String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount) + "đ";
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