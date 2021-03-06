package com.battery.saver.G.persistent;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.battery.saver.G.model.Geofences;

public class Storage {

    private Context mContext;

    public Storage(Context context) {
        this.mContext = context;
    }

    public boolean fenceExistsWithCustomId(Geofences.Geofence fence) {
        final String QUERY = GeofenceProvider.Geofence.KEY_CUSTOMID + " = ?";
        final String[] PARAMETERS = new String[]{
                fence.customId
        };
        final Uri URL = Uri.parse("content://" + "com.example.jeremy.controller" + "/geofences");
        ContentResolver resolver = mContext.getContentResolver();
        Cursor existingCursor = resolver.query(URL, null, QUERY, PARAMETERS, null);
        if (existingCursor != null) {
            boolean exists = existingCursor.getCount() > 0;
            existingCursor.close();
            return exists;
        }
        return false;
    }

    @Nullable
    public void insertOrUpdateFence(Geofences.Geofence fence) {
        final String QUERY = GeofenceProvider.Geofence.KEY_CUSTOMID + " = ?";
        final String[] PARAMETERS = new String[]{
                fence.customId
        };
        final Uri URL = Uri.parse("content://" + "com.example.jeremy.controller" + "/geofences");
        ContentResolver resolver = mContext.getContentResolver();
        Cursor existingCursor = resolver.query(URL, null, QUERY, PARAMETERS, null);
        try {
            if (existingCursor != null && existingCursor.getCount() > 0) {
                resolver.update(URL, makeContentValuesForGeofence(fence), QUERY, PARAMETERS);
            } else {
                resolver.insert(URL, makeContentValuesForGeofence(fence));
            }
        } finally {
            if (existingCursor != null) {
                existingCursor.close();
            }
        }
    }

    @NonNull
    private ContentValues makeContentValuesForGeofence(Geofences.Geofence fence) {
        ContentValues values = new ContentValues();
        values.put(GeofenceProvider.Geofence.KEY_NAME, fence.name);
        values.put(GeofenceProvider.Geofence.KEY_UUID, fence.uuid);
        values.put(GeofenceProvider.Geofence.KEY_RADIUS, fence.radiusMeters);
        values.put(GeofenceProvider.Geofence.KEY_CUSTOMID, fence.customId);
        values.put(GeofenceProvider.Geofence.KEY_ENTER_METHOD, fence.enterMethod);
        values.put(GeofenceProvider.Geofence.KEY_TRIGGER, fence.triggers);
        values.put(GeofenceProvider.Geofence.KEY_EXIT_METHOD, fence.exitMethod);
        values.put(GeofenceProvider.Geofence.KEY_LATITUDE, fence.latitude);
        values.put(GeofenceProvider.Geofence.KEY_LONGITUDE, fence.longitude);
        values.put(GeofenceProvider.Geofence.KEY_CURRENTLY_ENTERED, fence.currentlyEntered);
        return values;
    }
}
