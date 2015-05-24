package com.shansown.androidarchitecture;

import android.app.Application;
import com.shansown.androidarchitecture.di.Dagger;
import com.squareup.leakcanary.LeakCanary;
import net.danlew.android.joda.JodaTimeAndroid;
import timber.log.Timber;

public class App extends Application {

  private static App sInstance;

  // Hold reference for no GC-ing
  Dagger mDagger;

  public static App getInstance() {
    return sInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
    mDagger = Dagger.initDagger(this);
    Dagger.appComponent().inject(this);

    JodaTimeAndroid.init(this);
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }
  }
}