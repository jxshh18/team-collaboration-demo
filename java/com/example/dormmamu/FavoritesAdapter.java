package com.example.dormmamu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormmamu.model.Dorm;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private ArrayList<Dorm> list;
    private OnDormClickListener listener;
    private OnRemoveClickListener removeListener;

    public interface OnDormClickListener {
        void onClick(Dorm dorm);
    }

    public interface OnRemoveClickListener {
        void onRemove(Dorm dorm);
    }

    public FavoritesAdapter(ArrayList<Dorm> list,
                            OnDormClickListener listener,
                            OnRemoveClickListener removeListener) {

        this.list = list;
        this.listener = listener;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_dorm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dorm dorm = list.get(position);

        holder.tvName.setText(dorm.getName());
        holder.tvPrice.setText(dorm.getFormattedPrice());
        holder.tvLocation.setText(dorm.getLocation());

        // show first image from array
        if (dorm.getImageGallery() != null && dorm.getImageGallery().length > 0) {
            holder.imgDorm.setImageResource(dorm.getImageGallery()[0]);
        }


        // open dorm on click
        holder.itemView.setOnClickListener(v -> listener.onClick(dorm));

        // remove favorite button
        holder.btnRemoveFavorite.setOnClickListener(v -> removeListener.onRemove(dorm));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDorm, btnRemoveFavorite;
        TextView tvName, tvPrice, tvLocation;

        public ViewHolder(@NonNull View v) {
            super(v);
            imgDorm = v.findViewById(R.id.imgDorm);
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvLocation = v.findViewById(R.id.tvLocation);
            btnRemoveFavorite = v.findViewById(R.id.btnRemoveFavorite);
        }
    }
}
