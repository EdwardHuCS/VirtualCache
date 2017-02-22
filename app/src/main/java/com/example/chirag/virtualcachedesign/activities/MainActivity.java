package com.example.chirag.virtualcachedesign.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.chirag.virtualcachedesign.adapters.PagerAdapter;
import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.service.SubListItem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    PagerAdapter adapter;
    public String accessToken;
    private GoogleApiClient mGoogleApiClient;
    public String userID;
    public String email;
    int greyIcons[] = {R.drawable.grey_trending, R.drawable.grey_subscription,
            R.drawable.grey_tickets, R.drawable.grey_profile};
    int blueIcons[] = {R.drawable.blue_trending, R.drawable.blue_subscription,
            R.drawable.blue_tickets, R.drawable.blue_profile};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        accessToken = getIntent().getStringExtra("AccessToken");

        userID = getIntent().getStringExtra("account");
        email = getIntent().getStringExtra("email");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        //logSingleton = LogSingleton.getInstance();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        try {

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView yourTextView = (TextView) f.get(toolbar);
            Typeface font = Typeface.createFromAsset(getAssets(), "Avenir-Medium.ttf");
            yourTextView.setTypeface(font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        /*tabLayout.addTab(tabLayout.newTab().setIcon(blueIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(greyIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(greyIcons[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(greyIcons[3]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

        View view1 = getLayoutInflater().inflate(R.layout.tab_icon, null);
        view1.findViewById(R.id.icon).setBackgroundResource(blueIcons[0]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));


        View view2 = getLayoutInflater().inflate(R.layout.tab_icon, null);
        view2.findViewById(R.id.icon).setBackgroundResource(greyIcons[1]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));


        View view3 = getLayoutInflater().inflate(R.layout.tab_icon, null);
        view3.findViewById(R.id.icon).setBackgroundResource(greyIcons[2]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.tab_icon, null);
        view4.findViewById(R.id.icon).setBackgroundResource(greyIcons[3]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View view = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                view.findViewById(R.id.icon).setBackgroundResource(blueIcons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                view.findViewById(R.id.icon).setBackgroundResource(greyIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("account", userID);
        }
        super.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.main_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    public void changeCountryCode(String countryCode) {
        adapter.trendingFragment.fetchTrendingResults(countryCode);
    }

    public void fetchNewSearchResults() {
        adapter.trendingFragment.fetchNewSearchResults();
    }

    public void fetchNewChannels() {
        adapter.trendingFragment.fetchNewSearchResults();
    }

    public void fetchChannels(SubListItem.Item item) {
        adapter.subscriptionFragment.fetchChannels(item);
    }

    public void startPlayingActivity(String videoTitle, String videoID) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra("VIDEOID", videoID);
        intent.putExtra("VIDEONAME",videoTitle);
        Location temp = getLastLocation(mGoogleApiClient);
        double[] foo={};
        if(temp!=null) {
            foo = new double[]{temp.getLongitude(), temp.getLatitude()};
        }
        intent.putExtra("LOCATION", foo);
        intent.putExtra("account", userID);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected Location getLastLocation(GoogleApiClient mGoogleApiClient) {
        Log.i("VC","Get last location");
        Location mLastLocation;
        Log.d("google play onConnected", "connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;
    }
}