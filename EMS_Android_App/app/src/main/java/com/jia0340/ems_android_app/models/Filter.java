package com.jia0340.ems_android_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Class representing a Filter object.
 *
 * @author Willem Taylor
 * Created on 3/28/21
 */

public class Filter implements Parcelable {
    private FilterField mFilterField;
    private String mFilterValue;

    public Filter(FilterField filterField, String filterValue) {
        mFilterField = filterField;
        mFilterValue = filterValue;
    }

    protected Filter(Parcel in) {
        mFilterField = FilterField.valueOf(in.readString());
        mFilterValue = in.readString();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFilterField.name());
        dest.writeString(mFilterValue);
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
     * Getter for mFilterValues.
     *
     * @return the filter values
     */
    public String getFilterValues() {
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
