package com.jia0340.ems_android_app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jia0340.ems_android_app.R;

/**
 * Enum for Hospital Type values.
 *
 * @author Allison Nakazawa & Anna Dingler
 * Created on 1/24/21
 */
public enum HospitalType {

    @JsonProperty("Adult Trauma Centers-Level I")
    ADULT_TRAUMA_CENTER_LEVEL_I(R.drawable.person_1, R.string.adult_trauma_center_1),

    @JsonProperty("Adult Trauma Centers-Level II")
    ADULT_TRAUMA_CENTER_LEVEL_II(R.drawable.person_2, R.string.adult_trauma_center_2),

    @JsonProperty("Adult Trauma Centers-Level III")
    ADULT_TRAUMA_CENTER_LEVEL_III(R.drawable.person_3, R.string.adult_trauma_center_3),

    @JsonProperty("Adult Trauma Centers-Level IV")
    ADULT_TRAUMA_CENTER_LEVEL_IV(R.drawable.person_4, R.string.adult_trauma_center_4),

    @JsonProperty("Pediatric Trauma Centers-Pediatric Level I")
    PEDIATRIC_TRAUMA_CENTER_LEVEL_I(R.drawable.child_1, R.string.pediatric_trauma_center_1),

    @JsonProperty("Pediatric Trauma Centers-Pediatric Level II")
    PEDIATRIC_TRAUMA_CENTER_LEVEL_II(R.drawable.child_2, R.string.pediatric_trauma_center_2),

    @JsonProperty("Stroke Centers-Comprehensive Stroke Center")
    COMPREHENSIVE_STROKE_CENTER(R.drawable.brain_comprehensive, R.string.comprehensive_stroke_center),

    @JsonProperty("Stroke Centers-Thrombectomy Capable Stroke Center")
    THROMBECTOMY_CAPABLE_STROKE_CENTER(R.drawable.brain_thrombectomy, R.string.thrombectomy_stroke_center),

    @JsonProperty("Stroke Centers-Primary Stroke Center")
    PRIMARY_STROKE_CENTER(R.drawable.brain, R.string.primary_stroke_center),

    @JsonProperty("Stroke Centers-Remote Treatment Stroke Center")
    REMOTE_TREATMENT_STROKE_CENTER(R.drawable.brain_remote, R.string.remote_treatment_stroke_center),

    @JsonProperty("Emergency Cardiac Care Center-Level I ECCC")
    EMERGENCY_CARDIAC_CARE_CENTER_LEVEL_I(R.drawable.heart_1, R.string.eccc_1),

    @JsonProperty("Emergency Cardiac Care Center-Level II ECCC")
    EMERGENCY_CARDIAC_CARE_CENTER_LEVEL_II(R.drawable.heart_2, R.string.eccc_2),

    @JsonProperty("Emergency Cardiac Care Center-Level III ECCC")
    EMERGENCY_CARDIAC_CARE_CENTER_LEVEL_III(R.drawable.heart_3, R.string.eccc_3),

    @JsonProperty("Neonatal Center Designation-Level I Neonatal Center")
    NEONATAL_CENTER_LEVEL_I(R.drawable.baby_1, R.string.neonatal_center_1),

    @JsonProperty("Neonatal Center Designation-Level II Neonatal Center")
    NEONATAL_CENTER_LEVEL_II(R.drawable.baby_2, R.string.neonatal_center_2),

    @JsonProperty("Neonatal Center Designation-Level III Neonatal Center")
    NEONATAL_CENTER_LEVEL_III(R.drawable.baby_3, R.string.neonatal_center_3),

    @JsonProperty("Maternal Center Designation-Level I Maternal Center")
    MATERNAL_CENTER_LEVEL_I(R.drawable.pregnant_1, R.string.maternal_center_1),

    @JsonProperty("Maternal Center Designation-Level II Maternal Center")
    MATERNAL_CENTER_LEVEL_II(R.drawable.pregnant_2, R.string.maternal_center_2),

    @JsonProperty("Maternal Center Designation-Level III Maternal Center")
    MATERNAL_CENTER_LEVEL_III(R.drawable.pregnant_3, R.string.maternal_center_3);

    private int imageId;
    private int stringId;
    HospitalType(int imageId, int stringId) {
        this.imageId = imageId;
        this.stringId = stringId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getStringId() {
        return stringId;
    }
}
