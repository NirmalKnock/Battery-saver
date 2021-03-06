package com.battery.saver.G.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.battery.saver.G.JnaBatteryManagerApplication;
import com.battery.saver.G.R;
import com.battery.saver.G.utils.Preferences;
import com.battery.saver.G.utils.ResourceUtils;
import com.battery.saver.G.view.GeofencesActivity;
import com.google.android.gms.location.Geofence;

import javax.inject.Inject;

import static com.battery.saver.G.JnaBatteryManagerApplication.getApplication;


public class NotificationManager {

    @Inject
    SharedPreferences mPrefs;

    @Inject
    ResourceUtils mResourceUtils;

    private Context mContext;

    public NotificationManager(Context context) {
        ((JnaBatteryManagerApplication) getApplication()).inject(this);
        mContext = context;
    }

    public NotificationManager(SharedPreferences mPrefs, ResourceUtils mResourceUtils) {
        ((JnaBatteryManagerApplication) getApplication()).inject(this);
        this.mPrefs = mPrefs;
        this.mResourceUtils = mResourceUtils;
    }

    public void showNotification(String title, String body) {
        notify(getDefaultBuilder(title).setContentText(body), 0);
    }

    public void showNotification(String title, int id, int transitionType, String additionalNotification) {

        NotificationCompat.Builder builder = getDefaultBuilder(title)
                .setContentText("Has been " + getTransitionTypeString(transitionType) + additionalNotification);
        notify(builder, transitionType * 100 + id);
    }

    private void notify(NotificationCompat.Builder builder, int id) {
        android.app.NotificationManager notificationManager = getDefaultNotificationManager();
        if (mPrefs.getBoolean(Preferences.NOTIFICATION_SHOW_ONLY_LATEST, false)) {
            notificationManager.cancelAll();
        }
        notificationManager.notify(id, builder.build());
    }

    private NotificationCompat.Builder getDefaultBuilder(String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.location_black)
                .setColor(0x17BDFF)
                .setContentTitle(title)
                .setVibrate(new long[]{500, 500})
                //.setLights(0x17BDFF, 3000, 3000) //LED
                .setContentIntent(getActivityIntent())
                .setAutoCancel(true);


       /*  if (mPrefs.getBoolean(Preferences.NOTIFICATION_SOUND, false)) {
         builder.setSound(mResourceUtils.rawResourceUri(R.raw.notification));
         }*/

        return builder;
    }

    private android.app.NotificationManager getDefaultNotificationManager() {
        return (android.app.NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent getActivityIntent() {
        Intent intent = new Intent(mContext, GeofencesActivity.class);
        intent.putExtra(GeofencesActivity.NOTIFICATION_CLICK, true);
        return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getTransitionTypeString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "left";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "entered";
            default:
                return "visited";
        }
    }
}