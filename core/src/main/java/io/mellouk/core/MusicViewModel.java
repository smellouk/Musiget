package io.mellouk.core;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.Commandable;
import io.mellouk.common.base.BaseServiceViewModel;
import io.mellouk.core.di.CoreScope;
import io.mellouk.core.domain.getrandommusic.GetRandomMusicUseCase;
import io.mellouk.core.domain.loadmusic.LoadMusicDataState;
import io.mellouk.core.domain.loadmusic.LoadMusicListUseCase;

@CoreScope
public class MusicViewModel extends BaseServiceViewModel<ViewState> implements Commandable<Command> {
    @NonNull
    private final LoadMusicListUseCase loadMusicListUseCase;

    @NonNull
    private final GetRandomMusicUseCase getRandomMusicUseCase;

    @NonNull
    private final ViewStateMapper viewStateMapper;

    @Inject
    MusicViewModel(@NonNull final LoadMusicListUseCase loadMusicListUseCase,
                   @NonNull final GetRandomMusicUseCase getRandomMusicUseCase,
                   @NonNull final ViewStateMapper viewStateMapper
    ) {
        super();
        this.loadMusicListUseCase = loadMusicListUseCase;
        this.getRandomMusicUseCase = getRandomMusicUseCase;
        this.viewStateMapper = viewStateMapper;
    }

    @Override
    public ViewState getInitialState() {
        return ViewState.INITIAL.INSTANCE;
    }

    @Override
    public void onCommand(final Command command) {
        final ViewState viewState;
        switch (command) {
            case LOAD_MUSIC_LIST:
                viewState = onLoadMusic();
                break;
            case NEXT_MUSIC:
                viewState = onPrepareRandomMusic();
                break;
            default:
                throw new IllegalArgumentException("Command: " + command.name() + " is not handled here");
        }
        stateObserver.onNext(viewState);
    }

    private ViewState onLoadMusic() {
        addObservable(loadMusicListUseCase.buildObservable(), loadMusicDataState -> {
            if (loadMusicDataState == LoadMusicDataState.Successful) {
                onPrepareRandomMusic();
            } else {
                stateObserver.onNext(viewStateMapper.map(loadMusicDataState));
            }
        }, throwable -> {
            stateObserver.onNext(viewStateMapper.map(throwable));
        });
        return ViewState.PENDING.INSTANCE;
    }

    private ViewState onPrepareRandomMusic() {
        addObservable(getRandomMusicUseCase.buildObservable(),
                randomMusicDataState -> stateObserver.onNext(viewStateMapper.map(randomMusicDataState)),
                throwable -> stateObserver.onNext(viewStateMapper.map(throwable))
        );
        return ViewState.PENDING.INSTANCE;
    }
}

