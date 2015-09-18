package com.shansown.androidarchitecture.ui.trending;

import com.shansown.androidarchitecture.ui.BaseVM;
import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.interactor.GetRepositoriesUseCase;
import com.shansown.androidarchitecture.ui.model.Repository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * ViewModel created to represent a Trending from the presentation point of view.
 */
public final class TrendingVM extends BaseVM {

  private final GetRepositoriesUseCase getRepositoriesUseCase;

  private final PublishSubject<List<Repository>> showRepositories =
      PublishSubject.create();
  private final PublishSubject<Boolean> refreshViewVisibility = PublishSubject.create();
  private final PublishSubject<Boolean> loadViewVisibility = PublishSubject.create();

  private State state = State.LOADING;

  private Sort sort = Sort.STARS;
  private Order order = Order.DESC;
  private DateTime since = TrendingTimespan.WEEK.createdSince();

  private List<Repository> repositories = new ArrayList<>();

  @Inject public TrendingVM(GetRepositoriesUseCase getRepositoriesUseCase) {
    this.getRepositoriesUseCase = getRepositoriesUseCase;
    Timber.v("TrendingViewModel created: " + this);
  }

  @Override public void init() {
    super.init();
    load();
  }

  public void load() {
    Timber.d("Load");
    state = State.LOADING;
    showLoading();
    loadRepositories(false);
  }

  public void onRefresh() {
    Timber.d("On refresh");
    state = State.REFRESHING;
    showRefreshing();
    loadRepositories(true);
  }

  public Observable<List<Repository>> getShowRepositories() {
    return showRepositories.asObservable();
  }

  public Observable<Boolean> getRefreshViewVisibility() {
    return refreshViewVisibility.asObservable();
  }

  public Observable<Boolean> getLoadViewVisibility() {
    return loadViewVisibility.asObservable();
  }

  public void onRepositoryClicked(Repository repository) {
    Timber.d("On repository clicked: " + repository);
  }

  private void loadRepositories(boolean force) {
    getRepositoriesUseCase.execute(since, sort, order, force)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onRepositoriesLoaded, this::onRepositoriesFailed);
  }

  private void onRepositoriesLoaded(List<Repository> repos) {
    Timber.d("Publishing " + repos.size() + " repositories from the ViewModel");
    hideRefreshLoad();
    addNewRepositories(repos);
    state = State.ON_CONTENT;
    showRepositories(repositories);
  }

  private void onRepositoriesFailed(Throwable e) {
    Timber.e(e, "On repositories failed!");
    hideRefreshLoad();
    state = State.ON_ERROR;
  }

  private void hideRefreshLoad() {
    Timber.d("Hide refresh/load");
    switch (state) {
      case REFRESHING:
        hideRefreshing();
        break;
      case LOADING:
        hideLoading();
        break;
      default: Timber.w("View Model is in illegal state: " + state);
    }
  }

  private void addNewRepositories(List<Repository> repos) {
    switch (state) {
      case REFRESHING:
        repositories = repos;
        break;
      case LOADING:
        repositories.addAll(repos);
        break;
      default: Timber.w("View Model is in illegal state: " + state);
    }
  }

  // --- View actions ---
  private void showRefreshing() {
    Timber.d("Show refreshing");
    viewAction(() -> refreshViewVisibility.onNext(true));
  }

  private void hideRefreshing() {
    Timber.d("Hide refreshing");
    viewAction(() -> refreshViewVisibility.onNext(false));
  }

  private void showLoading() {
    Timber.d("Show loading");
    viewAction(() -> loadViewVisibility.onNext(true));
  }

  private void hideLoading() {
    Timber.d("Hide loading");
    viewAction(() -> loadViewVisibility.onNext(false));
  }

  private void showRepositories(List<Repository> repositories) {
    Timber.d("Show repositories");
    viewAction(() -> showRepositories.onNext(repositories));
  }

  private enum State {
    LOADING, REFRESHING, ON_ERROR, ON_CONTENT
  }
}