package com.jia0340.ems_android_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jia0340.ems_android_app.models.FilterField;
import com.jia0340.ems_android_app.models.HospitalType;

public class FilterSheetDialog extends BottomSheetDialogFragment {

    FilterDialogListener mListener;
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
//    RadioGroup mRadioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        Drawable expandImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.expand, null);
        Drawable collapseImg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.collapse, null);

        View view = inflater.inflate(R.layout.filter_dialog_layout, container, false);

        view.findViewById(R.id.exitButton).setOnClickListener(listener -> {
            dismiss();
        });

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

            for (HospitalType type: HospitalType.values()) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(type.getStringId());
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    }
                });

                // Add Checkbox to LinearLayout
                if (specialtyCenterValues != null) {
                    specialtyCenterValues.addView(checkBox);
                }
            }
        });

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

            for (int i = 1; i <= 10; i++) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(context.getString(R.string.filter_ems_region_value, String.valueOf(i)));
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    }
                });

                // Add Checkbox to LinearLayout
                if (emsRegionValues != null) {
                    emsRegionValues.addView(checkBox);
                }
            }
        });

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

            for (int i = 0; i < COUNTIES.length; i++) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(COUNTIES[i]);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    }
                });

                // Add Checkbox to LinearLayout
                if (countyValues != null) {
                    countyValues.addView(checkBox);
                }
            }
        });

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

            // A through N
            for (int i = 0; i < 14; i++) {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(Character.toString((char)(65 + i)));
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    }
                });

                // Add Checkbox to LinearLayout
                if (rchValues != null) {
                    rchValues.addView(checkBox);
                }
            }
        });
        return view;
    }

    public interface FilterDialogListener {
        void onFilterSelected(FilterField selectedFilter);
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
}
