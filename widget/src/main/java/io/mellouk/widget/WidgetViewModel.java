package io.mellouk.widget;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.Commandable;
import io.mellouk.common.base.BaseViewModel;
import io.mellouk.widget.di.WidgetScope;
import io.mellouk.widget.domain.GetCurrentPlayingMusicUseCase;

@WidgetScope
public class WidgetViewModel extends BaseViewModel<WidgetState> implements Commandable<Command> {
    private final WidgetStateMapper widgetStateMapper;
    private final GetCurrentPlayingMusicUseCase getCurrentPlayingMusicUseCase;

    @Inject
    WidgetViewModel(@NonNull final WidgetStateMapper widgetStateMapper,
                    @NonNull final GetCurrentPlayingMusicUseCase getCurrentPlayingMusicUseCase
    ) {
        this.widgetStateMapper = widgetStateMapper;
        this.getCurrentPlayingMusicUseCase = getCurrentPlayingMusicUseCase;
    }

    @Override
    public WidgetState getInitialState() {
        return WidgetState.PENDING.INSTANCE;
    }

    @Override
    public void onCommand(final Command command) {
        if (command instanceof Command.ON_PROGRESS_RECEIVE) {
            addObservable(getCurrentPlayingMusicUseCase.buildObservable(), currentMusicDataState -> {
                stateObserver.onNext(widgetStateMapper.map(currentMusicDataState, ((Command.ON_PROGRESS_RECEIVE) command).getMusicProgress()));
            }, throwable -> {
                stateObserver.onNext(widgetStateMapper.map(throwable));
            });
        } else if (command instanceof Command.UPDATE) {
            addObservable(getCurrentPlayingMusicUseCase.buildObservable(), currentMusicDataState -> {
                stateObserver.onNext(widgetStateMapper.map(currentMusicDataState));
            }, throwable -> {
                stateObserver.onNext(widgetStateMapper.map(throwable));
            });
        } else {
            stateObserver.onNext(widgetStateMapper.map(command));
        }
    }
}
