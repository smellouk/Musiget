package io.mellouk.core;

import android.app.Notification;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.mellouk.common.base.BaseService;
import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;
import io.mellouk.common.utils.BroadcastConstants;
import io.mellouk.common.utils.FormatUtils;
import io.mellouk.common.utils.RxUtils;
import io.mellouk.core.di.CoreComponent;
import io.reactivex.Flowable;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MusicService extends BaseService<CoreComponent.ComponentProvider, ServiceState, MusicViewModel>
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static final String PLAY_OR_PAUSE_ACTION = "PLAY_OR_PAUSE_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String NEXT_ACTION = "NEXT_ACTION";

    private final static String TAG = MusicService.class.getSimpleName();
    private static final int FREQUENCY = 1;
    final Intent musicIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION);
    final Intent errorIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION);
    final Intent progressIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_PROGRESS_ACTION);
    final Intent stopIntent = new Intent(BroadcastConstants.MUSIC_SERVICE_STOP_ACTION);

    @Inject
    LocalBroadcastManager localBroadcastManager;

    @Inject
    FormatUtils formatUtils;

    @Inject
    NotificationCompat.Builder notificationBuilder;

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
    public void handleServiceState(final ServiceState state) {
        if (state instanceof ServiceState.INITIAL) {
            defaultServiceState();
            startObservingDuration();
            viewModel.onCommand(Command.LOAD_MUSIC_LIST);
        } else if (state instanceof ServiceState.PENDING) {
            defaultServiceState();
        } else if (state instanceof ServiceState.MUSIC_READY) {
            handleMusicReadyState(state);
        } else if (state instanceof ServiceState.ERROR) {
            handleErrorState(state);
        } else if (state instanceof ServiceState.PLAY) {
            mediaPlayer.start();
        } else if (state instanceof ServiceState.PAUSE) {
            mediaPlayer.pause();
        } else if (state instanceof ServiceState.STOP) {
            stopPlayer();
        } else {
            throw new IllegalArgumentException("State: " + state.getClass().getName() + " is not handled");
        }
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case PLAY_OR_PAUSE_ACTION:
                    handlePlayPauseAction();
                    break;
                case NEXT_ACTION:
                    viewModel.onCommand(Command.NEXT_MUSIC);
                    break;
                case STOP_ACTION:
                    viewModel.onCommand(Command.STOP);
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(final MediaPlayer mp) {
        viewModel.onCommand(Command.NEXT_MUSIC);
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void handlePlayPauseAction() {
        if (mediaPlayer.isPlaying()) {
            viewModel.onCommand(Command.PAUSE);
        } else {
            viewModel.onCommand(Command.PLAY);
        }
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

    private void startObservingDuration() {
        compositeDisposable.add(
                Flowable.interval(FREQUENCY, TimeUnit.SECONDS, Schedulers.io())
                        .onBackpressureDrop()
                        .retry()
                        .flatMapMaybe((Function<Long, MaybeSource<MusicProgress>>) aLong -> RxUtils.ofNullable(getMusicProgress()))
                        .subscribe(this::broadcastProgress, throwable -> broadcastError(throwable.getMessage()))
        );
    }

    private void handleMusicReadyState(final ServiceState state) {
        final ServiceState.MUSIC_READY musicReadyState = (ServiceState.MUSIC_READY) state;
        playMusic(musicReadyState.getMusic());
        broadcastMusic(musicReadyState.getMusic());
        notificationBuilder.setContentTitle(musicReadyState.getMusic().getTitle());
    }

    private void handleErrorState(final ServiceState state) {
        final ServiceState.ERROR errorState = (ServiceState.ERROR) state;
        broadcastError(errorState.getMessage());
    }

    private void broadcastMusic(@NonNull final Music music) {
        musicIntent.putExtra(BroadcastConstants.MUSIC_SERVICE_MUSIC_KEY, music);
        localBroadcastManager.sendBroadcast(musicIntent);
    }

    private void broadcastError(@Nullable final String error) {
        errorIntent.putExtra(BroadcastConstants.MUSIC_SERVICE_ERROR_KEY, error);
        localBroadcastManager.sendBroadcast(errorIntent);
    }

    private void broadcastProgress(@NonNull final MusicProgress progress) {
        final Notification notification = notificationBuilder
                .setContentText(progress.getCurrentPosition() + " - " + progress.getDuration())
                .build();
        startForeground(1, notification);
        progressIntent.putExtra(BroadcastConstants.MUSIC_SERVICE_PROGRESS_KEY, progress);
        localBroadcastManager.sendBroadcast(progressIntent);
    }

    @Nullable
    private MusicProgress getMusicProgress() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            final Notification notification = notificationBuilder
                    .setContentText("Player is paused")
                    .build();
            startForeground(1, notification);
            return null;
        }

        try {
            final int currentPosition = mediaPlayer.getCurrentPosition();
            final int duration = mediaPlayer.getDuration();
            final String currentStr = formatUtils.toTime(currentPosition);
            final String durationStr = formatUtils.toTime(duration);
            final int progress = formatUtils.toProgress(currentPosition, duration);
            return new MusicProgress(currentStr, durationStr, progress);
        } catch (final Exception ignore) {
            return null;
        }
    }

    private void stopPlayer() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        localBroadcastManager.sendBroadcast(stopIntent);
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnPreparedListener(null);
            mediaPlayer = null;
        }
    }
}
