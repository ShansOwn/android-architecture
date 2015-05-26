package com.shansown.androidarchitecture.di.module;

import com.shansown.androidarchitecture.ui.ActivityHierarchyServer;
import com.shansown.androidarchitecture.ui.AppContainer;
import com.shansown.androidarchitecture.ui.debug.DebugAppContainer;
import com.shansown.androidarchitecture.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class DebugUiModule {

  @Provides @Singleton
  AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
    return debugAppContainer;
  }

  @Provides @Singleton
  ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}