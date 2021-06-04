package com.smartpet.online.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.textview.MaterialTextView;
import com.smartpet.online.R;
import com.smartpet.online.utilities.SharedPrefUtils;

import org.json.JSONException;

import static com.smartpet.online.utilities.Constants.USER_NAME;

public class HomeFragment extends BaseFragment {

    private MaterialTextView userName;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if (!isLoggedIn()) {
            navigateFragment(R.id.signInFragment);
        } else {
            userName = view.findViewById(R.id.userName);
            try {
                fetchUserData();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String text=SharedPrefUtils.getStringData(requireContext(),USER_NAME);
            userName.setText("Welcome: "+text);
        }
        return view;
    }
}