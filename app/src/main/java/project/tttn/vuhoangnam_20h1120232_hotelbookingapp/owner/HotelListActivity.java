package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.HotelAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class HotelListActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private ArrayList<Hotel> hotels;
    private HotelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_owner_hotel_list);

        firebaseHelper = new FirebaseHelper();

        FloatingActionButton fabtn_add = findViewById(R.id.fabtn_add);
        Button btn_backToOwnerHome = findViewById(R.id.btn_backToOwnerHome);
        SearchView searchView = findViewById(R.id.sv_searchview);
        ListView lvw_ownerHotelList = findViewById(R.id.lvw_ownerhotels);

        hotels = new ArrayList<>();
        adapter = new HotelAdapter(this, R.layout.item_hotel, hotels);
        lvw_ownerHotelList.setAdapter(adapter);

        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        loadHotelsByUser(currentUserManager.getUserId());

        fabtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelListActivity.this, HotelPostingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_backToOwnerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Set up item click listener for ListView
        lvw_ownerHotelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hotel selectedHotel = hotels.get(position);

                // Create Intent to start HotelDetailActivity
                Intent intent = new Intent(HotelListActivity.this, HotelDetailActivity.class);

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
    }

    private void loadHotelsByUser(String userId) {
        firebaseHelper.getHotelsByUserId(userId, new FirebaseHelper.HotelListCallback() {
            @Override
            public void onCallback(List<Hotel> hotelsList) {
                hotels.clear();
                hotels.addAll(hotelsList);
                adapter.notifyDataSetChanged();

                TextView txtv_empty = findViewById(R.id.txtv_emptyTxt);
                TextView txtv_emptyIcon = findViewById(R.id.txtv_emptyIcon);

                if (hotelsList.isEmpty()) {
                    txtv_emptyIcon.setVisibility(View.VISIBLE);
                    txtv_empty.setVisibility(View.VISIBLE);
                } else {
                    txtv_emptyIcon.setVisibility(View.GONE);
                    txtv_empty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle the error
            }
        });
    }
}
