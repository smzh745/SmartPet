package com.smartpet.online.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.smartpet.online.R;
import com.smartpet.online.models.FindShop;
import com.smartpet.online.utilities.Constants;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class FindShopAdapter extends RecyclerView.Adapter<FindShopAdapter.MyHolder> implements Filterable {
    private final Context context;
    private final ArrayList<FindShop> findPetArrayList;
    private final ArrayList<FindShop> findPetArrayListFilterd;

    public FindShopAdapter(Context context, ArrayList<FindShop> findPetArrayList) {
        this.context = context;
        this.findPetArrayList = findPetArrayList;
        findPetArrayListFilterd = new ArrayList<>(findPetArrayList);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_pet_layout, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        FindShop findPet = findPetArrayList.get(position);
        holder.size_text.setText("Shop Name: " + findPet.getShopName());
        holder.video_info.setText("Shopkeeper Name: " + findPet.getName() + " | " + "Number: " + findPet.getPhonNumber());
        holder.video_info.setSelected(true);
        Glide.with(context).load(R.drawable.shopping_cart2).into(holder.video_thumb);
        holder.datePost.setText("Shop Location: " + findPet.getShopLocation());
    }

    @Override
    public int getItemCount() {
        return findPetArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<FindShop> filterList = new ArrayList<>();
                if (charSequence.length() > 0) {
                    String pattern = charSequence.toString().toLowerCase().trim();
                    for (FindShop findPet : findPetArrayListFilterd) {
                        if (findPet.getShopName().toLowerCase().contains(pattern)) {
                            filterList.add(findPet);
                        } else {
                            Log.d(Constants.TAG, "performFiltering: nothing");
                        }
                    }
                } else {
                    filterList.addAll(findPetArrayListFilterd);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                findPetArrayList.clear();
                findPetArrayList.addAll((ArrayList<FindShop>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView video_thumb;
        MaterialTextView video_info, size_text, datePost;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            video_thumb = itemView.findViewById(R.id.video_thumb);
            video_info = itemView.findViewById(R.id.video_info);
            size_text = itemView.findViewById(R.id.size_text);
            datePost = itemView.findViewById(R.id.datePost);
        }
    }
}
