package io.mellouk.widget;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseDataState;
import io.mellouk.common.domain.CurrentMusicDataState;
import io.mellouk.common.domain.MusicProgress;
import io.mellouk.widget.Command.ON_MUSIC_RECEIVE;
import io.mellouk.widget.WidgetState.MUSIC_META;

public class WidgetStateMapper {
    @Inject
    public WidgetStateMapper() {

    }

    WidgetState map(final BaseDataState dataState) {
        return map(dataState, null);
    }

    WidgetState map(final BaseDataState dataState, final MusicProgress musicProgress) {
        if (dataState instanceof CurrentMusicDataState.Successful) {
            final CurrentMusicDataState.Successful state = (CurrentMusicDataState.Successful) dataState;
            if (musicProgress != null) {
                return new WidgetState.MUSIC_PROGRESS(state.getMusic(), musicProgress);
            } else {
                return new MUSIC_META(state.getMusic());
            }
        } else if (dataState instanceof CurrentMusicDataState.Fail) {
            return map(((CurrentMusicDataState.Fail) dataState).getThrowable());
        } else {
            return WidgetState.PENDING.INSTANCE;
        }
    }

    WidgetState map(final Command command) {
        if (command instanceof ON_MUSIC_RECEIVE) {
            final ON_MUSIC_RECEIVE musicReceiveCmd = (ON_MUSIC_RECEIVE) command;
            return new MUSIC_META(musicReceiveCmd.getMusic());
        } else {
            return WidgetState.PENDING.INSTANCE;
        }
    }

    WidgetState.ERROR map(@NonNull final Throwable throwable) {
        return new WidgetState.ERROR(throwable.getMessage());
    }
}
