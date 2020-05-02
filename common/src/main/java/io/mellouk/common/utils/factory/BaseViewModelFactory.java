package io.mellouk.common.utils.factory;


import androidx.annotation.NonNull;

import java.util.Map;

import javax.inject.Provider;

abstract class BaseViewModelFactory<ViewModel> {
    @NonNull
    final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    BaseViewModelFactory(@NonNull final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {
        this.viewModels = viewModels;
    }
}
