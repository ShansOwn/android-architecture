package com.shansown.androidarchitecture.di.component;

import com.shansown.androidarchitecture.App;
import com.shansown.androidarchitecture.di.AppGraph;
import com.shansown.androidarchitecture.di.module.AppModule;
import com.shansown.androidarchitecture.di.module.ProductionAppModule;
import dagger.Component;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {AppModule.class, ProductionAppModule.class})
public interface AppComponent extends AppGraph {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class Initializer {
    public static AppComponent init(App app) {
      return DaggerAppComponent.builder()
          .appModule(new AppModule(app))
          .productionAppModule(new ProductionAppModule())
          .build();
    }
  }
}