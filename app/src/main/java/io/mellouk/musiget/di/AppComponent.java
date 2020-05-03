package io.mellouk.musiget.di;

import dagger.Component;
import io.mellouk.core.di.CoreComponent;
import io.mellouk.musiget.App;
import io.mellouk.view.di.MainComponent;
import io.mellouk.widget.di.WidgetComponent;

@ApplicationScope
@Component(modules = {AppModule.class, ReposModule.class})
public interface AppComponent {
    void inject(App app);

    CoreComponent getCoreComponent();

    MainComponent getMainComponent();

    WidgetComponent getWidgetComponent();
}
