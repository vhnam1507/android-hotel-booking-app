package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.BookingFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.ProfileFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.SavedFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.SearchFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new SearchFragment());

        String provinceName = getIntent().getStringExtra("selectedProvinceName");
        String provinceId = getIntent().getStringExtra("selectedProvinceId");
        if (provinceName != null) {
            // Gửi tên tỉnh đến SearchFragment
            Bundle bundle = new Bundle();
            bundle.putString("selectedProvinceName", provinceName);
            bundle.putString("selectedProvinceId", provinceId);
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            replaceFragment(searchFragment);
        } else {
            // Không có tên tỉnh, chỉ đơn giản là hiển thị SearchFragment mà không có dữ liệu bổ sung
            replaceFragment(new SearchFragment());
        }

         // Ánh xạ bottomNavigationView từ layout
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.customer_search) {
                replaceFragment(new SearchFragment());
            } else if (itemId == R.id.customer_saved) {
                replaceFragment(new SavedFragment());
            } else if (itemId == R.id.customer_bookings) {
                replaceFragment(new BookingFragment());
            } else if (itemId == R.id.customer_profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_main, fragment);
        fragmentTransaction.commit();
    }
}