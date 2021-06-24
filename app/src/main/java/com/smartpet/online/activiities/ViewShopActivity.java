package com.smartpet.online.activiities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.smartpet.online.R;
import com.smartpet.online.models.FindShop;

import java.util.ArrayList;

public class ViewShopActivity extends AppCompatActivity {
    private ArrayList<FindShop> findPetArrayList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop);
        findPetArrayList = (ArrayList<FindShop>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position", 0);

        ImageView backArrow = findViewById(R.id.backArrow);
        TextInputEditText number = findViewById(R.id.number);
        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText clinicName = findViewById(R.id.clinicName);
        TextInputEditText clinicLoc = findViewById(R.id.clinicLoc);
        ImageView callNow = findViewById(R.id.callNow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name.setText(findPetArrayList.get(position).getName());
        number.setText(findPetArrayList.get(position).getPhonNumber());
        clinicName.setText(findPetArrayList.get(position).getShopName());
        clinicLoc.setText(findPetArrayList.get(position).getShopLocation());

        callNow.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", findPetArrayList.get(position).getPhonNumber(), null));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}