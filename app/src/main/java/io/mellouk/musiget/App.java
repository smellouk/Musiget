package io.mellouk.musiget;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import javax.inject.Inject;

import io.mellouk.core.di.CoreComponent;
import io.mellouk.musiget.di.AppComponent;
import io.mellouk.musiget.di.AppModule;
import io.mellouk.musiget.di.DaggerAppComponent;
import io.mellouk.view.MainActivity;
import io.mellouk.view.di.MainComponent;
import io.mellouk.widget.WidgetConstants;
import io.mellouk.widget.di.WidgetComponent;

public class App extends Application implements CoreComponent.ComponentProvider,
        MainComponent.ComponentProvider, WidgetComponent.ComponentProvider {
    @Inject
    LocalBroadcastManager localBroadcastManager;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);

        final IntentFilter intentFilter = new IntentFilter(WidgetConstants.OPEN_PERMISSION_ACTIVITY_ACTION);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                final Intent mainIntent = new Intent(App.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.setAction(intent.getAction());
                startActivity(mainIntent);
            }
        }, intentFilter);
    }

    @Override
    public CoreComponent getCoreComponent() {
        return appComponent.getCoreComponent();
    }

    @Override
    public MainComponent getMainComponent() {
        return appComponent.getMainComponent();
    }

    @Override
    public WidgetComponent getWidgetComponent() {
        return appComponent.getWidgetComponent();
    }
}
