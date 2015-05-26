package com.shansown.androidarchitecture.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.di.component.ActivityComponent;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Base activity created to be extended by every activity in this application. This class provides
 * dependency injection configuration, ButterKnife Android library configuration and some methods
 * common to every activity.
 */
public abstract class BaseActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @Optional @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Optional @InjectView(R.id.navigation_view) NavigationView navigationView;

  @Inject AppContainer appContainer;

  private Toolbar actionbarToolbar;
  private ActionBarDrawerToggle drawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectDependencies();
    setupView();
    injectViews();
    initActionbarToolbar();
    setupNavDrawer();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return (drawerToggle != null && drawerToggle.onOptionsItemSelected(item));
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    item.setChecked(true);
    closeNavDrawer();
    switch (id) {
      case R.id.navigation_item_1:
        Timber.d("ITEM_1 drawer item selected");
        return true;
      case R.id.navigation_item_2:
        Timber.d("ITEM_2 drawer item selected");
        return true;
    }
    return false;
  }

  @Override public void onBackPressed() {
    if (isNavDrawerOpen()) {
      closeNavDrawer();
    } else {
      super.onBackPressed();
    }
  }

  /**
   * Get {@link Toolbar} that was set as actionbar with {@link #setSupportActionBar}
   *
   * @return {@link Toolbar}
   * @throws IllegalStateException if toolbar is not been initialized
   */
  protected Toolbar getActionbarToolbar() {
    if (actionbarToolbar == null)
      throw new IllegalStateException("Toolbar is not defined in your activity or fragment layout");
    return actionbarToolbar;
  }

  protected boolean isNavDrawerOpen() {
    return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
  }

  protected void closeNavDrawer() {
    if (drawerLayout != null) {
      drawerLayout.closeDrawer(GravityCompat.START);
    }
  }

  /**
   * Get activity layout resource id which will be inflated as activity view
   *
   * @return layout resource id
   */
  @LayoutRes protected abstract int getLayoutId();

  /**
   * Init new Dagger component with Activity scope needed to this Activity.
   *
   * @return newly initialized component with new dependencies to provide.
   */
  protected abstract ActivityComponent initComponent();

  /**
   * Get Dagger component with Activity scope needed to this Activity.
   *
   * @return component with new dependencies to provide.
   */
  protected abstract ActivityComponent getComponent();

  /**
   * Called when actionbar toolbar has been initialized
   *
   * @param toolbar Initialized toolbar as actionbar
   */
  protected void onActionbarToolbarInit(Toolbar toolbar) {
  }

  private void setupView() {
    LayoutInflater inflater = getLayoutInflater();
    ViewGroup container = appContainer.bind(this);
    inflater.inflate(getLayoutId(), container);
  }

  /**
   * Get Dagger {@link ActivityComponent} child and inject the
   * declared one in the activity if exist.
   */
  private void injectDependencies() {
    ActivityComponent component = initComponent();
    if (component != null) {
      component.inject(this);
    }
  }

  /**
   * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
   * value.
   */
  private void injectViews() {
    ButterKnife.inject(this);
  }

  private boolean initActionbarToolbar() {
    actionbarToolbar = ButterKnife.findById(this, R.id.activity_toolbar);
    if (actionbarToolbar == null) {
      actionbarToolbar = ButterKnife.findById(this, R.id.fragment_toolbar);
    }
    if (actionbarToolbar == null) return false;
    setSupportActionBar(actionbarToolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    onActionbarToolbarInit(actionbarToolbar);
    return true;
  }

  /**
   * Set up navigation drawer if it's defined on layout
   *
   * @return <code>true</code> if the navigation drawer was setup, <code>false</code> otherwise.
   */
  private boolean setupNavDrawer() {
    if (drawerLayout == null) return false;
    drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionbarToolbar, R.string.drawer_open,
        R.string.drawer_close) {
      public void onDrawerClosed(View v) {
        super.onDrawerClosed(v);
        invalidateOptionsMenu();
        syncState();
      }

      public void onDrawerOpened(View v) {
        super.onDrawerOpened(v);
        invalidateOptionsMenu();
        syncState();
      }
    };

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
      setStatusBarColor(getWindow());
    }

    drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.theme_primary_dark));
    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    drawerLayout.setDrawerListener(drawerToggle);
    navigationView.setNavigationItemSelectedListener(this);
    drawerToggle.syncState();
    return true;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static void setStatusBarColor(Window window) {
    window.setStatusBarColor(Color.TRANSPARENT);
  }
}