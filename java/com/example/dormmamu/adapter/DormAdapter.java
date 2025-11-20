package com.example.dormmamu.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormmamu.R;
import com.example.dormmamu.model.Dorm;
import com.example.dormmamu.ui.DormDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class DormAdapter extends RecyclerView.Adapter<DormAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final List<Dorm> fullList;
    private final List<Dorm> shownList;

    private String query = "";
    private String bathroom = "ALL";
    private Set<String> cities;
    private int sortOpt = 0;
    private int min = 0, max = 999999;

    public DormAdapter(Context context, List<Dorm> list) {
        this.context = context;
        this.fullList = new ArrayList<>(list);
        this.shownList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dorm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Dorm d = shownList.get(pos);
        h.name.setText(d.getName());
        h.loc.setText(d.getLocation());
        h.price.setText(d.getFormattedPrice());
        h.img.setImageResource(d.getImageResId());
        h.rating.setRating(d.getRating());

        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DormDetailActivity.class);
            intent.putExtra("dorm_list", new ArrayList<>(shownList));
            intent.putExtra("selected_position", pos);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return shownList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, loc, price;
        RatingBar rating;

        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.dormImage);
            name = v.findViewById(R.id.dormName);
            loc = v.findViewById(R.id.dormLocation);
            price = v.findViewById(R.id.dormPrice);
            rating = v.findViewById(R.id.dormRating);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterQuery = (constraint == null) ? "" : constraint.toString().toLowerCase().trim();
                List<Dorm> filtered = new ArrayList<>();

                for (Dorm d : fullList) {
                    if (d.getName().toLowerCase().contains(filterQuery)
                            || d.getLocation().toLowerCase().contains(filterQuery)) {
                        filtered.add(d);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                shownList.clear();
                shownList.addAll((List<Dorm>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    //  Filtering + sorting combined logic
    public void applyFilters(String q, String bath, Set<String> ct, int sort, int minP, int maxP) {
        this.query = (q == null) ? "" : q.toLowerCase();
        this.bathroom = (bath == null || bath.isEmpty()) ? "ALL" : bath;
        this.cities = ct;
        this.sortOpt = sort;
        this.min = minP;
        this.max = maxP;

        List<Dorm> result = new ArrayList<>();

        for (Dorm d : fullList) {
            int priceVal = d.getPriceValue();
            if (priceVal < min || priceVal > max) continue;

            if (!TextUtils.isEmpty(query)) {
                String combined = (d.getName() + " " + d.getLocation()).toLowerCase();
                if (!combined.contains(query)) continue;
            }

            if (!bathroom.equalsIgnoreCase("ALL")) {
                if (!d.getBathroomType().equalsIgnoreCase(bathroom)) continue;
            }

            if (cities != null && !cities.isEmpty()) {
                boolean match = false;
                for (String c : cities) {
                    if (d.getLocation().toLowerCase().contains(c.toLowerCase())) {
                        match = true;
                        break;
                    }
                }
                if (!match) continue;
            }

            result.add(d);
        }

        //  Sort results
        switch (sortOpt) {
            case 1:
                Collections.sort(result, Comparator.comparingInt(Dorm::getPriceValue));
                break;
            case 2:
                Collections.sort(result, (a, b) -> b.getPriceValue() - a.getPriceValue());
                break;
            case 3:
                Collections.sort(result, (a, b) -> Float.compare(b.getRating(), a.getRating()));
                break;
            case 4:
                Collections.sort(result, (a, b) -> Float.compare(a.getRating(), b.getRating()));
                break;
        }

        shownList.clear();
        shownList.addAll(result);
        notifyDataSetChanged();
    }
}
