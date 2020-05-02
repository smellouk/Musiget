package io.mellouk.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.mellouk.common.base.BaseViewState;
import io.mellouk.common.domain.Music;

class ViewState implements BaseViewState {
    private ViewState() {

    }

    static final class INITIAL extends ViewState {
        public static final INITIAL INSTANCE = new INITIAL();
    }

    static final class PENDING extends ViewState {
        public static final PENDING INSTANCE = new PENDING();
    }

    static final class MUSIC_READY extends ViewState {
        @NonNull
        private final Music music;

        public MUSIC_READY(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }
    }

    static final class ERROR extends ViewState {
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


