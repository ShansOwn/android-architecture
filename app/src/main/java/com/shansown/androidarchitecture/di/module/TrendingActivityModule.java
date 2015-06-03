package com.shansown.androidarchitecture.di.module;

import com.shansown.androidarchitecture.di.annotation.PerActivity;
import com.shansown.androidarchitecture.ui.trending.TrendingActivity;
import dagger.Module;
import dagger.Provides;

@Module
public final class TrendingActivityModule extends ActivityModule {
  private final TrendingActivity trendingActivity;

  public TrendingActivityModule(TrendingActivity trendingActivity) {
    super(trendingActivity);
    this.trendingActivity = trendingActivity;
  }

  @Provides @PerActivity TrendingActivity provideTrendingActivity() {
    return trendingActivity;
  }
}