package io.mellouk.repository.repos;

import androidx.annotation.Nullable;

import java.util.List;

import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public interface MusicRepository {
    Single<List<MusicEntity>> getMusicList();

    void cacheMusicList(final List<MusicEntity> musicEntities);

    List<MusicEntity> getCachedMusicList();

    @Nullable
    MusicEntity getCurrentPlayingMusic();

    void setCurrentPlayingMusic(@Nullable final MusicEntity musicEntity);
}
