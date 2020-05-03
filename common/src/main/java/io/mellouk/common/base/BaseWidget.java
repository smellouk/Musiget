package io.mellouk.common.base;

import android.Manifest;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.mellouk.common.utils.factory.BaseViewModelFactory;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseWidget<ComponentProvider extends BaseComponentProvider,
        State extends BaseViewState,
        ViewModel extends BaseViewModel<State>> extends AppWidgetProvider {
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected ComponentProvider componentProvider;
    protected ViewModel viewModel;

    @Inject
    BaseViewModelFactory viewModelFactory;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        attachComponentProvider(context);

        inject();

        viewModel = viewModelFactory.get(getViewModelClass());

        compositeDisposable.add(viewModel.stateObserver.subscribe(this::handleWidgetState));
    }

    @Override
    public void onDeleted(final Context context, final int[] appWidgetIds) {
        compositeDisposable.clear();
        if (viewModel != null) {
            viewModel.onCleared();
        }
        super.onDeleted(context, appWidgetIds);
    }

    public void renderDefaultWidgetState() {
        // NO OP
    }

    public boolean isStoragePermissionGranted() {
        return sharedPreferences.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, false);
    }

    public void forceInjection(@NonNull final Context context) {
        attachComponentProvider(context);

        inject();
    }

    public abstract void inject();

    public abstract Class<ViewModel> getViewModelClass();

    public abstract Class<ComponentProvider> getComponentProviderClass();

    public abstract void handleWidgetState(State state);

    private void attachComponentProvider(final Context context) {
        final Class<ComponentProvider> componentProviderClass = getComponentProviderClass();
        final Application app = (Application) context.getApplicationContext();
        if (componentProviderClass.isInstance(app)) {
            componentProvider = (ComponentProvider) app;
        } else {
            throw new IllegalArgumentException("Component provider is not implemented on app class");
        }
    }
}
