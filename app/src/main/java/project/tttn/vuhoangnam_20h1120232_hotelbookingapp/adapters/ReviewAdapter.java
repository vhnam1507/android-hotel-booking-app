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
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Review;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.data.FirebaseHelper;

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context mContext;
    private int mResource;
    private FirebaseHelper firebaseHelper;

    public ReviewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Review> reviews) {
        super(context, resource, reviews);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Review review = getItem(position);
        if (review != null) {
            TextView txtv_mail = convertView.findViewById(R.id.txtv_mail);
            TextView txtv_date = convertView.findViewById(R.id.txtv_date);
            TextView txtv_rate = convertView.findViewById(R.id.txtv_rate);
            TextView txtv_detail = convertView.findViewById(R.id.txtv_detail);
            ImageView iv_avatar = convertView.findViewById(R.id.iv_avatar);

            txtv_mail.setText(review.getUserMail());
            txtv_date.setText(review.getDate());
            txtv_rate.setText("Rate: " + String.valueOf(review.getRate()) + "/5");
            txtv_mail.setText(review.getUserMail());
            txtv_detail.setText(review.getDetail());

            String url = review.getAvatarUrl().split(",")[0].trim();
            Picasso.get()
                    .load(Uri.parse(url))
                    .placeholder(R.drawable.app) // Placeholder image
                    .error(R.drawable.app) // Error image
                    .into(iv_avatar);
        }
        return convertView;
    }

}
