package com.smartpet.online.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import com.smartpet.online.activiities.ViewPetActivity;
import com.smartpet.online.activiities.ViewShopActivity;
import com.smartpet.online.adapters.FindShopAdapter;
import com.smartpet.online.models.FindShop;
import com.smartpet.online.utilities.ClickListener;
import com.smartpet.online.utilities.Constants;
import com.smartpet.online.utilities.RecyclerTouchListener;
import com.smartpet.online.utilities.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.smartpet.online.utilities.Constants.FIND_SHOP;

public class FIndShopFragment extends BaseFragment {
    private FindShopAdapter adapter;
    private ArrayList<FindShop> findPetArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_f_ind_shop, container, false);
        findPetArrayList = new ArrayList<>();
        ImageView backArrow = view.findViewById(R.id.backArrow);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        backArrow.setOnClickListener(view -> navigateFragment(R.id.homeFragment));
        initSearchView();
        fetchAllShops();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(requireContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(requireActivity(), ViewShopActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("list", findPetArrayList);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    private void fetchAllShops() {
        try {
            setProgressDialog("Loading...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    FIND_SHOP,
                    response -> {
                        try {
                            try {
                                Log.d(Constants.TAG, "loginUser: " + response);
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    findPetArrayList.add(new FindShop(jsonObject.getString("uid"),
                                            jsonObject.getString("name"), jsonObject.getString("phonenumber"),
                                            jsonObject.getString("sName"), jsonObject.getString("slocation"),
                                            jsonObject.getString("utype")));
                                }
                                adapter = new FindShopAdapter(requireActivity(), findPetArrayList);
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
                            R.string.keyword_shop
                    ) + "</font>"));

            searchView.setQueryHint(getString(R.string.keyword_shop));
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