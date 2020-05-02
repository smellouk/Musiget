package io.mellouk.common.utils.factory;

import androidx.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import io.mellouk.common.base.BaseServiceViewModel;

public class BaseServiceViewModelFactory extends BaseViewModelFactory<BaseServiceViewModel> {

    @Inject
    public BaseServiceViewModelFactory(@NonNull final Map<Class<? extends BaseServiceViewModel>, Provider<BaseServiceViewModel>> viewModels) {
        super(viewModels);
    }

    @NonNull
    public <T extends BaseServiceViewModel> T get(@NonNull final Class<T> viewModelClass) {
        return (T) viewModels.get(viewModelClass).get();
    }
}
