package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters.ProvinceAdapter;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Province;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class ProvinceSearchActivity extends AppCompatActivity {

    private ArrayList<Province> provinces;
    private ArrayList<Province> originalProvinces; // Danh sách tỉnh ban đầu
    private ProvinceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_customer_province_search);

        Button btn_backToSearch = findViewById(R.id.btn_backToSearch);
        SearchView searchView = findViewById(R.id.sv_searchview);
        ListView lvwProv = findViewById(R.id.lvw_prov);
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        originalProvinces = new ArrayList<>();

        firebaseHelper.getAllProvinces(new FirebaseHelper.ProvinceListCallback() {
            @Override
            public void onCallback(List<Province> provinceList) {
                originalProvinces.clear();
                originalProvinces.addAll(provinceList);
                provinces.clear();
                provinces.addAll(originalProvinces);
                adapter.notifyDataSetChanged();

                for (Province province : provinceList) {
                    Log.d("Province", "ID: " + province.getId() + ", Name: " + province.getName());
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("GetAllProvinces", "Error: " + e.getMessage());
            }
        });

        provinces = new ArrayList<>(originalProvinces); // Sao chép danh sách ban đầu

        adapter = new ProvinceAdapter(this, R.layout.item_province, provinces);

        lvwProv.setAdapter(adapter);
        lvwProv.setOnItemClickListener((parent, view, position, id) -> {
            Province selectedProvince = provinces.get(position);
            Intent intent = new Intent(ProvinceSearchActivity.this, HomeActivity.class);
            intent.putExtra("selectedProvinceName", selectedProvince.getName());
            intent.putExtra("selectedProvinceId", selectedProvince.getId());
            startActivity(intent);
            finish();
        });

        btn_backToSearch.setOnClickListener(v -> finish());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProvinces(newText);
                return false;
            }
        });
    }

    private void filterProvinces(String newText) {
        provinces.clear();
        if (newText.isEmpty()) {
            provinces.addAll(originalProvinces);
        } else {
            newText = newText.toLowerCase();
            for (Province prv : originalProvinces) {
                if (prv.getName().toLowerCase().contains(newText) || prv.getId().toLowerCase().contains(newText)) {
                    provinces.add(prv);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
