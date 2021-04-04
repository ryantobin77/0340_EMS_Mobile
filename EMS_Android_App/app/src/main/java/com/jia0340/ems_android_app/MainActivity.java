package com.jia0340.ems_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jia0340.ems_android_app.models.Filter;
import com.jia0340.ems_android_app.models.FilterField;
import com.jia0340.ems_android_app.models.Hospital;
import com.jia0340.ems_android_app.network.DataService;
import com.jia0340.ems_android_app.network.RetrofitClientInstance;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that corresponds to the activity_main.xml file
 * This is the first activity displayed when the app is launched
 *
 * @author Anna Dingler
 * Created on 1/24/21
 */
public class MainActivity extends AppCompatActivity implements FilterSheetDialog.FilterDialogListener {

    private SwipeRefreshLayout mSwipeContainer;
    private ArrayList<Hospital> mHospitalList;
    private HospitalListAdapter mHospitalAdapter;
    private Toolbar mToolbar;
    private FilterSheetDialog mFilterDialog;
    private Button mClearAllButton;
    private LinearLayout mAppliedFiltersHolder;
    private BroadcastReceiver mFilterDialogReceiver;

    /**
     * Create method for application
     * Called when the app is started
     * Used to set the content view, get initial hospital data, and setup recyclerView
     * @param savedInstanceState saved instance of app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attaching the layout to the toolbar object
        mToolbar = findViewById(R.id.toolbar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(mToolbar);

        mClearAllButton = findViewById(R.id.clearAllButton);

        // Instantiate empty hospital list and attach to Adapter
        mHospitalList = new ArrayList<Hospital>();
        mHospitalAdapter = new HospitalListAdapter(mHospitalList, this);

        // Setup on click handler for clear all filters buton
        mAppliedFiltersHolder = findViewById(R.id.appliedFiltersHolder);
        mClearAllButton.setOnClickListener(view -> {
            mHospitalAdapter.setFilterList(new ArrayList<Filter>());
            mAppliedFiltersHolder.removeAllViews();
            mClearAllButton.setVisibility(View.GONE);
        });

        registerFilterDialogReciever();

        //initial load of hospital data
        initializeHospitalData();

        // Attaching the layout to the swipe container view
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(() -> updateHospitalData());
    }

    /**
     * Instantiates the menu at the top of the screen
     * Includes call, sort, filter and search buttons
     * @param menu Menu that we want our layout set to
     * @return Return true if menu was created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Called when one of the buttons in the menu is called
     * @param item The item that has been selected
     * @return Return true is logic is completed successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: logic for menu

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            if (mFilterDialog == null) {
                mFilterDialog = new FilterSheetDialog();
            }

            mFilterDialog.show(getSupportFragmentManager(), "filterSheet");
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the hospital data from the database and assigns it to mHospitalList
     * If response was retrieved correctly, set up the recyclerView and populate with data
     */
    public void initializeHospitalData() {
        /*Create handle for the RetrofitInstance interface*/
        DataService service = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
        Call<List<Hospital>> call = service.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                // Save the returned list
                mHospitalList = (ArrayList<Hospital>) response.body();
                // Now we can update the recyclerView
                mHospitalAdapter.setHospitalList(mHospitalList);
                mHospitalAdapter.notifyDataSetChanged();
                // Now we can setup the recyclerView
                instantiateRecyclerView();
            }
            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                // Failed to collect hospital data
                // TODO: what do we want to happen when it fails?
                Log.d("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Gets the hospital data from the database and assigns it to mHospitalList
     * If response was retreived corrrectly, update data in recyclerView
     */
    public void updateHospitalData() {
        /*Create handle for the RetrofitInstance interface*/
        DataService service = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
        Call<List<Hospital>> call = service.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                // Save the returned list
                mHospitalList = (ArrayList<Hospital>) response.body();
                // Retrieve pinned hospitals in new list
                List<Hospital> pinnedList = new ArrayList<>();
                for (Hospital pinned : mHospitalAdapter.getPinnedList()) {
                    for (Hospital h : mHospitalList) {
                        if (h.getName().equals(pinned.getName())) {
                            pinnedList.add(h);
                        }
                    }
                }
                // Repin hospitals
                for (Hospital pinned : pinnedList) {
                    mHospitalList.remove(pinned);
                    pinned.setFavorite(true);
                    mHospitalList.add(0, pinned);
                }
                // Reassign the pinned list
                mHospitalAdapter.setPinnedList(pinnedList);
                // Now we can update the recyclerView
                mHospitalAdapter.setHospitalList(mHospitalList);
                mHospitalAdapter.notifyDataSetChanged();
                // Notify the swipe refresher that the data is done refreshing
                mSwipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                // Failed to collect hospital data
                // TODO: what do we want to happen when it fails?
                Log.d("MainActivity", t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Set up the recyclerView and adapter
     */
    private void instantiateRecyclerView() {

        RecyclerView hospitalRecyclerView = findViewById(R.id.hospital_list);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        hospitalRecyclerView.addItemDecoration(itemDecoration);

        hospitalRecyclerView.setAdapter(mHospitalAdapter);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onFilterSelected(List<Filter> filterList) {
        Log.d("MainActivity", "LISTENER FILTER!");
        mHospitalAdapter.setFilterList(new ArrayList<Filter>(filterList));

        if (mHospitalAdapter.getFilterList().size() > 0) {
            mClearAllButton.setVisibility(View.VISIBLE);
        } else {
            mClearAllButton.setVisibility(View.GONE);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(mAppliedFiltersHolder.getContext());
        mAppliedFiltersHolder.removeAllViews();

        for (Filter f : mHospitalAdapter.getFilterList()) {
            View filterCard = layoutInflater.inflate(R.layout.filter_card, mAppliedFiltersHolder, false);
            filterCard.setId(f.hashCode());
            TextView tv = filterCard.findViewById(R.id.filterValueLabel);
            switch (f.getFilterField()) {
                case HOSPITAL_TYPES:
                    tv.setText(f.getFilterValue());
                    break;
                case REGION:
                    tv.setText(mAppliedFiltersHolder.getContext().getString(R.string.filter_ems_region_value, f.getFilterValue()));
                    break;
                case COUNTY:
                    tv.setText(f.getFilterValue());
                    break;
                case REGIONAL_COORDINATING_HOSPITAL:
                    tv.setText(mAppliedFiltersHolder.getContext().getString(R.string.filter_rch_value, f.getFilterValue()));
                    break;
            }

            // set up remove button on click handler
            filterCard.findViewById(R.id.removeButton).setOnClickListener((e) -> {
                mHospitalAdapter.getFilterList().remove(f);
                mAppliedFiltersHolder.removeView(filterCard);

                if (mHospitalAdapter.getFilterList().size() == 0) {
                    mClearAllButton.setVisibility(View.GONE);
                }
            });

            // set spacing between filter cards
            if (filterCard.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) filterCard.getLayoutParams();
                p.setMargins(8,0, 8, 0);
                filterCard.requestLayout();
            }

            mAppliedFiltersHolder.addView(filterCard);
        }
        // TODO: Call filter method here
    }

    /**
     * Listens for filter dialog view to be created and updates with applied filters
     */
    private void registerFilterDialogReciever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("FILTER_DIALOG_VIEW_CREATED");

        mFilterDialogReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mFilterDialog.updateAppliedFilters(mHospitalAdapter.getFilterList());
            }
        };
        registerReceiver(mFilterDialogReceiver, filter);
    }
}