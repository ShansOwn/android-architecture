package com.shansown.androidarchitecture.ui.trending;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.interactor.GetRepositoriesUseCase;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import rx.Observable;
import rx.Observer;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * ViewModel created to represent a Trending from the presentation point of view.
 */
public final class TrendingViewModel {

  private final GetRepositoriesUseCase getRepositoriesUseCase;

  private final BehaviorSubject<List<RepositoryData>> repositoriesSubject =
      BehaviorSubject.create();

  private State state = State.LOADING;

  private Sort sort = Sort.STARS;
  private Order order = Order.DESC;
  private DateTime since = TrendingTimespan.WEEK.createdSince();

  @Inject public TrendingViewModel(GetRepositoriesUseCase getRepositoriesUseCase) {
    this.getRepositoriesUseCase = getRepositoriesUseCase;
    Timber.v("TrendingViewModel created: " + this);
  }

  public void onLoad() {
    Timber.d("On load");
    state = State.LOADING;
    loadRepositories();
  }

  public void onRefresh() {
    Timber.d("On refresh");
    state = State.REFRESHING;
    loadRepositories();
  }

  public Observable<List<RepositoryData>> getRepositories() {
    return repositoriesSubject;
  }

  public void onRepositoryClicked(RepositoryData repository) {
    Timber.d("On repository clicked: " + repository);
  }

  private void loadRepositories() {
    getRepositoriesUseCase.execute(since, sort, order)
        .subscribe(repositoriesObserver);
  }

  private final Observer<List<RepositoryData>> repositoriesObserver =
      new Observer<List<RepositoryData>>() {
        @Override public void onCompleted() {
        }

        @Override public void onError(Throwable e) {
          state = State.ON_ERROR;
          repositoriesSubject.onError(e);
        }

        @Override public void onNext(List<RepositoryData> repositories) {
          Timber.d("Publishing " + repositories.size() + " repositories from the ViewModel");
          state = State.ON_CONTENT;
          repositoriesSubject.onNext(repositories);
        }
      };

  private enum State {
    LOADING, REFRESHING, ON_ERROR, ON_CONTENT
  }
}