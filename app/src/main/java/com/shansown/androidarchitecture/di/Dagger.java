package com.shansown.androidarchitecture.di;

import android.content.SharedPreferences;
import com.shansown.androidarchitecture.App;
import com.shansown.androidarchitecture.di.annotation.PerActivity;
import com.shansown.androidarchitecture.di.annotation.PerUser;
import com.shansown.androidarchitecture.di.component.AppComponent;
import com.shansown.androidarchitecture.util.Preconditions;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Singleton for holding and retrieving Dagger components with long lifetime cycle
 * such as {@link PerUser} or {@link Singleton}
 *
 * A short lifetime components such as {@link PerActivity} should be instantiated
 * by appropriate component initializers
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dagger implements SharedPreferences.OnSharedPreferenceChangeListener {

  private static final Dagger INSTANCE = new Dagger();

  @Singleton
  private AppComponent mAppComponent;

  public static Dagger initDagger(App app) {
    Dagger dagger = getInstance();
    if (dagger.mAppComponent != null) {
      throw new IllegalStateException("Dagger has already been initialized");
    }
    dagger.mAppComponent = AppComponent.Initializer.init(app);
    return dagger;
  }

  @Singleton
  public static AppComponent appComponent() {
    Dagger dagger = getInstance();
    if (dagger.mAppComponent == null) {
      throw new IllegalStateException("Dagger has not been initialized");
    }
    return getInstance().mAppComponent;
  }

  private static Dagger getInstance() {
    return INSTANCE;
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // TODO
  }

  static class ComponentHolder<K, C> {

    @Setter @Getter
    private boolean outdated = true;

    private K k;
    private C c;

    public C set(K key, C component) {
      Preconditions.checkNotNull(key, "key == null");
      Preconditions.checkNotNull(component, "component == null");
      k = key;
      C oldComponent = c;
      c = component;
      outdated = false;
      return oldComponent;
    }

    public C get() {
      return c;
    }

    public boolean containsKey(Object key) {
      return k != null && k.equals(key);
    }

    public boolean containsComponent(Object component) {
      return c != null && c.equals(component);
    }

    public boolean isEmpty() {
      return k == null || c == null;
    }
  }
}