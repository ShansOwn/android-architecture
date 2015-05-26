package com.shansown.androidarchitecture.di.module;

import android.app.Application;
import android.content.Context;
import com.shansown.androidarchitecture.App;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;

/**
 * Module that provides objects which will live during the application lifecycle.
 */
@Module(includes = {DataModule.class})
@AllArgsConstructor
public final class AppModule {

  private final App application;

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  Context provideAppContext() {
    return application;
  }
}