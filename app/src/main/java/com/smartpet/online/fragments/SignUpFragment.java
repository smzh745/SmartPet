package com.smartpet.online.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.smartpet.online.utilities.Constants.REGISTER_USER;


public class SignUpFragment extends BaseFragment {
    private TextInputLayout  snameinput;
    private TextInputEditText email, pass, phone, name, sname, saddress;
    private Spinner selectUser;
    private LinearLayout layoutOther;

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
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ImageView backArrow = view.findViewById(R.id.backArrow);
        MaterialTextView openSignIn = view.findViewById(R.id.openSignIn);
        MaterialButton signUpBtn = view.findViewById(R.id.signUpBtn);
        selectUser = view.findViewById(R.id.selectUser);
        email = view.findViewById(R.id.email);
        pass = view.findViewById(R.id.pass);
        phone = view.findViewById(R.id.phone);
        name = view.findViewById(R.id.name);
        layoutOther = view.findViewById(R.id.layoutOther);
        snameinput = view.findViewById(R.id.snameinput);
        sname = view.findViewById(R.id.sname);
        saddress = view.findViewById(R.id.saddress);


        setSpinner(R.array.user_array, selectUser);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(R.id.signInFragment);
            }
        });
        openSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(R.id.signInFragment);
            }
        });
        selectUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos == 0) {
                    layoutOther.setVisibility(View.GONE);

                } else if (pos == 1) {
                    layoutOther.setVisibility(View.GONE);

                } else if (pos == 2) {
                    layoutOther.setVisibility(View.VISIBLE);
                    snameinput.setHint(getString(R.string.enter_clinic_name));

                } else if (pos == 3) {
                    layoutOther.setVisibility(View.VISIBLE);
                    snameinput.setHint(getString(R.string.enter_shop_name));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                layoutOther.setVisibility(View.GONE);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUder = selectUser.getSelectedItem().toString();
                if (checkTextEmpty(name) || checkTextEmpty(pass) || checkTextEmpty(email) || checkTextEmpty(phone)) {
                    showToast("Please fill the field");
                } else if (layoutOther.getVisibility() == View.VISIBLE) {
                    if (checkTextEmpty(sname) || checkTextEmpty(saddress)) {
                        showToast("Please fill the field");
                    }
                    else if (Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && pass.getText().length() >= 6) {
                        if (InternetConnection.checkConnection(requireContext())) {
                            if (selectedUder.equalsIgnoreCase("Select User")) {
                                showToast("Please select a user");
                            } else {
                                registerUser();
                            }

                        } else {
                            showToast("No internet connection");
                        }
                    } else {
                        showToast("Please check error before proceed!");
                    }
                } else if (Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && pass.getText().length() >= 6) {
                    if (InternetConnection.checkConnection(requireContext())) {
                        if (selectedUder.equalsIgnoreCase("Select User")) {
                            showToast("Please select a user");
                        } else {
                            registerUser();
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

    private void registerUser() {
        try {
            setProgressDialog("Authenticating user...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    REGISTER_USER,
                    response -> {
                        try {
                            try {
                                Log.d(Constants.TAG, "registerUser: "+response);
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    showToast(obj.getString("message"));
                                    navigateFragment(R.id.signInFragment);
                                    showToast("Use same login to sign in");
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
                    params.put("name", name.getText().toString());
                    params.put("phone", phone.getText().toString());
                    params.put("sName", sname.getText().toString());
                    params.put("slocation", saddress.getText().toString());
                    params.put("utype", selectUser.getSelectedItem().toString());
                    params.put("createDate", getCurrentDateTime());

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
                public void retry(VolleyError error) {

                }
            });
            RequestHandler.getInstance(requireContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}