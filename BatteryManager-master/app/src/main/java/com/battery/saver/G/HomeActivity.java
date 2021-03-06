package com.battery.saver.G;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.battery.saver.G.persistent.Storage;
import com.battery.saver.G.view.GeofenceFragment;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity {

    // UI
    private BottomNavigationView mBottomNav;
    private ViewPager mViewpager;
    private FrameLayout mMainFrame;

    @Inject
    Storage mStorage;
    //Fragments
    private BatteryFragment batteryFragment;
    private ControllerFragment controllerFragment;
    private GeofencingFragment geofencingFragment;

    private String fragmentTag = GeofenceFragment.TAG;
    private static final String FRAGMENTTAG = "current.fragment";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FRAGMENTTAG)) {
                fragmentTag = savedInstanceState.getString(FRAGMENTTAG);
            }
        }

        super.onCreate(savedInstanceState);
        ((JnaBatteryManagerApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_home);

        mMainFrame = (FrameLayout) findViewById(R.id.main_container);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        mContext = getApplicationContext();
        batteryFragment = new BatteryFragment();
        controllerFragment = new ControllerFragment();
        geofencingFragment = new GeofencingFragment();

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transparent));
        }

        //Sets the initial fragment upon startup.
        setFragment(batteryFragment, "Battery");
        updateToolbarText("Battery");

        //Enable translucent navigation
        //setTheme(R.style.AppTheme_TranslucentNavigation);

        mBottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_battery:
                                setFragment(batteryFragment, "Battery");
                                updateToolbarText("Battery");
                                refresh();
                                return true;
                            case R.id.menu_controller:
                                setFragment(controllerFragment, "Controller");
                                updateToolbarText("Controller");
                                refresh();
                                return true;
                            case R.id.menu_geofencing:
                                setFragment(geofencingFragment, "Geofencing");
                                updateToolbarText("Geofencing");
                                refresh();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }

    private void setFragment(Fragment fragment, String name) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment, name);
        fragmentTransaction.commit();
    }

    private void refresh() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrolly);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, (findViewById(R.id.activity_main)).getTop());
            }
        });
    }

    public void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }


    /**
     * Checks if the fragment is the battery fragment (or Home Fragment). If not then it will set
     * the current fragment as the battery fragment. If it is the battery fragment, then close the app
     * (the app will still be running however).
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getVisibleFragment() != null) {
            String nameFragment = getVisibleFragment().toString();
            if (nameFragment.contains("Battery")) {
                this.finishAffinity();
                /**
                 * finishAffinity();
                 * Finishes this activity as well as all activities immediately below it in the current
                 * that have the same affinity.  Follows Google's material design guidelines.
                 */
            } else {
                setFragment(batteryFragment, "Battery");
                mBottomNav.setSelectedItemId(R.id.menu_battery);
            }
        } else {
            setFragment(batteryFragment, "Battery");
            mBottomNav.setSelectedItemId(R.id.menu_battery);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENTTAG, fragmentTag);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}