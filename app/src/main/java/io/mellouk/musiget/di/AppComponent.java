package io.mellouk.musiget.di;

import dagger.Component;
import io.mellouk.core.di.CoreComponent;

@ApplicationScope
@Component(modules = {AppModule.class, ReposModule.class})
public interface AppComponent {
    CoreComponent getCoreComponent();
}
