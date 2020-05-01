package io.mellouk.repository.di;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.mellouk.repository.disk.MusicFetcher;
import io.mellouk.repository.repos.MusicRepository;
import io.mellouk.repository.repos.MusicRepositoryImpl;

@Module
class RepositoryModule {
    @Singleton
    @Provides
    MusicRepository provideMusicRepository(@NonNull final MusicFetcher musicFetcher) {
        return new MusicRepositoryImpl(musicFetcher);
    }
}
