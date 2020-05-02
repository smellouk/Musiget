package io.mellouk.musiget;

import android.app.Application;

import io.mellouk.core.di.CoreComponent;
import io.mellouk.musiget.di.AppComponent;
import io.mellouk.musiget.di.AppModule;
import io.mellouk.musiget.di.DaggerAppComponent;
import io.mellouk.view.di.MainComponent;

public class App extends Application implements CoreComponent.ComponentProvider, MainComponent.ComponentProvider {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public CoreComponent getCoreComponent() {
        return appComponent.getCoreComponent();
    }

    @Override
    public MainComponent getMainComponent() {
        return appComponent.getMainComponent();
    }
}
