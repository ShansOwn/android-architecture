package com.shansown.androidarchitecture.di;

import android.app.Application;
import com.shansown.androidarchitecture.App;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

public interface AppGraph {

  // Field injections of any dependencies of the App
  void inject(App application);

  // ---Exported for child-components---
  // App module
  Application application();

  // Network module
  OkHttpClient okHttpClient();
  Picasso picasso();
}