package com.shansown.androidarchitecture.ui.trending;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.interactor.GetRepositoriesUseCase;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * ViewModel created to represent a Trending from the presentation point of view.
 */
public final class TrendingViewModel {

  private final GetRepositoriesUseCase getRepositoriesUseCase;

  private final BehaviorSubject<List<RepositoryData>> repositoriesSubject =
      BehaviorSubject.create();
  private final BehaviorSubject<Boolean> refreshViewVisibilitySubject = BehaviorSubject.create();
  private final BehaviorSubject<Boolean> loadViewVisibilitySubject = BehaviorSubject.create();

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
    showLoading();
    loadRepositories();
  }

  public void onRefresh() {
    Timber.d("On refresh");
    state = State.REFRESHING;
    showRefreshing();
    loadRepositories();
  }

  public Observable<List<RepositoryData>> getRepositories() {
    return repositoriesSubject;
  }

  public Observable<Boolean> getRefreshViewVisibility() {
    return refreshViewVisibilitySubject;
  }

  public BehaviorSubject<Boolean> getLoadViewVisibility() {
    return loadViewVisibilitySubject;
  }

  public void onRepositoryClicked(RepositoryData repository) {
    Timber.d("On repository clicked: " + repository);
  }

  private void loadRepositories() {
    getRepositoriesUseCase.execute(since, sort, order)
        .subscribe(this::onRepositoriesLoaded, this::onRepositoriesFailed);
  }

  private void showRefreshing() {
    refreshViewVisibilitySubject.onNext(true);
  }

  private void hideRefreshing() {
    refreshViewVisibilitySubject.onNext(false);
  }

  private void showLoading() {
    loadViewVisibilitySubject.onNext(true);
  }

  private void hideLoading() {
    loadViewVisibilitySubject.onNext(false);
  }

  private void hideRefreshLoad(State state) {
    switch (state) {
      case REFRESHING:
        hideRefreshing();
        break;
      case LOADING:
        hideLoading();
        break;
      default: throw new IllegalStateException("View Model is in illegal state: " + state);
    }
  }

  private void onRepositoriesLoaded(List<RepositoryData> repositories) {
    Timber.d("Publishing " + repositories.size() + " repositories from the ViewModel");
    hideRefreshLoad(state);
    state = State.ON_CONTENT;
    repositoriesSubject.onNext(repositories);
  }

  private void onRepositoriesFailed(Throwable e) {
    Timber.e(e, "On repositories failed!");
    hideRefreshLoad(state);
    state = State.ON_ERROR;
  }

  private enum State {
    LOADING, REFRESHING, ON_ERROR, ON_CONTENT
  }
}