package io.mellouk.repository.di;

import javax.inject.Singleton;

import dagger.Component;
import io.mellouk.repository.OfflineRepositories;

@Singleton
@Component(modules = {RepositoryModule.class, FetcherModule.class})
public interface OfflineComponent {
    void inject(OfflineRepositories offlineRepositories);
}
