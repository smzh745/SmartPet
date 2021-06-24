package com.smartpet.online.activiities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.smartpet.online.R;
import com.smartpet.online.models.FindPet;

import java.util.ArrayList;

import static com.smartpet.online.utilities.Constants.UPLOAD_FOLDER;

public class ViewPetActivity extends AppCompatActivity {
    private ArrayList<FindPet> findPetArrayList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet);
        findPetArrayList = (ArrayList<FindPet>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position", 0);
        ImageView backArrow = findViewById(R.id.backArrow);
        TextInputEditText number = findViewById(R.id.number);
        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText postedBy = findViewById(R.id.postedBy);
        TextInputEditText numberBy = findViewById(R.id.numberBy);
        ImageView image = findViewById(R.id.image);
        TextInputEditText type = findViewById(R.id.type);
        ImageView callNow = findViewById(R.id.callNow);

        Glide.with(this).load(UPLOAD_FOLDER + findPetArrayList.get(position).getPetImage()).into(image);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("image", findPetArrayList.get(position).getPetImage());
                startActivity(intent);
            }
        });
        name.setText(findPetArrayList.get(position).getPetName());
        number.setText(findPetArrayList.get(position).getPetPrice());
        type.setText(findPetArrayList.get(position).getPetType());
        postedBy.setText(findPetArrayList.get(position).getName());
        numberBy.setText(findPetArrayList.get(position).getPhoneNum());
        callNow.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", findPetArrayList.get(position).getPhoneNum(), null));
            startActivity(intent);
        });
    }
}