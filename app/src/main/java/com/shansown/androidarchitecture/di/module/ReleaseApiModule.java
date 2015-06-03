package com.shansown.androidarchitecture.di.module;

import com.google.gson.Gson;
import com.shansown.androidarchitecture.data.api.GithubService;
import com.shansown.androidarchitecture.di.annotation.Api;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public final class ReleaseApiModule {
  @Provides @Singleton
  Endpoint provideEndpoint() {
    return Endpoints.newFixedEndpoint(ApiModule.PRODUCTION_API_URL);
  }

  @Provides @Singleton @Api OkHttpClient provideApiClient(OkHttpClient client) {
    return client.clone();
  }

  @Provides @Singleton RestAdapter provideRestAdapter(Endpoint endpoint,
      @Api OkHttpClient client, Gson gson) {
    return new RestAdapter.Builder() //
        .setClient(new OkClient(client)) //
        .setEndpoint(endpoint) //
        .setConverter(new GsonConverter(gson)) //
        .build();
  }

  @Provides @Singleton GithubService provideGithubService(RestAdapter restAdapter) {
    return restAdapter.create(GithubService.class);
  }
}