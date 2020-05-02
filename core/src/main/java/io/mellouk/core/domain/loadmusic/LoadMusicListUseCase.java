package io.mellouk.core.domain.loadmusic;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseUseCase;
import io.mellouk.repository.repos.MusicRepository;
import io.reactivex.Observable;

public class LoadMusicListUseCase implements BaseUseCase<LoadMusicDataState> {
    @NonNull
    private final MusicRepository musicRepository;

    @Inject
    LoadMusicListUseCase(@NonNull final MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Observable<LoadMusicDataState> buildObservable() {
        return musicRepository.getMusicList().toObservable()
                .doOnNext(musicRepository::cacheMusicList)
                .doOnError(LoadMusicDataState.Fail::new)
                .map(musicEntities -> LoadMusicDataState.Successful);
    }
}
