package io.mellouk.core.di;

import dagger.Subcomponent;
import io.mellouk.common.base.BaseComponentProvider;
import io.mellouk.core.MusicService;

@CoreScope
@Subcomponent(modules = CoreModule.class)
public interface CoreComponent {
    void inject(final MusicService musicService);

    interface ComponentProvider extends BaseComponentProvider {
        CoreComponent getCoreComponent();
    }
}
