package com.example.myyelp;

import android.content.Context;
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

public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.ViewHolder> {

    MyDatabaseHelper myDatabaseHelper;
    String databaseName = "FAVS";
    int dbVersion = 1;

    Context m_context;
    private final ArrayList<Response2> favBusinesses;

    public BusinessCardAdapter(Context context, ArrayList<Response2> favBusinesses) {
        this.m_context = context;
        this.favBusinesses = favBusinesses;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_business_details, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> removeFromFavorites(holder.getAdapterPosition()));

        Response2 response2 = favBusinesses.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.list_item_imageView);
        Glide.with(m_context.getApplicationContext()).load(response2.image_url).into(imageView);
        TextView businessName = cardView.findViewById(R.id.business_name);
        businessName.setText(response2.name);
        RatingBar rating = cardView.findViewById(R.id.ratingBar);
        rating.setRating(response2.rating);
        TextView priceAndCategories = cardView.findViewById(R.id.priceAndCategories);
        StringBuilder s = Optional.ofNullable(response2.price).map(StringBuilder::new).orElse(null);
        if (s == null) {
            s = new StringBuilder(" | ");
        }
        else
        {
            s.append(" | ");
        }
        for (int i = 0; i < response2.categories.size(); i++) {
            if (i == response2.categories.size() - 1) {
                s.append(response2.categories.get(i).title);
                break;
            }
            s.append(response2.categories.get(i).title).append(", ");
        }
        priceAndCategories.setText(s.toString());
        TextView phone = cardView.findViewById(R.id.phone);
        phone.setText(response2.display_phone);
        TextView address = cardView.findViewById(R.id.address);
        address.setText(String.format("%s, %s, %s", response2.location.address1, response2.location.city, response2.location.state));
    }

    @Override
    public int getItemCount() {
        return favBusinesses.size();
    }

    // launch alert dialog and then add to favourites list in database
    public void removeFromFavorites(int position) {
        // alert dialog methods for adding to favorites
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
        builder.setTitle("Remove from Favourites?")
                .setMessage("Do you want to remove this item from Favourites?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    //what happens if YES remove from favorites
                    //del from DB
                    myDatabaseHelper = new MyDatabaseHelper(m_context, databaseName, null, dbVersion);
                    SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                    db.delete("MYFAVOURITES", "YELPID = ?", new String[] {FavouritesFragment.getInstance().favBusinesses.get(position).id});
                    db.close();
                    Toast.makeText(m_context, "Removed from Favourites\nThis will be apparent next time you load Favourites", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    //nothing
                })
                .create().show();
    }
}