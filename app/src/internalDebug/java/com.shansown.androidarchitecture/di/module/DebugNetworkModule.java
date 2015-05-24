package com.shansown.androidarchitecture.di.module;

import android.app.Application;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import timber.log.Timber;

@Module
public final class DebugNetworkModule {

  @Provides
  @Singleton Picasso providePicasso(Application app, OkHttpClient client) {
    Picasso instance = new Picasso.Builder(app)
        .downloader(new OkHttpDownloader(client))
        .listener((picasso, uri, e) -> Timber.e(e, "Failed to load image: %s", uri))
        .build();
    instance.setIndicatorsEnabled(true);
    return instance;
  }
}