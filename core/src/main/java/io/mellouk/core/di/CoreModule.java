package io.mellouk.core.di;

import androidx.annotation.NonNull;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mellouk.common.base.BaseViewModel;
import io.mellouk.common.di.ViewModelKey;
import io.mellouk.core.MusicViewModel;

@Module
public interface CoreModule {
    @Binds
    @IntoMap
    @CoreScope
    @ViewModelKey(MusicViewModel.class)
    BaseViewModel bindMusicViewModel(@NonNull MusicViewModel musicViewModel);
}
