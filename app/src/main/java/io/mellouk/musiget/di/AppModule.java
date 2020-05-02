package io.mellouk.musiget.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dagger.Module;
import dagger.Provides;
import io.mellouk.common.utils.FormatUtils;

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

    @ApplicationScope
    @Provides
    LocalBroadcastManager provideBroadCastManager(@NonNull final Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    @ApplicationScope
    @Provides
    FormatUtils provideFormatUtils() {
        return new FormatUtils();
    }
}
