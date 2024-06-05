package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Review;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class ReviewPostingActivity extends AppCompatActivity {
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_review_posting);

        firebaseHelper = new FirebaseHelper();

        Intent intent = getIntent();
        String hotelId = intent.getStringExtra("hotelId");
        String bookingId = intent.getStringExtra("bookingId");

        EditText etxt_detail = findViewById(R.id.etxt_detail);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView txtv_mail = findViewById(R.id.txtv_mail);
        ImageView iv_avatar = findViewById(R.id.iv_avatar);
        Button btn_reviewBooking = findViewById(R.id.btn_reviewBooking);
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        txtv_mail.setText(currentUserManager.getUserEmail());

        Picasso.get()
                .load(Uri.parse(currentUserManager.getAvatarUrl()))
                .placeholder(R.drawable.app) // Placeholder image
                .error(R.drawable.app) // Error image
                .into(iv_avatar);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());



        btn_reviewBooking.setOnClickListener(v -> {
            String detail = String.valueOf(etxt_detail.getText());
            int rate = (int) ratingBar.getRating();

            Review review = new Review(null, hotelId, currentUserManager.getAvatarUrl(), currentUserManager.getUserEmail(),  bookingId, detail, date, rate);
            firebaseHelper.addReview(review, new FirebaseHelper.ReviewAddCallback() {
                @Override
                public void onSuccess() {
                    Log.d("HotelDetailActivity", "Review đã được thêm thành công");
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("HotelDetailActivity", "Lỗi khi thêm review", e);
                }
            });
        });


    }
}