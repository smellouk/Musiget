package io.mellouk.repository.repos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import io.mellouk.repository.disk.MusicFetcher;
import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public class MusicRepositoryImpl implements MusicRepository {
    @NonNull
    private final MusicFetcher fetcher;
    @Nullable
    private MusicEntity current = null;

    @NonNull
    private List<MusicEntity> musicEntities = Collections.emptyList();

    public MusicRepositoryImpl(@NonNull final MusicFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public Single<List<MusicEntity>> getMusicList() {
        return fetcher.getMusic();
    }

    @Override
    public void cacheMusicList(final List<MusicEntity> musicEntities) {
        this.musicEntities = musicEntities;
    }

    @Override
    public List<MusicEntity> getCachedMusicList() {
        return musicEntities;
    }

    @Nullable
    @Override
    public MusicEntity getCurrentPlayingMusic() {
        return current;
    }

    @Override
    public void setCurrentPlayingMusic(@Nullable final MusicEntity musicEntity) {
        current = musicEntity;
    }
}
