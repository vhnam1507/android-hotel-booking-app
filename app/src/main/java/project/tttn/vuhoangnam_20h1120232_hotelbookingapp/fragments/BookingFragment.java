package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingFragment extends Fragment {

    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        replaceFragmentInside(new BookingOnactiFragment());
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView_booking);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.onActi) {
                replaceFragmentInside(new BookingOnactiFragment());
            } else if (itemId == R.id.done) {
                replaceFragmentInside(new BookingDoneFragment());
            } else if (itemId == R.id.canceled) {
                replaceFragmentInside(new BookingCanceledFragment());
            }
            return true;
        });

        return view;
    }

    private void replaceFragmentInside(Fragment fragment) {
        // Sử dụng getChildFragmentManager() để quản lý các fragment con
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_bookings, fragment);
        fragmentTransaction.commit();
    }

}