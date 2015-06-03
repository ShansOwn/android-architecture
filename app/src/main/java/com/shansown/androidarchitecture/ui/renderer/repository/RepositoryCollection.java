package com.shansown.androidarchitecture.ui.renderer.repository;

import com.pedrogomez.renderers.AdapteeCollection;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class RepositoryCollection implements AdapteeCollection<RepositoryData> {

  private final List<RepositoryData> repositories;

  public RepositoryCollection() {
    this.repositories = new LinkedList<>();
  }

  @Override public int size() {
    return repositories.size();
  }

  @Override public RepositoryData get(int i) {
    return repositories.get(i);
  }

  @Override public boolean add(RepositoryData repositoryData) {
    return repositories.add(repositoryData);
  }

  @Override public boolean remove(Object o) {
    return (o instanceof RepositoryData) && repositories.remove(o);
  }

  @Override public boolean addAll(Collection<? extends RepositoryData> collection) {
    return repositories.addAll(collection);
  }

  @Override public boolean removeAll(Collection<?> collection) {
    return repositories.removeAll(collection);
  }

  @Override public void clear() {
    repositories.clear();
  }
}