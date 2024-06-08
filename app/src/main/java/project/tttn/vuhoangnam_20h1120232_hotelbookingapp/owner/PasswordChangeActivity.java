package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class PasswordChangeActivity extends AppCompatActivity {

    private ImageView iv_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_password_change);

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        TextView etxt_ownerCurrentPassword = findViewById(R.id.etxt_ownerCurrentPassword);
        TextView etxt_ownerNewPassword = findViewById(R.id.etxt_ownerNewPassword);
        TextView etxt_ownerReNewPassword = findViewById(R.id.etxt_ownerReNewPassword);
        Button btn_owner_savePassword = findViewById(R.id.btn_owner_savePassword);
        Button btn_owner_backToInfo = findViewById(R.id.btn_owner_backToInfo);
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

        btn_owner_savePassword.setOnClickListener(v -> {
            // Handle click event for btn_owner_savePassword
            String currentPassword = etxt_ownerCurrentPassword.getText().toString().trim();
            String newPassword = etxt_ownerNewPassword.getText().toString().trim();
            String reNewPassword = etxt_ownerReNewPassword.getText().toString().trim();

            firebaseHelper.userAuthen(currentUserManager.getUserEmail(), currentPassword, new FirebaseHelper.UserAuthCallback() {
                @Override
                public void onCallback(boolean isAuthenticated) {
                    if (isAuthenticated) {
                        Log.d("Checked", "Thông tin đăng nhập hợp lệ!");

                        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Mật khẩu phải ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!newPassword.equals(reNewPassword)) {
                            Toast.makeText(getApplicationContext(), "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        firebaseHelper.updateUserPassword(currentUserManager.getUserId(), newPassword, new FirebaseHelper.UserPasswordUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PasswordChangeActivity.this, EditProfileActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getApplicationContext(), "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Log.d("Login", "Thông tin đăng nhập sai lệch!");
                    }
                }
                @Override
                public void onError(Exception e) {
                    Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
                }
            });
        });

        btn_owner_backToInfo.setOnClickListener(v -> {
            // Handle click event for btn_owner_backToInfo
            Intent intent = new Intent(PasswordChangeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
}