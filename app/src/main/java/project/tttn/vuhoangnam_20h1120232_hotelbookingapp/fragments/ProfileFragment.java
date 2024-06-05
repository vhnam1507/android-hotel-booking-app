package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.LoginActivity;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.EditProfileActivity;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.HomeActivity;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView txtv_customerFullname = view.findViewById(R.id.txtv_customerFullName);
        TextView txtv_customerEmail = view.findViewById(R.id.txtv_customerEmail);
        Button btn_myReview = view.findViewById(R.id.btn_myReview);
        Button btn_accountSetting = view.findViewById(R.id.btn_accountSetting);
        Button btn_backToSearchPage = view.findViewById(R.id.btn_backToSearchPage);
        Button btn_logout = view.findViewById(R.id.btn_logout);
        ImageView iv_avatar = view.findViewById(R.id.iv_avatar);

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        txtv_customerFullname.setText(currentUserManager.getUserFullName());
        txtv_customerEmail.setText(currentUserManager.getUserEmail());

        Picasso.get()
                .load(Uri.parse(currentUserManager.getAvatarUrl()))
                .placeholder(R.drawable.app) // Placeholder image
                .error(R.drawable.app) // Error image
                .into(iv_avatar);

        btn_myReview.setOnClickListener(v -> {
            // Handle My Reviews button click event
        });

        btn_accountSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.EditProfileActivity.class);
            startActivity(intent);
        });

        btn_backToSearchPage.setOnClickListener(v -> {
            Fragment searchFragment = new SearchFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.framelayout_main, searchFragment);
            fragmentTransaction.commit();
        });

        btn_logout.setOnClickListener(v -> {
            // Handle click event for btn_logout
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginPrefs", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa toàn bộ dữ liệu trong SharedPreferences
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
