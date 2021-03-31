package com.jia0340.ems_android_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jia0340.ems_android_app.models.SortField;

public class SortSheetDialog extends BottomSheetDialogFragment {

    SortDialogListener mListener;
    RadioGroup mRadioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sort_dialog_layout, container, false);
        mRadioGroup = view.findViewById(R.id.sort_radio_group);

        if (savedInstanceState != null) {
            int selectedSort = savedInstanceState.getInt("SELECTED_SORT");
            mRadioGroup.check(selectedSort);
        }

        RadioButton distanceButton = view.findViewById(R.id.sort_distance);
        RadioButton nedocsButton = view.findViewById(R.id.sort_nedocs);
        RadioButton nameButton = view.findViewById(R.id.sort_a_z);

        distanceButton.setOnClickListener(view13 -> {
            mListener.onSortSelected(SortField.DISTANCE);
            dismiss();
        });

        nedocsButton.setOnClickListener(view12 -> {
            mListener.onSortSelected(SortField.NEDOCS_SCORE);
            dismiss();
        });

        nameButton.setOnClickListener(view1 -> {
            mListener.onSortSelected(SortField.NAME);
            dismiss();
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SELECTED_SORT", mRadioGroup.getCheckedRadioButtonId());
    }

    public interface SortDialogListener {
        void onSortSelected(SortField selectedSort);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (SortDialogListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " need to implement SortDialogListener");
        }
    }
}
