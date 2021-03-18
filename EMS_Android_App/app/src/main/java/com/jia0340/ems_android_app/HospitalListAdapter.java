package com.jia0340.ems_android_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jia0340.ems_android_app.models.Hospital;
import com.jia0340.ems_android_app.models.HospitalType;
import com.jia0340.ems_android_app.models.NedocsScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter used to bind individual items in the recyclerView
 *
 * @author Anna Dingler
 * Created on 1/27/21
 */
class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder> {

    private List<Hospital> mHospitalList;
    private List<Hospital> mPinnedList;
    private Context mContext;

    /**
     * Constructor of the custom adapter
     *
     * @param hospitalList The dataset that the recyclerView to be populated with
     */
    public HospitalListAdapter(List<Hospital> hospitalList, Context context) {
        mHospitalList = hospitalList;
        mPinnedList = new ArrayList<Hospital>();
        mContext = context;
    }

    /**
     * Creates the view for a specific hospital and stores it within a viewHolder
     *
     * @param parent Parent view to the individual item
     * @param viewType
     * @return the viewholder that contains the item view for a specific hospital
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View hospitalView = layoutInflater.inflate(R.layout.hospital_card, parent, false);

        ViewHolder holder = new ViewHolder(hospitalView);

        return holder;
    }

    /**
     * Binds each aspect of a hospital's data to its respective view in the viewHolder
     *
     * @param holder Holder that contains the elements for the speicifc hospital
     * @param position Position of the hospital in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hospital hospital = mHospitalList.get(position);

        holder.mHospitalName.setText(hospital.getName());
        holder.mDistanceLabel.setText(mContext.getString(R.string.distance, hospital.getDistance()));
        holder.mPhoneNumber.setText(hospital.getPhoneNumber());
        holder.mAddressView.setText(mContext.getString(R.string.address, hospital.getStreetAddress(),
                                            hospital.getCity(), hospital.getState(), hospital.getZipCode()));
        holder.mCountyRegionText.setText(mContext.getString(R.string.county_region, hospital.getCounty(),
                                            hospital.getRegion()));
        holder.mRegionalCoordinatingText.setText(mContext.getString(R.string.regional_coordinating_hospital,
                                            hospital.getRegionalCoordinatingHospital()));

        handleNedocsValues(holder, hospital.getNedocsScore());

        handleDiversions(holder, hospital.getDiversions());

        handleHospitalTypes(holder, hospital.getHospitalTypes());

        handleFavoritePin(holder, hospital);

        handleExpandCollapse(holder, hospital, position);
    }

    /**
     * Get the number of items in stored in recyclerView
     *
     * @return number of items in hospitalList
     */
    @Override
    public int getItemCount() {
        return mHospitalList.size();
    }

