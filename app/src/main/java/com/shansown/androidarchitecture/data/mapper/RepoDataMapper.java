package com.shansown.androidarchitecture.data.mapper;

import com.shansown.androidarchitecture.data.api.dto.RepositoriesResponse;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepoDataMapper {

  @Inject
  public RepoDataMapper() {
  }

  public List<RepositoryData> transform(RepositoriesResponse repositoriesResponse) {
    return repositoriesResponse.getItems() == null //
        ? Collections.<RepositoryData>emptyList() //
        : repositoriesResponse.getItems();
  }
}