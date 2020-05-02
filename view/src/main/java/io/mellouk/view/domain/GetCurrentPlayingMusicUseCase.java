package io.mellouk.view.domain;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseUseCase;
import io.mellouk.common.utils.RxUtils;
import io.mellouk.repository.entity.MusicEntity;
import io.mellouk.repository.repos.MusicRepository;
import io.mellouk.view.di.MainScope;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

@MainScope
public class GetCurrentPlayingMusicUseCase implements BaseUseCase<CurrentMusicDataState> {
    @NonNull
    private final MusicRepository musicRepository;

    @NonNull
    private final MusicMapper mapper;

    @Inject
    GetCurrentPlayingMusicUseCase(@NonNull final MusicRepository musicRepository,
                                  @NonNull final MusicMapper mapper
    ) {
        this.musicRepository = musicRepository;
        this.mapper = mapper;
    }

    @Override
    public Observable<CurrentMusicDataState> buildObservable() {
        return Single.create((SingleOnSubscribe<MusicEntity>) emitter -> {
            final MusicEntity entity = musicRepository.getCurrentPlayingMusic();
            if (entity != null) {
                emitter.onSuccess(entity);
            } else {
                emitter.onError(new PreparingMusicException());
            }
        })
                .toObservable()
                .flatMapMaybe(entity -> RxUtils.ofNullable(mapper.map(entity)))
                .map(music -> {
                            if (music == null) {
                                return new CurrentMusicDataState.Fail(new Exception("No current music found, try to add music and restart the app"));
                            } else {
                                return new CurrentMusicDataState.Successful(music);
                            }
                        }

                );
    }

    public static class PreparingMusicException extends Exception {
        PreparingMusicException() {
            super("Preparing music");
        }
    }
}
