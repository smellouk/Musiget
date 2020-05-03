package io.mellouk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import javax.inject.Inject;

import io.mellouk.common.base.BaseWidget;
import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;
import io.mellouk.common.utils.BroadcastConstants;
import io.mellouk.core.MusicService;
import io.mellouk.widget.WidgetState.MUSIC_META;
import io.mellouk.widget.di.WidgetComponent;
import io.mellouk.widget.view.PlayerView;

import static io.mellouk.widget.WidgetConstants.NEXT_PLAYER_ACTION;
import static io.mellouk.widget.WidgetConstants.OPEN_PERMISSION_ACTIVITY_ACTION;
import static io.mellouk.widget.WidgetConstants.PLAY_PLAYER_ACTION;
import static io.mellouk.widget.WidgetConstants.STOP_PLAYER_ACTION;
import static io.mellouk.widget.WidgetState.MUSIC_PROGRESS;
import static io.mellouk.widget.WidgetState.PENDING;

public class PlayerWidget extends BaseWidget<WidgetComponent.ComponentProvider, WidgetState, WidgetViewModel> {
    private static final String IS_PLAYING_KEY = "IS_PLAYING_KEY";

    @Inject
    LocalBroadcastManager localBroadcastManager;

    @Inject
    IntentFilter intentFilter;

    @Inject
    Context context;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    PlayerView playerView;

    @Override
    public void inject() {
        componentProvider.getWidgetComponent().inject(this);
    }

    @Override
    public Class<WidgetViewModel> getViewModelClass() {
        return WidgetViewModel.class;
    }

    @Override
    public Class<WidgetComponent.ComponentProvider> getComponentProviderClass() {
        return WidgetComponent.ComponentProvider.class;
    }

    @Override
    public void handleWidgetState(final WidgetState state) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final int[] appWidgetIds = getAppWidgetIds(context);
        final RemoteViews remoteViews = playerView.createRemoteView();
        playerView.setStopClickListener(context, remoteViews);
        playerView.setPlayClickListener(context, remoteViews);
        playerView.setNextClickListener(context, remoteViews);

        playerView.showErrorView(remoteViews, false);
        if (state instanceof PENDING) {
            renderDefaultWidgetState();
        } else if (state instanceof MUSIC_META) {
            final MUSIC_META musicMetaState = (MUSIC_META) state;
            playerView.setMusicMetaViews(remoteViews, musicMetaState.getMusic());
        } else if (state instanceof MUSIC_PROGRESS) {
            playerView.setMusicMetaViews(remoteViews, ((MUSIC_PROGRESS) state).getMusic());
            playerView.setMusicProgress(remoteViews, ((MUSIC_PROGRESS) state).getProgress());
            sharedPreferences.edit().putBoolean(IS_PLAYING_KEY, true).apply();
            playerView.setPlayerImg(remoteViews, true);
        } else if (state instanceof WidgetState.ERROR) {
            playerView.setRootClickListener(context, remoteViews);
            playerView.showErrorView(remoteViews, true);
            playerView.setErrorMessage(remoteViews, ((WidgetState.ERROR) state).getMessage());
        }

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        localBroadcastManager.registerReceiver(this, intentFilter);

        for (final int widgetId : appWidgetIds) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_player_widget);
            checkPermission(context, remoteViews);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        forceInjection(context);
        final String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case OPEN_PERMISSION_ACTIVITY_ACTION:
                    openPermissionActivity();
                    break;
                case STOP_PLAYER_ACTION:
                    playerView.reset(getAppWidgetIds(context));
                    onStopMusic(context);
                    break;
                case PLAY_PLAYER_ACTION:
                    onPlayPauseMusic(context);
                    final boolean isPlaying = sharedPreferences.getBoolean(IS_PLAYING_KEY, false);
                    playerView.changeWidgetsPlayerImg(getAppWidgetIds(context), isPlaying);
                    break;
                case NEXT_PLAYER_ACTION:
                    playerView.reset(getAppWidgetIds(context));
                    onNextMusic(context);
                    break;
                case BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION:
                    final Music music = intent.getParcelableExtra(BroadcastConstants.MUSIC_SERVICE_MUSIC_KEY);
                    viewModel.onCommand(new Command.ON_MUSIC_RECEIVE(music));
                    break;
                case BroadcastConstants.MUSIC_SERVICE_PROGRESS_ACTION:
                    final MusicProgress musicProgress = intent.getParcelableExtra(BroadcastConstants.MUSIC_SERVICE_PROGRESS_KEY);
                    viewModel.onCommand(new Command.ON_PROGRESS_RECEIVE(musicProgress));
                    break;
                case BroadcastConstants.MUSIC_SERVICE_STOP_ACTION:
                    break;
                case BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION:
                    break;
                default:
                    //NO-OP
                    break;
            }
        }
    }

    private void checkPermission(@NonNull final Context context, @NonNull final RemoteViews remoteViews) {
        if (isStoragePermissionGranted()) {
            viewModel.onCommand(Command.UPDATE.INSTANCE);
            playerView.showErrorView(remoteViews, false);
        } else {
            playerView.setRootClickListener(context, remoteViews);
            playerView.showErrorView(remoteViews, true);
        }
    }

    private void openPermissionActivity() {
        localBroadcastManager.sendBroadcast(new Intent(OPEN_PERMISSION_ACTIVITY_ACTION));
    }

    private int[] getAppWidgetIds(@NonNull final Context context) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final ComponentName playerWidget = new ComponentName(context, PlayerWidget.class);
        return appWidgetManager.getAppWidgetIds(playerWidget);
    }

    private void onNextMusic(@NonNull final Context context) {
        final Intent intent = new Intent(context, MusicService.class);
        intent.setAction(MusicService.NEXT_ACTION);
        startMusicService(context, intent);
    }

    private void onStopMusic(@NonNull final Context context) {
        final Intent intent = new Intent(context, MusicService.class);
        intent.setAction(MusicService.STOP_ACTION);
        startMusicService(context, intent);
    }

    private void onPlayPauseMusic(@NonNull final Context context) {
        final boolean isPlaying = !sharedPreferences.getBoolean(IS_PLAYING_KEY, false);
        sharedPreferences.edit().putBoolean(IS_PLAYING_KEY, isPlaying).apply();

        final Intent intent = new Intent(context, MusicService.class);
        intent.setAction(MusicService.PLAY_OR_PAUSE_ACTION);
        startMusicService(context, intent);
    }

    private void startMusicService(@NonNull final Context context, @NonNull final Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
