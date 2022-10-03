package com.example.myyelp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response2 {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("image_url")
        public String image_url;

        @SerializedName("display_phone")
        public String display_phone;

        @SerializedName("categories")
        public ArrayList<Categories> categories;

        class Categories{

            @SerializedName("title")
            public String title;

        }

        @SerializedName("rating")
        public float rating;

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
        @SerializedName("price")
        public String price;
    }