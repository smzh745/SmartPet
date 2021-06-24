package com.smartpet.online.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.smartpet.online.R;
import com.smartpet.online.utilities.Constants;
import com.smartpet.online.utilities.InternetConnection;
import com.smartpet.online.utilities.RequestHandler;
import com.smartpet.online.utilities.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.smartpet.online.utilities.Constants.IS_LOGGIN;
import static com.smartpet.online.utilities.Constants.LOGIN_USER;
import static com.smartpet.online.utilities.Constants.REGISTER_USER;
import static com.smartpet.online.utilities.Constants.USER_DATA;


public class SignInFragment extends BaseFragment {

    private TextInputEditText email, pass;
    private MaterialButton loginBtn;
    private Spinner selectUser;

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
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        if (isLoggedIn()) {
            navigateFragment(R.id.homeFragment);
        } else {
            email = view.findViewById(R.id.email);
            pass = view.findViewById(R.id.pass);
            loginBtn = view.findViewById(R.id.loginBtn);
            MaterialTextView forgetPassBtn = view.findViewById(R.id.forgetPassBtn);
            MaterialTextView openSignUp = view.findViewById(R.id.openSignUp);
            selectUser = view.findViewById(R.id.selectUser);
            setSpinner(R.array.user_array, selectUser);

            openSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateFragment(R.id.signUpFragment);
                }
            });
            forgetPassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateFragment(R.id.forgetPasswordFragment);
                }
            });

        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUder = selectUser.getSelectedItem().toString();

                if (checkTextEmpty(pass) || checkTextEmpty(email)) {
                    showToast("Please fill the field");
                } else if (Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && pass.getText().length() >= 6) {
                    if (InternetConnection.checkConnection(requireContext())) {
                        if (selectedUder.equalsIgnoreCase("Select User")) {
                            showToast("Please select a user");
                        } else {
                            loginUser();
                        }

                    } else {
                        showToast("No internet connection");
                    }
                } else {
                    showToast("Please check error before proceed!");
                }
            }
        });
        return view;
    }

    private void loginUser() {
        try {
            setProgressDialog("Authenticating user...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    LOGIN_USER,
                    response -> {
                        try {
                            try {
                                Log.d(Constants.TAG, "loginUser: " + response);
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    SharedPrefUtils.saveData(requireContext(), IS_LOGGIN, true);
                                    SharedPrefUtils.saveData(requireContext(), USER_DATA, obj.getString("data"));

                                    navigateFragment(R.id.homeFragment);
                                    fetchUserData();
                                } else {
                                    showToast(obj.getString("message"));
                                }
                                cancelProgressDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                cancelProgressDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        try {
                            cancelProgressDialog();
                            showToast(error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    params.put("email", email.getText().toString());
                    params.put("password", pass.getText().toString());
                    params.put("utype", selectUser.getSelectedItem().toString());

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestHandler.getInstance(requireContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}