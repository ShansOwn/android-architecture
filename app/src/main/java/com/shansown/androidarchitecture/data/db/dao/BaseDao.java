package com.shansown.androidarchitecture.data.db.dao;

import java.util.List;
import rx.Observable;

public interface BaseDao<E> {
  Observable<Integer> insert(List<E> entities);
  Observable<Integer> update(List<E> entities);
  Batch<E> getBatch();
}