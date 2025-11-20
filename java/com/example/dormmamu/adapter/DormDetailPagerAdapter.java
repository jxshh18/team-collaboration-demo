package com.example.dormmamu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormmamu.R;
import com.example.dormmamu.model.Dorm;

import java.util.ArrayList;

public class DormDetailPagerAdapter extends RecyclerView.Adapter<DormDetailPagerAdapter.ViewHolder> {

    private ArrayList<Dorm> dorms;

    public DormDetailPagerAdapter(ArrayList<Dorm> dorms) {
        this.dorms = dorms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dorm_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dorm dorm = dorms.get(position);
        holder.imageView.setImageResource(dorm.getImageResId());
    }

    @Override
    public int getItemCount() {
        return dorms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgDorm);
        }
    }
}
