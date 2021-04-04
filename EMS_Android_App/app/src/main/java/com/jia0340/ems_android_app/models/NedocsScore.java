package com.jia0340.ems_android_app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jia0340.ems_android_app.R;

/**
 * Enum for Nedocs Score values.
 *
 * @author Allison Nakazawa & Anna Dingler
 * Created on 1/24/21
 */
public enum NedocsScore {
    @JsonProperty("Normal")
    NORMAL(R.color.normal, R.string.normal),
    @JsonProperty("Busy")
    BUSY(R.color.busy, R.string.busy),
    @JsonProperty("Overcrowded")
    OVERCROWDED(R.color.overcrowded, R.string.overcrowded),
    @JsonProperty("Severe")
    SEVERE(R.color.severe, R.string.severe);

    private int mColor;
    private int mLabel;

    NedocsScore(int color, int label) {
        mColor = color;
        mLabel = label;
    }

    public int getColor() {
        return mColor;
    }

    public int getLabel() {
        return mLabel;
    }
}
