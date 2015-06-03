package com.shansown.androidarchitecture;

import android.app.Application;
import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.di.Injector;
import com.shansown.androidarchitecture.di.component.AppComponent;
import com.shansown.androidarchitecture.ui.ActivityHierarchyServer;
import com.shansown.androidarchitecture.util.PrettyLogger;
import com.shansown.androidarchitecture.util.LumberYard;
import com.squareup.leakcanary.LeakCanary;
import javax.inject.Inject;
import net.danlew.android.joda.JodaTimeAndroid;
import timber.log.Timber;

public final class App extends Application {

  private AppComponent component;

  @Inject ActivityHierarchyServer activityHierarchyServer;
  @Inject LumberYard lumberYard;

  @Override public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new PrettyLogger());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }

    component = initComponent();
    component.inject(this);

    lumberYard.cleanUp();
    Timber.plant(lumberYard.tree());

    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  private AppComponent initComponent() {
    return AppComponent.Initializer.init(this);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name, AppComponent.class)) {
      return component;
    }
    return super.getSystemService(name);
  }
}