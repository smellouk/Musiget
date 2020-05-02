package io.mellouk.common.base;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import io.mellouk.common.utils.factory.BaseServiceViewModelFactory;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseService<ComponentProvider extends BaseComponentProvider,
        State extends BaseViewState,
        ViewModel extends BaseServiceViewModel<State>> extends Service {
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected ComponentProvider componentProvider;
    protected ViewModel viewModel;

    @Inject
    BaseServiceViewModelFactory viewModelFactory;

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        attachComponentProvider();

        inject();

        viewModel = viewModelFactory.get(getViewModelClass());

        compositeDisposable.add(viewModel.stateObserver.subscribe(this::handleServiceState));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        viewModel.onCleared();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        compositeDisposable.clear();
        viewModel.onCleared();
        super.onTaskRemoved(rootIntent);
    }

    public void renderDefaultViewState() {
        // NO OP
    }

    public abstract void inject();

    public abstract Class<ViewModel> getViewModelClass();

    public abstract Class<ComponentProvider> getComponentProviderClass();

    public abstract void handleServiceState(State state);

    private void attachComponentProvider() {
        final Class<ComponentProvider> componentProviderClass = getComponentProviderClass();
        final Application app = getApplication();
        if (componentProviderClass.isInstance(app)) {
            componentProvider = (ComponentProvider) app;
        } else {
            throw new IllegalArgumentException("Component provider is not implemented on app class");
        }
    }
}
