package com.shansown.androidarchitecture.di.module;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import com.shansown.androidarchitecture.ui.AppContainer;
import com.shansown.androidarchitecture.ui.ActivityHierarchyServer;

@Module
public final class ProductionUiModule {

  @Provides @Singleton
  AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @Singleton
  ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}