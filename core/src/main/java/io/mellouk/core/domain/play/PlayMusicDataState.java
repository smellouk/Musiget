package io.mellouk.core.domain.play;

import io.mellouk.common.base.BaseDataState;

public class PlayMusicDataState implements BaseDataState {

    private PlayMusicDataState() {

    }

    public static class CanPlay extends PlayMusicDataState {
        static CanPlay INSTANCE = new CanPlay();

        private CanPlay() {
        }
    }

    public static class Fail extends PlayMusicDataState {
        static Fail INSTANCE = new Fail();

        private Fail() {
        }
    }
}
