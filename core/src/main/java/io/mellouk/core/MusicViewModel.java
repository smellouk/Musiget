package io.mellouk.core;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.Commandable;
import io.mellouk.common.base.BaseServiceViewModel;
import io.mellouk.core.di.CoreScope;
import io.mellouk.core.domain.getrandommusic.GetRandomMusicUseCase;
import io.mellouk.core.domain.loadmusic.LoadMusicDataState;
import io.mellouk.core.domain.loadmusic.LoadMusicListUseCase;
import io.mellouk.core.domain.play.PlayMusicDataState;
import io.mellouk.core.domain.play.PlayMusicUseCase;

@CoreScope
public class MusicViewModel extends BaseServiceViewModel<ServiceState> implements Commandable<Command> {
    @NonNull
    private final LoadMusicListUseCase loadMusicListUseCase;

    @NonNull
    private final GetRandomMusicUseCase getRandomMusicUseCase;

    @NonNull
    private final PlayMusicUseCase playMusicUseCase;


    @NonNull
    private final ServiceStateMapper serviceStateMapper;

    @Inject
    MusicViewModel(@NonNull final LoadMusicListUseCase loadMusicListUseCase,
                   @NonNull final GetRandomMusicUseCase getRandomMusicUseCase,
                   @NonNull final PlayMusicUseCase playMusicUseCase,
                   @NonNull final ServiceStateMapper serviceStateMapper
    ) {
        super();
        this.loadMusicListUseCase = loadMusicListUseCase;
        this.getRandomMusicUseCase = getRandomMusicUseCase;
        this.playMusicUseCase = playMusicUseCase;
        this.serviceStateMapper = serviceStateMapper;
    }

    @Override
    public ServiceState getInitialState() {
        return ServiceState.INITIAL.INSTANCE;
    }

    @Override
    public void onCommand(final Command command) {
        final ServiceState serviceState;
        switch (command) {
            case LOAD_MUSIC_LIST:
                serviceState = onLoadMusic();
                break;
            case NEXT_MUSIC:
                serviceState = onPrepareRandomMusic();
                break;
            case PLAY:
                serviceState = onPlayMusic();
                break;
            case PAUSE:
                serviceState = ServiceState.PAUSE.INSTANCE;
                break;
            case STOP:
                serviceState = ServiceState.STOP.INSTANCE;
                break;
            default:
                throw new IllegalArgumentException("Command: " + command.name() + " is not handled here");
        }
        stateObserver.onNext(serviceState);
    }

    private ServiceState onLoadMusic() {
        addObservable(loadMusicListUseCase.buildObservable(), loadMusicDataState -> {
            if (loadMusicDataState == LoadMusicDataState.Successful) {
                onPrepareRandomMusic();
            } else {
                stateObserver.onNext(serviceStateMapper.map(loadMusicDataState));
            }
        }, throwable -> stateObserver.onNext(serviceStateMapper.map(throwable)));
        return ServiceState.PENDING.INSTANCE;
    }

    private ServiceState onPrepareRandomMusic() {
        addObservable(getRandomMusicUseCase.buildObservable(),
                randomMusicDataState -> stateObserver.onNext(serviceStateMapper.map(randomMusicDataState)),
                throwable -> stateObserver.onNext(serviceStateMapper.map(throwable))
        );
        return ServiceState.PENDING.INSTANCE;
    }

    private ServiceState onPlayMusic() {
        addObservable(playMusicUseCase.buildObservable(), playMusicDataState -> {
            if (playMusicDataState instanceof PlayMusicDataState.Fail) {
                //Try to load the music list again
                onCommand(Command.LOAD_MUSIC_LIST);
            }
            stateObserver.onNext(serviceStateMapper.map(playMusicDataState));
        }, throwable -> stateObserver.onNext(serviceStateMapper.map(throwable)));
        return ServiceState.PENDING.INSTANCE;
    }
}

