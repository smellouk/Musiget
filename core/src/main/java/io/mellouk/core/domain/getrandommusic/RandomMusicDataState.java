package io.mellouk.core.domain.getrandommusic;

import androidx.annotation.NonNull;

import io.mellouk.common.base.BaseDataState;
import io.mellouk.common.domain.Music;

public class RandomMusicDataState implements BaseDataState {
    public static class Successful extends RandomMusicDataState {
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

    public static class Fail extends RandomMusicDataState {
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
