package io.mellouk.repository.di;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.mellouk.repository.disk.MusicContentCursorProvider;
import io.mellouk.repository.disk.MusicFetcher;
import io.mellouk.repository.disk.MusicFetcherImpl;

@Module
public class FetcherModule {
    private final Context context;

    public FetcherModule(@NonNull final Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    MusicContentCursorProvider provideMusicContentCursorProvider(final Context context) {
        return new MusicContentCursorProvider(context);
    }

    @Singleton
    @Provides
    MusicFetcher provideMusicFetcher(@NonNull final MusicContentCursorProvider contentCursorProvider) {
        return new MusicFetcherImpl(contentCursorProvider);
    }
}
