package com.jia0340.ems_android_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jia0340.ems_android_app.models.Filter;
import com.jia0340.ems_android_app.models.FilterField;
import com.jia0340.ems_android_app.models.HospitalType;

import java.util.ArrayList;
import java.util.List;

public class FilterSheetDialog extends BottomSheetDialogFragment {

    FilterDialogListener mListener;
    List<Filter> mFilterList;
    static final String[] COUNTIES = {"Appling", "Bacon", "Baldwin", "Barrow", "Bartow", "Ben Hill",
            "Berrien", "Bibb", "Bleckley", "Brooks", "Bulloch", "Burke", "Butts", "Camden",
            "Candler", "Carroll", "Catoosa", "Chatham", "Cherokee", "Clarke", "Clayton", "Clinch",
            "Cobb", "Coffee", "Colquitt", "Cook", "Coweta", "Crisp", "DeKalb", "Decatur", "Dodge",
            "Dougherty", "Douglas", "Early", "Effingham", "Elbert", "Emanuel", "Evans", "Fannin",
            "Fayette", "Floyd", "Forsyth", "Franklin", "Fulton", "Glynn", "Gordon", "Grady",
            "Greene", "Gwinnett", "Habersham", "Hall", "Haralson", "Henry", "Houston", "Irwin",
            "Jasper", "Jeff Davis", "Jefferson", "Jenkins", "Lanier", "Laurens", "Liberty",
            "Lowndes", "Lumpkin", "Macon", "McDuffie", "Meriwether", "Miller", "Mitchell", "Monroe",
            "Morgan", "Murray", "Muscogee", "Newton", "Paulding", "Peach", "Pickens", "Polk",
            "Pulaski", "Putnam", "Rabun", "Randolph", "Richmond", "Rockdale", "Screven", "Seminole",
            "Spalding", "Stephens", "Sumter", "Tattnall", "Thomas", "Tift", "Toombs", "Towns",
            "Troup", "Union", "Upson", "Walton", "Ware", "Washington", "Wayne", "Whitfield",
            "Wilkes", "Worth"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.filter_dialog_layout, container, false);

        // Get applied filter list from saved state or initialize with empty ArrayList<Filter> if saved state is null
        mFilterList = savedInstanceState != null ? savedInstanceState.getParcelableArrayList("FILTER_LIST") : new ArrayList<Filter>();

