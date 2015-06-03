package com.shansown.androidarchitecture.ui.trending;

import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.component.TrendingActivityComponent;
import com.shansown.androidarchitecture.ui.BaseActivity;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public final class TrendingActivity extends BaseActivity {

  private TrendingActivityComponent component;
  @Inject Picasso picasso;

  @Override protected int getLayoutId() {
    return R.layout.trending_activity;
  }

  @Override protected void onInjectDependencies() {
    super.onInjectDependencies();
    initComponent().inject(this);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name, TrendingActivityComponent.class)) {
      return component;
    }
    return super.getSystemService(name);
  }

  private TrendingActivityComponent initComponent() {
    component = TrendingActivityComponent.Initializer.init(this);
    return component;
  }
}