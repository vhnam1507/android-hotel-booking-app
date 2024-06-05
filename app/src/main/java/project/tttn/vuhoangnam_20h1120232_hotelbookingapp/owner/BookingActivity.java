package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.BookingCanceledFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.BookingDoneFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.BookingFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.BookingOnactiFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.ProfileFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.SavedFragment;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments.SearchFragment;

public class BookingActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_booking);

        bottomNavigationView = findViewById(R.id.bottomNavigationView_booking);
        replaceFragment(new BookingOnactiFragment());

        // Ánh xạ bottomNavigationView từ layout
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.onActi) {
                replaceFragment(new BookingOnactiFragment());
            } else if (itemId == R.id.done) {
                replaceFragment(new BookingDoneFragment());
            } else if (itemId == R.id.canceled) {
                replaceFragment(new BookingCanceledFragment());
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