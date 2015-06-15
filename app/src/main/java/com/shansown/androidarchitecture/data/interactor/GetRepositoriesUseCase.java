package com.shansown.androidarchitecture.data.interactor;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.ui.model.Repository;
import com.shansown.androidarchitecture.data.repository.RepoRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton public final class GetRepositoriesUseCase {

  private final RepoRepository repoRepository;

  @Inject public GetRepositoriesUseCase(RepoRepository repoRepository) {
    this.repoRepository = repoRepository;
    Timber.v("GetRepositoriesUseCase created: " + this);
  }

  public Observable<List<Repository>> execute(DateTime since, Sort sort, Order order,
      boolean force) {
    return getRepos(since, sort, order, force) //
        .doOnError(loadingError) //
        .doOnNext(loadingSuccess) //
        .subscribeOn(Schedulers.computation());
  }

  private Observable<List<Repository>> getRepos(DateTime since, Sort sort, Order order,
      boolean force) {
    if (force) {
      return repoRepository.getForce(since, sort, order);
    } else {
      return repoRepository.get(since, sort, order);
    }
  }

  private final Action1<Throwable> loadingError =
      throwable -> Timber.e(throwable, "Error on loading repositories");

  private final Action1<List<Repository>> loadingSuccess = repositories -> Timber.d(
      "Publishing " + repositories.size() + " repositories from the GetRepositoriesUseCase");
}