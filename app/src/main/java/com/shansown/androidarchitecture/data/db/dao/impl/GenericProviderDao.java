package com.shansown.androidarchitecture.data.db.dao.impl;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import com.shansown.androidarchitecture.data.db.dao.Batch;
import com.shansown.androidarchitecture.data.db.provider.AAProvider;
import com.shansown.androidarchitecture.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import timber.log.Timber;

public abstract class GenericProviderDao<E, U> {

  protected final ContentResolver contentResolver;
  private final ConcurrentMap<Uri, Subject<E, E>> querySingleSubjectMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<Uri, Subject<List<E>, List<E>>> querySubjectMap =
      new ConcurrentHashMap<>();
  private final ConcurrentMap<Uri, Subject<Cursor, Cursor>> queryCursorSubjectMap =
      new ConcurrentHashMap<>();

  public GenericProviderDao(ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  public ProviderBatch getBatch() {
    return new ProviderBatch();
  }

  public Observable<E> get(U id) {
    Timber.v("Get by id: %s", id);
    Uri uri = buildUriForId(id);
    final Subject<E, E> subject = getQuerySingleSubject(uri);
    return subject.startWith(Observable.defer(() -> Observable.just(querySingle(uri))));
  }

  public Observable<List<E>> getAll() {
    return get(getContentUri(), null, null, null, null);
  }

  protected Observable<List<E>> get(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    Timber.v("Get for %s", uri);
    final Subject<List<E>, List<E>> subject = getQuerySubject(uri);
    return subject.startWith(Observable.defer(() -> //
        Observable.just(query(uri, projection, selection, selectionArgs, sortOrder))));
  }

  protected Observable<Cursor> getCursor(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    Timber.v("Get for %s", uri);
    final Subject<Cursor, Cursor> subject = getQueryCursorSubject(uri);
    return subject.startWith(Observable.defer(() -> //
        Observable.just(queryCursor(uri, projection, selection, selectionArgs, sortOrder))));
  }

  public Observable<Uri> insert(E entity) {
    Uri uri = getContentUri();
    Timber.v("Insert to %s", uri);
    ContentValues values = toContentValues(entity);
    Timber.v("Values: %s", values);
    return Observable.defer(() -> Observable.just(insert(uri, values)));
  }

  public Observable<Integer> insert(List<E> entities) {
    Timber.v("Batch insert");
    ArrayList<ContentProviderOperation> batch = new ArrayList<>();
    Uri uri = getContentUri();
    for (E entity : entities) {
      Timber.v("Insert to %s", uri);
      ContentValues values = toContentValues(entity);
      Timber.v("Values: %s", values);
      batch.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
    }
    return Observable.defer(() -> {
      ContentProviderResult[] results = null;
      try {
        results = applyBatch(batch);
      } catch (RemoteException | OperationApplicationException e) {
        Timber.e(e, "Insert entities failed");
        //TODO
      }
      return Observable.just(results.length);
    });
  }

  public Observable<Boolean> update(E entity) {
    Uri uri = getContentUri();
    Timber.v("Update to %s", uri);
    ContentValues values = toContentValues(entity);
    Timber.v("Values: %s", values);
    return Observable.defer(() -> Observable.just(update(uri, values, null, null) > 0));
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
      return Observable.just(results.length);
    });
  }

  /*protected void insertOrUpdate(E entity) {
    Uri uri = buildUriForId(getIdFromEntity(entity));
    Timber.v("insertOrUpdate to %s", uri);
    ContentValues values = toContentValues(entity);
    Timber.v("values(%s)", values);
    if (contentResolver.update(uri, values, null, null) == 0) {
      final Uri resultUri = contentResolver.insert(uri, values);
      Timber.v("Inserted at %s", resultUri);
    } else {
      Timber.v("Updated at %s", uri);
    }
  }*/

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

  protected E parseEntityCursor(Cursor cursor) {
    E result = null;
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        result = fromCursor(cursor);
      }
      cursor.close();
    }
    return result;
  }

  protected List<E> parseEntityListCursor(Cursor cursor) {
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

  protected <J> Pair<E, J> parseJoinCursor(Cursor cursor, Func1<Cursor, J> cursorToJoinEntity) {
    Pair<E, J> result = null;
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        result = new Pair<>(fromCursor(cursor), cursorToJoinEntity.call(cursor));
      }
      cursor.close();
    }
    return result;
  }

  protected <J> List<Pair<E, J>> parseJoinListCursor(Cursor cursor,
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

  private Subject<E, E> getQuerySingleSubject(Uri uri) {
    Timber.v("Get query single subject for %s", uri);
    querySingleSubjectMap.putIfAbsent(uri, PublishSubject.<E>create());
    return querySingleSubjectMap.get(uri);
  }

  private Subject<List<E>, List<E>> getQuerySubject(Uri uri) {
    Timber.v("Get query subject for %s", uri);
    querySubjectMap.putIfAbsent(uri, PublishSubject.<List<E>>create());
    return querySubjectMap.get(uri);
  }

  private Subject<Cursor, Cursor> getQueryCursorSubject(Uri uri) {
    Timber.v("Get query subject for %s", uri);
    queryCursorSubjectMap.putIfAbsent(uri, PublishSubject.<Cursor>create());
    return queryCursorSubjectMap.get(uri);
  }

  private E querySingle(Uri uri) {
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    E result = parseEntityCursor(cursor);
    if (result == null) {
      Timber.v("Could not find by uri: %s", uri);
    }
    return result;
  }

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

  protected class ProviderBatch implements Batch<E> {
    private ArrayList<ContentProviderOperation> ops = new ArrayList<>();

    public Batch insert(E entity) {
      ops.add(ContentProviderOperation.newInsert(getContentUri())
          .withValues(toContentValues(entity))
          .build());
      return this;
    }

    @SuppressWarnings("unchecked") public Batch merge(Batch that) {
      if (that.getClass() != ProviderBatch.class) {
        throw new IllegalArgumentException("Batch should be ProviderBatch");
      }
      ops.addAll(((ProviderBatch) that).ops);
      return this;
    }

    public Observable<ContentProviderResult[]> apply() {
      return Observable.defer(() -> {
        ContentProviderResult[] results = null;
        try {
          results = applyBatch(ops);
          Timber.v("Apply batch result: " + Arrays.toString(results));
        } catch (RemoteException | OperationApplicationException e) {
          Timber.e(e, "Apply batch failed");
          //TODO
        }
        return Observable.just(results);
      });
    }
  }

  protected abstract Uri buildUriForId(U id);

  protected abstract U getIdFromEntity(E entity);

  protected abstract E fromCursor(Cursor cursor);

  protected abstract ContentValues toContentValues(E entity);

  protected abstract Uri getContentUri();
}