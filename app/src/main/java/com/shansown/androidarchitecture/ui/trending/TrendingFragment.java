package com.shansown.androidarchitecture.ui.trending;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.component.TrendingActivityComponent;
import com.shansown.androidarchitecture.ui.BaseFragment;
import com.shansown.androidarchitecture.ui.misc.DividerItemDecoration;
import com.shansown.androidarchitecture.ui.renderer.repository.RepositoryCollection;
import com.shansown.androidarchitecture.ui.renderer.repository.RepositoryRendererAdapterFactory;
import com.shansown.androidarchitecture.util.RxBinderUtil;
import java.util.List;
import javax.inject.Inject;
import rx.Observer;
import timber.log.Timber;

import static com.shansown.androidarchitecture.ui.misc.DividerItemDecoration.VERTICAL_LIST;

public final class TrendingFragment extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener {

  @InjectView(R.id.trending_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
  @InjectView(R.id.trending_list) RecyclerView trendingList;

  @Inject TrendingViewModel viewModel;
  @Inject RepositoryRendererAdapterFactory repositoryRendererAdapterFactory;

  private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);

  private Toolbar toolbar;
  private RVRendererAdapter<RepositoryData> adapter;
  private RepositoryCollection repositoryCollection = new RepositoryCollection();

  @Override protected void onInjectDependencies() {
    super.onInjectDependencies();
    Injector.obtain(getActivity(), TrendingActivityComponent.class).inject(this);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initViews(view);
  }

  @Override public void onResume() {
    super.onResume();
    bindViewModel();
    onRefresh();
  }

  @Override public void onPause() {
    super.onPause();
    unbindViewModel();
  }

  @Override protected int getLayoutId() {
    return R.layout.trending_fragment;
  }

  @Override protected int getMenuId() {
    return R.menu.menu_trending;
  }

  @Override protected void onToolbarInit(Toolbar toolbar) {
    super.onToolbarInit(toolbar);
    this.toolbar = toolbar;
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_settings:
        Timber.d("Settings action selected");
        return true;
    }
    return super.onMenuItemClick(item);
  }

  @Override public void onRefresh() {
    showLoading();
    viewModel.onRefresh();
  }

  private void initViews(View rootView) {
    float dividerPaddingStart =
        getResources().getDimensionPixelSize(R.dimen.content_left_margin_from_screen_edge);

    trendingList.setLayoutManager(new LinearLayoutManager(getActivity()));
    trendingList.addItemDecoration(
        new DividerItemDecoration(getActivity(), VERTICAL_LIST, dividerPaddingStart,
            isRtl(rootView)));

    adapter = repositoryRendererAdapterFactory.getRendererAdapter(repositoryCollection);
    trendingList.setAdapter(adapter);

    swipeRefreshView.setColorSchemeResources(R.color.theme_accent);
    swipeRefreshView.setOnRefreshListener(this);
  }

  private void bindViewModel() {
    rxBinderUtil.bindObserver(viewModel.getRepositories(), repositoriesObserver);
  }

  private void unbindViewModel() {
    rxBinderUtil.clear();
  }

  private void showLoading() {
    swipeRefreshView.setRefreshing(true);
  }

  private void hideLoading() {
    swipeRefreshView.setRefreshing(false);
  }

  private void onRepositoriesLoaded(List<RepositoryData> repositories) {
    Timber.d("On repositories loaded: " + repositories);
    repositoryCollection.clear();
    repositoryCollection.addAll(repositories);
    adapter.notifyDataSetChanged();
    hideLoading();
  }

  private void onRepositoriesFailed(Throwable e) {
    Timber.e(e, "On repositories failed");
    hideLoading();
  }

  private boolean isRtl(@NonNull View view) {
    return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
  }

  private final Observer<List<RepositoryData>> repositoriesObserver =
      new Observer<List<RepositoryData>>() {
        @Override public void onCompleted() {
        }

        @Override public void onError(Throwable e) {
          onRepositoriesFailed(e);
        }

        @Override public void onNext(List<RepositoryData> repositories) {
          onRepositoriesLoaded(repositories);
        }
      };
}