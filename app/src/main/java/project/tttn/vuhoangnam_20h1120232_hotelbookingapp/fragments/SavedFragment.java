package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.HotelAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HotelDetailActivity;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HotelListActivity;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class SavedFragment extends Fragment {

    private FirebaseHelper firebaseHelper;
    private ArrayList<Hotel> hotels;
    private HotelAdapter adapter;

    public SavedFragment() {
        // Required empty public constructor
    }

    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        firebaseHelper = new FirebaseHelper();

        ListView lvw_savedHotel = view.findViewById(R.id.lvw_savedHotel);

        hotels = new ArrayList<>();
        adapter = new HotelAdapter(getActivity(), R.layout.item_hotel, hotels);
        lvw_savedHotel.setAdapter(adapter);
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();

        firebaseHelper.getSavedHotelsByUserId(currentUserManager.getUserId(), new FirebaseHelper.SavedHotelsCallback() {
            @Override
            public void onSuccess(List<Hotel> hotelsList) {
                // Xử lý danh sách các khách sạn đã lưu
                hotels.clear();
                hotels.addAll(hotelsList);
                adapter.notifyDataSetChanged();

                TextView txtv_empty = getView().findViewById(R.id.txtv_emptyTxt);
                TextView txtv_emptyIcon = getView().findViewById(R.id.txtv_emptyIcon);
                if (hotelsList.isEmpty()) {
                    txtv_emptyIcon.setVisibility(View.VISIBLE);
                    txtv_empty.setVisibility(View.VISIBLE);
                    lvw_savedHotel.setVisibility(View.GONE);
                } else {
                    txtv_emptyIcon.setVisibility(View.GONE);
                    txtv_empty.setVisibility(View.GONE);
                    lvw_savedHotel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi
            }
        });

        // Set up item click listener for ListView
        lvw_savedHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hotel selectedHotel = hotels.get(position);

                // Create Intent to start HotelDetailActivity
                Intent intent = new Intent(getActivity(), project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.HotelDetailActivity.class);

                // Pass hotel details to the intent
                intent.putExtra("hotelId", selectedHotel.getId());
                intent.putExtra("ownerId", selectedHotel.getOwnerId());
                intent.putExtra("hotelName", selectedHotel.getName());
                intent.putExtra("hotelAddress", selectedHotel.getAddress());
                intent.putExtra("hotelProvinceID", selectedHotel.getProvinceID());
                intent.putExtra("hotelAmenities", selectedHotel.getAmenities());
                intent.putExtra("hotelImageUrls", selectedHotel.getImageUrls());
                intent.putExtra("hotelNumRooms", selectedHotel.getNumRooms());
                intent.putExtra("hotelNumMaxGuest", selectedHotel.getNumMaxGuest());
                intent.putExtra("hotelPrice", selectedHotel.getPrice());

                // Start HotelDetailActivity
                startActivity(intent);
            }
        });

        return view;
    }
}