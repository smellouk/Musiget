package io.mellouk.widget;

import androidx.annotation.NonNull;

import io.mellouk.common.base.BaseCommand;
import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;

class Command implements BaseCommand {
    private Command() {
    }

    static class UPDATE extends Command {
        static final UPDATE INSTANCE = new UPDATE();

        private UPDATE() {
        }
    }

    static class ON_MUSIC_RECEIVE extends Command {
        @NonNull
        private final Music music;

        public ON_MUSIC_RECEIVE(@NonNull final Music music) {
            this.music = music;
        }

        @NonNull
        public Music getMusic() {
            return music;
        }
    }

    static class ON_PROGRESS_RECEIVE extends Command {
        @NonNull
        private final MusicProgress musicProgress;

        public ON_PROGRESS_RECEIVE(@NonNull final MusicProgress musicProgress) {
            this.musicProgress = musicProgress;
        }

        @NonNull
        public MusicProgress getMusicProgress() {
            return musicProgress;
        }
    }
}
