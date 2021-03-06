package com.battery.saver.G.persistent;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.battery.saver.G.model.Geofences;

import de.triplet.simpleprovider.AbstractProvider;
import de.triplet.simpleprovider.Column;
import de.triplet.simpleprovider.Table;

public class GeofenceProvider extends AbstractProvider {

    private static int SCHEMA_VERSION = 2;

    public static final int TRIGGER_ON_ENTER = 0x01;
    public static final int TRIGGER_ON_EXIT = 0x02;

    protected String getAuthority() {
        return "com.example.jeremy.controller";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = super.insert(uri, values);
        Toast.makeText(getContext(), "Geofence added", Toast.LENGTH_SHORT)
                .show();
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = super.delete(uri, selection, selectionArgs);
        Toast.makeText(getContext(), "Geofence removed", Toast.LENGTH_SHORT)
                .show();
        return result;
    }

    public static Geofences.Geofence fromCursor(Cursor cursor) {
        Geofences.Geofence geofence = new Geofences.Geofence(
                cursor.getString(cursor.getColumnIndex(Geofence.KEY_UUID)),
                cursor.getString(cursor.getColumnIndex(Geofence.KEY_CUSTOMID)),
                cursor.getString(cursor.getColumnIndex(Geofence.KEY_NAME)),
                cursor.getInt(cursor.getColumnIndex(Geofence.KEY_TRIGGER)),
                cursor.getFloat(cursor.getColumnIndex(Geofence.KEY_LATITUDE)),
                cursor.getFloat(cursor.getColumnIndex(Geofence.KEY_LONGITUDE)),
                cursor.getInt(cursor.getColumnIndex(Geofence.KEY_RADIUS)),
                cursor.getInt(cursor.getColumnIndex(Geofence.KEY_ENTER_METHOD)),
                cursor.getInt(cursor.getColumnIndex(Geofence.KEY_EXIT_METHOD)),
                cursor.getInt(cursor.getColumnIndex(Geofence.KEY_CURRENTLY_ENTERED))
        );
        return geofence;
    }

    @Override
    public int getSchemaVersion() {
        return SCHEMA_VERSION;
    }

    @Table
    public class Geofence {

        @Column(value = Column.FieldType.INTEGER, primaryKey = true)
        public static final String KEY_ID = "_id";

        @Column(value = Column.FieldType.TEXT, unique = true)
        public static final String KEY_CUSTOMID = "custom_id";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_ENTER_METHOD = "enter_method";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_EXIT_METHOD = "exit_method";

        @Column(Column.FieldType.FLOAT)
        public static final String KEY_LONGITUDE = "longitude";

        @Column(Column.FieldType.FLOAT)
        public static final String KEY_LATITUDE = "latitude";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_NAME = "name";

        @Column(Column.FieldType.FLOAT)
        public static final String KEY_RADIUS = "radius"; // in meters?

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_TRIGGER = "triggers";

        @Column(Column.FieldType.INTEGER)
        public static final String KEY_TYPE = "type";

        @Column(Column.FieldType.TEXT)
        public static final String KEY_UUID = "uuid";

        @Column(value = Column.FieldType.INTEGER, since = 2)
        public static final String KEY_CURRENTLY_ENTERED = "currently_entered";
    }
}