package com.jia0340.ems_android_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jia0340.ems_android_app.models.Filter;
import com.jia0340.ems_android_app.models.FilterField;
import com.jia0340.ems_android_app.models.HospitalType;

import java.util.ArrayList;
import java.util.List;

public class FilterSheetDialog extends BottomSheetDialogFragment {

    View mFilterDialog;
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
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.filter_dialog).getParent();
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) bottomSheet.getParent();
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
                coordinatorLayout.getParent().requestLayout();
            }
        });

        mFilterDialog = inflater.inflate(R.layout.filter_dialog_layout, container, false);
        mFilterList = new ArrayList<Filter>();

        Context context = getContext();
        Drawable expandImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.expand, null);
        Drawable collapseImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.collapse, null);

        // specialty center layouts
        Button specialtyCenterButton = mFilterDialog.findViewById(R.id.specialtyCenterButton);
        ScrollView specialtyCenterScrollView = mFilterDialog.findViewById(R.id.specialtyCenterScrollView);
        LinearLayout specialtyCenterValues = mFilterDialog.findViewById(R.id.specialtyCenterValues);
        // ems region layouts
        Button emsRegionButton = mFilterDialog.findViewById(R.id.emsRegionButton);
        ScrollView emsRegionScrollView = mFilterDialog.findViewById(R.id.emsRegionScrollView);
        LinearLayout emsRegionValues = mFilterDialog.findViewById(R.id.emsRegionValues);
        // county layouts
        Button countyButton = mFilterDialog.findViewById(R.id.countyButton);
        ScrollView countyScrollView = mFilterDialog.findViewById(R.id.countyScrollView);
        LinearLayout countyValues = mFilterDialog.findViewById(R.id.countyValues);
        // regional coordinating hospital layouts
        Button rchButton = mFilterDialog.findViewById(R.id.rchButton);
        ScrollView rchScrollView = mFilterDialog.findViewById(R.id.rchScrollView);
        LinearLayout rchValues = mFilterDialog.findViewById(R.id.rchValues);

        // Handle Specialty Centers
        specialtyCenterButton.setOnClickListener(listener -> {
            if (specialtyCenterScrollView.getVisibility() == View.VISIBLE) {
                specialtyCenterScrollView.setVisibility(View.GONE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                specialtyCenterScrollView.setVisibility(View.VISIBLE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
                emsRegionScrollView.setVisibility(View.GONE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                countyScrollView.setVisibility(View.GONE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                rchScrollView.setVisibility(View.GONE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            }
        });

        // Make EMS Region check boxes
        for (HospitalType type: HospitalType.values()) {
            String specialtyCenter = context.getString(type.getStringId());
            CheckBox checkBox = makeCheckBox(FilterField.HOSPITAL_TYPES, specialtyCenter, specialtyCenter);
            // Add Checkbox to LinearLayout
            if (specialtyCenterValues != null) {
                specialtyCenterValues.addView(checkBox);
            }
        }

        // Handle EMS Regions
        emsRegionButton.setOnClickListener(listener -> {
            if (emsRegionScrollView.getVisibility() == View.VISIBLE) {
                emsRegionScrollView.setVisibility(View.GONE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                emsRegionScrollView.setVisibility(View.VISIBLE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
                specialtyCenterScrollView.setVisibility(View.GONE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                countyScrollView.setVisibility(View.GONE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                rchScrollView.setVisibility(View.GONE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            }
        });

        // Make EMS Region check boxes
        for (int i = 1; i <= 10; i++) {
            String emsRegion = String.valueOf(i);
            CheckBox checkBox = makeCheckBox(FilterField.REGION, emsRegion, context.getString(R.string.filter_ems_region_value, emsRegion));
            // Add Checkbox to LinearLayout
            if (emsRegionValues != null) {
                emsRegionValues.addView(checkBox);
            }
        }

        // Handle counties
        countyButton.setOnClickListener(listener -> {
            if (countyScrollView.getVisibility() == View.VISIBLE) {
                countyScrollView.setVisibility(View.GONE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                countyScrollView.setVisibility(View.VISIBLE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
                specialtyCenterScrollView.setVisibility(View.GONE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                emsRegionScrollView.setVisibility(View.GONE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                rchScrollView.setVisibility(View.GONE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            }
        });

        // Make County check boxes
        for (int i = 0; i < COUNTIES.length; i++) {
            String county = COUNTIES[i];
            CheckBox checkBox = makeCheckBox(FilterField.COUNTY, county, county);
            // Add Checkbox to LinearLayout
            if (countyValues != null) {
                countyValues.addView(checkBox);
            }
        }

        // Handle regional coordinating hospital
        rchButton.setOnClickListener(listener -> {
            if (rchScrollView.getVisibility() == View.VISIBLE) {
                rchScrollView.setVisibility(View.GONE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            } else {
                rchScrollView.setVisibility(View.VISIBLE);
                rchButton.setCompoundDrawablesWithIntrinsicBounds(null, null, collapseImg, null);
                specialtyCenterScrollView.setVisibility(View.GONE);
                specialtyCenterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                emsRegionScrollView.setVisibility(View.GONE);
                emsRegionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
                countyScrollView.setVisibility(View.GONE);
                countyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, expandImg, null);
            }
        });

        // Make Regional Coordinating Hospital check boxes
        for (int i = 0; i < 14; i++) {
            String rch = Character.toString((char)(i + 65));
            CheckBox checkBox = makeCheckBox(FilterField.REGIONAL_COORDINATING_HOSPITAL, rch, context.getString(R.string.filter_rch_value, rch));
            // Add Checkbox to LinearLayout
            if (rchValues != null) {
                rchValues.addView(checkBox);
            }
        }

        // Setup exit button handling
        mFilterDialog.findViewById(R.id.exitButton).setOnClickListener(listener -> {
            dismiss();
        });

        // Setup clear all button handling
        mFilterDialog.findViewById(R.id.clearAllButton).setOnClickListener(listener -> {
            updateAppliedFilters(new ArrayList<Filter>());
        });

        // Setup done button handling
        mFilterDialog.findViewById(R.id.doneButton).setOnClickListener(listener -> {
            mListener.onFilterSelected(mFilterList);
            dismiss();
        });

        sendCompleteBroadcast();

        return mFilterDialog;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
     * Takes in a list of applied Filters and updates mFilterList and checked CheckBoxes accordingly.
     *
     * @param filterList a list of applied Filter objects
     */
    public void updateAppliedFilters(List<Filter> filterList) {
        LinearLayout specialtyCenterValues = mFilterDialog.findViewById(R.id.specialtyCenterValues);
        for (int i = 0; i < specialtyCenterValues.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) specialtyCenterValues.getChildAt(i);
            boolean test = (filterList.contains(new Filter(FilterField.HOSPITAL_TYPES, (String) checkBox.getText())));
            checkBox.setChecked(test);
        }
        LinearLayout emsRegionValues = mFilterDialog.findViewById(R.id.emsRegionValues);
        for (int i = 0; i < emsRegionValues.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) emsRegionValues.getChildAt(i);
            String displayString = (String) checkBox.getText();
            // Only the last character of the display string is stored as the value
            checkBox.setChecked(filterList.contains(new Filter(FilterField.REGION, displayString.substring(displayString.length() - 1))));
        }
        LinearLayout countyValues = mFilterDialog.findViewById(R.id.countyValues);
        for (int i = 0; i < countyValues.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) countyValues.getChildAt(i);
            checkBox.setChecked(filterList.contains(new Filter(FilterField.COUNTY, (String) checkBox.getText())));
        }
        LinearLayout rchValues = mFilterDialog.findViewById(R.id.rchValues);
        for (int i = 0; i < rchValues.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) rchValues.getChildAt(i);
            String displayString = (String) checkBox.getText();
            // Only the last character of the display string is stored as the value
            checkBox.setChecked(filterList.contains(new Filter(FilterField.REGIONAL_COORDINATING_HOSPITAL, displayString.substring(displayString.length() - 1))));
        }
    }

    /**
     * Helper method to make a checkbox.
     *
     * @param field FilterField that the CheckBox belongs to
     * @param value value of the Filter for the CheckBox
     * @return a CheckBox for a specific Filter value
     */
    private CheckBox makeCheckBox(FilterField field, String value, String displayString) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(displayString);
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

        return checkBox;
    }

    /**
     * Sends a broadcast that the filter dialog view has been created.
     */
    private void sendCompleteBroadcast() {
        Log.d("FilterSheetDialog: ", "Sending broadcast...");

        Intent intent = new Intent("FILTER_DIALOG_VIEW_CREATED");
        mFilterDialog.getContext().sendBroadcast(intent);
    }
}
