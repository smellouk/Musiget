package io.mellouk.common.utils.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ActivityViewModelFactory extends BaseViewModelFactory<ViewModel> implements ViewModelProvider.Factory {
    @Inject
    public ActivityViewModelFactory(@NonNull final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {
        super(viewModels);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        return (T) viewModels.get(modelClass).get();
    }
}
