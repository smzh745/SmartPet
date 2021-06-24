package com.smartpet.online.activiities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.smartpet.online.R;

import static com.smartpet.online.utilities.Constants.UPLOAD_FOLDER;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView image = findViewById(R.id.image);
        String img= String.valueOf(getIntent().getStringExtra("image"));
        Glide.with(this).load(UPLOAD_FOLDER +img).into(image);
    }
}