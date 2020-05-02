package io.mellouk.repository.repos;

import java.util.List;

import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public interface MusicRepository {
    Single<List<MusicEntity>> getMusicList();

    void cacheMusicList(final List<MusicEntity> musicEntities);

    List<MusicEntity> getCachedMusicList();
}
