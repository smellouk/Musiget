package io.mellouk.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.mellouk.common.base.BaseViewState;
import io.mellouk.common.domain.Music;

class ServiceState implements BaseViewState {
    private ServiceState() {

    }

    static final class INITIAL extends ServiceState {
        static final INITIAL INSTANCE = new INITIAL();
    }

    static final class PENDING extends ServiceState {
        static final PENDING INSTANCE = new PENDING();

        private PENDING() {
        }
    }

    static final class MUSIC_READY extends ServiceState {
        @NonNull
        private final Music music;

        MUSIC_READY(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        Music getMusic() {
            return music;
        }
    }

    static final class ERROR extends ServiceState {
        @Nullable
        private final String message;

        ERROR(@Nullable final String message) {
            this.message = message;
        }

        @Nullable
        String getMessage() {
            return message;
        }
    }

    static final class PLAY extends ServiceState {
        static final PLAY INSTANCE = new PLAY();
    }

    static final class PAUSE extends ServiceState {
        static final PAUSE INSTANCE = new PAUSE();
    }

    static final class STOP extends ServiceState {
        static final STOP INSTANCE = new STOP();
    }
}


