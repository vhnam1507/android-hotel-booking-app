package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<String> mImageUrls;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<String> imageUrls) {
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

        holder.btnDelete.setOnClickListener(v -> {
            removeImage(position);
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public void addImage(String imageUrl) {
        mImageUrls.add(imageUrl);
        notifyItemInserted(mImageUrls.size() - 1);
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }


    public void removeImage(int position) {
        if (position < mImageUrls.size()) {
            mImageUrls.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mImageUrls.size());
        }
    }

    public void setImageUrls(List<String> imageUrls) {
        this.mImageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void moveImage(int fromPosition, int toPosition) {
        if (fromPosition < mImageUrls.size() && toPosition < mImageUrls.size()) {
            Collections.swap(mImageUrls, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageButton btnDelete;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
