package com.shansown.androidarchitecture.di.module;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;

@Module @AllArgsConstructor
public class ActivityModule {
  private final Activity activity;

  @Provides @Singleton Activity provideContext() {
    return activity;
  }
}