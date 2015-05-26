package com.shansown.androidarchitecture.di.component;

import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.annotation.PerActivity;
import com.shansown.androidarchitecture.di.module.MainActivityModule;
import com.shansown.androidarchitecture.ui.main.MainActivity;
import dagger.Component;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@PerActivity
@Component(dependencies = AppComponent.class,
    modules = {MainActivityModule.class})
public interface MainActivityComponent extends ActivityComponent {

  void inject(MainActivity mainActivity);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class Initializer {
    public static MainActivityComponent init(MainActivity mainActivity) {
      return DaggerMainActivityComponent.builder()
          .appComponent(Injector.obtain(mainActivity.getApplicationContext(), AppComponent.class))
          .mainActivityModule(new MainActivityModule())
          .build();
    }
  }
}
