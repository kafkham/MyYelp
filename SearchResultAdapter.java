package com.example.myyelp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Optional;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    MyDatabaseHelper myDatabaseHelper;
    String databaseName = "FAVS";
    int dbVersion = 1;

    Context m_context;
    private final ArrayList<UserResponse.Business> myBusinesses;

    public SearchResultAdapter(Context context, ArrayList<UserResponse.Business> myBusinesses) {
        this.m_context = context;
        this.myBusinesses = myBusinesses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_business_details, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> addToFavorites(holder.getAdapterPosition()));

        UserResponse.Business business = myBusinesses.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.list_item_imageView);
        Glide.with(m_context.getApplicationContext()).load(business.image_url).into(imageView);
        TextView businessName = cardView.findViewById(R.id.business_name);
        businessName.setText(business.name);
        RatingBar rating = cardView.findViewById(R.id.ratingBar);
        rating.setRating(business.rating);
        TextView priceAndCategories = cardView.findViewById(R.id.priceAndCategories);
        StringBuilder s = Optional.ofNullable(business.price).map(StringBuilder::new).orElse(null);
        if (s == null) {
            s = new StringBuilder(" | ");
        }
        else
        {
            s.append(" | ");
        }
        for (int i = 0; i < business.categories.size(); i++) {
            if (i == business.categories.size() - 1) {
                s.append(business.categories.get(i).title);
                break;
            }
            s.append(business.categories.get(i).title).append(", ");
        }
        priceAndCategories.setText(s.toString());
        TextView phone = cardView.findViewById(R.id.phone);
        phone.setText(business.phone);
        TextView address = cardView.findViewById(R.id.address);
        address.setText(String.format("%s, %s, %s", business.location.address1, business.location.city, business.location.state));
    }

    @Override
    public int getItemCount() {
        try {
            return myBusinesses.size();
        }
        catch (NullPointerException e) {
            return 0;
        }
    }

    // launch alert dialog and then add to favourites list in database
    public void addToFavorites(int position) {
        // alert dialog methods for adding to favorites
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
        builder.setTitle("Add to favourites?")
                .setMessage("Do you want to add this item to favourites?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    //what happens if YES add to favorites
                    //check DB
                    myDatabaseHelper = new MyDatabaseHelper(m_context, databaseName, null, dbVersion);
                    SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                    Cursor cursor = db.query("MYFAVOURITES", new String[]
                                    {"YELPID"},
                            null, null, null, null, null);

                    //create list of all favourite IDs
                    ArrayList<String> fav_business_IDs = new ArrayList<>();
                    if (cursor.moveToFirst()) {
                        fav_business_IDs.add(cursor.getString(0));
                        while (cursor.moveToNext()) {
                            fav_business_IDs.add(cursor.getString(0));
                        }
                    }
                    cursor.close();
                    db.close();

                    //if it's not already in the favs list, add it, otherwise notify that it's there already
                    if (!fav_business_IDs.contains(SearchFragment.getInstance().myBusinessList.get(position).id)) {
                        db = myDatabaseHelper.getWritableDatabase();
                        ContentValues favValues = new ContentValues();
                        favValues.put("YELPID", SearchFragment.getInstance().myBusinessList.get(position).id);
                        db.insert("MYFAVOURITES", null, favValues);
                        Toast.makeText(m_context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(m_context, "Already in Favourites", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    //what happens if NO don't add to favorites
                    //nothing
//                    Toast.makeText(m_context, "Nothing happened...", Toast.LENGTH_SHORT).show();
                })
                .create().show();
    }
}