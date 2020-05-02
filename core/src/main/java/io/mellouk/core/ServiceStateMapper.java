package io.mellouk.core;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.base.BaseDataState;
import io.mellouk.core.domain.getrandommusic.RandomMusicDataState;
import io.mellouk.core.domain.loadmusic.LoadMusicDataState;
import io.mellouk.core.domain.play.PlayMusicDataState;

public class ServiceStateMapper {
    @Inject
    public ServiceStateMapper() {
    }

    ServiceState map(@NonNull final BaseDataState dataState) {
        final ServiceState serviceState;
        if (dataState == LoadMusicDataState.Successful) {
            serviceState = ServiceState.PENDING.INSTANCE;
        } else if (dataState instanceof LoadMusicDataState.Fail) {
            final LoadMusicDataState.Fail fail = (LoadMusicDataState.Fail) dataState;
            serviceState = map(fail.getThrowable());
        } else if (dataState instanceof RandomMusicDataState.Successful) {
            final RandomMusicDataState.Successful successful = (RandomMusicDataState.Successful) dataState;
            serviceState = new ServiceState.MUSIC_READY(successful.getMusic());
        } else if (dataState instanceof RandomMusicDataState.Fail) {
            final RandomMusicDataState.Fail fail = (RandomMusicDataState.Fail) dataState;
            serviceState = map(fail.getThrowable());
        } else if (dataState instanceof PlayMusicDataState.CanPlay) {
            serviceState = ServiceState.PLAY.INSTANCE;
        } else {
            serviceState = ServiceState.PENDING.INSTANCE;
        }

        return serviceState;
    }

    ServiceState.ERROR map(@NonNull final Throwable throwable) {
        return new ServiceState.ERROR(throwable.getMessage());
    }
}
