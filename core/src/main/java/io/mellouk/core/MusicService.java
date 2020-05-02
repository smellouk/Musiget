package io.mellouk.core;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;

import javax.inject.Inject;

import io.mellouk.common.base.BaseService;
import io.mellouk.common.domain.Music;
import io.mellouk.common.utils.BroadcastConstants;
import io.mellouk.core.di.CoreComponent;

public class MusicService extends BaseService<CoreComponent.ComponentProvider, ViewState, MusicViewModel>
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private final static String TAG = MusicService.class.getSimpleName();
    final Intent musicIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION);
    final Intent errorIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION);
    final Intent progressIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION);

    @Inject
    LocalBroadcastManager localBroadcastManager;
    private MediaPlayer mediaPlayer;

    @Override
    public void inject() {
        componentProvider.getCoreComponent().inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
    }

    @Override
    public Class<MusicViewModel> getViewModelClass() {
        return MusicViewModel.class;
    }

    @Override
    public Class<CoreComponent.ComponentProvider> getComponentProviderClass() {
        return CoreComponent.ComponentProvider.class;
    }

    @Override
    public void renderViewState(final ViewState state) {
        if (state instanceof ViewState.INITIAL) {
            renderDefaultViewState();
            viewModel.onCommand(Command.LOAD_MUSIC_LIST);
        } else if (state instanceof ViewState.PENDING) {
            renderDefaultViewState();
        } else if (state instanceof ViewState.MUSIC_READY) {
            handleMusicReadyState(state);
        } else if (state instanceof ViewState.ERROR) {
            handleErrorState(state);
        } else {
            throw new IllegalArgumentException("State: " + state.getClass().getName() + " is not handled");
        }
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(final MediaPlayer mp) {
        mp.stop();
        viewModel.onCommand(Command.NEXT_MUSIC);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void playMusic(final Music music) {
        final String mediaPath = music.getPath();
        if (!mediaPath.isEmpty()) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mediaPath);
                mediaPlayer.prepareAsync();
            } catch (final Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                //Retry with different file
                viewModel.onCommand(Command.NEXT_MUSIC);
            }
        } else {
            //Retry with different file
            viewModel.onCommand(Command.NEXT_MUSIC);
        }
    }

    private void handleMusicReadyState(final ViewState state) {
        final ViewState.MUSIC_READY musicReadyState = (ViewState.MUSIC_READY) state;
        playMusic(musicReadyState.getMusic());
        broadcastMusic(musicReadyState.getMusic());
    }

    private void handleErrorState(final ViewState state) {
        final ViewState.ERROR errorState = (ViewState.ERROR) state;
        broadcastError(errorState.getMessage());
    }

    private void broadcastMusic(@NonNull final Music music) {
        musicIntent.putExtra(BroadcastConstants.MUSIC_SERVICE_MUSIC_KEY, music);
        localBroadcastManager.sendBroadcast(musicIntent);
    }

    private void broadcastError(@Nullable final String error) {
        errorIntent.putExtra(BroadcastConstants.MUSIC_SERVICE_ERROR_KEY, error);
        localBroadcastManager.sendBroadcast(musicIntent);
    }
}
