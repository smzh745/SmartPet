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
import com.smartpet.online.models.FindPet;
import com.smartpet.online.utilities.Constants;

import java.util.ArrayList;

import static com.smartpet.online.utilities.Constants.UPLOAD_FOLDER;

@SuppressWarnings("ALL")
public class FindPetAdapter extends RecyclerView.Adapter<FindPetAdapter.MyHolder> implements Filterable {
    private final Context context;
    private final ArrayList<FindPet> findPetArrayList;
    private final ArrayList<FindPet> findPetArrayListFilterd;

    public FindPetAdapter(Context context, ArrayList<FindPet> findPetArrayList) {
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
        FindPet findPet = findPetArrayList.get(position);
        holder.size_text.setText("Posted by: " + findPet.getName());
        holder.video_info.setText("Pet Price: Rs." + findPet.getPetPrice() + " | " + "Pet Type: " + findPet.getPetType());
        holder.video_info.setSelected(true);
        Glide.with(context).load(UPLOAD_FOLDER + findPet.getPetImage()).into(holder.video_thumb);
        holder.datePost.setText("Posted at: " + findPet.getPetDate());
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
                ArrayList<FindPet> filterList = new ArrayList<>();
                if (charSequence.length() > 0) {
                    String pattern = charSequence.toString().toLowerCase().trim();
                    for (FindPet findPet : findPetArrayListFilterd) {
                        if (findPet.getPetType().toLowerCase().contains(pattern)) {
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
                findPetArrayList.addAll((ArrayList<FindPet>) filterResults.values);
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
