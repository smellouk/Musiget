package io.mellouk.repository.repos;

import androidx.annotation.NonNull;

import java.util.List;

import io.mellouk.repository.disk.MusicFetcher;
import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public class MusicRepositoryImpl implements MusicRepository {
    @NonNull
    private final MusicFetcher fetcher;

    public MusicRepositoryImpl(@NonNull final MusicFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public Single<List<MusicEntity>> getMusicList() {
        return fetcher.getMusic();
    }
}
