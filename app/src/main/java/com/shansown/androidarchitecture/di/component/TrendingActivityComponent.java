package com.shansown.androidarchitecture.di.component;

import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.annotation.PerActivity;
import com.shansown.androidarchitecture.di.module.TrendingActivityModule;
import com.shansown.androidarchitecture.ui.trending.TrendingActivity;
import com.shansown.androidarchitecture.ui.trending.TrendingFragment;
import com.shansown.androidarchitecture.ui.trending.TrendingViewModel;
import dagger.Component;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@PerActivity
@Component(dependencies = AppComponent.class,
    modules = {TrendingActivityModule.class})
public interface TrendingActivityComponent extends ActivityComponent {

  void inject(TrendingActivity trendingActivity);
  void inject(TrendingFragment trendingFragment);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class Initializer {
    public static TrendingActivityComponent init(TrendingActivity trendingActivity) {
      return DaggerTrendingActivityComponent.builder()
          .appComponent(Injector.obtain(trendingActivity.getApplicationContext(), AppComponent.class))
          .trendingActivityModule(new TrendingActivityModule(trendingActivity))
          .build();
    }
  }
}