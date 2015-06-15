package com.shansown.androidarchitecture.data.mapper;

import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.data.api.dto.RepositoriesResponse;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import com.shansown.androidarchitecture.data.db.entity.RepositoryEntity;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.ui.model.Repository;
import com.shansown.androidarchitecture.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class RepositoryMapper {

  private final UserMapper userMapper;

  @Inject public RepositoryMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @NonNull public List<RepositoryData> toRepoDataList(RepositoriesResponse repositoriesResponse) {
    return repositoriesResponse.getItems() == null //
        ? Collections.<RepositoryData>emptyList() //
        : repositoriesResponse.getItems();
  }

  @NonNull public List<RepositoryEntity> toRepoEntityListFromRepoList(List<Repository> repos) {
    List<RepositoryEntity> result = new ArrayList<>(repos.size());
    for (Repository repo : repos) {
      result.add(toRepoEntity(repo));
    }
    return result;
  }

  @NonNull public List<RepositoryEntity> toRepoEntityListFromDataList(
      List<RepositoryData> repositoryDataList) {
    List<RepositoryEntity> result = new ArrayList<>(repositoryDataList.size());
    for (RepositoryData data : repositoryDataList) {
      result.add(toRepoEntity(data));
    }
    return result;
  }

  @NonNull public List<Repository> toRepoList(List<RepositoryData> repositoryDataList) {
    List<Repository> result = new ArrayList<>(repositoryDataList.size());
    for (RepositoryData data : repositoryDataList) {
      result.add(toRepository(data));
    }
    return result;
  }

  @NonNull public List<Repository> toRepoList(List<RepositoryEntity> repoEntities,
      List<UserEntity> userEntities) {
    int count = repoEntities.size();
    List<Repository> result = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      result.add(toRepository(repoEntities.get(i), userEntities.get(i)));
    }
    return result;
  }

  @NonNull
  public List<Repository> toRepoListFromPairList(List<Pair<RepositoryEntity, UserEntity>> join) {
    List<Repository> result = new ArrayList<>(join.size());
    for (Pair<RepositoryEntity, UserEntity> pair : join) {
      result.add(toRepository(pair));
    }
    return result;
  }

  public RepositoryEntity toRepoEntity(Repository repo) {
    return new RepositoryEntity.Builder() //
        .id(repo.getId())
        .name(repo.getName())
        .owner(repo.getOwner().getId())
        .forks(repo.getForks())
        .stars(repo.getStars())
        .htmlUrl(repo.getHtmlUrl())
        .updatedAt(repo.getUpdatedAt())
        .description(repo.getDescription())
        .build();
  }

  public RepositoryEntity toRepoEntity(RepositoryData data) {
    return new RepositoryEntity.Builder() //
        .id(data.getId())
        .name(data.getName())
        .owner(data.getOwner().getId())
        .forks(data.getForks())
        .stars(data.getStars())
        .htmlUrl(data.getHtmlUrl())
        .updatedAt(data.getUpdatedAt())
        .description(data.getDescription())
        .build();
  }

  public Repository toRepository(RepositoryData data) {
    return new Repository.Builder() //
        .id(data.getId())
        .name(data.getName())
        .owner(userMapper.toUser(data.getOwner()))
        .forks(data.getForks())
        .stars(data.getStars())
        .htmlUrl(data.getHtmlUrl())
        .updatedAt(data.getUpdatedAt())
        .description(data.getDescription())
        .build();
  }

  public Repository toRepository(RepositoryEntity repoEntity, UserEntity userEntity) {
    return new Repository.Builder() //
        .id(repoEntity.getId())
        .name(repoEntity.getName())
        .owner(userMapper.toUser(userEntity))
        .forks(repoEntity.getForks())
        .stars(repoEntity.getStars())
        .htmlUrl(repoEntity.getHtmlUrl())
        .updatedAt(repoEntity.getUpdatedAt())
        .description(repoEntity.getDescription())
        .build();
  }

  public Repository toRepository(Pair<RepositoryEntity, UserEntity> pair) {
    RepositoryEntity repoEntity = pair.getLeft();
    UserEntity userEntity = pair.getRight();
    return new Repository.Builder() //
        .id(repoEntity.getId())
        .name(repoEntity.getName())
        .owner(userMapper.toUser(userEntity))
        .forks(repoEntity.getForks())
        .stars(repoEntity.getStars())
        .htmlUrl(repoEntity.getHtmlUrl())
        .updatedAt(repoEntity.getUpdatedAt())
        .description(repoEntity.getDescription())
        .build();
  }
}