package com.example.myyelp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static SearchFragment searchFragment = null;
    private final YelpAPI yelpAPI = new YelpClient().build();

    ArrayList<UserResponse.Business> myBusinessList = new ArrayList<>();

    boolean firstTime = true;
    String searchTerm = "sushi";

    Spinner spinner;
    RecyclerView searchRecycler;
    SearchResultAdapter adapter;

    public SearchFragment() {
        getYelpData(searchTerm);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialize a new adapter, unless this fragment has been retained
        if (adapter == null) {
            adapter = new SearchResultAdapter(this.getContext(), getMyBusinessList());
        }

        // inflate the layout for the fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        searchRecycler = v.findViewById(R.id.search_recycler_view);

        // set adapter, return recycler view
        searchRecycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        searchRecycler.setLayoutManager(linearLayoutManager);

        // search view methods
        SearchView searchView = v.findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getYelpData(query);
                spinner.setSelection(0);
                searchTerm = String.valueOf(query);
                adapter = new SearchResultAdapter(getContext(), myBusinessList);
                searchRecycler.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // spinner sort methods
        spinner = v.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addListData(position);
                adapter = new SearchResultAdapter(getContext(), getMyBusinessList());
                searchRecycler.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }

    public ArrayList<UserResponse.Business> getMyBusinessList() {
        return myBusinessList;
    }

    public void setMyBusinessList(ArrayList<UserResponse.Business> newBusinessList) {
        this.myBusinessList.clear();
        this.myBusinessList.addAll(newBusinessList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (firstTime) {
            getYelpData(searchTerm);
            firstTime = false;
        }
        setRetainInstance(true);
    }

    public static SearchFragment getInstance() {
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        return searchFragment;
    }

    // API call method
    public void getYelpData(String term) {

        String location = "Montreal";
        yelpAPI.getUser(term, location).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                UserResponse res = response.body();
                if (res != null) {
                    setMyBusinessList(res.businesses);
                    adapter = new SearchResultAdapter(getContext(), getMyBusinessList());
                    searchRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
            }
        });
    }

    // update and sort search result list
    public void addListData(int i) {

        ArrayList<UserResponse.Business> sorted = new ArrayList<>(getMyBusinessList());
        switch (i) {
            default:
                break;
            case 1:
                sorted.sort(new PriceComparator());
                break;
            case 2:
                sorted.sort(new RatingComparator());
                break;
        }
        setMyBusinessList(sorted);
    }
}