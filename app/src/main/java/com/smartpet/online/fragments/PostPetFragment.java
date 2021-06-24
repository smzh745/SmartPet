package com.smartpet.online.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartpet.online.R;
import com.smartpet.online.utilities.InternetConnection;
import com.smartpet.online.utilities.SharedPrefUtils;
import com.smartpet.online.utilities.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.smartpet.online.utilities.Constants.PICK_CAM;
import static com.smartpet.online.utilities.Constants.PICK_IMAGE;
import static com.smartpet.online.utilities.Constants.POST_PET;
import static com.smartpet.online.utilities.Constants.USER_ID;


public class PostPetFragment extends BaseFragment {

    private TextInputEditText name, price, description;
    private Spinner selectPet;
    private ImageView imagePick;
    private Bitmap thumbnail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_pet, container, false);
        ImageView backArrow = view.findViewById(R.id.backArrow);
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        selectPet = view.findViewById(R.id.selectPet);
        description = view.findViewById(R.id.description);
        imagePick = view.findViewById(R.id.imagePick);
        MaterialButton postBtn = view.findViewById(R.id.postBtn);

        setSpinner(R.array.pet_array, selectPet);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(R.id.homeFragment);
            }
        });
        imagePick.setOnClickListener(v -> showDialog());
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String select = selectPet.getSelectedItem().toString();
                if (checkTextEmpty(name) || checkTextEmpty(price) || checkTextEmpty(description) ||
                        select.equalsIgnoreCase("Select Pet") || thumbnail == null) {
                    showToast("Please choose all options");
                } else {
                    postPetToServer();
                }
            }
        });
        return view;
    }

    private void postPetToServer() {
        if (InternetConnection.checkConnection(Objects.requireNonNull(getActivity()))) {

            setProgressDialog("Loading..");
            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, POST_PET,

                    response -> {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getBoolean("error")) {
                                showToast(obj.getString("message"));
                            } else {
                                showToast(obj.getString("message"));
                                navigateFragment(R.id.homeFragment);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cancelProgressDialog();
                    },
                    error -> {
                        showToast(error.getMessage());
                        cancelProgressDialog();

                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", SharedPrefUtils.getStringData(Objects.requireNonNull(getActivity()), USER_ID));
                    params.put("name", name.getText().toString());
                    params.put("price", price.getText().toString());
                    params.put("type", selectPet.getSelectedItem().toString());
                    params.put("description", description.getText().toString());
                    params.put("createDate", getCurrentDateTime());
                    return params;
                }

                /*
                 * Here we are passing image by renaming it with a unique name
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(thumbnail)));
                    return params;
                }
            };
            volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) {
                    Log.d("TAGI", "retry: " + error.getMessage());
                }
            });
            //adding the request to volley
            Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(volleyMultipartRequest);
        } else {
            showToast("No Internet Connection!");

        }
    }
    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    //choose dialog
    private void showDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        pictureDialog.setTitle("Select Action");

        String[] pictureDialogItems = {
                "Pick Image from Gallery",
                "Take Photo from Camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                            break;
                        case 1:
                            try {
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent1, PICK_CAM);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    assert data != null;

                    Uri path = Uri.parse(Objects.requireNonNull(data.getData()).toString());
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagePick.setImageURI(path);

                }
                break;
            case PICK_CAM:
                try {
                    assert data != null;
                    thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    imagePick.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;

        }
    }

}