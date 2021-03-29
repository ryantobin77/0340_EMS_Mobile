package com.jia0340.ems_android_app.models;

import java.util.ArrayList;

/**
 * Class representing a Filter object.
 *
 * @author Willem Taylor
 * Created on 3/28/21
 */

public class Filter {
    private FilterField mFilterField;
    private ArrayList<String> mFilterValues;

    public Filter(FilterField filterField, ArrayList<String> filterValues) {
        mFilterField = filterField;
        mFilterValues = filterValues;
    }
}
