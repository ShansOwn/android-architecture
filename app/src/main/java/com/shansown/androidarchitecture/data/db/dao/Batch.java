package com.shansown.androidarchitecture.data.db.dao;

import android.content.ContentProviderResult;
import rx.Observable;

public interface Batch<E> {
  Batch insert(E entity);

  Batch merge(Batch that);

  Observable<ContentProviderResult[]> apply();
}