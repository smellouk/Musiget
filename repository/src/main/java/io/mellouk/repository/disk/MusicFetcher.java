package io.mellouk.repository.disk;

import androidx.annotation.NonNull;

import java.util.List;

import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public interface MusicFetcher {

    @NonNull
    Single<List<MusicEntity>> getMusic();
}