    /**
     * Handles the UI for the nedocs score
     * Sets the text and background color based on enum value
     * @param holder View for the specific hospital
     * @param score Nedocs score for the hospital
     */
    public void handleNedocsValues(ViewHolder holder, NedocsScore score) {

        holder.mNedocsView.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), score.getColor(), null));
        holder.mNedocsLabel.setText(mContext.getString(score.getLabel()));

        if (score.equals(NedocsScore.OVERCROWDED)) {
            holder.mNedocsLabel.setTextSize(14);
        } else {
            holder.mNedocsLabel.setTextSize(16);
        }

    }

    /**
     * Handles the UI for the diversions
     * Displays the diversion icon if needed and adds a description to the expanded view
     * @param holder View for the specific hospital
     * @param diversions List of diversions for the hospital
     */
    public void handleDiversions(ViewHolder holder, ArrayList<String> diversions) {

        if (diversions != null && diversions.size() > 0) {

            if (diversions.size() == 1 && diversions.get(0).equals("Normal")) {
                hideDiversions(holder);
            } else {
                // set visibility of icon within collapsed/expanded views
                holder.mDiversionView.setVisibility(View.VISIBLE);
                holder.mExpandedDiversionView.setVisibility(View.VISIBLE);

                Drawable currImage;
                switch (diversions.size()) {
                    case 1:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_1, null);
                        break;
                    case 2:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_2, null);
                        break;
                    case 3:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_3, null);
                        break;
                    case 4:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_4, null);
                        break;
                    case 5:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_5, null);
                        break;
                    case 6:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning_6, null);
                        break;
                    default:
                        currImage = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.warning, null);
                        break;
                }
                holder.mDiversionView.setImageDrawable(currImage);
                holder.mExpandedDiversionView.setImageDrawable(currImage);

                // assign text for expandedView
                String description = diversions.get(0);

                for (int i = 1; i < diversions.size(); i++) {
                    description += "\n";
                    description += diversions.get(i);
                }

                holder.mDiversionDescription.setText(description);
                holder.mDiversionDescription.setVisibility(View.VISIBLE);
            }
        } else {
            hideDiversions(holder);
        }
    }

    public void hideDiversions(ViewHolder holder) {
        holder.mDiversionView.setVisibility(View.GONE);
        holder.mExpandedDiversionView.setVisibility(View.GONE);
        holder.mDiversionDescription.setVisibility(View.GONE);
    }

    /**
     * Displays icons and descriptions for hospital types
     * Handles up to 3 hospital types
     * @param holder View for the specific hospital
     * @param hospitalTypes List of hospital types for a hospital
     */
    public void handleHospitalTypes(ViewHolder holder, ArrayList<HospitalType> hospitalTypes) {

        holder.mHospitalTypeOneImage.setVisibility(View.GONE);
        holder.mHospitalTypeTwoImage.setVisibility(View.GONE);
        holder.mHospitalTypeThreeImage.setVisibility(View.GONE);

        holder.mTypeOneView.setVisibility(View.GONE);
        holder.mTypeTwoView.setVisibility(View.GONE);
        holder.mTypeThreeView.setVisibility(View.GONE);

        if (hospitalTypes != null) {

            for (int i = 0; i < hospitalTypes.size(); i++) {

                ImageView currHospitalIcon = null;
                TextView currTypeView = null;

                switch (i) {
                    case 0:
                        currHospitalIcon = holder.mHospitalTypeOneImage;
                        currTypeView = holder.mTypeOneView;
                        break;
                    case 1:
                        currHospitalIcon = holder.mHospitalTypeTwoImage;
                        currTypeView = holder.mTypeTwoView;
                        break;
                    case 2:
                        currHospitalIcon = holder.mHospitalTypeThreeImage;
                        currTypeView = holder.mTypeThreeView;
                        break;
                }

                if (currHospitalIcon != null && currTypeView != null) {
                    currHospitalIcon.setVisibility(View.VISIBLE);
                    currTypeView.setVisibility(View.VISIBLE);

                    HospitalType currHospitalType = hospitalTypes.get(i);
                    Drawable currImage = ResourcesCompat.getDrawable(mContext.getResources(), currHospitalType.getImageId(), null);
                    String currText = mContext.getString(currHospitalType.getStringId());

                    currHospitalIcon.setImageDrawable(currImage);
                    currTypeView.setCompoundDrawablesWithIntrinsicBounds(currImage, null, null, null);
                    currTypeView.setText(currText);
                }
            }
        }
    }

    /**
     * Updates the pin icon
     * Displays the filled in pin if the hospital is a favorite
     * Displays the pin outline if it is not
     * Also instantiates an onClickListener for the pin that will update the UI
     * @param holder View for the specific hospital
     * @param hospital Object for current hospital
     */
    public void handleFavoritePin(ViewHolder holder, Hospital hospital) {
        if (hospital.isFavorite())
            holder.mFavoriteView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.filled_favorite_pin, null));
        else
            holder.mFavoriteView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.outlined_favorite_pin, null));

        int position = mHospitalList.indexOf(hospital);
        holder.mFavoriteView.setOnClickListener(view -> {
            int pos = mHospitalList.indexOf(hospital);

            boolean favorite = hospital.isFavorite();

            hospital.setFavorite(!favorite);

            if (hospital.isFavorite()) {
                holder.mFavoriteView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.filled_favorite_pin, null));
                mPinnedList.add(hospital);
                mHospitalList.remove(hospital);
                mHospitalList.add(0, hospital);
                notifyItemMoved(pos, 0);
                //swapItem(pos, 0);

            } else {
                holder.mFavoriteView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.outlined_favorite_pin, null));
                mPinnedList.remove(hospital);
                mHospitalList.remove(hospital);
                mHospitalList.add(mPinnedList.size()+1, hospital);
                notifyItemMoved(0, mPinnedList.size()+1);
                //swapItem(pos, mPinnedList.size()+1);

            }
        });

    }

    /**
     * Updates the expand/collapse view by changing visibility of views
     * Set onClickListeners for the expand/collapse buttons to handle UI changes
     * @param holder
     * @param hospital
     * @param position
     */
    public void handleExpandCollapse(ViewHolder holder, Hospital hospital, int position) {
        holder.mExpandButton.setVisibility(hospital.isExpanded() ? View.GONE : View.VISIBLE);
        holder.mExpandedHospitalCard.setVisibility(hospital.isExpanded() ? View.VISIBLE : View.GONE);

        holder.mExpandButton.setOnClickListener(view -> {

            hospital.setExpanded(true);
            notifyItemChanged(position);

        });

        holder.mCollapseButton.setOnClickListener(view -> {

            hospital.setExpanded(false);
            notifyItemChanged(position);

        });
    }

    /**
     * Custom ViewHolder that contains the individual views found in the item xml
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout mNedocsView;
        public TextView mNedocsLabel;
        public TextView mHospitalName;
        public ImageButton mFavoriteView;
        public TextView mDistanceLabel;
        public ImageView mDiversionView;
        public ImageView mHospitalTypeOneImage;
        public ImageView mHospitalTypeTwoImage;
        public ImageView mHospitalTypeThreeImage;
        public ImageButton mExpandButton;

        public ConstraintLayout mExpandedHospitalCard;
        public TextView mPhoneNumber;
        public TextView mAddressView;
        public ImageView mExpandedDiversionView;
        public TextView mDiversionDescription;
        public TextView mTypeOneView;
        public TextView mTypeTwoView;
        public TextView mTypeThreeView;
        public TextView mCountyRegionText;
        public TextView mRegionalCoordinatingText;
        public ImageButton mCollapseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNedocsView = itemView.findViewById(R.id.nedocsView);
            mNedocsLabel = itemView.findViewById(R.id.nedocsLabel);
            mHospitalName = itemView.findViewById(R.id.hospitalName);
            mFavoriteView = itemView.findViewById(R.id.favoriteView);
            mDistanceLabel = itemView.findViewById(R.id.distanceLabel);
            mDiversionView = itemView.findViewById(R.id.diversionView);
            mHospitalTypeOneImage = itemView.findViewById(R.id.hospitalType1View);
            mHospitalTypeTwoImage = itemView.findViewById(R.id.hospitalType2View);
            mHospitalTypeThreeImage = itemView.findViewById(R.id.hospitalType3View);
            mExpandButton = itemView.findViewById(R.id.expandButton);

            mExpandedHospitalCard = itemView.findViewById(R.id.expandedHospitalCard);
            mPhoneNumber = itemView.findViewById(R.id.phoneNumberView);
            mAddressView = itemView.findViewById(R.id.addressView);
            mExpandedDiversionView = itemView.findViewById(R.id.expandedDiversionView);
            mDiversionDescription = itemView.findViewById(R.id.diversionDescription);;
            mTypeOneView = itemView.findViewById(R.id.hospitalType1Description);
            mTypeTwoView = itemView.findViewById(R.id.hospitalType2Description);
            mTypeThreeView = itemView.findViewById(R.id.hospitalType3Description);
            mCountyRegionText = itemView.findViewById(R.id.countyRegionView);
            mRegionalCoordinatingText = itemView.findViewById(R.id.regionalCoordinatingHospitalView);
            mCollapseButton = itemView.findViewById(R.id.collapseButton);
        }
    }

}
