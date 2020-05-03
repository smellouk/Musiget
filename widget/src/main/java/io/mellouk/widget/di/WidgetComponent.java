package io.mellouk.widget.di;

import dagger.Subcomponent;
import io.mellouk.common.base.BaseComponentProvider;
import io.mellouk.widget.PlayerWidget;

@WidgetScope
@Subcomponent(modules = {WidgetModule.class})
public interface WidgetComponent {
    void inject(final PlayerWidget playerWidget);

    interface ComponentProvider extends BaseComponentProvider {
        WidgetComponent getWidgetComponent();
    }
}
