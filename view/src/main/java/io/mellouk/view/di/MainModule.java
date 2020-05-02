package io.mellouk.view.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mellouk.common.di.ActivityViewModelKey;
import io.mellouk.view.MainActivityViewModel;

@Module
public interface MainModule {
    @Binds
    @IntoMap
    @MainScope
    @ActivityViewModelKey(MainActivityViewModel.class)
    ViewModel bindMainActivityViewModel(@NonNull MainActivityViewModel viewModel);
}
