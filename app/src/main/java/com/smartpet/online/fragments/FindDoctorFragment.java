package com.smartpet.online.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartpet.online.R;
import com.smartpet.online.adapters.FindDoctorAdapter;
import com.smartpet.online.models.FindDoctor;
import com.smartpet.online.utilities.Constants;
import com.smartpet.online.utilities.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.smartpet.online.utilities.Constants.FIND_DOCTOR;


public class FindDoctorFragment extends BaseFragment {
    private FindDoctorAdapter adapter;
    private ArrayList<FindDoctor> findPetArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_find_doctor, container, false);
        findPetArrayList = new ArrayList<>();
        ImageView backArrow = view.findViewById(R.id.backArrow);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        backArrow.setOnClickListener(view -> navigateFragment(R.id.homeFragment));
        initSearchView();
        fetchAllDoctors();
        return view;
    }

    private void fetchAllDoctors() {
        try {
            setProgressDialog("Loading...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    FIND_DOCTOR,
                    response -> {
                        try {
                            try {
                                Log.d(Constants.TAG, "loginUser: " + response);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    findPetArrayList.add(new FindDoctor(jsonObject.getString("uid"),
                                            jsonObject.getString("name"), jsonObject.getString("phonenumber"),
                                            jsonObject.getString("sName"), jsonObject.getString("slocation"),
                                            jsonObject.getString("utype")));
                                }
                                adapter = new FindDoctorAdapter(requireActivity(), findPetArrayList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                                recyclerView.setAdapter(adapter);
                                cancelProgressDialog();
                            } catch (Exception e) {
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
                    });

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

    private void initSearchView() {
        try {

            searchView.onActionViewExpanded();
            searchView.clearFocus();
            searchView.setQueryHint(Html.fromHtml(
                    "<font color = #FFFFFF>" + getString(
                            R.string.keyword_doctor
                    ) + "</font>"));

            searchView.setQueryHint(getString(R.string.keyword_doctor));
            SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);

                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}