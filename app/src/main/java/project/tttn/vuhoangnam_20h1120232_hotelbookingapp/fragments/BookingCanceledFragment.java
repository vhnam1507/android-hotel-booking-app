package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.HotelAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class BookingCanceledFragment extends Fragment {

    private FirebaseHelper firebaseHelper;
    private ArrayList<Hotel> hotels;
    private HotelAdapter adapter;
    private Map<String, String> hotelBookingIdMap;

    public BookingCanceledFragment() {
        // Required empty public constructor
    }

    public static BookingCanceledFragment newInstance(String param1, String param2) {
        BookingCanceledFragment fragment = new BookingCanceledFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings_canceld, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        firebaseHelper = new FirebaseHelper();
        ListView lvwCancelHotel = view.findViewById(R.id.lvw_cancelHotel);

        hotels = new ArrayList<>();
        adapter = new HotelAdapter(getActivity(), R.layout.item_hotel, hotels);
        lvwCancelHotel.setAdapter(adapter);
        hotelBookingIdMap = new HashMap<>();

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        String userRole = currentUserManager.getUserRole();
        String userId = currentUserManager.getUserId();

        if (Objects.equals(userRole, "own")) {
            loadCanceledBookedHotelsForOwner(userId);
            setOnItemClickListener(lvwCancelHotel, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner.BookingDetailActivity.class);
        } else if (Objects.equals(userRole, "guest")) {
            loadCanceledBookedHotelsForGuest(userId);
            setOnItemClickListener(lvwCancelHotel, project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer.BookingDetailActivity.class);
        }
    }

    private void loadCanceledBookedHotelsForOwner(String userId) {
        firebaseHelper.getAllCancelBookedHotels(userId, new FirebaseHelper.HotelListWithBookingIdCallback() {
            @Override
            public void onSuccess(List<Hotel> hotelsList, Map<String, String> bookingIdMap) {
                updateHotelList(hotelsList, bookingIdMap);
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetBookedHotels", "Lỗi: ", e);
            }
        });
    }

    private void loadCanceledBookedHotelsForGuest(String userId) {
        firebaseHelper.getAllCancelHotelByGuestID(userId, new FirebaseHelper.HotelListWithBookingIdCallback() {
            @Override
            public void onSuccess(List<Hotel> hotelsList, Map<String, String> bookingIdMap) {
                updateHotelList(hotelsList, bookingIdMap);
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetHotels", "Lỗi: ", e);
            }
        });
    }

    private void updateHotelList(List<Hotel> hotelsList, Map<String, String> bookingIdMap) {
        for (Map.Entry<String, String> entry : bookingIdMap.entrySet()) {
            Log.d("BookingMapCheck", "Key: " + entry.getKey() + ", Booking ID: " + entry.getValue());
        }
        hotels.clear();
        hotels.addAll(hotelsList);
        hotelBookingIdMap.clear();
        hotelBookingIdMap.putAll(bookingIdMap);
        adapter.notifyDataSetChanged();

        TextView txtv_empty = getView().findViewById(R.id.txtv_emptyTxt);
        TextView txtv_emptyIcon = getView().findViewById(R.id.txtv_emptyIcon);
        ListView lvw_cancelHotel = getView().findViewById(R.id.lvw_cancelHotel);
        if (bookingIdMap.isEmpty()) {
            txtv_emptyIcon.setVisibility(View.VISIBLE);
            txtv_empty.setVisibility(View.VISIBLE);
            lvw_cancelHotel.setVisibility(View.GONE);
        } else {
            txtv_emptyIcon.setVisibility(View.GONE);
            txtv_empty.setVisibility(View.GONE);
            lvw_cancelHotel.setVisibility(View.VISIBLE);
        }
    }

    private void setOnItemClickListener(ListView listView, Class<?> activityClass) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Hotel selectedHotel = hotels.get(position);
            String bookingId = findBookingIdForHotel(selectedHotel.getId());

            Log.d("SelectedHotel", "Hotel ID: " + selectedHotel.getId() + ", Booking ID: " + bookingId);

            Intent intent = new Intent(getActivity(), activityClass);
            populateIntentWithHotelDetails(intent, selectedHotel, bookingId);

            startActivity(intent);
        });
    }

    private String findBookingIdForHotel(String hotelId) {
        for (Map.Entry<String, String> entry : hotelBookingIdMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(hotelId + "-")) {
                return entry.getValue();
            }
            else {
                return entry.getValue();
            }
        }
        return null;
    }

    private void populateIntentWithHotelDetails(Intent intent, Hotel hotel, String bookingId) {
        intent.putExtra("hotelId", hotel.getId());
        intent.putExtra("ownerId", hotel.getOwnerId());
        intent.putExtra("hotelName", hotel.getName());
        intent.putExtra("hotelAddress", hotel.getAddress());
        intent.putExtra("hotelProvinceID", hotel.getProvinceID());
        intent.putExtra("hotelAmenities", hotel.getAmenities());
        intent.putExtra("hotelImageUrls", hotel.getImageUrls());
        intent.putExtra("hotelNumRooms", hotel.getNumRooms());
        intent.putExtra("hotelNumMaxGuest", hotel.getNumMaxGuest());
        intent.putExtra("hotelPrice", hotel.getPrice());
        intent.putExtra("bookingId", bookingId);
    }
}
