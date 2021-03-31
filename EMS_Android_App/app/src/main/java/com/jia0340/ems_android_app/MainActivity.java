package com.jia0340.ems_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jia0340.ems_android_app.models.Hospital;
import com.jia0340.ems_android_app.network.DataService;
import com.jia0340.ems_android_app.network.RetrofitClientInstance;

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
public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeContainer;
    private ArrayList<Hospital> mHospitalList;
    private HospitalListAdapter mHospitalAdapter;
    private Toolbar mToolbar;

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

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
                // Now we can setup the recyclerView
                instantiateRecyclerView();
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
        DataService service = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
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
     * Set up the recyclerView and adapter
     */
    private void instantiateRecyclerView() {

        RecyclerView hospitalRecyclerView = findViewById(R.id.hospital_list);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        hospitalRecyclerView.addItemDecoration(itemDecoration);

        mHospitalAdapter = new HospitalListAdapter(mHospitalList, this);
        hospitalRecyclerView.setAdapter(mHospitalAdapter);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}