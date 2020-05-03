package io.mellouk.common.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import io.mellouk.common.utils.factory.ActivityViewModelFactory;

public abstract class BaseActivity<ComponentProvider extends BaseComponentProvider,
        State extends BaseViewState,
        ViewModel extends BaseActivityViewModel<State>> extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 123;
    protected ComponentProvider componentProvider;
    protected ViewModel viewModel;
    @Inject
    ActivityViewModelFactory viewModelFactory;

    @Inject
    SharedPreferences sharedPreferences;

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

    public void createToast(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    public boolean isPermissionGranted(final String permission) {
        final boolean isPermissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        sharedPreferences.edit().putBoolean(permission, isPermissionGranted).apply();
        return isPermissionGranted;
    }

    public void requestPermission(final String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showRequestPermissionRationale();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    public abstract void inject();

    public abstract Class<ViewModel> getViewModelClass();

    public abstract Class<ComponentProvider> getComponentProviderClass();

    public abstract void renderViewState(State state);

    public abstract void showRequestPermissionRationale();

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
