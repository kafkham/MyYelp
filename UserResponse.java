package com.example.myyelp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserResponse {

    @SerializedName("businesses")
    public ArrayList<Business> businesses;

    public class Business {

        @SerializedName("rating")
        public float rating;

        @SerializedName("price")
        public String price;

        @SerializedName("phone")
        public String phone;

        @SerializedName("id")
        public String id;

        @SerializedName("categories")
        public ArrayList<Categories> categories;

        class Categories{

            @SerializedName("title")
            public String title;
        }

        @SerializedName("name")
        public String name;

        @SerializedName("image_url")
        public String image_url;

        @SerializedName("location")
        public Location location;

        class Location {

            @SerializedName("city")
            public String city;

            @SerializedName("state")
            public String state;

            @SerializedName("address1")
            public String address1;

        }
    }

}
