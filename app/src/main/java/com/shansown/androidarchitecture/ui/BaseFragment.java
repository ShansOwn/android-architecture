package com.shansown.androidarchitecture.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.di.component.ActivityComponent;
import lombok.Getter;
import timber.log.Timber;

/**
 * Base fragment created to be extended by every fragment in this application. This class provides
 * dependency injection configuration, ButterKnife Android library configuration and some methods
 * common to every fragment.
 */
public abstract class BaseFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

  @Optional @InjectView(R.id.toolbar_fragment) Toolbar toolbar;

  @Getter
  private boolean wasPaused;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    onInjectDependencies();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(getLayoutId(), container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    injectViews(view);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initToolbar();
  }

  @Override public void onPause() {
    super.onPause();
    wasPaused = true;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(getMenuId(), menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return onMenuItemClick(item) || super.onOptionsItemSelected(item);
  }

  /**
   * This method will be invoked when a menu item is clicked if the item itself did
   * not already handle the event.
   *
   * Every fragment should override this method if it needs to proceed menu items clicks
   *
   * @param item {@link MenuItem} that was clicked
   * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
   */
  @Override public boolean onMenuItemClick(MenuItem item) {
    return false;
  }

  /**
   * Get fragment layout resource id which will be inflated as fragment view
   *
   * @return layout resource id
   */
  @LayoutRes protected abstract int getLayoutId();

  /**
   * Get fragment menu resource id which will be inflated as fragment toolbar menu.
   * If your fragment has no menu you should return -1
   *
   * @return menu resource id or -1 if fragment has no menu
   */
  @MenuRes protected int getMenuId() {
    return -1;
  }

  /**
   * Best place to getForce Dagger Activity scope component
   * and inject the declared one in the fragment if exist.
   */
  protected void onInjectDependencies() {
  }

  /**
   * Called when fragment toolbar has been initialized
   *
   * @param toolbar Initialized toolbar
   */
  protected void onToolbarInit(Toolbar toolbar) {
  }

  /**
   * Get fragment {@link Toolbar}
   *
   * @return {@link Toolbar}
   * @throws IllegalStateException if toolbar is not been initialized
   */
  protected Toolbar getToolbar() {
    if (toolbar == null) {
      throw new IllegalStateException(
          "Toolbar is not defined in your layout or not yet initialized");
    }
    return toolbar;
  }

  private boolean hasActivityOwnToolbar() {
    return ButterKnife.findById(getActivity(), R.id.toolbar_activity) != null;
  }

  private boolean initToolbar() {
    if (toolbar == null) return false;
    int menuResId = getMenuId();
    if (menuResId > 0) {
      if (!hasActivityOwnToolbar()) {
        setHasOptionsMenu(true);
      } else {
        toolbar.inflateMenu(menuResId);
        toolbar.setOnMenuItemClickListener(this);
      }
    }
    onToolbarInit(toolbar);
    return true;
  }

  /**
   * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
   * value.
   *
   * @param view to extract each widget injected in the fragment.
   */
  private void injectViews(final View view) {
    ButterKnife.inject(this, view);
  }
}