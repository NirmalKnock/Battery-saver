package com.battery.saver.G.modules;

import android.content.Context;

import com.battery.saver.G.JnaBatteryManagerApplication;
import com.battery.saver.G.notification.NotificationManager;
import com.battery.saver.G.service.TriggerManager;
import com.battery.saver.G.utils.ResourceUtils;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private JnaBatteryManagerApplication mApp;

    public AppModule(JnaBatteryManagerApplication application) {
        mApp = application;
    }


    @Provides
    Context getApplicationContext() {
        return mApp;
    }

    @Provides
    NotificationManager provideNotificationManager(Context context) {
        return new NotificationManager(context);
    }

    @Provides
    TriggerManager provideTriggerManager() {
        return new TriggerManager();
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }

    @Provides
    ResourceUtils provideResourceUtils() {
        return new ResourceUtils(mApp);
    }

}
