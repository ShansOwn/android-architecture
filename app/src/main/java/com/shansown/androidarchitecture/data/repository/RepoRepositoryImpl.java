package com.shansown.androidarchitecture.data.repository;

import com.shansown.androidarchitecture.data.api.GithubService;
import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.SearchQuery;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.db.dao.Batch;
import com.shansown.androidarchitecture.data.db.dao.RepositoryDao;
import com.shansown.androidarchitecture.data.db.dao.UserDao;
import com.shansown.androidarchitecture.data.db.entity.RepositoryEntity;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.data.mapper.RepositoryMapper;
import com.shansown.androidarchitecture.data.mapper.UserMapper;
import com.shansown.androidarchitecture.ui.model.Repository;
import com.shansown.androidarchitecture.util.Pair;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import rx.Observable;
import timber.log.Timber;

@Singleton public final class RepoRepositoryImpl implements RepoRepository {

  private final GithubService githubService;
  private final RepositoryMapper repoMapper;
  private final UserMapper userMapper;

  private final RepositoryDao repoDao;
  private final UserDao userDao;

  @Inject public RepoRepositoryImpl(GithubService githubService, RepositoryMapper repoMapper,
      UserMapper userMapper, RepositoryDao repoDao, UserDao userDao) {
    this.githubService = githubService;
    this.repoMapper = repoMapper;
    this.userMapper = userMapper;
    this.repoDao = repoDao;
    this.userDao = userDao;
    Timber.v("RepoRepositoryImpl created: " + this);
  }

  public Observable<List<Repository>> getForce(DateTime since, Sort sort, Order order) {
    return getFromServer(since, sort, order);
  }

  public Observable<List<Repository>> get(DateTime since, Sort sort, Order order) {
    return getFromDb(since, sort, order);
  }

  private Observable<List<Repository>> getFromServer(DateTime since, Sort sort, Order order) {
    Batch<RepositoryEntity> repoBatch = repoDao.getBatch();
    Batch<UserEntity> userBatch = userDao.getBatch();
    return githubService //
        .repositories(new SearchQuery.Builder().createdSince(since).build(), sort, order) //
        .map(repoMapper::toRepoDataList) //
        .map(repoMapper::toRepoList) //
        .flatMap(Observable::from) //
        .doOnNext(r -> {
          RepositoryEntity entity = repoMapper.toRepoEntity(r);
          Timber.v("Add to batch: Insert repo entity: " + entity);
          repoBatch.insert(entity);
        })
        .doOnNext(r -> {
          UserEntity entity = userMapper.toUserEntity(r.getOwner());
          Timber.v("Add to batch: Insert user entity: " + entity);
          userBatch.insert(entity);
        })
        .doOnCompleted(() -> {
          Timber.v("Apply batch");
          repoBatch.merge(userBatch).apply().subscribe();
        })
        .toList();
  }

  private Observable<List<Repository>> getFromDb(DateTime since, Sort sort, Order order) {
    //Observable<List<RepositoryEntity>> repos = repoDao.get(since, sort, order);
    //Observable<List<UserEntity>> users = repos.flatMap(r -> userDao.get(gatherUserIds(r)));

    //Observable<List<Pair<RepositoryEntity, UserEntity>>>
    return repoDao.getJoinOwners(since, sort, order).map(repoMapper::toRepoListFromPairList);

    //return Observable.zip(repos, users, repoMapper::toRepoList);
  }

  private List<String> gatherUserIds(List<RepositoryEntity> repos) {
    List<String> userIds = new ArrayList<>(repos.size());
    for (RepositoryEntity repo : repos) {
      userIds.add(repo.getOwnerId());
    }
    return userIds;
  }
}