package com.smartpet.online.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.smartpet.online.R;
import com.smartpet.online.utilities.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.smartpet.online.utilities.Constants.IS_LOGGIN;
import static com.smartpet.online.utilities.Constants.USER_DATA;
import static com.smartpet.online.utilities.Constants.USER_EMAIL;
import static com.smartpet.online.utilities.Constants.USER_ID;
import static com.smartpet.online.utilities.Constants.USER_NAME;
import static com.smartpet.online.utilities.Constants.USER_NUMBER;

public class BaseFragment extends Fragment {
    public View view;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(requireActivity());
    }

    //TODO: check login
    public boolean isLoggedIn() {
        return SharedPrefUtils.getBooleanData(requireContext(), IS_LOGGIN);
    }

    //TODO: show message on screen
    public void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    //TODO: navigate to fragment
    void navigateFragment(int id) {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(id);
    }

    public void setSpinner(int array, Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    //TODO: show progress dialog
    public void setProgressDialog(String message) {
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //TODO: cancel progress dialog
    public void cancelProgressDialog() {

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public boolean checkTextEmpty(TextInputEditText textInputEditText) {
        return TextUtils.isEmpty(textInputEditText.getText().toString());
    }

    public String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public void fetchUserData() throws JSONException {
        JSONArray jsonArray= new JSONArray(SharedPrefUtils.getStringData(requireContext(),USER_DATA).toString());
        for (int i=0;i<=jsonArray.length();i++){
            JSONObject jsonObject= jsonArray.getJSONObject(i);
            SharedPrefUtils.saveData(requireContext(),USER_ID,jsonObject.getString("uid"));
            SharedPrefUtils.saveData(requireContext(),USER_NAME,jsonObject.getString("name"));
            SharedPrefUtils.saveData(requireContext(),USER_NUMBER,jsonObject.getString("phonenumber"));
            SharedPrefUtils.saveData(requireContext(),USER_EMAIL,jsonObject.getString("email"));
        }
    }
}
