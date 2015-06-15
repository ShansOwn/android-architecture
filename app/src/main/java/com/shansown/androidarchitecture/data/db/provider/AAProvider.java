package com.shansown.androidarchitecture.data.db.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.shansown.androidarchitecture.BuildConfig;
import com.shansown.androidarchitecture.data.db.SelectionBuilder;
import com.shansown.androidarchitecture.data.db.table.RepositoryTable;
import com.shansown.androidarchitecture.data.db.table.UserTable;
import java.util.ArrayList;
import java.util.Arrays;
import timber.log.Timber;

import static com.shansown.androidarchitecture.data.db.table.BaseTable.JOIN;
import static com.shansown.androidarchitecture.data.db.table.BaseTable.PATH_NODE_ANY_STR;
import static com.shansown.androidarchitecture.data.db.table.BaseTable.QUERY_PARAMETER_LIMIT;

public final class AAProvider extends ContentProvider {

  public static final String CONTENT_AUTHORITY = "com.shansown.androidarchitecture";

  /**
   * URI ID for route: /{@link UserTable#TABLE_NAME}
   */
  private static final int USER = 100;
  /**
   * URI ID for route: /{@link UserTable#TABLE_NAME}/{ID}
   */
  private static final int USER_ID = 101;
  /**
   * URI ID for route: /{@link RepositoryTable#TABLE_NAME}
   */
  private static final int REPOSITORY = 200;
  /**
   * URI ID for route: /{@link RepositoryTable#TABLE_NAME}/{ID}
   */
  private static final int REPOSITORY_ID = 201;

  /**
   * URI ID for route: /{@link RepositoryTable#TABLE_NAME}/join/{@link UserTable#TABLE_NAME}
   */
  private static final int REPOSITORY_JOIN_USER = 202;

  private static final UriMatcher sUriMatcher = buildUriMatcher();

  private AADatabase dbHelper;

  /**
   * Build and return a {@link UriMatcher} that catches all {@link Uri}
   * variations supported by this {@link ContentProvider}.
   */
  private static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    matcher.addURI(CONTENT_AUTHORITY, UserTable.TABLE_NAME, USER);
    matcher.addURI(CONTENT_AUTHORITY, UserTable.TABLE_NAME + PATH_NODE_ANY_STR, USER_ID);

    matcher.addURI(CONTENT_AUTHORITY, RepositoryTable.TABLE_NAME, REPOSITORY);
    matcher.addURI(CONTENT_AUTHORITY, RepositoryTable.TABLE_NAME + PATH_NODE_ANY_STR,
        REPOSITORY_ID);
    matcher.addURI(CONTENT_AUTHORITY, RepositoryTable.TABLE_NAME + JOIN + UserTable.TABLE_NAME,
        REPOSITORY_JOIN_USER);

