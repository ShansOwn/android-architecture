package com.shansown.androidarchitecture.data.api.dto;

import java.util.List;
import lombok.Getter;

@Getter
public final class RepositoriesResponse {
  private final List<RepositoryData> items;

  public RepositoriesResponse(List<RepositoryData> items) {
    this.items = items;
  }
}
