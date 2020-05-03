package io.mellouk.common.domain;

import androidx.annotation.NonNull;

import io.mellouk.common.base.BaseDataState;

public class CurrentMusicDataState implements BaseDataState {
    public static class Successful extends CurrentMusicDataState {
        @NonNull
        private final Music music;

        public Successful(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }
    }

    public static class Fail extends CurrentMusicDataState {
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
