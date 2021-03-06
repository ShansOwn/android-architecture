package com.shansown.androidarchitecture.di.module;

import android.app.Application;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shansown.androidarchitecture.data.Clock;
import com.shansown.androidarchitecture.data.api.DateTimeConverter;
import com.shansown.androidarchitecture.data.db.dao.RepositoryDao;
import com.shansown.androidarchitecture.data.db.dao.UserDao;
import com.shansown.androidarchitecture.data.db.dao.impl.RepositoryProviderDao;
import com.shansown.androidarchitecture.data.db.dao.impl.UserProviderDao;
import com.shansown.androidarchitecture.data.repository.RepoRepository;
import com.shansown.androidarchitecture.data.repository.RepoRepositoryImpl;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import org.joda.time.DateTime;

import static android.content.Context.MODE_PRIVATE;
import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Module(includes = ApiModule.class)
public final class DataModule {

  static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  @Provides @Singleton
  SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences("AA", MODE_PRIVATE);
  }

  @Provides @Singleton Gson provideGson(DateTimeConverter dateTimeConverter) {
    return new GsonBuilder()
        .registerTypeAdapter(DateTime.class, dateTimeConverter)
        .create();
  }

  @Provides @Singleton RepoRepository provideRepoRepository(RepoRepositoryImpl repoRepository) {
    return repoRepository;
  }

  @Provides @Singleton RepositoryDao provideRepositoryDao(RepositoryProviderDao repositoryDao) {
    return repositoryDao;
  }

  @Provides @Singleton UserDao provideUserDao(UserProviderDao userDao) {
    return userDao;
  }

  @Provides @Singleton Clock provideClock() {
    return Clock.REAL;
  }

  static OkHttpClient createOkHttpClient(Application app) {
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(10, SECONDS);
    client.setReadTimeout(10, SECONDS);
    client.setWriteTimeout(10, SECONDS);

    // Install an HTTP cache in the application cache directory.
    File cacheDir = new File(app.getCacheDir(), "http");
    Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
    client.setCache(cache);

    return client;
  }
}