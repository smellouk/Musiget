package io.mellouk.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.mellouk.common.base.BaseViewState;
import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;

public class WidgetState implements BaseViewState {
    private WidgetState() {

    }

    static final class PENDING extends WidgetState {
        public static final PENDING INSTANCE = new PENDING();

        private PENDING() {
        }
    }

    static final class MUSIC_META extends WidgetState {
        @NonNull
        private final Music music;

        public MUSIC_META(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }
    }

    static final class MUSIC_PROGRESS extends WidgetState {
        @NonNull
        private final Music music;

        @NonNull
        private final MusicProgress progress;

        public MUSIC_PROGRESS(@NonNull final Music music, @NonNull final MusicProgress progress) {
            this.music = music;
            this.progress = progress;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }

        @NonNull
        public MusicProgress getProgress() {
            return progress;
        }
    }

    public static class ERROR extends WidgetState {
        @Nullable
        private final String message;

        public ERROR(@Nullable final String message) {
            this.message = message;
        }

        @Nullable
        public String getMessage() {
            return message;
        }
    }
}
