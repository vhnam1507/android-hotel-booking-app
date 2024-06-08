package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;

public class DateSelectionActivity extends AppCompatActivity {

    private static final int MIN_NIGHTS = 1;
    private static final int MAX_NIGHTS = 14;

    private int nightCount = 1;

    private TextView txtv_dayNum;
    private Button btn_dateInc;
    private Button btn_dateDec;
    private Button btn_done;
    private Hotel selectedHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_date_selection);

        btn_dateInc = findViewById(R.id.btn_dateInc);
        btn_dateDec = findViewById(R.id.btn_dateDec);
        btn_done = findViewById(R.id.btn_done);
        txtv_dayNum = findViewById(R.id.txtv_dayNum);

        updateNightCountDisplay();

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

        btn_dateInc.setOnClickListener(v -> increaseNightCount());
        btn_dateDec.setOnClickListener(v -> decreaseNightCount());
        btn_done.setOnClickListener(v -> openBookingDetailActivity());
    }

    private void updateNightCountDisplay() {
        txtv_dayNum.setText(String.valueOf(nightCount));
        btn_dateDec.setEnabled(nightCount > MIN_NIGHTS);
        btn_dateInc.setEnabled(nightCount < MAX_NIGHTS);
    }

    private void increaseNightCount() {
        if (nightCount < MAX_NIGHTS) {
            nightCount++;
            updateNightCountDisplay();
        }
    }

    private void decreaseNightCount() {
        if (nightCount > MIN_NIGHTS) {
            nightCount--;
            updateNightCountDisplay();
        }
    }

    private void openBookingDetailActivity() {

        int dayNum = Integer.parseInt(txtv_dayNum.getText().toString());
        Intent intent = new Intent(this, CreateBookingActivity.class);
        intent.putExtra("hotelId", selectedHotel.getId());
        intent.putExtra("ownerId", selectedHotel.getOwnerId());
        intent.putExtra("hotelName", selectedHotel.getName());
        intent.putExtra("hotelAddress", selectedHotel.getAddress());
        intent.putExtra("hotelProvinceID", selectedHotel.getProvinceID());
        intent.putExtra("hotelAmenities", selectedHotel.getAmenities());
        intent.putExtra("hotelImageUrls", selectedHotel.getImageUrls());
        intent.putExtra("hotelNumRooms", selectedHotel.getNumRooms());
        intent.putExtra("hotelNumMaxGuest", selectedHotel.getNumMaxGuest());
        intent.putExtra("hotelPrice", selectedHotel.getPrice());
        intent.putExtra("dayNum", dayNum);
        startActivity(intent);
    }
}
