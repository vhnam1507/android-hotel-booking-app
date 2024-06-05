package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;

public class CustomerImageAdapter extends RecyclerView.Adapter<CustomerImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<String> mImageUrls;
    private LayoutInflater mInflater;

    public CustomerImageAdapter(Context context, List<String> imageUrls) {
        this.mContext = context;
        this.mImageUrls = imageUrls;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = mImageUrls.get(position);
        Glide.with(mContext)
                .load(imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.mImageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
