package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.HomeActivity;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView iv_avatar;
    private Uri selectedImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseHelper firebaseHelper;
    private CurrentUserManager currentUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_edit_profile);

        firebaseHelper = new FirebaseHelper();
        currentUserManager = CurrentUserManager.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        TextView etxt_ownerEmailAddress = findViewById(R.id.etxt_ownerEmailAddress);
        TextView etxt_ownerFullName = findViewById(R.id.etxt_ownerFullName);
        TextView etxt_ownerPhone = findViewById(R.id.etxt_ownerPhone);
        Button btn_owner_changePassword = findViewById(R.id.btn_owner_changePassword);
        Button btn_owner_saveInfo = findViewById(R.id.btn_owner_saveInfo);
        Button btn_owner_backToHome = findViewById(R.id.btn_owner_backToHome);
        iv_avatar = findViewById(R.id.iv_avatar);

        etxt_ownerEmailAddress.setText(currentUserManager.getUserEmail());
        etxt_ownerFullName.setText(currentUserManager.getUserFullName());
        etxt_ownerPhone.setText(currentUserManager.getUserPhone());

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

        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        iv_avatar.setImageURI(selectedImageUri);
                    }
                });

        iv_avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btn_owner_saveInfo.setOnClickListener(v -> {
            String newEmail = etxt_ownerEmailAddress.getText().toString().trim();
            String newFullName = etxt_ownerFullName.getText().toString().trim();
            String newPhone = etxt_ownerPhone.getText().toString().trim();

            if (selectedImageUri != null) {
                uploadAvatarAndSaveInfo(selectedImageUri, newEmail, newFullName, newPhone);
            } else {
                updateUserInfo(newEmail, newFullName, newPhone);
            }

            Intent intent = new Intent(EditProfileActivity.this, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.HomeActivity.class);
            startActivity(intent);
        });

        btn_owner_changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, PasswordChangeActivity.class);
            startActivity(intent);
        });

        btn_owner_backToHome.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.HomeActivity.class);
            startActivity(intent);
        });
    }

    private void uploadAvatarAndSaveInfo(Uri imageUri, String newEmail, String newFullName, String newPhone) {
        String userId = currentUserManager.getUserId();
        StorageReference avatarRef = storageReference.child("avatars/" + userId + ".jpg");

        UploadTask uploadTask = avatarRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String avatarUrl = uri.toString();
            firebaseHelper.updateAvatarUrl(userId, avatarUrl, new FirebaseHelper.UserAvatarUpdateCallback() {
                @Override
                public void onSuccess() {
                    currentUserManager.setAvatarUrl(avatarUrl);
                    updateUserInfo(newEmail, newFullName, newPhone);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getApplicationContext(), "Lỗi khi cập nhật avatar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        })).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Lỗi khi tải lên ảnh đại diện: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUserInfo(String newEmail, String newFullName, String newPhone) {
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
    }
}