package io.mellouk.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mellouk.common.base.BaseActivity;
import io.mellouk.common.domain.Music;
import io.mellouk.common.domain.MusicProgress;
import io.mellouk.common.utils.BroadcastConstants;
import io.mellouk.core.MusicService;
import io.mellouk.view.di.MainComponent;

public class MainActivity extends BaseActivity<MainComponent.ComponentProvider, ViewState, MainActivityViewModel> {
    private static final String PERMISSION_TO_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final boolean IS_PLAYING = true;
    private final IntentFilter intentFilter = new IntentFilter();
    private final CompoundButton.OnCheckedChangeListener checkedListener = (buttonView, isChecked) -> {
        onPlayBtnClick(isChecked);
    };

    @BindView(R2.id.tv_artist)
    TextView tvArtist;

    @BindView(R2.id.tv_title)
    TextView tvTitle;

    @BindView(R2.id.tv_path)
    TextView tvPath;

    @BindView(R2.id.tv_duration)
    TextView tvDuration;

    @BindView(R2.id.progress)
    ProgressBar progressBar;

    @BindView(R2.id.cb_play)
    CheckBox cbPlay;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            handleReceivedIntent(intent);
        }
    };
    @BindView(R2.id.im_next)
    View imNext;
    @BindView(R2.id.im_stop)
    View imStop;
    @Inject
    LocalBroadcastManager localBroadcastManager;

    @Override

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cbPlay.setOnCheckedChangeListener(checkedListener);
    }

    @Override
    public void inject() {
        componentProvider.getMainComponent().inject(this);
    }

    @Override
    public Class<MainActivityViewModel> getViewModelClass() {
        return MainActivityViewModel.class;
    }

    @Override
    public Class<MainComponent.ComponentProvider> getComponentProviderClass() {
        return MainComponent.ComponentProvider.class;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isPermissionGranted(PERMISSION_TO_READ_STORAGE)) {
            registerReceiver();
            startService();
        } else {
            requestPermission(PERMISSION_TO_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerReceiver();
                startService();
            } else {
                createToast(getString(R.string.permission_error));
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    @Override
    public void renderViewState(final ViewState state) {
        if (state instanceof ViewState.PENDING) {
            renderDefaultViewState();
        } else if (state instanceof ViewState.INITIAL) {
            final ViewState.INITIAL initialState = (ViewState.INITIAL) state;
            renderMusicMeta(initialState.getMusic());
        } else if (state instanceof ViewState.ERROR) {
            final ViewState.ERROR errorState = (ViewState.ERROR) state;
            renderErrorState(errorState);
        }
    }

    @Override
    public void showRequestPermissionRationale() {
        createToast(getString(R.string.permission_error));
    }

    @OnClick({R2.id.im_stop, R2.id.im_next})
    public void onClick(final View view) {
        final int id = view.getId();
        if (id == R.id.im_stop) {
            onStopMusic();
        } else if (id == R.id.im_next) {
            onNextMusic();
        } else {
            throw new IllegalArgumentException("Click is not handled!");
        }
    }

    private void registerReceiver() {
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_PROGRESS_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION);
        intentFilter.addAction(BroadcastConstants.MUSIC_SERVICE_STOP_ACTION);

        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    private void unregisterReceiver() {
        localBroadcastManager.unregisterReceiver(receiver);
    }

    private void startService() {
        final Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        viewModel.onCommand(Command.INIT);
    }

    private void handleReceivedIntent(@NonNull final Intent intent) {
        final String action = intent.getAction();
        if (action != null) {

            switch (action) {
                case BroadcastConstants.MUSIC_SERVICE_MUSIC_ACTION:
                    handleMusicAction(intent);
                    break;
                case BroadcastConstants.MUSIC_SERVICE_PROGRESS_ACTION:
                    handleProgressAction(intent);
                    break;
                case BroadcastConstants.MUSIC_SERVICE_ERROR_ACTION:
                    handleErrorAction(intent);
                    break;
                case BroadcastConstants.MUSIC_SERVICE_STOP_ACTION:
                    resetView();
                    break;
                default:
                    //NO-OP
                    break;
            }
        }
    }

    private void handleMusicAction(@NonNull final Intent intent) {
        final Music music = intent.getParcelableExtra(BroadcastConstants.MUSIC_SERVICE_MUSIC_KEY);
        renderMusicMeta(music);
    }

    private void handleProgressAction(@NonNull final Intent intent) {
        final MusicProgress musicProgress = intent.getParcelableExtra(BroadcastConstants.MUSIC_SERVICE_PROGRESS_KEY);
        if (musicProgress != null) {
            final String duration = String.format("%s - %s", musicProgress.getCurrentPosition(), musicProgress.getDuration());
            tvDuration.setText(duration);
            progressBar.setProgress(musicProgress.getProgress());
        }
    }

    private void handleErrorAction(@NonNull final Intent intent) {
        final String error = intent.getStringExtra(BroadcastConstants.MUSIC_SERVICE_ERROR_KEY);
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
        resetView();
    }

    private void renderMusicMeta(@Nullable final Music music) {
        if (music != null) {
            tvArtist.setText(music.getArtist());
            tvTitle.setText(music.getTitle());
            tvPath.setText(music.getPath());
            setPlayingStatus(IS_PLAYING);
        }
    }

    private void renderErrorState(final ViewState.ERROR errorState) {
        createToast(errorState.getMessage());
        resetView();
    }

    private void onNextMusic() {
        resetView();
        final Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.NEXT_ACTION);
        startService(intent);
    }

    private void onStopMusic() {
        final Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.STOP_ACTION);
        startService(intent);
    }

    private void onPlayBtnClick(final boolean isPlay) {
        final Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.PLAY_OR_PAUSE_ACTION);
        intent.putExtra(MusicService.PLAY_OR_PAUSE_KEY, isPlay);
        startService(intent);
    }

    private void resetView() {
        setPlayingStatus(!IS_PLAYING);
        progressBar.setProgress(0);
        tvDuration.setText(R.string.default_duration);
    }

    private void setPlayingStatus(final boolean isPlaying) {
        cbPlay.setOnCheckedChangeListener(null);
        cbPlay.setChecked(isPlaying);
        cbPlay.setOnCheckedChangeListener(checkedListener);
    }
}
