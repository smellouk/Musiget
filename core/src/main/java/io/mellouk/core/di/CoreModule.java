package io.mellouk.core.di;

import androidx.annotation.NonNull;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mellouk.common.base.BaseServiceViewModel;
import io.mellouk.common.di.ServiceViewModelKey;
import io.mellouk.core.MusicViewModel;

@Module
public interface CoreModule {
    @Binds
    @IntoMap
    @CoreScope
    @ServiceViewModelKey(MusicViewModel.class)
    BaseServiceViewModel bindMusicViewModel(@NonNull MusicViewModel musicViewModel);
}
