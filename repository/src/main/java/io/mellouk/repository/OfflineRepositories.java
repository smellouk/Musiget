package io.mellouk.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.repository.di.DaggerOfflineComponent;
import io.mellouk.repository.di.FetcherModule;
import io.mellouk.repository.repos.MusicRepository;

public class OfflineRepositories {
    @Inject
    MusicRepository musicRepository;

    public OfflineRepositories(@NonNull final Context context) {
        DaggerOfflineComponent.builder()
                .fetcherModule(new FetcherModule(context))
                .build()
                .inject(this);
    }

    @NonNull
    public MusicRepository getMusicRepository() {
        return musicRepository;
    }
}
