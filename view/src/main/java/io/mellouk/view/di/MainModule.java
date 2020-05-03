package io.mellouk.view.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mellouk.common.di.ActivityViewModelKey;
import io.mellouk.view.MainViewModel;

@Module
public interface MainModule {
    @Binds
    @IntoMap
    @MainScope
    @ActivityViewModelKey(MainViewModel.class)
    ViewModel bindMainActivityViewModel(@NonNull MainViewModel viewModel);
}
