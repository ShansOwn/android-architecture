package com.shansown.androidarchitecture.di.module;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import com.shansown.androidarchitecture.ui.AppContainer;
import com.shansown.androidarchitecture.ui.TelescopeAppContainer;
import com.shansown.androidarchitecture.ui.ActivityHierarchyServer;

@Module
public final class InternalReleaseUiModule {

  @Provides @Singleton
  AppContainer provideAppContainer(
      TelescopeAppContainer telescopeAppContainer) {
    return telescopeAppContainer;
  }

  @Provides @Singleton
  ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}