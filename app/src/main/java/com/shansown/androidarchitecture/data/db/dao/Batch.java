package com.shansown.androidarchitecture.data.db.dao;

import android.content.ContentProviderResult;
import rx.Observable;

public interface Batch<E> {
  Batch<E> insert(E entity);

  Batch<E> deleteAll();

  Batch<E> merge(Batch that);

  Observable<ContentProviderResult[]> apply();
}