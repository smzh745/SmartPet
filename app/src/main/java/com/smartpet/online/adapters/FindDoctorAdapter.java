package com.smartpet.online.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.smartpet.online.R;
import com.smartpet.online.activiities.ViewDoctorActivity;
import com.smartpet.online.models.FindDoctor;
import com.smartpet.online.utilities.Constants;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class FindDoctorAdapter extends RecyclerView.Adapter<FindDoctorAdapter.MyHolder> implements Filterable {
    private final Context context;
    private final ArrayList<FindDoctor> findPetArrayList;
    private final ArrayList<FindDoctor> findPetArrayListFilterd;

    public FindDoctorAdapter(Context context, ArrayList<FindDoctor> findPetArrayList) {
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
        FindDoctor findPet = findPetArrayList.get(position);
        holder.size_text.setText("Clinic Name: " + findPet.getClinicName());
        holder.video_info.setText("Doctor Name: " + findPet.getName() + " | " + "Number: " + findPet.getPhoneNum());
        holder.video_info.setSelected(true);
        Glide.with(context).load(R.drawable.doctor2).into(holder.video_thumb);
        holder.datePost.setText("Clinic Location: " + findPet.getClinicLocation());

    /*    holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewDoctorActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("list",findPetArrayList);
                context.startActivity(intent);
            }
        });*/
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
                ArrayList<FindDoctor> filterList = new ArrayList<>();
                if (charSequence.length() > 0) {
                    String pattern = charSequence.toString().toLowerCase().trim();
                    for (FindDoctor findPet : findPetArrayListFilterd) {
                        if (findPet.getClinicLocation().toLowerCase().contains(pattern)) {
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
                findPetArrayList.addAll((ArrayList<FindDoctor>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView video_thumb;
        MaterialTextView video_info, size_text, datePost;
        MaterialCardView viewBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            video_thumb = itemView.findViewById(R.id.video_thumb);
            video_info = itemView.findViewById(R.id.video_info);
            size_text = itemView.findViewById(R.id.size_text);
            datePost = itemView.findViewById(R.id.datePost);
            viewBtn = itemView.findViewById(R.id.video_card);
        }
    }
}
