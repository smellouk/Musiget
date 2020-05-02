package io.mellouk.view.di;

import dagger.Subcomponent;
import io.mellouk.common.base.BaseComponentProvider;
import io.mellouk.view.MainActivity;

@MainScope
@Subcomponent(modules = {MainModule.class})
public interface MainComponent {
    void inject(final MainActivity mainActivity);

    public interface ComponentProvider extends BaseComponentProvider {
        MainComponent getMainComponent();
    }
}
