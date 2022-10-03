package com.example.myyelp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesFragment extends Fragment {

    private static FavouritesFragment favouritesFragment = null;

    MyDatabaseHelper myDatabaseHelper;
    String databaseName = "FAVS";
    int dbVersion = 1;

    private final YelpAPI2 yelpAPI2 = new YelpClient2().build();
    final ArrayList<Response2> favBusinesses = new ArrayList<>();
    ArrayList<String> fav_business_IDs = new ArrayList<>();

    RecyclerView favouriteRecycler;

    public FavouritesFragment() {
    }

    public static FavouritesFragment getInstance() {
        if (favouritesFragment == null) {
            favouritesFragment = new FavouritesFragment();
        }
        return favouritesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for the fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        favouriteRecycler = v.findViewById(R.id.favourites_recycler_view);
        // create and set adapter, return recycler view
        favBusinesses.clear();
        BusinessCardAdapter adapter = new BusinessCardAdapter(this.getContext(), favBusinesses);
        favouriteRecycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        favouriteRecycler.setLayoutManager(linearLayoutManager);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //check DB and build String array of fav ids
        checkDB();
        // call Yelp API and return details for all ids
        for (int i = 0; i < fav_business_IDs.size(); i++) {
            getFavData(fav_business_IDs.get(i));
        }
    }

    public void checkDB() {
        // check database for favs
        myDatabaseHelper = new MyDatabaseHelper(getContext(), databaseName, null, dbVersion);
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("MYFAVOURITES", new String[]
                        {"YELPID"},
                null, null, null, null, null);

        fav_business_IDs.clear();
        //populate string array w/ all IDs of favs in the DB
        if (cursor.moveToFirst()) {
            fav_business_IDs.add(cursor.getString(0));
            while (cursor.moveToNext()) {
                fav_business_IDs.add(cursor.getString(0));
            }
        }
        cursor.close();
        db.close();
    }

    // Yelp API call to get individual business data, by ID (String)
    public void getFavData(String id) {

        yelpAPI2.getFav(id).enqueue(new Callback<Response2>() {
            @Override
            public void onResponse(@NonNull Call<Response2> call, @NonNull Response<Response2> response) {
                Response2 response2 = response.body();
                if (response2 != null) {
                    // for each business ID add an object to the favBusinesses business array
                    synchronized (favBusinesses) {
                        favBusinesses.add(response2);
                        BusinessCardAdapter adapter = new BusinessCardAdapter (getContext(), favBusinesses);
                        favouriteRecycler.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response2> call, @NonNull Throwable t) {
            }
        });
    }
}
