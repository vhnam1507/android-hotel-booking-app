package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.User;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtRePassword, edtPhone, edtFullName;
    private RadioGroup radioGroupRole;
    private RadioButton rbtnCustomer, rbtnOwner;
    private CheckBox chkboxAgree;
    private Button btnRegister;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_signup);

        // Initialize the views
        edtEmail = findViewById(R.id.etxtEmailAddress);
        edtFullName = findViewById(R.id.etxtFullName);
        edtPhone = findViewById(R.id.etxtPhone);
        edtPassword = findViewById(R.id.etxtPasswords);
        edtRePassword = findViewById(R.id.etxtRePasswords);
        radioGroupRole = findViewById(R.id.radioGroup);
        rbtnCustomer = findViewById(R.id.rbtn_customer);
        rbtnOwner = findViewById(R.id.rbtn_owner);
        chkboxAgree = findViewById(R.id.chkbox_agree);
        btnRegister = findViewById(R.id.btnSignup);

        firebaseHelper = new FirebaseHelper();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String rePassword = edtRePassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
        boolean isAgree = chkboxAgree.isChecked();

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Xin hãy nhập email hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Tên đầy đủ không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRoleId == -1) {
            Toast.makeText(this, "Xin hãy chọn vai trò của bạn trong hệ thống.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isAgree) {
            Toast.makeText(this, "Bạn cần phải đồng ý với điều khoản của ứng dụng.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected role
        String SelectedRole = ((RadioButton) findViewById(selectedRoleId)).getText().toString();
        if ("Tài khoản khách hàng".equals(SelectedRole)) {
            SelectedRole = "guest";
        } else if ("Tài khoản chủ khách sạn".equals(SelectedRole)) {
            SelectedRole = "own";
        }
        String role = SelectedRole;

        // Check if the email already exists
        firebaseHelper.isEmailExists(email, new FirebaseHelper.EmailCheckCallback() {
            @Override
            public void onCallback(boolean exists) {
                if (exists) {
                    Toast.makeText(SignupActivity.this, "Email đã được sử dụng trong hệ thống, hãy sử dụng email khác.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new user object
                    User newUser = new User(email, fullName, password, phone, role);
                    // Save the new user
                    firebaseHelper.addUser(newUser, new FirebaseHelper.UserAddCallback() {
                        @Override
                        public void onSuccess() {
                            // Người dùng đã được thêm thành công
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công rồi nè!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Exception e) {
                            // Có lỗi xảy ra khi thêm người dùng
                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi khi kiểm tra email
                Toast.makeText(SignupActivity.this, "Lỗi khi kiểm tra email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
