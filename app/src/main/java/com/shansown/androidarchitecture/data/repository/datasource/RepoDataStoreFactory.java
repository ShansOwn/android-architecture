package com.shansown.androidarchitecture.data.repository.datasource;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

/**
 * Factory that creates different implementations of {@link RepoDataStore}.
 */
@Singleton
public final class RepoDataStoreFactory {

  @Inject Lazy<ServerRepoDataStore> serverRepoDataStoreLazy;

  @Inject public RepoDataStoreFactory() {
    Timber.v("RepoDataStoreFactory created: " + this);
  }

  /**
   * Get or create {@link RepoDataStore}
   *
   * @return RepoDataStore
   */
  public RepoDataStore get() {
    return serverRepoDataStoreLazy.get();
  }
}