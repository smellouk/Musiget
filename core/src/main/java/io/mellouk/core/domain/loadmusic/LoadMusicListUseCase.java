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
                .map(musicEntities -> {
                    if (musicEntities.size() == 0) {
                        return new LoadMusicDataState.Fail(new ListEmptyException());
                    } else {
                        return LoadMusicDataState.Successful;
                    }
                });
    }

    private static class ListEmptyException extends Exception {
        ListEmptyException() {
            super("External storage does not have any music, please add music and restart the app.");
        }
    }
}
