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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private FirebaseHelper firebaseHelper;
    private EditText etxtDetail;
    private RatingBar ratingBar;
    private TextView txtvMail;
    private ImageView ivAvatar;
    private Button btnReviewBooking;
    private CurrentUserManager currentUserManager;
    private String hotelId, bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_review_posting);

        initializeViews();
        initializeFirebaseHelper();
        populateUserDetails();
        setReviewButtonListener();
    }

    private void initializeViews() {
        etxtDetail = findViewById(R.id.etxt_detail);
        ratingBar = findViewById(R.id.ratingBar);
        txtvMail = findViewById(R.id.txtv_mail);
        ivAvatar = findViewById(R.id.iv_avatar);
        btnReviewBooking = findViewById(R.id.btn_reviewBooking);
    }

    private void initializeFirebaseHelper() {
        firebaseHelper = new FirebaseHelper();
    }

    private void populateUserDetails() {
        currentUserManager = CurrentUserManager.getInstance();
        txtvMail.setText(currentUserManager.getUserEmail());

        Picasso.get()
                .load(Uri.parse(currentUserManager.getAvatarUrl()))
                .placeholder(R.drawable.app) // Placeholder image
                .error(R.drawable.app) // Error image
                .into(ivAvatar);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");
        bookingId = intent.getStringExtra("bookingId");
    }

    private void setReviewButtonListener() {
        btnReviewBooking.setOnClickListener(v -> {
            String detail = etxtDetail.getText().toString();
            int rate = (int) ratingBar.getRating();

            if (rate == 0) {
                Toast.makeText(this, "Xin hãy đánh giá sao trước khi tạo đánh giá.", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            String date = dateFormat.format(calendar.getTime());

            Review review = new Review(null, hotelId, currentUserManager.getAvatarUrl(), currentUserManager.getUserEmail(), bookingId, detail, date, rate);
            firebaseHelper.isReviewDuplicate(bookingId, currentUserManager.getUserId(), new FirebaseHelper.ReviewCheckCallback() {
                @Override
                public void onCallback(boolean isDuplicate) {
                    if (isDuplicate) {
                        Toast.makeText(ReviewPostingActivity.this, "Bạn đã đánh giá booking này trước đó.", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseHelper.addReview(review, new FirebaseHelper.ReviewAddCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d("ReviewPostingActivity", "Review đã được thêm thành công");
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("ReviewPostingActivity", "Lỗi khi thêm review", e);
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ReviewPostingActivity", "Lỗi khi kiểm tra đánh giá trùng lặp", e);
                }
            });
        });
    }
}
