package com.shansown.androidarchitecture.data.repository;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.repository.datasource.RepoDataStoreFactory;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import rx.Observable;
import timber.log.Timber;

public final class RepoRepositoryImpl implements RepoRepository {

  private final RepoDataStoreFactory repoDataStoreFactory;

  @Inject
  public RepoRepositoryImpl(RepoDataStoreFactory repoDataStoreFactory) {
    this.repoDataStoreFactory = repoDataStoreFactory;
    Timber.v("RepoRepositoryImpl created: " + this);
  }

  @Override
  public Observable<List<RepositoryData>> getRepositories(DateTime since, Sort sort, Order order) {
    return repoDataStoreFactory.get().getRepositories(since, sort, order);
  }
}