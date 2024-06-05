package project.tttn.vuhoangnam_20h1120232_hotelbookingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.FirebaseApp;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    private static final long INTRO_DELAY = 3000;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);



        // Kiểm tra và yêu cầu quyền truy cập vào bộ nhớ
        checkAndRequestStoragePermission();

        // Chuyển sang LoginActivity sau khi thời gian INTRO_DELAY đã qua
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent loginIntent = new Intent(IntroActivity.this, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }, INTRO_DELAY);
    }

    // Kiểm tra và yêu cầu quyền truy cập vào bộ nhớ
    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }
}
