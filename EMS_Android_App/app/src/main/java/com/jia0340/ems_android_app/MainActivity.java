package com.jia0340.ems_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.jia0340.ems_android_app.models.Hospital;
import com.jia0340.ems_android_app.network.DatabaseService;
import com.jia0340.ems_android_app.network.RetrofitClientDatabaseAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SwipeRefreshLayout mSwipeContainer;
    private ArrayList<Hospital> mHospitalList;
    private HospitalListAdapter mHospitalAdapter;
    private Toolbar mToolbar;
    private SearchView mSearchBar;
    private BroadcastReceiver mDistanceReceiver;

    private DistanceController mDistanceController;
    private boolean mPermissionsGranted = false;

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

        // Setting up the search bar
        mSearchBar = findViewById(R.id.search_bar);
        mSearchBar.setVisibility(View.GONE);
        mSearchBar.setIconifiedByDefault(true);
        mSearchBar.setOnQueryTextListener(this);

        mSearchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchBar.clearFocus();
                mSearchBar.setVisibility(View.GONE);
                return false;
            }
        });
        registerDistanceReceiver();

        checkPermissions();

        //initial load of hospital data
        initializeHospitalData();

        // Attaching the layout to the swipe container view
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(() -> updateHospitalData());
    }

    //Handles submit action from search bar
    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchBar.setVisibility(View.GONE);
        return false;
    }

    //Handles text changing in search bar
    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println(newText);
        mHospitalAdapter.handleSearch(newText);
        mHospitalAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    protected void onDestroy() {

        if (mDistanceReceiver != null) {
            unregisterReceiver(mDistanceReceiver);
            mDistanceReceiver = null;
        }

        super.onDestroy();
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

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Search button clicked
        if (id == R.id.action_search) {
            if (mSearchBar.getVisibility() == View.VISIBLE) {
                mSearchBar.setVisibility(View.GONE);
            } else {
                mSearchBar.setVisibility(View.VISIBLE);
                mSearchBar.setFocusable(true);
                mSearchBar.setIconified(false);
                mSearchBar.requestFocusFromTouch();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the hospital data from the database and assigns it to mHospitalList
     * If response was retrieved correctly, set up the recyclerView and populate with data
     */
    public void initializeHospitalData() {
        /*Create handle for the RetrofitInstance interface*/
        DatabaseService service = RetrofitClientDatabaseAPI.getRetrofitInstance().create(DatabaseService.class);
        Call<List<Hospital>> call = service.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                // Save the returned list
                mHospitalList = (ArrayList<Hospital>) response.body();
                // Now we can finish setting up the app
                finishInstantiation();
            }
            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                // Failed to collect hospital data
                // TODO: what do we want to happen when it fails?
                Log.d("MainActiity", t.getMessage());
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
        DatabaseService service = RetrofitClientDatabaseAPI.getRetrofitInstance().create(DatabaseService.class);
        Call<List<Hospital>> call = service.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                // Save the returned list
                mHospitalList = (ArrayList<Hospital>) response.body();
                mHospitalAdapter.setAllHospitalList(mHospitalList);
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

                if (mPermissionsGranted) {
                    new Thread(() -> requestDistances()).start();
                }

                // Notify the swipe refresher that the data is done refreshing
                mSwipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                // Failed to collect hospital data
                // TODO: what do we want to happen when it fails?
                Log.d("MainActiity", t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Finish instantiating objects
     */
    private void finishInstantiation() {

        RecyclerView hospitalRecyclerView = findViewById(R.id.hospital_list);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        hospitalRecyclerView.addItemDecoration(itemDecoration);

        mHospitalAdapter = new HospitalListAdapter(mHospitalList, this);
        hospitalRecyclerView.setAdapter(mHospitalAdapter);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mPermissionsGranted) {
            instantiateDistanceController();
        }
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        } else {
            mPermissionsGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity: ", "First Permission Granted");

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    // Requesting the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            2);
                } else {
                    mPermissionsGranted = true;
                }

            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity: ", "Second Permission Granted");

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    // Requesting the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                } else {
                    mPermissionsGranted = true;
                }

            }
        }
    }

    private void instantiateDistanceController() {

        List<String> names = new ArrayList<>();
        List<Double> latitudes = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            names = mHospitalList.stream().map(Hospital::getName).collect(Collectors.toList());
            latitudes = mHospitalList.stream().map(Hospital::getLatitude).collect(Collectors.toList());
            longitudes = mHospitalList.stream().map(Hospital::getLongitude).collect(Collectors.toList());
        } else {
            for (Hospital hospital : mHospitalList) {
                names.add(hospital.getName());
                latitudes.add(hospital.getLatitude());
                longitudes.add(hospital.getLongitude());
            }
        }

        mDistanceController = new DistanceController(names, latitudes, longitudes, this);

        new Thread(this::requestDistances).start();
    }

    private void requestDistances() {
        if (mDistanceController != null) {
            mDistanceController.checkForNewLocation(this);
        }
    }

    private void updateDistances() {
        if (mDistanceController != null) {

            for (Hospital hospital : mHospitalList) {
                hospital.setDistance(mDistanceController.getDistanceToHospital(hospital.getName()));
            }

            mHospitalAdapter.notifyDataSetChanged();

        }
    }

    private void registerDistanceReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("DISTANCE_COMPLETE");

        mDistanceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateDistances();
            }
        };
        registerReceiver(mDistanceReceiver, filter);
    }
}