package io.mellouk.musiget;

import android.app.Application;

import io.mellouk.musiget.di.AppComponent;
import io.mellouk.musiget.di.AppModule;
import io.mellouk.musiget.di.DaggerAppComponent;

public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }
}
