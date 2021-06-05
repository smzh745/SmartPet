package com.smartpet.online.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.smartpet.online.R;
import com.smartpet.online.utilities.PermissionsUtils;
import com.smartpet.online.utilities.SharedPrefUtils;

import org.json.JSONException;

import static com.smartpet.online.utilities.Constants.USER_NAME;

public class HomeFragment extends BaseFragment {

    private MaterialTextView userName;
    private MaterialCardView postPetBtn, findPet, findDoctor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                requireActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if (!isLoggedIn()) {
            navigateFragment(R.id.signInFragment);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                PermissionsUtils permissionsUtils = PermissionsUtils.getInstance(requireActivity());
                if (permissionsUtils.isAllPermissionAvailable()) {
                    Log.d("TAG", "onCreate: permission accepted");
                } else {
                    permissionsUtils.setActivity(requireActivity());
                    permissionsUtils.requestPermissionsIfDenied();
                }
            }
            userName = view.findViewById(R.id.userName);
            postPetBtn = view.findViewById(R.id.postPetBtn);
            findPet = view.findViewById(R.id.findPet);
            findDoctor = view.findViewById(R.id.findDoctor);
            try {
                fetchUserData();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String text = SharedPrefUtils.getStringData(requireContext(), USER_NAME);
            userName.setText("Welcome: " + text);

            postPetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateFragment(R.id.postPetFragment);
                }
            });
            findPet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateFragment(R.id.findPetFragment);
                }
            });
            findDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateFragment(R.id.findDoctorFragment);
                }
            });
        }
        return view;
    }
}