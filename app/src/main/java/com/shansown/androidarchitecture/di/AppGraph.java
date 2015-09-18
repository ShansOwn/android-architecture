package com.shansown.androidarchitecture.di;

import android.app.Application;
import android.content.Context;
import com.shansown.androidarchitecture.App;
import com.shansown.androidarchitecture.data.Clock;
import com.shansown.androidarchitecture.data.interactor.GetRepositoriesUseCase;
import com.shansown.androidarchitecture.ui.ActivityHierarchyServer;
import com.shansown.androidarchitecture.ui.AppContainer;
import com.shansown.androidarchitecture.ui.renderer.repository.RepositoryRendererAdapterFactory;
import com.shansown.androidarchitecture.util.LumberYard;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

public interface AppGraph {

  // Field injections of any dependencies of the App
  void inject(App application);

  // ---Exported for child-components---
  // App module
  Application application();
  Context appContext();

  // UI module
  AppContainer appContainer();
  ActivityHierarchyServer activityHierarchyServer();
  LumberYard lumberYard();

  RepositoryRendererAdapterFactory repositoryRendererAdapterFactory();

  // Data module
  OkHttpClient okHttpClient();
  Picasso picasso();
  GetRepositoriesUseCase getRepositoriesUseCase();
  Clock clock();
}