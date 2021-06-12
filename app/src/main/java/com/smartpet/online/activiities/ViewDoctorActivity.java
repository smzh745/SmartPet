package com.smartpet.online.activiities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.smartpet.online.R;
import com.smartpet.online.models.FindDoctor;

import java.util.ArrayList;

public class ViewDoctorActivity extends BaseActivity {
    private ArrayList<FindDoctor> findPetArrayList;
    private int position;
    private ImageView backArrow;
    private TextInputEditText name,number,clinicName,clinicLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);
        findPetArrayList = (ArrayList<FindDoctor>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position", 0);

        backArrow = findViewById(R.id.backArrow);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        clinicName = findViewById(R.id.clinicName);
        clinicLoc = findViewById(R.id.clinicLoc);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name.setText(findPetArrayList.get(position).getName());
        number.setText(findPetArrayList.get(position).getPhoneNum());
        clinicName.setText(findPetArrayList.get(position).getClinicName());
        clinicLoc.setText(findPetArrayList.get(position).getClinicLocation());
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}