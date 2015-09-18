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
import com.shansown.androidarchitecture.ui.BaseVM;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.ui.model.Repository;
import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.component.TrendingActivityComponent;
import com.shansown.androidarchitecture.ui.BaseFragment;
import com.shansown.androidarchitecture.ui.misc.BetterViewAnimator;
import com.shansown.androidarchitecture.ui.misc.DividerItemDecoration;
import com.shansown.androidarchitecture.ui.renderer.repository.RepositoryCollection;
import com.shansown.androidarchitecture.ui.renderer.repository.RepositoryRendererAdapterFactory;
import com.shansown.androidarchitecture.util.RxBinderUtil;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

import static com.shansown.androidarchitecture.ui.misc.DividerItemDecoration.VERTICAL_LIST;

public final class TrendingFragment extends BaseFragment {

  @InjectView(R.id.trending_animator) BetterViewAnimator animatorView;
  @InjectView(R.id.trending_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
  @InjectView(R.id.trending_list) RecyclerView trendingList;

  @Inject TrendingVM viewModel;
  @Inject RepositoryRendererAdapterFactory repositoryRendererAdapterFactory;

  private Toolbar toolbar;
  private RVRendererAdapter<Repository> adapter;
  private RepositoryCollection repositoryCollection = new RepositoryCollection();

  @Override protected void onInjectDependencies() {
    super.onInjectDependencies();
    Injector.obtain(getActivity(), TrendingActivityComponent.class).inject(this);
  }

  @Override protected void bindViewModel(RxBinderUtil rxBinderUtil) {
    rxBinderUtil.bindProperty(viewModel.getRefreshViewVisibility(), this::showRefreshing);
    rxBinderUtil.bindProperty(viewModel.getLoadViewVisibility(), this::showLoading);
    rxBinderUtil.bindProperty(viewModel.getShowRepositories(), this::showRepositories);
  }

  @Override protected BaseVM getViewModel() {
    return viewModel;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initViews(view);
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
    swipeRefreshView.setOnRefreshListener(viewModel::onRefresh);
  }

  private void showContent() {
    if (!(animatorView.getDisplayedChildId() == R.id.trending_swipe_refresh)) {
      animatorView.setDisplayedChildId(R.id.trending_swipe_refresh);
    }
  }

  private void showRefreshing(Boolean visible) {
    Timber.d("Show refreshing: " + visible);
    swipeRefreshView.setRefreshing(visible);
  }

  private void showLoading(Boolean visible) {
    Timber.d("Show loading: " + visible);
    if (visible) {
      if (!(animatorView.getDisplayedChildId() == R.id.trending_loading)) {
        animatorView.setDisplayedChildId(R.id.trending_loading);
      }
    }
  }

  private void showRepositories(List<Repository> repositories) {
    Timber.d("On repositories show: " + repositories);
    repositoryCollection.clear();
    repositoryCollection.addAll(repositories);
    adapter.notifyDataSetChanged();
    showContent();
  }

  private boolean isRtl(@NonNull View view) {
    return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
  }
}