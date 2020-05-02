package io.mellouk.common.base;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import io.mellouk.common.utils.factory.ActivityViewModelFactory;

public abstract class BaseActivity<ComponentProvider extends BaseComponentProvider,
        State extends BaseViewState,
        ViewModel extends BaseActivityViewModel<State>> extends AppCompatActivity {

    protected ComponentProvider componentProvider;
    protected ViewModel viewModel;

    @Inject
    ActivityViewModelFactory viewModelFactory;

    @Override
    protected void onStart() {
        super.onStart();
        attachComponentProvider();

        inject();

        final ViewModelProvider viewModelProvider = new ViewModelProvider(this, viewModelFactory);

        viewModel = viewModelProvider.get(getViewModelClass());

        viewModel.stateObserver.observe(this, state -> renderViewState(state));
    }

    public void renderDefaultViewState() {
        // NO OP
    }

    public abstract void inject();

    public abstract Class<ViewModel> getViewModelClass();

    public abstract Class<ComponentProvider> getComponentProviderClass();

    public abstract void renderViewState(State state);

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
