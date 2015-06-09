package com.shansown.androidarchitecture.data.interactor;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.repository.RepoRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public final class GetRepositoriesUseCase extends UseCase {

  private final RepoRepository repoRepository;

  @Inject public GetRepositoriesUseCase(RepoRepository repoRepository) {
    this.repoRepository = repoRepository;
    Timber.v("GetRepositoriesUseCase created: " + this);
  }

  public Observable<List<RepositoryData>> execute(DateTime since, Sort sort, Order order) {
    return repoRepository.getRepositories(since, sort, order)
        .doOnError(loadingError)
        .doOnNext(loadingSuccess)
        .observeOn(Schedulers.computation());
  }

  @Override protected Observable buildUseCaseObservable() {
    return null;
  }

  private final Action1<Throwable> loadingError =
      throwable -> Timber.e(throwable, "Error on loading repositories");

  private final Action1<List<RepositoryData>> loadingSuccess = repositories -> Timber.d(
      "Publishing " + repositories.size() + " repositories from the GetRepositoriesUseCase");
}