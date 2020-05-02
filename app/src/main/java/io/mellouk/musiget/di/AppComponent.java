package io.mellouk.musiget.di;

import dagger.Component;
import io.mellouk.core.di.CoreComponent;
import io.mellouk.view.di.MainComponent;

@ApplicationScope
@Component(modules = {AppModule.class, ReposModule.class})
public interface AppComponent {
    CoreComponent getCoreComponent();

    MainComponent getMainComponent();
}
