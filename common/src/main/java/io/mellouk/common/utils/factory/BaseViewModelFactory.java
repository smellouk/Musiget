package io.mellouk.common.utils.factory;

import androidx.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.mellouk.common.base.BaseViewModel;

public class BaseViewModelFactory {
    @NonNull
    private final Map<Class<? extends BaseViewModel>, Provider<BaseViewModel>> viewModels;

    @Inject
    BaseViewModelFactory(@NonNull final Map<Class<? extends BaseViewModel>, Provider<BaseViewModel>> viewModels) {
        this.viewModels = viewModels;
    }

    @NonNull
    public <T extends BaseViewModel> T get(@NonNull final Class<T> viewModelClass) {
        return (T) viewModels.get(viewModelClass).get();
    }
}