        Context context = getContext();
        Drawable expandImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.expand, null);
        Drawable collapseImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.collapse, null);

        // Handle Specialty Centers
        Button specialtyCenterButton = view.findViewById(R.id.specialtyCenterButton);
        ScrollView specialtyCenterScrollView = view.findViewById(R.id.specialtyCenterScrollView);
        LinearLayout specialtyCenterValues = view.findViewById(R.id.specialtyCenterValues);

        specialtyCenterButton.setOnClickListener(listener -> {
            if (specialtyCenterScrollView.getVisibility() == View.VISIBLE) {
                specialtyCenterScrollView.setVisibility(View.GONE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                specialtyCenterScrollView.setVisibility(View.VISIBLE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
            }
        });

        // Make EMS Region check boxes
        for (HospitalType type: HospitalType.values()) {
            String specialtyCenter = context.getString(type.getStringId());
            CheckBox checkBox = makeCheckBox(FilterField.HOSPITAL_TYPES, specialtyCenter);
            // Add Checkbox to LinearLayout
            if (specialtyCenterValues != null) {
                specialtyCenterValues.addView(checkBox);
            }
        }

        // Handle EMS Regions
        Button emsRegionButton = view.findViewById(R.id.emsRegionButton);
        ScrollView emsRegionScrollView = view.findViewById(R.id.emsRegionScrollView);
        LinearLayout emsRegionValues = view.findViewById(R.id.emsRegionValues);

        emsRegionButton.setOnClickListener(listener -> {
            if (emsRegionScrollView.getVisibility() == View.VISIBLE) {
                emsRegionScrollView.setVisibility(View.GONE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                emsRegionScrollView.setVisibility(View.VISIBLE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
            }
        });

        // Make EMS Region check boxes
        for (int i = 1; i <= 10; i++) {
            String emsRegion = String.valueOf(i);
            CheckBox checkBox = makeCheckBox(FilterField.COUNTY, emsRegion);
            // Add Checkbox to LinearLayout
            if (emsRegionValues != null) {
                emsRegionValues.addView(checkBox);
            }
        }

        // Handle counties
        Button countyButton = view.findViewById(R.id.countyButton);
        ScrollView countyScrollView = view.findViewById(R.id.countyScrollView);
        LinearLayout countyValues = view.findViewById(R.id.countyValues);

        countyButton.setOnClickListener(listener -> {
            if (countyScrollView.getVisibility() == View.VISIBLE) {
                countyScrollView.setVisibility(View.GONE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                countyScrollView.setVisibility(View.VISIBLE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
            }
        });

        // Make County check boxes
        for (int i = 0; i < COUNTIES.length; i++) {
            String county = COUNTIES[i];
            CheckBox checkBox = makeCheckBox(FilterField.COUNTY, county);
            // Add Checkbox to LinearLayout
            if (countyValues != null) {
                countyValues.addView(checkBox);
            }
        }

        // Handle regional coordinating hospital
        Button rchButton = view.findViewById(R.id.rchButton);
        ScrollView rchScrollView = view.findViewById(R.id.rchScrollView);
        LinearLayout rchValues = view.findViewById(R.id.rchValues);

        rchButton.setOnClickListener(listener -> {
            if (rchScrollView.getVisibility() == View.VISIBLE) {
                rchScrollView.setVisibility(View.GONE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                rchScrollView.setVisibility(View.VISIBLE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
            }
        });

        // Make Regional Coordinating Hospital check boxes
        for (int i = 0; i < 14; i++) {
            String rch = Character.toString((char)(i + 65));
            CheckBox checkBox = makeCheckBox(FilterField.REGIONAL_COORDINATING_HOSPITAL, rch);
            // Add Checkbox to LinearLayout
            if (rchValues != null) {
                rchValues.addView(checkBox);
            }
        }

        // Setup exit button handling
        view.findViewById(R.id.exitButton).setOnClickListener(listener -> {
            dismiss();
        });

        // Setup clear all button handling
        view.findViewById(R.id.clearAllButton).setOnClickListener(listener -> {
            mFilterList = new ArrayList<Filter>();
            for (int i = 0; i < specialtyCenterValues.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) specialtyCenterValues.getChildAt(i);
                checkBox.setChecked(false);
            }
            for (int i = 0; i < emsRegionValues.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) emsRegionValues.getChildAt(i);
                checkBox.setChecked(false);
            }
            for (int i = 0; i < countyValues.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) countyValues.getChildAt(i);
                checkBox.setChecked(false);
            }
            for (int i = 0; i < rchValues.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) rchValues.getChildAt(i);
                checkBox.setChecked(false);
            }
        });

        // Setup done button handling
        view.findViewById(R.id.doneButton).setOnClickListener(listener -> {
            mListener.onFilterSelected(mFilterList);
            dismiss();
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("FILTER_LIST",(ArrayList<Filter>) mFilterList);
    }

    public interface FilterDialogListener {
        void onFilterSelected(List<Filter> filterList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (FilterDialogListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " need to implement FilterDialogListener");
        }
    }

    /**
     * Helper method to make a checkbox.
     *
     * @param field FilterField that the CheckBox belongs to
     * @param value value of the Filter for the CheckBox
     * @return a CheckBox for a specific Filter value
     */
    private CheckBox makeCheckBox(FilterField field, String value) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(value);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFilterList.add(new Filter(field, value));
                } else {
                    mFilterList.remove(new Filter(field, value));
                }
            }
        });

        // checks if a filter is applied on this value
        if (mFilterList.contains(new Filter(field, value))) {
            checkBox.isChecked();
        }

        return checkBox;
    }
}
