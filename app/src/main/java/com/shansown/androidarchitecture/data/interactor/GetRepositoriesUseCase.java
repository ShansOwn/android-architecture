package com.shansown.androidarchitecture.data.interactor;

import com.shansown.androidarchitecture.data.api.GithubService;
import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.SearchQuery;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoriesResponse;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class GetRepositoriesUseCase extends UseCase {

  private final GithubService githubService;

  @Inject public GetRepositoriesUseCase(GithubService githubService) {
    this.githubService = githubService;
  }

  public Observable<List<RepositoryData>> execute(SearchQuery query, Sort sort, Order order) {
    return githubService.repositories(query, sort, order)
        .flatMap(repositoriesResponseToRepositoriesList)
        .doOnError(loadingError)
        .doOnNext(loadingSuccess)
        .observeOn(Schedulers.computation());
  }

  @Override protected Observable buildUseCaseObservable() {
    return null;
  }

  private final Func1<RepositoriesResponse, Observable<List<RepositoryData>>>
      repositoriesResponseToRepositoriesList =
      repositoriesResponse -> repositoriesResponse.getItems() == null //
          ? Observable.just(Collections.<RepositoryData>emptyList()) //
          : Observable.just(repositoriesResponse.getItems());

  private final Action1<Throwable> loadingError =
      throwable -> Timber.e(throwable, "Error on loading repositories");

  private final Action1<List<RepositoryData>> loadingSuccess = repositories -> Timber.d(
      "Publishing " + repositories.size() + " repositories from the GetRepositoriesUseCase");
}