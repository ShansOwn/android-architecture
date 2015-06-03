package com.shansown.androidarchitecture.di.module;

import android.app.Application;
import android.content.SharedPreferences;
import com.shansown.androidarchitecture.di.annotation.PixelGridEnabled;
import com.shansown.androidarchitecture.di.annotation.PixelRatioEnabled;
import com.shansown.androidarchitecture.di.annotation.ScalpelEnabled;
import com.shansown.androidarchitecture.di.annotation.ScalpelWireframeEnabled;
import com.shansown.androidarchitecture.data.prefs.RxSharedPreferences;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.Observable;
import timber.log.Timber;

@Module(includes = DebugApiModule.class)
public final class DebugDataModule {

  private static final boolean DEFAULT_PICASSO_DEBUGGING = true; // Debug indicators displayed
  private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
  private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
  private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
  private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by default.

  @Provides @Singleton
  RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
    return RxSharedPreferences.create(preferences);
  }

  @Provides @Singleton @PixelGridEnabled
  Observable<Boolean> provideObservablePixelGridEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
  }

  @Provides @Singleton @PixelRatioEnabled
  Observable<Boolean> provideObservablePixelRatioEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
  }

  @Provides @Singleton @ScalpelEnabled
  Observable<Boolean> provideObservableScalpelEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
  }

  @Provides @Singleton @ScalpelWireframeEnabled
  Observable<Boolean> provideObservableScalpelWireframeEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_wireframe_drawer",
        DEFAULT_SCALPEL_WIREFRAME_ENABLED);
  }

  @Provides @Singleton
  OkHttpClient provideOkHttpClient(Application app) {
    return DataModule.createOkHttpClient(app);
  }

  @Provides @Singleton
  Picasso providePicasso(Application app, OkHttpClient client) {
    Picasso instance = new Picasso.Builder(app).downloader(new OkHttpDownloader(client))
        .listener((picasso, uri, e) -> Timber.e(e, "Failed to load image: %s", uri))
        .build();
    instance.setIndicatorsEnabled(DEFAULT_PICASSO_DEBUGGING);
    return instance;
  }
}