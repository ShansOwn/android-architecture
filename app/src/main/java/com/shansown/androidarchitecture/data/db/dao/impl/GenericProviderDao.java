package com.shansown.androidarchitecture.data.db.dao.impl;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.data.db.dao.BaseDao;
import com.shansown.androidarchitecture.data.db.dao.Batch;
import com.shansown.androidarchitecture.data.db.provider.AAProvider;
import com.shansown.androidarchitecture.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public abstract class GenericProviderDao<E, U> implements BaseDao<E> {

  protected final ContentResolver contentResolver;

  public GenericProviderDao(ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  public Batch<E> getBatch() {
    return new ProviderBatch();
  }

  public Observable<E> get(U id) {
    Timber.v("Get by id: %s", id);
    Uri uri = buildUriForId(id);
    return Observable.defer(() -> { //
      E entity = querySingle(uri);
      return (entity != null) //
          ? Observable.just(entity) //
          : Observable.<E>empty();
    }).subscribeOn(Schedulers.io());
  }

  public Observable<List<E>> getAll() {
    return get(getContentUri(), null, null, null, null);
  }

  public Observable<Uri> insert(E entity) {

    return Observable.defer(() -> {
      Uri uri = getContentUri();
      Timber.v("Insert to %s", uri);
      ContentValues values = toContentValues(entity);
      Timber.v("Values: %s", values);
      return Observable.just(insert(uri, values));
    }).subscribeOn(Schedulers.io());
  }

  public Observable<Integer> insert(List<E> entities) {
    Timber.v("Batch insert");
    return Observable.defer(() -> {
      ArrayList<ContentProviderOperation> batch = new ArrayList<>();
      Uri uri = getContentUri();
      for (E entity : entities) {
        Timber.v("Insert to %s", uri);
        ContentValues values = toContentValues(entity);
        Timber.v("Values: %s", values);
        batch.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
      }
      ContentProviderResult[] results = null;
      try {
        results = applyBatch(batch);
      } catch (RemoteException | OperationApplicationException e) {
        Timber.e(e, "Insert entities failed");
        //TODO
      }
      return (results != null) ? Observable.just(results.length) : Observable.<Integer>empty();
    }).subscribeOn(Schedulers.io());
  }

  public Observable<Boolean> update(E entity) {
    Uri uri = getContentUri();
    Timber.v("Update to %s", uri);
    ContentValues values = toContentValues(entity);
    Timber.v("Values: %s", values);
    return Observable.defer(() -> Observable.just(update(uri, values, null, null) > 0))
        .subscribeOn(Schedulers.io());
  }

  public Observable<Integer> update(List<E> entities) {
    Timber.v("Batch update");
    ArrayList<ContentProviderOperation> batch = new ArrayList<>();
    Uri uri = getContentUri();
    for (E entity : entities) {
      Timber.v("Update to %s", uri);
      ContentValues values = toContentValues(entity);
      Timber.v("Values: %s", values);
      batch.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
    }
    return Observable.defer(() -> {
      ContentProviderResult[] results = null;
      try {
        results = applyBatch(batch);
      } catch (RemoteException | OperationApplicationException e) {
        Timber.e(e, "Update entities failed");
        //TODO
      }
      return (results != null) ? Observable.just(results.length) : Observable.<Integer>empty();
    }).subscribeOn(Schedulers.io());
  }

  protected Observable<List<E>> get(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    Timber.v("Get for %s", uri);
    return Observable.defer(() -> { //
      List<E> result = query(uri, projection, selection, selectionArgs, sortOrder);
      return (!result.isEmpty()) //
          ? Observable.just(result) //
          : Observable.<List<E>>empty();
    }).subscribeOn(Schedulers.io());
  }

  protected <J> Observable<List<Pair<E, J>>> getJoin(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder, Func1<Cursor, J> cursorToJoinEntity) {
    return getCursor(uri, projection, selection, selectionArgs, sortOrder) //
        .map(c -> parseJoinListCursor(c, cursorToJoinEntity));
  }

  protected ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> batch)
      throws RemoteException, OperationApplicationException {
    return contentResolver.applyBatch(AAProvider.CONTENT_AUTHORITY, batch);
  }

  protected String generateMultipleSelection(String column, int count) {
    if (count <= 0) return null; // select all
    StringBuilder selection = new StringBuilder(column + " IN (");
    for (int i = 0; i < count; i++) {
      selection.append("?");
      if (i < count - 1) {
        selection.append(", ");
      }
    }
    selection.append(")");
    return selection.toString();
  }

  private Observable<Cursor> getCursor(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    Timber.v("Get for %s", uri);
    return Observable.defer(() -> { //
      Cursor cursor = queryCursor(uri, projection, selection, selectionArgs, sortOrder);
      return (cursor != null && cursor.getCount() > 0) //
          ? Observable.just(cursor) //
          : Observable.<Cursor>empty();
    }).subscribeOn(Schedulers.io());
  }

  private E querySingle(Uri uri) {
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    E result = parseEntityCursor(cursor);
    if (result == null) {
      Timber.v("Could not find by uri: %s", uri);
    }
    return result;
  }

  @NonNull
  private List<E> query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    List<E> result = parseEntityListCursor(cursor);
    if (result.isEmpty()) {
      Timber.v("Could not find by uri: %s", uri);
    }
    return result;
  }

  private Cursor queryCursor(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
  }

  private Uri insert(Uri uri, ContentValues values) {
    return contentResolver.insert(uri, values);
  }

  private int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return contentResolver.update(uri, values, selection, selectionArgs);
  }

  private <J> Pair<E, J> parseJoinCursor(Cursor cursor, Func1<Cursor, J> cursorToJoinEntity) {
    Pair<E, J> result = null;
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        result = new Pair<>(fromCursor(cursor), cursorToJoinEntity.call(cursor));
      }
      cursor.close();
    }
    return result;
  }

  @NonNull private <J> List<Pair<E, J>> parseJoinListCursor(Cursor cursor,
      Func1<Cursor, J> cursorToJoinEntity) {
    List<Pair<E, J>> result = new ArrayList<>();
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        do {
          result.add(new Pair<>(fromCursor(cursor), cursorToJoinEntity.call(cursor)));
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return result;
  }

  private E parseEntityCursor(Cursor cursor) {
    E result = null;
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        result = fromCursor(cursor);
      }
      cursor.close();
    }
    return result;
  }

  @NonNull private List<E> parseEntityListCursor(Cursor cursor) {
    List<E> result = new ArrayList<>();
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        do {
          result.add(fromCursor(cursor));
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return result;
  }

  protected class ProviderBatch implements Batch<E> {
    private ArrayList<ContentProviderOperation> ops = new ArrayList<>();

    public Batch<E> insert(E entity) {
      ops.add(ContentProviderOperation.newInsert(getContentUri())
          .withValues(toContentValues(entity))
          .build());
      return this;
    }

    public Batch<E> deleteAll() {
      ops.add(ContentProviderOperation.newDelete(getContentUri()).build());
      return this;
    }

    @SuppressWarnings("unchecked") public Batch<E> merge(Batch that) {
      if (that.getClass() != ProviderBatch.class) {
        throw new IllegalArgumentException("Batch should be ProviderBatch");
      }
      ops.addAll(((ProviderBatch) that).ops);
      return this;
    }

    public Observable<ContentProviderResult[]> apply() {
      return Observable.<ContentProviderResult[]>create(s -> {
        ContentProviderResult[] results = null;
        try {
          results = applyBatch(ops);
          s.onNext(results);
          s.onCompleted();
        } catch (Exception e) {
          s.onError(e);
        }
      }).subscribeOn(Schedulers.io());
    }
  }

  protected abstract Uri buildUriForId(U id);

  protected abstract U getIdFromEntity(E entity);

  protected abstract E fromCursor(Cursor cursor);

  protected abstract ContentValues toContentValues(E entity);

  protected abstract Uri getContentUri();
}