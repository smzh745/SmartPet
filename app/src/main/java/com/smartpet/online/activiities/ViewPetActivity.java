package com.smartpet.online.activiities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.smartpet.online.R;
import com.smartpet.online.models.FindDoctor;
import com.smartpet.online.models.FindPet;

import java.util.ArrayList;

import static com.smartpet.online.utilities.Constants.UPLOAD_FOLDER;

public class ViewPetActivity extends BaseActivity {
    private ArrayList<FindPet> findPetArrayList;
    private int position;
    private ImageView image;
    private ImageView backArrow;
    private TextInputEditText name, number, postedBy, numberBy, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet);
        findPetArrayList = (ArrayList<FindPet>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position", 0);
        backArrow = findViewById(R.id.backArrow);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        postedBy = findViewById(R.id.postedBy);
        numberBy = findViewById(R.id.numberBy);
        image = findViewById(R.id.image);
        type = findViewById(R.id.type);
        Glide.with(this).load(UPLOAD_FOLDER +findPetArrayList.get(position).getPetImage()).into(image);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),ViewImageActivity.class);
                intent.putExtra("image",findPetArrayList.get(position).getPetImage());
                startActivity(intent);
            }
        });
        name.setText(findPetArrayList.get(position).getPetName());
        number.setText(findPetArrayList.get(position).getPetPrice());
        type.setText(findPetArrayList.get(position).getPetType());
        postedBy.setText(findPetArrayList.get(position).getName());
        numberBy.setText(findPetArrayList.get(position).getPhoneNum());
    }
}