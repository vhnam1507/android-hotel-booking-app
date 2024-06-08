package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.CustomerImageAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.HotelAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ReviewAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Review;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.User;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class HotelDetailActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerViewImages;
    private CustomerImageAdapter imageAdapter;
    private ReviewAdapter reviewAdapter;
    private TextView txtv_hotelName, txtv_hotelAddress, txtv_hotelProvince, txtv_hotelNumRoom, txtv_hotelMaxGuest, txtv_price, txtv_hotelOwnerName, txtv_hotelOwnerEmail, txtv_hotelOwnerPhone;
    private Button btn_saveHotel, btn_bookHotel, btn_unsaveHotel;
    private CheckBox checkBoxPool, checkBoxWifi, checkBoxTV, checkBox24;
    private Hotel selectedHotel;
    private ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_hotel_detail);

        txtv_hotelName = findViewById(R.id.txtv_hotelName);
        txtv_hotelAddress = findViewById(R.id.txtv_hotelAddress);
        txtv_hotelProvince = findViewById(R.id.txtv_hotelProvince);
        txtv_hotelNumRoom = findViewById(R.id.txtv_hotelNumRoom);
        txtv_hotelMaxGuest = findViewById(R.id.txtv_hotelMaxGuest);
        txtv_price = findViewById(R.id.txtv_price);
        txtv_hotelOwnerName = findViewById(R.id.txtv_hotelOwnerName);
        txtv_hotelOwnerEmail = findViewById(R.id.txtv_hotelOwnerEmail);
        txtv_hotelOwnerPhone = findViewById(R.id.txtv_hotelOwnerPhone);
        checkBoxPool = findViewById(R.id.checkBoxeditPool);
        checkBoxWifi = findViewById(R.id.checkBoxeditWifi);
        checkBoxTV = findViewById(R.id.checkBoxeditTV);
        checkBox24 = findViewById(R.id.checkBoxedit24);
        btn_saveHotel = findViewById(R.id.btn_saveHotel);
        btn_bookHotel = findViewById(R.id.btn_bookHotel);
        btn_unsaveHotel = findViewById(R.id.btn_unsaveHotel);
        recyclerViewImages = findViewById(R.id.recyclerView_editImages);
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

        firebaseHelper = new FirebaseHelper();
        firebaseHelper.getProvinceNameById(selectedHotel.getProvinceID(), new FirebaseHelper.ProvinceNameCallback() {
            @Override
            public void onCallback(String provinceName) {
                if (provinceName != null) {
                    txtv_hotelProvince.setText(provinceName);
                } else {
                    Log.e("GetProvinceName", "Province not found");
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e("GetProvinceName", "Error fetching data", e);
            }
        });

        String numRoomFormat = String.valueOf(selectedHotel.getNumRooms()) + " phòng";
        String numMaxGuestFormat = String.valueOf(selectedHotel.getNumMaxGuest()) + " người/phòng";
        String priceFormat = formatCurrency(selectedHotel.getPrice());

        txtv_hotelName.setText(selectedHotel.getName());
        txtv_hotelAddress.setText(selectedHotel.getAddress());
        txtv_hotelNumRoom.setText(numRoomFormat);
        txtv_hotelMaxGuest.setText(numMaxGuestFormat);
        txtv_price.setText(priceFormat);

        firebaseHelper.getUserById(selectedHotel.getOwnerId(), new FirebaseHelper.UserGetCallback(){
            public void onCallback(User user) {
                if (user != null) {
                    String hotelOwnerNameFormat = "Họ tên: " + String.valueOf(user.getFullName());
                    String hotelOwnerEmailFormat = "Email: " + String.valueOf(user.getEmail());
                    String hotelOwnerPhoneFormat = "Số điện thoại: " + String.valueOf(user.getPhone());

                    txtv_hotelOwnerName.setText(hotelOwnerNameFormat);
                    txtv_hotelOwnerEmail.setText(hotelOwnerEmailFormat);
                    txtv_hotelOwnerPhone.setText(hotelOwnerPhoneFormat);
                    Log.d("OwnerInfo", "Lấy owner info thành công!");

                } else {
                    Toast.makeText(HotelDetailActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
            }
        });

        ArrayList<String> imageUrlList = new ArrayList<>(Arrays.asList(selectedHotel.getImageUrls().split(",")));

        setAmenities(selectedHotel.getAmenities());
        setupRecyclerView();
        loadImages(imageUrlList);
        //TODO: CHECK VAR
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, R.layout.item_review, reviews);
        lvw_reviews.setAdapter(reviewAdapter);
        loadReviews(selectedHotel.getId());

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        String userId = currentUserManager.getUserId();

        firebaseHelper.checkIfHotelIsSaved(userId, selectedHotel.getId(), new FirebaseHelper.HotelSavedCheckCallback() {
            @Override
            public void onCheckCompleted(boolean isSaved) {
                if (isSaved) {
                    btn_saveHotel.setVisibility(View.GONE);
                    btn_unsaveHotel.setVisibility(View.VISIBLE);
                } else {
                    btn_saveHotel.setVisibility(View.VISIBLE);
                    btn_unsaveHotel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(HotelDetailActivity.this, "Lỗi khi kiểm tra lưu khách sạn", Toast.LENGTH_SHORT).show();
            }
        });

        btn_saveHotel.setOnClickListener(v -> {
            firebaseHelper.addSaved(userId, selectedHotel.getId(), new FirebaseHelper.SavedAddCallback() {
                @Override
                public void onSuccess(String savedId) {
                    // Hiển thị thông báo thành công
                    runOnUiThread(() -> {
                        Toast.makeText(HotelDetailActivity.this, "Lưu khách sạn thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(Exception e) {
                    // Hiển thị thông báo lỗi
                    runOnUiThread(() -> {
                        Toast.makeText(HotelDetailActivity.this, "Lưu khách sạn thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onAlreadyExists() {
                    // Hiển thị thông báo đã tồn tại
                    runOnUiThread(() -> {
                        Toast.makeText(HotelDetailActivity.this, "Bạn đã lưu khách sạn này rồi!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        btn_unsaveHotel.setOnClickListener(v -> {
            firebaseHelper.removeSaved(userId, selectedHotel.getId(), new FirebaseHelper.SavedRemoveCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(HotelDetailActivity.this, "Bỏ lưu khách sạn thành công!", Toast.LENGTH_SHORT).show();
                        btn_saveHotel.setVisibility(View.VISIBLE);
                        btn_unsaveHotel.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(HotelDetailActivity.this, "Bỏ lưu khách sạn thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        btn_bookHotel.setOnClickListener(v -> {
            Intent nextIntent = new Intent(HotelDetailActivity.this, DateSelectionActivity.class);
            nextIntent.putExtra("hotelId", selectedHotel.getId());
            nextIntent.putExtra("ownerId", selectedHotel.getOwnerId());
            nextIntent.putExtra("hotelName", selectedHotel.getName());
            nextIntent.putExtra("hotelAddress", selectedHotel.getAddress());
            nextIntent.putExtra("hotelProvinceID", selectedHotel.getProvinceID());
            nextIntent.putExtra("hotelAmenities", selectedHotel.getAmenities());
            nextIntent.putExtra("hotelImageUrls", selectedHotel.getImageUrls());
            nextIntent.putExtra("hotelNumRooms", selectedHotel.getNumRooms());
            nextIntent.putExtra("hotelNumMaxGuest", selectedHotel.getNumMaxGuest());
            nextIntent.putExtra("hotelPrice", selectedHotel.getPrice());
            startActivity(nextIntent);
        });

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

    private void setupRecyclerView() {
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new CustomerImageAdapter(this, new ArrayList<>());
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void loadImages(ArrayList<String> imageUrls) {
        // Tạo một adapter mới nếu cần
        if (imageAdapter == null) {
            imageAdapter = new CustomerImageAdapter(this, imageUrls);
            recyclerViewImages.setAdapter(imageAdapter);
        } else {
            // Cập nhật danh sách URL ảnh trong adapter
            imageAdapter.setImageUrls(imageUrls);
            // Thông báo cho adapter biết dữ liệu đã thay đổi
            imageAdapter.notifyDataSetChanged();
        }
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
}