package com.jia0340.ems_android_app.models;

/**
 * Class representing a Filter object.
 *
 * @author Willem Taylor
 * Created on 3/28/21
 */

public class Filter {
    private FilterField mFilterField;
    private String mFilterValue;

    public Filter(FilterField filterField, String filterValue) {
        mFilterField = filterField;
        mFilterValue = filterValue;
    }

    /**
     * Getter for mFilterField.
     *
     * @return the filter field
     */
    public FilterField getFilterField() {
        return mFilterField;
    }


    /**
     * Getter for mFilterValue.
     *
     * @return the filter value
     */
    public String getFilterValue() {
        return mFilterValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Filter)) {
            return false;
        } else {
            Filter f = (Filter) o;
            return this.mFilterField == f.mFilterField && this.mFilterValue.equals(f.mFilterValue);
        }
    }
}
