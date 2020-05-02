package io.mellouk.core.domain.play;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseUseCase;
import io.mellouk.common.utils.RxUtils;
import io.mellouk.repository.entity.MusicEntity;
import io.mellouk.repository.repos.MusicRepository;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class PlayMusicUseCase implements BaseUseCase<PlayMusicDataState> {

    @NonNull
    private final MusicRepository musicRepository;

    @Inject
    public PlayMusicUseCase(@NonNull final MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Observable<PlayMusicDataState> buildObservable() {
        return RxUtils.ofNullable(musicRepository.getCurrentPlayingMusic())
                .switchIfEmpty(Maybe.just(new MusicEntity()))
                .map(entity -> {
                    if (entity.getId() != -1) {
                        return PlayMusicDataState.CanPlay.INSTANCE;
                    } else {
                        return PlayMusicDataState.Fail.INSTANCE;
                    }
                }).toObservable();
    }
}