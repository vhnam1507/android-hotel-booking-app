package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.HotelAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ReviewAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Review;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class MyReviewActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private ArrayList<Review> reviews;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_my_review);

        firebaseHelper = new FirebaseHelper();
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, R.layout.item_review, reviews);

        ListView lvw_myReview = findViewById(R.id.lvw_myReview);
        lvw_myReview.setAdapter(reviewAdapter);
        loadReviews(currentUserManager.getUserEmail());

    }

    private void loadReviews(String userMail){
        firebaseHelper.getReviewsByUserMail(userMail, new FirebaseHelper.ReviewCallback() {
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