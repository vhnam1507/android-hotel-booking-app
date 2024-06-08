package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.CustomerImageAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Booking;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.User;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class BookingDetailActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private RecyclerView recyclerViewImages;
    private CustomerImageAdapter imageAdapter;
    private TextView txtv_guestName, txtv_guestEmail, txtv_guestPhone,
            txtv_hotelName, txtv_hotelAddress, txtv_hotelProvince,
            txtv_bookingStatus, txtv_bookingNumRoom, txtv_bookingNumAdult, txtv_bookingNumKid,
            txtv_bookingCheckin, txtv_bookingCheckout, txtv_price, txtv_totalPrice,
            txtv_hotelOwnerName, txtv_hotelOwnerEmail, txtv_hotelOwnerPhone;
    private Button btn_confirmBooking, btn_cancelBooking, btn_completeBooking;
    private CheckBox checkBoxPool, checkBoxWifi, checkBoxTV, checkBox24;
    private Hotel selectedHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_booking_detail);

        txtv_guestName = findViewById(R.id.txtv_guestName);
        txtv_guestEmail = findViewById(R.id.txtv_guestEmail);
        txtv_guestPhone = findViewById(R.id.txtv_guestPhone);
        txtv_hotelName = findViewById(R.id.txtv_hotelName);
        txtv_hotelAddress = findViewById(R.id.txtv_hotelAddress);
        txtv_hotelProvince = findViewById(R.id.txtv_hotelProvince);
        txtv_bookingStatus = findViewById(R.id.txtv_bookingStatus);
        txtv_bookingNumRoom = findViewById(R.id.txtv_bookingNumRoom);
        txtv_bookingNumAdult = findViewById(R.id.txtv_bookingNumAdult);
        txtv_bookingNumKid = findViewById(R.id.txtv_bookingNumKid);
        txtv_bookingCheckin = findViewById(R.id.txtv_bookingCheckin);
        txtv_bookingCheckout = findViewById(R.id.txtv_bookingCheckout);
        txtv_price = findViewById(R.id.txtv_price);
        txtv_totalPrice = findViewById(R.id.txtv_totalPrice);
        txtv_hotelOwnerName = findViewById(R.id.txtv_hotelOwnerName);
        txtv_hotelOwnerEmail = findViewById(R.id.txtv_hotelOwnerEmail);
        txtv_hotelOwnerPhone = findViewById(R.id.txtv_hotelOwnerPhone);
        checkBoxPool = findViewById(R.id.checkBoxeditPool);
        checkBoxWifi = findViewById(R.id.checkBoxeditWifi);
        checkBoxTV = findViewById(R.id.checkBoxeditTV);
        checkBox24 = findViewById(R.id.checkBoxedit24);
        btn_confirmBooking = findViewById(R.id.btn_confirmBooking);
        btn_cancelBooking = findViewById(R.id.btn_cancelBooking);
        btn_completeBooking = findViewById(R.id.btn_completeBooking);
        recyclerViewImages = findViewById(R.id.recyclerView_editImages);

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
        String bookingId = intent.getStringExtra("bookingId");
        final String[] status = new String[1]; // Khai báo biến status là final mảng String

        firebaseHelper = new FirebaseHelper();
        firebaseHelper.getBookingById(bookingId, new FirebaseHelper.BookingCallback() {
            @Override
            public void onSuccess(Booking booking) {
                String numRoomFormat = String.valueOf(booking.getNumberOfRooms()) + " phòng";
                String numAdultFormat = String.valueOf(booking.getNumAdult()) + " người lớn";
                String numKidFormat = String.valueOf(booking.getNumKid()) + " trẻ em";
                String totalPriceFormat = formatCurrency(booking.getTotalPrice());

                txtv_bookingNumRoom.setText(numRoomFormat);
                txtv_bookingNumAdult.setText(numAdultFormat);
                txtv_bookingNumKid.setText(numKidFormat);
                txtv_bookingCheckin.setText(booking.getCheckInDate());
                txtv_bookingCheckout.setText(booking.getCheckOutDate());
                txtv_totalPrice.setText(totalPriceFormat);

                String status = booking.getStatus(); // Lấy giá trị trạng thái từ booking

                if (Objects.equals(status, "requestConfirm")){
                    txtv_bookingStatus.setText("Đang được xem xét");
                    txtv_bookingStatus.setTextColor(getResources().getColor(R.color.light_blue));
                    btn_confirmBooking.setVisibility(View.VISIBLE);
                    btn_completeBooking.setVisibility(View.GONE);
                    btn_cancelBooking.setVisibility(View.VISIBLE);
                } else if (Objects.equals(status, "confirmed")){
                    txtv_bookingStatus.setText("Đang hoạt động");
                    btn_confirmBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.VISIBLE);
                    btn_cancelBooking.setVisibility(View.GONE);
                } else if (Objects.equals(status, "requestComplete")){
                    txtv_bookingStatus.setText("Yêu cầu hoàn tất");
                    txtv_bookingStatus.setTextColor(getResources().getColor(R.color.lavender));
                    btn_confirmBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.VISIBLE);
                    btn_cancelBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.VISIBLE);
                } else if (Objects.equals(status, "completed")){
                    txtv_bookingStatus.setText("Đã hoàn thành");
                    txtv_bookingStatus.setTextColor(getResources().getColor(R.color.red));
                    btn_confirmBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.GONE);
                    btn_cancelBooking.setVisibility(View.GONE);
                } else if (Objects.equals(status, "requestCancel")) {
                    txtv_bookingStatus.setText("Yêu cầu huỷ");
                    txtv_bookingStatus.setTextColor(getResources().getColor(R.color.yellow));
                    btn_confirmBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.GONE);
                    btn_cancelBooking.setVisibility(View.VISIBLE);
                } else if (Objects.equals(status, "canceled")) {
                    txtv_bookingStatus.setText("Đã huỷ");
                    txtv_bookingStatus.setTextColor(getResources().getColor(R.color.red));
                    btn_confirmBooking.setVisibility(View.GONE);
                    btn_completeBooking.setVisibility(View.GONE);
                    btn_cancelBooking.setVisibility(View.GONE);
                }

                firebaseHelper.getUserById(booking.getUserId(), new FirebaseHelper.UserGetCallback(){
                    public void onCallback(User user) {
                        if (user != null) {
                            String guestNameFormat = "Họ tên: " + String.valueOf(user.getFullName());
                            String guestEmailFormat = "Email: " + String.valueOf(user.getEmail());
                            String guestPhoneFormat = "Số điện thoại: " + String.valueOf(user.getPhone());

                            txtv_guestName.setText(guestNameFormat);
                            txtv_guestEmail.setText(guestEmailFormat);
                            txtv_guestPhone.setText(guestPhoneFormat);
                            Log.d("OwnerInfo", "Lấy owner info thành công!");

                        } else {
                            Toast.makeText(BookingDetailActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetBooking", "Lỗi: ", e);
            }
        });

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        String priceFormat = formatCurrency(selectedHotel.getPrice());

        txtv_hotelOwnerName.setText(currentUserManager.getUserFullName());
        txtv_hotelOwnerEmail.setText(currentUserManager.getUserEmail());
        txtv_hotelOwnerPhone.setText(currentUserManager.getUserPhone());
        txtv_hotelName.setText(selectedHotel.getName());
        txtv_hotelAddress.setText(selectedHotel.getAddress());
        txtv_price.setText(priceFormat);

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

        ArrayList<String> imageUrlList = new ArrayList<>(Arrays.asList(selectedHotel.getImageUrls().split(",")));

        setAmenities(selectedHotel.getAmenities());
        setupRecyclerView();
        loadImages(imageUrlList);

        btn_cancelBooking.setOnClickListener(v -> {
            firebaseHelper.updateBookingStatus(bookingId, "canceled", new FirebaseHelper.BookingUpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(BookingDetailActivity.this, "Đã xác nhận yêu cầu hủy booking", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(BookingDetailActivity.this, "Gặp lỗi khi xác nhận yêu cầu hủy booking", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_completeBooking.setOnClickListener(v -> {
            firebaseHelper.updateBookingStatus(bookingId, "requestComplete", new FirebaseHelper.BookingUpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(BookingDetailActivity.this, "Yêu cầu hoàn thành booking đã được gửi đi", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(BookingDetailActivity.this, "Gặp lỗi khi yêu cầu hoàn thành booking", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_confirmBooking.setOnClickListener(v -> {
            firebaseHelper.updateBookingStatus(bookingId, "confirmed", new FirebaseHelper.BookingUpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(BookingDetailActivity.this, "Đã xác nhận booking", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(BookingDetailActivity.this, "Gặp lỗi khi xác nhận booking", Toast.LENGTH_SHORT).show();
                }
            });
        });

        txtv_guestPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số điện thoại từ TextView
                String phoneNumber = txtv_guestPhone.getText().toString();
                phoneNumber = phoneNumber.substring(15);

                // Tạo Intent để mở ứng dụng gọi điện
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        txtv_hotelOwnerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số điện thoại từ TextView
                String phoneNumber = txtv_hotelOwnerPhone.getText().toString();
                phoneNumber = phoneNumber.substring(15);

                // Tạo Intent để mở ứng dụng gọi điện
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
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
}