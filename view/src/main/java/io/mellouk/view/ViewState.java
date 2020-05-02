package io.mellouk.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.mellouk.common.base.BaseViewState;
import io.mellouk.common.domain.Music;

public class ViewState implements BaseViewState {
    private ViewState() {
    }

    static final class PENDING extends ViewState {
        public static final PENDING INSTANCE = new PENDING();

        private PENDING() {
        }
    }

    public static class INITIAL extends ViewState {
        @NonNull
        private final Music music;

        public INITIAL(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }
    }

    public static class ERROR extends ViewState {
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
