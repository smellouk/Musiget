package io.mellouk.view;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseDataState;
import io.mellouk.view.domain.CurrentMusicDataState;
import io.mellouk.view.domain.GetCurrentPlayingMusicUseCase.PreparingMusicException;

public class ViewStateMapper {
    @Inject
    public ViewStateMapper() {
    }

    ViewState map(@NonNull final BaseDataState dataState) {
        final ViewState viewState;
        if (dataState instanceof CurrentMusicDataState.Successful) {
            final CurrentMusicDataState.Successful successful = (CurrentMusicDataState.Successful) dataState;
            viewState = new ViewState.INITIAL(successful.getMusic());
        } else if (dataState instanceof CurrentMusicDataState.Fail) {
            final CurrentMusicDataState.Fail fail = (CurrentMusicDataState.Fail) dataState;
            viewState = map(fail.getThrowable());
        } else {
            throw new IllegalArgumentException("Data state is not handled here");
        }

        return viewState;
    }

    ViewState map(@NonNull final Throwable throwable) {
        if (throwable instanceof PreparingMusicException) {
            return ViewState.PENDING.INSTANCE;
        }
        return new ViewState.ERROR(throwable.getMessage());
    }
}
