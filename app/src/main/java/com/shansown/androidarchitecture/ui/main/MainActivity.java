package com.shansown.androidarchitecture.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.component.ActivityComponent;
import com.shansown.androidarchitecture.di.component.MainActivityComponent;
import com.shansown.androidarchitecture.ui.BaseActivity;

public final class MainActivity extends BaseActivity {

  private MainActivityComponent component;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override protected int getLayoutId() {
    return R.layout.main_activity;
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name, MainActivityComponent.class)) {
      return component;
    }
    return super.getSystemService(name);
  }

  @Override protected ActivityComponent initComponent() {
    component = MainActivityComponent.Initializer.init(this);
    return component;
  }

  @Override protected ActivityComponent getComponent() {
    return component;
  }
}