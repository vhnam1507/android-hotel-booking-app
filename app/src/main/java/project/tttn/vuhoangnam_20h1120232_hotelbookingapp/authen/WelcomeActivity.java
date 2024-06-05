package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;

public class WelcomeActivity extends AppCompatActivity {

    private TextView txtv_greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_welcome);

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        String userFullName = currentUserManager.getUserFullName();
        String userRole = currentUserManager.getUserRole();

        txtv_greeting = findViewById(R.id.tv_hi);
        String greeting = "Chào mừng trở lại" + (userFullName != null && !userFullName.isEmpty() ? "\n" + userFullName : "");
        txtv_greeting.setText(greeting);

        Button btnGoToMain = findViewById(R.id.btnGoToMain);
        btnGoToMain.setOnClickListener(v -> {
            Class<?> destinationClass = "own".equals(userRole) ? project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.HomeActivity.class : project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HomeActivity.class;
            Intent intent = new Intent(WelcomeActivity.this, destinationClass);
            startActivity(intent);
            finish();
        });
    }
}