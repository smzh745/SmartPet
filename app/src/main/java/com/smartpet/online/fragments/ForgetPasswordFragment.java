package com.smartpet.online.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.smartpet.online.utilities.GMailSender;
import com.smartpet.online.utilities.InternetConnection;
import com.smartpet.online.utilities.RequestHandler;
import com.smartpet.online.utilities.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.smartpet.online.utilities.Constants.FORGET_PASSWORD;
import static com.smartpet.online.utilities.Constants.IS_LOGGIN;
import static com.smartpet.online.utilities.Constants.LOGIN_USER;
import static com.smartpet.online.utilities.Constants.USER_DATA;


public class ForgetPasswordFragment extends BaseFragment {

    private ImageView backArrow;
    private TextInputLayout emailinput;
    private TextInputEditText email;
    private MaterialButton loginBtn;
    private MaterialTextView backSignInBtn;
    private Spinner selectUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navigateFragment(R.id.signInFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        backArrow = view.findViewById(R.id.backArrow);
        emailinput = view.findViewById(R.id.emailinput);
        email = view.findViewById(R.id.email);
        loginBtn = view.findViewById(R.id.loginBtn);
        backSignInBtn = view.findViewById(R.id.backSignInBtn);
        selectUser = view.findViewById(R.id.selectUser);
        setSpinner(R.array.user_array, selectUser);

        backSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(R.id.signInFragment);
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(R.id.signInFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkTextEmpty(email) || selectUser.getSelectedItem().toString().equalsIgnoreCase("Select User")) {
                    showToast("Please select all options");
                } else {
                    if (InternetConnection.checkConnection(requireContext())) {

                        forgetPasswordRequest();


                    } else {
                        showToast("No internet connection");
                    }
                }
            }
        });
        return view;
    }

    private void forgetPasswordRequest() {
        try {
            setProgressDialog("Authenticating user...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    FORGET_PASSWORD,
                    response -> {
                        try {
                            try {
                                Log.d(Constants.TAG, "loginUser: " + response);
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    try {
                                        GMailSender sender = new GMailSender("smartpetapp12@gmail.com", "smartpetapp1234");
                                        sender.sendMail("Smart Pet Password",
                                                "This is your password for Smart Pet App  you forgot it. \n\n" +
                                                        "Password is: " + obj.getString("data") + "\n\n" +
                                                        "Regards \n" +
                                                        "Smart Pet Team",
                                                "smartpetapp12@gmail.com",
                                                email.getText().toString());
                                    } catch (Exception e) {
                                        Log.d(Constants.TAG, e.getMessage(), e);
                                    }
                                    showToast("We have sent you your password on your registered email address.");
                                    navigateFragment(R.id.signInFragment);
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