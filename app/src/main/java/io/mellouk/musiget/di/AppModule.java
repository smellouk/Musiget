package io.mellouk.musiget.di;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dagger.Module;
import dagger.Provides;
import io.mellouk.common.utils.BroadcastConstants;
import io.mellouk.common.utils.FormatUtils;
import io.mellouk.musiget.R;
import io.mellouk.view.MainActivity;

@Module
public class AppModule {
    private static final String SHARED_PREFS_FILE = "sharedPrefs";
    private static final String CHANNEL_NAME = "Musiget Player";
    private static final String CHANNEL_ID = "musiget_player";
    private final Context context;

    public AppModule(final Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    Context provideContext() {
        return context;
    }

    @ApplicationScope
    @Provides
    LocalBroadcastManager provideBroadCastManager(@NonNull final Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    @ApplicationScope
    @Provides
    IntentFilter provideIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_PROGRESS_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_STOP_ACTION);
        return intentFilter;
    }

    @ApplicationScope
    @Provides
    FormatUtils provideFormatUtils() {
        return new FormatUtils();
    }

    @ApplicationScope
    @Provides
    SharedPreferences provideSharedPreferences(final Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    @ApplicationScope
    @Provides
    NotificationCompat.Builder providerNotificationBuilder(final Context context) {
        final Intent notificationIntent = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        return new NotificationCompat.Builder(context, createNotificationChannel(context))
                .setSmallIcon(R.drawable.ic_play_notif)
                .setContentIntent(pendingIntent);
    }

    private String createNotificationChannel(final Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final NotificationChannel chan = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(chan);
            }
        }
        return CHANNEL_ID;
    }
}
