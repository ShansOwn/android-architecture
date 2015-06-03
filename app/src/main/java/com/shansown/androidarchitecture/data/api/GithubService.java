package com.shansown.androidarchitecture.data.api;

import com.shansown.androidarchitecture.data.api.dto.RepositoriesResponse;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GithubService {
  @GET("/search/repositories") //
  Observable<RepositoriesResponse> repositories( //
      @Query("q") SearchQuery query, //
      @Query("sort") Sort sort, //
      @Query("order") Order order);
}