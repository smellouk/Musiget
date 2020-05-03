package io.mellouk.widget.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;
import io.mellouk.widget.PlayerWidget;
import io.mellouk.widget.R;
import io.mellouk.widget.di.WidgetScope;

import static io.mellouk.widget.WidgetConstants.NEXT_PLAYER_ACTION;
import static io.mellouk.widget.WidgetConstants.OPEN_PERMISSION_ACTIVITY_ACTION;
import static io.mellouk.widget.WidgetConstants.PLAY_PLAYER_ACTION;
import static io.mellouk.widget.WidgetConstants.STOP_PLAYER_ACTION;

@WidgetScope
public class PlayerView {
    @Inject
    Context context;

    @Inject
    public PlayerView() {
    }

    public void showErrorView(@NonNull final RemoteViews remoteViews, final boolean isVisible) {
        remoteViews.setInt(R.id.player, "setVisibility", isVisible ? View.GONE : View.VISIBLE);
        remoteViews.setInt(R.id.tv_error, "setVisibility", isVisible ? View.VISIBLE : View.GONE);
    }

    public void setRootClickListener(@NonNull final Context context, @NonNull final RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.root, getPendingSelfIntent(context, OPEN_PERMISSION_ACTIVITY_ACTION));
    }

    public void setStopClickListener(@NonNull final Context context, @NonNull final RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.im_stop, getPendingSelfIntent(context, STOP_PLAYER_ACTION));
    }

    public void setPlayClickListener(@NonNull final Context context, @NonNull final RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.im_play, getPendingSelfIntent(context, PLAY_PLAYER_ACTION));
    }

    public void setNextClickListener(@NonNull final Context context, @NonNull final RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.im_next, getPendingSelfIntent(context, NEXT_PLAYER_ACTION));
    }

    public void setErrorMessage(@NonNull final RemoteViews remoteViews, final String message) {
        remoteViews.setTextViewText(R.id.tv_error, message);
    }

    public void setMusicMetaViews(@NonNull final RemoteViews remoteViews, @NonNull final Music music) {
        remoteViews.setTextViewText(R.id.tv_title, music.getTitle());
    }

    public void setMusicProgress(@NonNull final RemoteViews remoteViews, @NonNull final MusicProgress musicProgress) {
        remoteViews.setTextViewText(R.id.tv_progress, musicProgress.getCurrentPosition() + " - " + musicProgress.getDuration());
    }

    public void setPlayerImg(@NonNull final RemoteViews remoteViews, final boolean isPlaying) {
        final int imageSrc;
        if (isPlaying) {
            imageSrc = R.drawable.ic_pause;
        } else {
            imageSrc = R.drawable.ic_play;
        }

        remoteViews.setImageViewResource(R.id.im_play, imageSrc);
    }


    public void reset(final int[] appWidgetIds) {
        final RemoteViews remoteViews = createRemoteView();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        showErrorView(remoteViews, false);
        setMusicProgress(remoteViews, new MusicProgress("00:00", "00:00", -1));
        setPlayerImg(remoteViews, false);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public void changeWidgetsPlayerImg(final int[] appWidgetIds, final boolean isPlaying) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews remoteViews = createRemoteView();
        setPlayerImg(remoteViews, isPlaying);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }


    public RemoteViews createRemoteView() {
        return new RemoteViews(context.getPackageName(), R.layout.music_player_widget);
    }

    private PendingIntent getPendingSelfIntent(@NonNull final Context context, @NonNull final String action) {
        final Intent intent = new Intent(context, PlayerWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
