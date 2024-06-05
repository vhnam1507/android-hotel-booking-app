package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.User;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etxtEmail, etxtPassword;
    private Button btnLogin, btnGotoSignup;
    private FirebaseHelper firebaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFirebaseHelper();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String userEmail = sharedPreferences.getString("userEmail", "");

        if (isLoggedIn) {
            setupFirebaseHelper();
            firebaseHelper.getUser(userEmail, new FirebaseHelper.UserGetCallback() {
                @Override
                public void onCallback(User user) {
                    if (user != null) {
                        // Xử lý khi đăng nhập thành công và có thông tin user
                        setCurrentUser(user);
                        navigateToWelcomeScreen();
                    } else {
                        Toast.makeText(LoginActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
                }
            });
            return;
        }

        setupUI();
        setupListeners();
    }

    private void setupFirebaseHelper() {
        firebaseHelper = new FirebaseHelper();
    }

    private void setCurrentUser(User user) {
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        currentUserManager.setUserId(user.getId());
        currentUserManager.setUserEmail(user.getEmail());
        currentUserManager.setUserFullName(user.getFullName());
        currentUserManager.setUserPhone(user.getPhone());
        currentUserManager.setAvatarUrl(user.getAvatarUrl());
        currentUserManager.setUserRole(user.getRole());
        Log.d("Login", "Đăng nhập thành công! Khai thác singleton thành công!");
    }

    private void navigateToWelcomeScreen() {
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        etxtEmail = findViewById(R.id.etxtEmail);
        etxtPassword = findViewById(R.id.etxtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGotoSignup = findViewById(R.id.btnGotoSignup);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            etxtEmail.setText(email);
        }
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etxtEmail.getText().toString().trim();
                String password = etxtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    authenticateUser(email, password);
                }
            }
        });

        btnGotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String email, String password) {
        firebaseHelper.userAuthen(email, password, new FirebaseHelper.UserAuthCallback() {
            @Override
            public void onCallback(boolean isAuthenticated) {
                if (isAuthenticated) {
                    firebaseHelper.getUser(email, new FirebaseHelper.UserGetCallback() {
                        @Override
                        public void onCallback(User user) {
                            if (user != null) {
                                setCurrentUser(user);
                                saveLoginCredentials(email, password);
                                navigateToWelcomeScreen();
                            } else {
                                Toast.makeText(LoginActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "Đăng nhập thất bại!");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("FirebaseError", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    private void saveLoginCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", email);
        editor.putString("password", password);
        editor.apply();
    }
}
