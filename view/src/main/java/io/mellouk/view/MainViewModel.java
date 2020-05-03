package io.mellouk.view;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.Commandable;
import io.mellouk.common.base.BaseActivityViewModel;
import io.mellouk.view.di.MainScope;
import io.mellouk.view.domain.GetCurrentPlayingMusicUseCase;

@MainScope
public class MainViewModel extends BaseActivityViewModel<ViewState> implements Commandable<Command> {
    @NonNull
    private final GetCurrentPlayingMusicUseCase getCurrentPlayingMusicUseCase;
    @NonNull
    private final ViewStateMapper viewStateMapper;


    @Inject
    MainViewModel(@NonNull final GetCurrentPlayingMusicUseCase getCurrentPlayingMusicUseCase,
                  @NonNull final ViewStateMapper viewStateMapper
    ) {
        this.getCurrentPlayingMusicUseCase = getCurrentPlayingMusicUseCase;
        this.viewStateMapper = viewStateMapper;
    }

    @Override
    public ViewState getInitialState() {
        return ViewState.PENDING.INSTANCE;
    }

    @Override
    public void onCommand(final Command command) {
        switch (command) {
            case INIT:
                stateObserver.setValue(init());
                break;
            default:
                throw new IllegalArgumentException("Command is not handled");
        }
    }

    private ViewState.PENDING init() {
        addObservable(getCurrentPlayingMusicUseCase.buildObservable(),
                currentMusicDataState -> stateObserver.setValue(viewStateMapper.map(currentMusicDataState)),
                throwable -> stateObserver.setValue(viewStateMapper.map(throwable))
        );
        return ViewState.PENDING.INSTANCE;
    }
}
