package io.mellouk.musiget.di;

import android.content.Context;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import io.mellouk.repository.OfflineRepositories;
import io.mellouk.repository.repos.MusicRepository;

@Module
class ReposModule {
    @ApplicationScope
    @Provides
    OfflineRepositories provideOfflineRepositories(@NonNull final Context context) {
        return new OfflineRepositories(context);
    }

    @ApplicationScope
    @Provides
    MusicRepository provideMusicRepository(@NonNull final OfflineRepositories offlineRepositories) {
        return offlineRepositories.getMusicRepository();
    }
}
