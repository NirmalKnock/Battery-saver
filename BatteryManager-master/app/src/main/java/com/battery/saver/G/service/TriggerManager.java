package com.battery.saver.G.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.battery.saver.G.JnaBatteryManagerApplication;
import com.battery.saver.G.controller.AudioController;
import com.battery.saver.G.controller.BluetoothController;
import com.battery.saver.G.controller.NetworkController;
import com.battery.saver.G.model.EventType;
import com.battery.saver.G.model.Geofences;
import com.battery.saver.G.notification.NotificationManager;
import com.battery.saver.G.utils.Constants;
import com.battery.saver.G.utils.Preferences;
import com.google.android.gms.location.Geofence;

import java.util.Random;

import javax.inject.Inject;

import static com.battery.saver.G.JnaBatteryManagerApplication.getAppContext;
import static com.battery.saver.G.JnaBatteryManagerApplication.getApplication;

//import com.example.jeremy.controller.LocativeApplication;

//import static com.example.jeremy.controller.LocativeApplication.getApplication;

public class TriggerManager {

    @Inject
    SharedPreferences mPreferences;

    @Inject
    NotificationManager mNotificationManager;

    private boolean wifiGeofenceToggle = false;
    private boolean bluetoothGeofenceToggle = false;
    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());

    NetworkController networkController = NetworkController.getInstance(getAppContext());
    BluetoothController bluetoothController = BluetoothController.getInstance(getAppContext());
    AudioController audioController = AudioController.getInstance(getAppContext());

    public TriggerManager() {
        ((JnaBatteryManagerApplication) getApplication()).inject(this);
    }

    public void triggerTransition(Geofences.Geofence fence, int transitionType) {
        String additionalNotification = "";
        bluetoothGeofenceToggle = preferences.getBoolean("bluetoothGeofenceToggleEnabled", false);
        wifiGeofenceToggle = preferences.getBoolean("wifiGeofenceToggleEnabled", false);
        // not global url is set, bail out and show classic notification
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            if (bluetoothGeofenceToggle) {
                bluetoothController.toggleBluetooth(true);
                additionalNotification += " , bluetooth turned on";
            }
            if (wifiGeofenceToggle) {
                networkController.toggleWiFi(true);
                additionalNotification += " , wifi turned on";
            }
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            if (bluetoothGeofenceToggle) {
                bluetoothController.toggleBluetooth(false);
                additionalNotification += ", bluetooth turned off";
            }
            if (wifiGeofenceToggle) {
                networkController.toggleWiFi(false);
                additionalNotification += ", wifi turned off";
            }
        }
        Log.d(Constants.LOG, "Presenting classic notification for " + fence.uuid);
        if (mPreferences.getBoolean(Preferences.NOTIFICATION_SUCCESS, false)) {
            mNotificationManager.showNotification(
                    fence.getRelevantId(),
                    new Random().nextInt(),
                    transitionType, additionalNotification
            );
        }
    }

    @Nullable
    private EventType getEventType(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return EventType.ENTER;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return EventType.EXIT;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return EventType.ENTER;
            default:
                return null;
        }

    }
}
