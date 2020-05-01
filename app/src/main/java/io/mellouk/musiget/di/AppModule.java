package io.mellouk.musiget.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(final Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    Context provideContext() {
        return context;
    }
}
