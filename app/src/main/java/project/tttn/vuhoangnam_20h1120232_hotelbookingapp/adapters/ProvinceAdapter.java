package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;
import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes.Province;

public class ProvinceAdapter extends ArrayAdapter<Province>{
    private Context mContext;
    private int mResource;

    public ProvinceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Province> provinces) {
        super(context, resource, provinces);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Province province = getItem(position);

        TextView tvId = convertView.findViewById(R.id.tvProID);
        TextView tvName = convertView.findViewById(R.id.tvProvin);

        if (province != null) {
            tvId.setText(province.getId().substring(province.getId().length() - 2));
            tvName.setText(province.getName());
        }

        return convertView;
    }
}
