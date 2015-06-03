package com.shansown.androidarchitecture.ui.trending;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.SearchQuery;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.interactor.GetRepositoriesUseCase;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * ViewModel created to represent a Trending from the presentation point of view.
 */
public final class TrendingViewModel {

  private final GetRepositoriesUseCase getRepositoriesUseCase;

  private final BehaviorSubject<List<RepositoryData>> repositoriesSubject =
      BehaviorSubject.create();

  private Sort sort = Sort.STARS;
  private Order order = Order.DESC;
  private SearchQuery query =
      new SearchQuery.Builder().createdSince(TrendingTimespan.WEEK.createdSince()).build();

  @Inject public TrendingViewModel(GetRepositoriesUseCase getRepositoriesUseCase) {
    this.getRepositoriesUseCase = getRepositoriesUseCase;
    Timber.v("TrendingViewModel created: " + this);
  }

  public void onRefresh() {
    Timber.d("On refresh");
    getRepositoriesUseCase.execute(query, sort, order)
        .subscribe(repositoriesObserver);
  }

  public Observable<List<RepositoryData>> getRepositories() {
    return repositoriesSubject;
  }

  public void onRepositoryClicked(RepositoryData repository) {
    Timber.d("On repository clicked: " + repository);
  }

  private final Observer<List<RepositoryData>> repositoriesObserver =
      new Observer<List<RepositoryData>>() {
        @Override public void onCompleted() {
        }

        @Override public void onError(Throwable e) {
          repositoriesSubject.onError(e);
        }

        @Override public void onNext(List<RepositoryData> repositories) {
          Timber.d("Publishing " + repositories.size() + " repositories from the ViewModel");
          repositoriesSubject.onNext(repositories);
        }
      };
}