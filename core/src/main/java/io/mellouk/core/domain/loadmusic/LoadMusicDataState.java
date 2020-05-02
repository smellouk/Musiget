package io.mellouk.core.domain.loadmusic;

import androidx.annotation.NonNull;

import io.mellouk.common.base.BaseDataState;

public class LoadMusicDataState implements BaseDataState {
    public final static LoadMusicDataState Successful = new LoadMusicDataState();

    private LoadMusicDataState() {

    }

    public static class Fail extends LoadMusicDataState {
        @NonNull
        private final Throwable throwable;

        public Fail(@NonNull final Throwable throwable) {
            this.throwable = throwable;
        }

        @NonNull
        public Throwable getThrowable() {
            return throwable;
        }
    }
}
