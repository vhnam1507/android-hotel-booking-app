package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Hotel;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class HotelAdapter extends ArrayAdapter<Hotel> {
    private Context mContext;
    private int mResource;
    private FirebaseHelper firebaseHelper;

    public HotelAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Hotel> hotels) {
        super(context, resource, hotels);
        this.mContext = context;
        this.mResource = resource;
        this.firebaseHelper = new FirebaseHelper(); // Initialize FirebaseHelper here
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Hotel hotel = getItem(position);
        if (hotel != null) {
            TextView tv_Name = convertView.findViewById(R.id.tv_hotelname);
            TextView tv_Address = convertView.findViewById(R.id.tv_address);
            TextView tv_price = convertView.findViewById(R.id.tv_price);
            TextView tv_extra = convertView.findViewById(R.id.tv_extra);
            ImageView iv_cover = convertView.findViewById(R.id.imgvw_cover);

            String address = hotel.getAddress();
            tv_Address.setText(address); // Set initial address without province

            firebaseHelper.getProvinceNameById(hotel.getProvinceID(), new FirebaseHelper.ProvinceNameCallback() {
                @Override
                public void onCallback(String provinceName) {
                    if (provinceName != null) {
                        String fullAddress = address + " - " + provinceName;
                        tv_Address.setText(fullAddress); // Update address with province name
                    } else {
                        Log.e("HotelAdapter", "Province not found");
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("HotelAdapter", "Error fetching data", e);
                }
            });

            int price = hotel.getPrice();
            DecimalFormat df = new DecimalFormat("###,###");
            String formattedPrice = "VND " + df.format(price) + "/đêm";
            String amenities = hotel.getAmenities();
            String[] amenitiesArray = amenities.split(",");
            StringBuilder formattedAmenities = new StringBuilder();
            for (String amenity : amenitiesArray) {
                formattedAmenities.append(amenity.trim()).append(", ");
            }
            if (formattedAmenities.length() > 0) {
                formattedAmenities.setLength(formattedAmenities.length() - 2);
            }

            tv_Name.setText(hotel.getName());
            tv_price.setText(formattedPrice);
            tv_extra.setText(formattedAmenities.toString());

            String url = hotel.getImageUrls().split(",")[0].trim();
            Picasso.get()
                    .load(Uri.parse(url))
                    .placeholder(R.drawable.app) // Placeholder image
                    .error(R.drawable.app) // Error image
                    .into(iv_cover);
        }
        return convertView;
    }
}
