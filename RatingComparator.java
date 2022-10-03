package com.example.myyelp;

import java.util.Comparator;

public class RatingComparator implements Comparator<UserResponse.Business> {

    @Override
    public int compare(UserResponse.Business o1, UserResponse.Business o2) {
        return Float.compare(o2.rating, o1.rating);
    }
}