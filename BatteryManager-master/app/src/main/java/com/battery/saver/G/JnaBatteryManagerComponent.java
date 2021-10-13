package com.battery.saver.G;

import com.battery.saver.G.modules.AppModule;
import com.battery.saver.G.modules.PersistencyModule;
import com.battery.saver.G.notification.NotificationManager;
import com.battery.saver.G.service.ReceiveTransitionsIntentService;
import com.battery.saver.G.service.TransitionService;
import com.battery.saver.G.service.TriggerManager;
import com.battery.saver.G.view.AddEditGeofenceActivity;
import com.battery.saver.G.view.BaseActivity;
import com.battery.saver.G.view.GeofencesActivity;
import com.battery.saver.G.view.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PersistencyModule.class})
public interface JnaBatteryManagerComponent {
    void inject(ReceiveTransitionsIntentService object);

    void inject(GeofencesActivity object);

    void inject(SettingsActivity object);

    void inject(BaseActivity object);

    void inject(AddEditGeofenceActivity object);

    void inject(TriggerManager object);

    void inject(TransitionService object);

    void inject(NotificationManager object);

    void inject(HomeActivity object);

    void inject(BatteryLevelReceiver object);
}