    return matcher;
  }

  @Override public boolean onCreate() {
    dbHelper = new AADatabase(getContext());
    return true;
  }

  /** {@inheritDoc} */
  @Override public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    String limit = uri.getQueryParameter(QUERY_PARAMETER_LIMIT);
    // avoid the expensive string concatenation below if not loggable
    if (BuildConfig.DEBUG) {
      Timber.v("Query: uri=" + uri + ", proj=" + Arrays.toString(projection) +
          ", selection=" + selection + ", args=" + Arrays.toString(selectionArgs) +
          ", limit=" + limit);
    }
    final SQLiteDatabase db = dbHelper.getReadableDatabase();
    final SelectionBuilder builder = buildExpendedSelection(uri);
    return builder //
        .where(selection, selectionArgs) //
        .query(db, false, projection, sortOrder, limit);
  }

  @Override public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case USER:
        return UserTable.CONTENT_TYPE;
      case USER_ID:
        return UserTable.CONTENT_ITEM_TYPE;
      case REPOSITORY:
        return RepositoryTable.CONTENT_TYPE;
      case REPOSITORY_ID:
        return RepositoryTable.CONTENT_ITEM_TYPE;

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  /** {@inheritDoc} */
  @Override public Uri insert(Uri uri, ContentValues values) {
    // avoid the expensive string concatenation below if not loggable
    if (BuildConfig.DEBUG) {
      Timber.v("Insert: uri=" + uri + ", values=" + values.toString());
    }
    final SQLiteDatabase db = dbHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case USER:
        db.insert(UserTable.TABLE_NAME, null, values);
        notifyChange(uri);
        return UserTable.buildUri(values.getAsString(UserTable.COLUMN_ID));
      case REPOSITORY:
        db.insert(RepositoryTable.TABLE_NAME, null, values);
        notifyChange(uri);
        return RepositoryTable.buildUri(values.getAsString(RepositoryTable.COLUMN_ID));
      default:
        throw new UnsupportedOperationException("Unknown insert uri: " + uri);
    }
  }

  /** {@inheritDoc} */
  @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
    if (BuildConfig.DEBUG) {
      Timber.v("Delete: uri=" + uri + ", selection=" + selection +
          ", args=" + Arrays.toString(selectionArgs));
    }
    final SQLiteDatabase db = dbHelper.getWritableDatabase();
    final SelectionBuilder builder = buildSimpleSelection(uri);
    int count = builder.where(selection, selectionArgs).delete(db);
    notifyChange(uri);
    return count;
  }

  /** {@inheritDoc} */
  @Override public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
    if (BuildConfig.DEBUG) {
      Timber.v("Update: uri=" + uri + ", selection=" + selection + ", args=" + Arrays.toString(
          selectionArgs) + ")");
    }
    final SQLiteDatabase db = dbHelper.getWritableDatabase();
    final SelectionBuilder builder = buildSimpleSelection(uri);
    int count = builder.where(selection, selectionArgs).update(db, values);
    notifyChange(uri);
    return count;
  }

  /**
   * Apply the given set of {@link android.content.ContentProviderOperation}, executing inside
   * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
   * any single one fails.
   */
  @Override //
  public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
      throws OperationApplicationException {
    final SQLiteDatabase db = dbHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      final int numOperations = operations.size();
      final ContentProviderResult[] results = new ContentProviderResult[numOperations];
      for (int i = 0; i < numOperations; i++) {
        results[i] = operations.get(i).apply(this, results, i);
      }
      db.setTransactionSuccessful();
      return results;
    } finally {
      db.endTransaction();
    }
  }

  private void notifyChange(Uri uri) {
    getContext().getContentResolver().notifyChange(uri, null);
  }

  /**
   * Build a simple {@link SelectionBuilder} to match the requested
   * {@link Uri}. This is usually enough to support
   * {@link #update} and {@link #delete} operations.
   */
  private SelectionBuilder buildSimpleSelection(Uri uri) {
    final SelectionBuilder builder = new SelectionBuilder();
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case USER:
        return builder.table(UserTable.TABLE_NAME);
      case USER_ID:
        final String userId = UserTable.getId(uri);
        return builder.table(UserTable.TABLE_NAME) //
            .where(UserTable.COLUMN_ID + "=?", userId);
      case REPOSITORY:
        return builder.table(RepositoryTable.TABLE_NAME);
      case REPOSITORY_ID:
        final String repoId = RepositoryTable.getId(uri);
        return builder.table(RepositoryTable.TABLE_NAME) //
            .where(UserTable.COLUMN_ID + "=?", repoId);
      default:
        throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
    }
  }

  /**
   * Build an advanced {@link SelectionBuilder} to match the requested
   * {@link Uri}. This is usually only used by {@link #query}, since it
   * performs table joins useful for {@link Cursor} data.
   */
  private SelectionBuilder buildExpendedSelection(Uri uri) {
    final SelectionBuilder builder = new SelectionBuilder();
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case USER:
        return builder.table(UserTable.TABLE_NAME);
      case USER_ID:
        final String userId = UserTable.getId(uri);
        return builder.table(UserTable.TABLE_NAME) //
            .where(UserTable.COLUMN_ID + "=?", userId);
      case REPOSITORY:
        return builder.table(RepositoryTable.TABLE_NAME);
      case REPOSITORY_ID:
        final String repoId = RepositoryTable.getId(uri);
        return builder.table(RepositoryTable.TABLE_NAME) //
            .where(UserTable.COLUMN_ID + "=?", repoId);
      case REPOSITORY_JOIN_USER:
        return builder.table(RepositoryTable.TABLE_JOIN_USER);
      default:
        throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
    }
  }
}