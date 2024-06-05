package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_edit_profile);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        TextView etxt_customerEmailAddress = findViewById(R.id.etxt_customerEmailAddress);
        TextView etxt_customerFullName = findViewById(R.id.etxt_customerFullName);
        TextView etxt_customerPhone = findViewById(R.id.etxt_customerPhone);
        Button btn_customer_changePassword = findViewById(R.id.btn_customer_changePassword);
        Button btn_customer_saveInfo = findViewById(R.id.btn_customer_saveInfo);
        Button btn_customer_backToHome = findViewById(R.id.btn_customer_backToHome);

        etxt_customerEmailAddress.setText(currentUserManager.getUserEmail());
        etxt_customerFullName.setText(currentUserManager.getUserFullName());
        etxt_customerPhone.setText(currentUserManager.getUserPhone());

        btn_customer_saveInfo.setOnClickListener(v -> {
            // lấy giá trị mới
            String newEmail = etxt_customerEmailAddress.getText().toString().trim();
            String newFullName = etxt_customerFullName.getText().toString().trim();
            String newPhone = etxt_customerPhone.getText().toString().trim();

            firebaseHelper.updateUserInfo(currentUserManager.getUserId(), newEmail, newFullName, newPhone, new FirebaseHelper.UserInfoUpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin người dùng thành công", Toast.LENGTH_SHORT).show();
                    currentUserManager.setUserEmail(newEmail);
                    currentUserManager.setUserFullName(newFullName);
                    currentUserManager.setUserPhone(newPhone);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            Intent intent = new Intent(EditProfileActivity.this, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HomeActivity.class);
            startActivity(intent);
        });

        btn_customer_changePassword.setOnClickListener(v -> {
            // Handle click event for btn_customer_changePassword
            Intent intent = new Intent(EditProfileActivity.this, PasswordChangeActivity.class);
            startActivity(intent);
        });

        btn_customer_backToHome.setOnClickListener(v -> {
            // Handle click event for btn_customer_backToHome
            Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}