package io.mellouk.musiget.di;

import androidx.annotation.NonNull;

import dagger.Component;
import io.mellouk.musiget.App;

@ApplicationScope
@Component(modules = {AppModule.class, ReposModule.class})
public interface AppComponent {
    void inject(@NonNull final App app);
}
