package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView iv_avatar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_home);

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        Button btn_editProfile = findViewById(R.id.btn_editProfile);
        ImageButton btn_logout = findViewById(R.id.btn_ownerlogout);
        CardView hotelListCard = findViewById(R.id.hotelListCard);
        CardView bookingListCard = findViewById(R.id.bookingListCard);
        TextView txtv_ownerFullName = findViewById(R.id.txtv_ownerFullName);
        TextView txtv_ownerEmail = findViewById(R.id.txtv_ownerEmail);
        iv_avatar = findViewById(R.id.iv_avatar);

        String avatarUrl = currentUserManager.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get()
                    .load(Uri.parse(avatarUrl))
                    .placeholder(R.drawable.app) // Placeholder image
                    .error(R.drawable.app) // Error image
                    .into(iv_avatar);
        } else {
            Picasso.get()
                    .load(R.drawable.app) // Default image
                    .into(iv_avatar);
        }

        txtv_ownerFullName.setText(currentUserManager.getUserFullName());
        txtv_ownerEmail.setText(currentUserManager.getUserEmail());

        // Set click listener for hotelListCard
        hotelListCard.setOnClickListener(v -> {
            // Handle click event for hotelListCard
            Intent intent = new Intent(HomeActivity.this, HotelListActivity.class);
            startActivity(intent);
        });

        bookingListCard.setOnClickListener(v -> {
            // Handle click event for bookingListCard
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            startActivity(intent);
        });

        btn_editProfile.setOnClickListener(v -> {
            // Handle click event for btn_editProfile
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(v -> {
            // Handle click event for btn_logout
            SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Xóa toàn bộ dữ liệu trong SharedPreferences
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}