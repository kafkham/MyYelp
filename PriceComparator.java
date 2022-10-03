package com.example.myyelp;

import java.util.Comparator;

public class PriceComparator implements Comparator<UserResponse.Business> {
    @Override
    public int compare(UserResponse.Business o1, UserResponse.Business o2) {

        int o1Length;
        int o2Length;

        try {
            o1Length = o1.price.length();
        }
        catch (NullPointerException e) {
            o1Length = 0;
        }
        try {
            o2Length = o2.price.length();
        }
        catch (NullPointerException e) {
            o2Length = 0;
        }

        return Integer.compare(o1Length, o2Length);
    }
}
