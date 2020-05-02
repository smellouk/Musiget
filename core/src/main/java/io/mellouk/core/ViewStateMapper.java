package io.mellouk.core;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseDataState;
import io.mellouk.core.domain.getrandommusic.RandomMusicDataState;
import io.mellouk.core.domain.loadmusic.LoadMusicDataState;

public class ViewStateMapper {
    @Inject
    public ViewStateMapper() {
    }

    ViewState map(@NonNull final BaseDataState dataState) {
        final ViewState viewState;
        if (dataState == LoadMusicDataState.Successful) {
            viewState = ViewState.PENDING.INSTANCE;
        } else if (dataState instanceof LoadMusicDataState.Fail) {
            final LoadMusicDataState.Fail fail = (LoadMusicDataState.Fail) dataState;
            viewState = map(fail.getThrowable());
        } else if (dataState instanceof RandomMusicDataState.Successful) {
            final RandomMusicDataState.Successful successful = (RandomMusicDataState.Successful) dataState;
            viewState = new ViewState.MUSIC_READY(successful.getMusic());
        } else if (dataState instanceof RandomMusicDataState.Fail) {
            final RandomMusicDataState.Fail fail = (RandomMusicDataState.Fail) dataState;
            viewState = map(fail.getThrowable());
        } else {
            throw new IllegalArgumentException("Data state is not handled here");
        }

        return viewState;
    }

    ViewState.ERROR map(@NonNull final Throwable throwable) {
        return new ViewState.ERROR(throwable.getMessage());
    }
}
