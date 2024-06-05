package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_owner_edit_profile);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        TextView etxt_ownerEmailAddress = findViewById(R.id.etxt_ownerEmailAddress);
        TextView etxt_ownerFullName = findViewById(R.id.etxt_ownerFullName);
        TextView etxt_ownerPhone = findViewById(R.id.etxt_ownerPhone);
        Button btn_owner_changePassword = findViewById(R.id.btn_owner_changePassword);
        Button btn_owner_saveInfo = findViewById(R.id.btn_owner_saveInfo);
        Button btn_owner_backToHome = findViewById(R.id.btn_owner_backToHome);

        etxt_ownerEmailAddress.setText(currentUserManager.getUserEmail());
        etxt_ownerFullName.setText(currentUserManager.getUserFullName());
        etxt_ownerPhone.setText(currentUserManager.getUserPhone());

        btn_owner_saveInfo.setOnClickListener(v -> {
            // lấy giá trị mới
            String newEmail = etxt_ownerEmailAddress.getText().toString().trim();
            String newFullName = etxt_ownerFullName.getText().toString().trim();
            String newPhone = etxt_ownerPhone.getText().toString().trim();

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

            Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btn_owner_changePassword.setOnClickListener(v -> {
            // Handle click event for btn_owner_changePassword
            Intent intent = new Intent(EditProfileActivity.this, PasswordChangeActivity.class);
            startActivity(intent);
        });

        btn_owner_backToHome.setOnClickListener(v -> {
            // Handle click event for btn_owner_backToHome
            Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}