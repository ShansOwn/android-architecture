package com.shansown.androidarchitecture.data.repository.datasource;

import com.shansown.androidarchitecture.data.api.GithubService;
import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.SearchQuery;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoriesResponse;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.mapper.RepoDataMapper;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

@Singleton
public class ServerRepoDataStore implements RepoDataStore {

  private final GithubService githubService;
  private final RepoDataMapper repoDataMapper;

  @Inject public ServerRepoDataStore(GithubService githubService, RepoDataMapper repoDataMapper) {
    this.githubService = githubService;
    this.repoDataMapper = repoDataMapper;
    Timber.v("ServerRepoDataStore created: " + this);
  }

  @Override
  public Observable<List<RepositoryData>> getRepositories(DateTime since, Sort sort, Order order) {
    return githubService.repositories(new SearchQuery.Builder().createdSince(since).build(), sort,
        order).flatMap(repositoriesResponseToRepositoriesList);
  }

  private final Func1<RepositoriesResponse, Observable<List<RepositoryData>>>
      repositoriesResponseToRepositoriesList =
      new Func1<RepositoriesResponse, Observable<List<RepositoryData>>>() {
        @Override public Observable<List<RepositoryData>> call(RepositoriesResponse response) {
          return Observable.just(repoDataMapper.transform(response));
        }
      };
}