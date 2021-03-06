package com.battery.saver.G.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;

import com.battery.saver.G.JnaBatteryManagerApplication;
import com.battery.saver.G.R;
import com.battery.saver.G.geo.LocativeGeocoder;
import com.battery.saver.G.model.Geofences;
import com.battery.saver.G.persistent.GeofenceProvider;
import com.battery.saver.G.persistent.Storage;
import com.battery.saver.G.service.LocativeService;
import com.battery.saver.G.utils.Dialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

//import com.battery.saver.G.Loca

public class GeofencesActivity extends BaseActivity implements GeofenceFragment.OnFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String NOTIFICATION_CLICK = "notification_click";

    @BindView(R.id.container)
    FrameLayout mContentFrame;

    @BindView(R.id.add_geofence)
    FloatingActionButton mFabButton;

    @Inject
    Storage mStorage;

    private GeofenceFragment mGeofenceFragment = null;

    private String fragmentTag = GeofenceFragment.TAG;
    private static final String FRAGMENTTAG = "current.fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FRAGMENTTAG)) {
                fragmentTag = savedInstanceState.getString(FRAGMENTTAG);
            }
        }
        super.onCreate(savedInstanceState);
        ((JnaBatteryManagerApplication) getApplication()).getComponent().inject(this);

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

        if (Geofences.ITEMS.size() == 0) {
            load();
        }
        android.support.v4.app.FragmentManager fragman = getSupportFragmentManager();
        switch (fragmentTag) {
            case GeofenceFragment.TAG: {
                android.support.v4.app.Fragment f = fragman.getFragment(new Bundle(), GeofenceFragment.TAG);
                if (mGeofenceFragment == null)
                    mGeofenceFragment = f != null ? (GeofenceFragment) f : GeofenceFragment.newInstance("str1", "str2");
                fragman.beginTransaction().replace(R.id.container, mGeofenceFragment, GeofenceFragment.TAG).commit();
                if (Geofences.ITEMS.size() == 0) {
                    load();
                }
                break;
            }
        }
    }

    public void load() {
        if (getLoaderManager().getLoader(0) == null)
            getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onFragmentInteraction(String id) {

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
    protected int getLayoutResourceId() {
        return R.layout.activity_geofences;
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected int getMenuResourceId() {
        return 0;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.add_geofence)
    public void addGeofenceClick() {
        createGeofence();
    }

    private void createGeofence() {
        Intent addEditGeofencesIntent = new Intent(GeofencesActivity.this, AddEditGeofenceActivity.class);
        GeofencesActivity.this.startActivity(addEditGeofencesIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse("content://" + "com.battery.saver.G" + "/geofences");
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGeofenceFragment.geofences.clear();
        ArrayList<Geofences.Geofence> items = new ArrayList<>();
        while (data.moveToNext()) {
            Geofences.Geofence item = GeofenceProvider.fromCursor(data);
            mGeofenceFragment.geofences.addItem(item);
            items.add(item);
        }
        mGeofenceFragment.refresh();

        updateGeofencingService(items);
    }

    private void updateGeofencingService(ArrayList<Geofences.Geofence> items) {
        Intent geofencingService = new Intent(this, LocativeService.class);
        geofencingService.putExtra(LocativeService.EXTRA_ACTION, LocativeService.Action.ADD);
        geofencingService.putExtra(LocativeService.EXTRA_GEOFENCE, items);
        this.startService(geofencingService);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public SharedPreferences getPrefs() {
        return this.getPreferences(MODE_PRIVATE);
    }


    public void onGeofenceImportSelection(final Geofences.Geofence fence) {
        if (!mStorage.fenceExistsWithCustomId(fence)) {
            insertGeofence(fence);
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage("A Geofence with the same custom ID already exists. Would you like to overwrite the existing Geofence?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insertGeofence(fence);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void insertGeofence(final Geofences.Geofence fence) {
        final Activity self = this;
        final ProgressDialog dialog = Dialog.getIndeterminateProgressDialog(this, "Importing Geofence???");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Address address = new LocativeGeocoder().getFromLatLong(fence.latitude, fence.longitude, self);
                if (address != null) {
                    fence.name = address.getAddressLine(0);
                }
                mStorage.insertOrUpdateFence(fence);
                final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                        Geofences.ITEMS.add(fence);
                        transaction.replace(R.id.container, mGeofenceFragment, GeofenceFragment.TAG).commit();
                        setTitle("Geofences");
                        mFabButton.show();
                    }
                });
            }
        }).run();

    }

}
