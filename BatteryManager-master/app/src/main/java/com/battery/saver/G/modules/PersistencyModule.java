package com.battery.saver.G.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.battery.saver.G.JnaBatteryManagerApplication;
import com.battery.saver.G.persistent.Storage;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistencyModule {

    private JnaBatteryManagerApplication mApp;

    public PersistencyModule(JnaBatteryManagerApplication app) {
        mApp = app;
    }

    @SuppressWarnings("unused")
    @Provides
    Storage provideStorage(Context context) {
        return new Storage(context);
    }

    @SuppressWarnings("unused")
    @Provides
    SharedPreferences providePreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
