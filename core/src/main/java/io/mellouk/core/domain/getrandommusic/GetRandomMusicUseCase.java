package io.mellouk.core.domain.getrandommusic;

import androidx.annotation.NonNull;

import java.util.Random;

import javax.inject.Inject;

import io.mellouk.common.base.BaseUseCase;
import io.mellouk.common.domain.Music;
import io.mellouk.core.domain.MusicMapper;
import io.mellouk.repository.entity.MusicEntity;
import io.mellouk.repository.repos.MusicRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class GetRandomMusicUseCase implements BaseUseCase<RandomMusicDataState> {
    @NonNull
    private final MusicRepository musicRepository;

    @NonNull
    private final MusicMapper mapper;

    private final Random rand = new Random();

    @Inject
    public GetRandomMusicUseCase(@NonNull final MusicRepository musicRepository,
                                 @NonNull final MusicMapper mapper
    ) {
        this.musicRepository = musicRepository;
        this.mapper = mapper;
    }

    @Override
    public Observable<RandomMusicDataState> buildObservable() {
        return Single.just(musicRepository.getCachedMusicList()).toObservable().map(
                musicEntities -> {
                    if (musicEntities.size() == 0) {
                        return null;
                    }
                    final MusicEntity selectedEntity = musicEntities.get(rand.nextInt(musicEntities.size()));
                    musicRepository.setCurrentPlayingMusic(selectedEntity);
                    return mapper.map(selectedEntity);
                }
        ).map(new Function<Music, RandomMusicDataState>() {
            @Override
            public RandomMusicDataState apply(final Music music) throws Exception {
                if (music == null) {
                    return new RandomMusicDataState.Fail(new Exception("Music list entity is empty!"));
                } else {
                    return new RandomMusicDataState.Successful(music);
                }
            }
        });
    }
}
