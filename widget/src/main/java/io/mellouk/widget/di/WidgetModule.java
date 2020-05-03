package io.mellouk.widget.di;

import androidx.annotation.NonNull;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mellouk.common.base.BaseViewModel;
import io.mellouk.common.di.ViewModelKey;
import io.mellouk.widget.WidgetViewModel;

@Module
public interface WidgetModule {
    @Binds
    @IntoMap
    @WidgetScope
    @ViewModelKey(WidgetViewModel.class)
    BaseViewModel bindWidgetViewModel(@NonNull WidgetViewModel viewModel);
}
