package com.battery.saver.G.view;

import android.app.FragmentManager;

public interface AddGeofenceDialog {
    public void show(FragmentManager fragmentManager);
    public void setLocallyListener(AddGeofenceDialogFragment.AddGeofenceResultListener resultListener);
    public void setImportListener(AddGeofenceDialogFragment.AddGeofenceResultListener resultListener);
}