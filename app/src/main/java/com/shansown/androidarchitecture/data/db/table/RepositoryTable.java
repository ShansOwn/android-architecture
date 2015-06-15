package com.shansown.androidarchitecture.data.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.shansown.androidarchitecture.data.db.entity.RepositoryEntity;
import org.joda.time.DateTime;
import timber.log.Timber;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE;

public final class RepositoryTable extends BaseTable {
  public static final String TABLE_NAME = "repository";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_OWNER_ID = "owner_id";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_STARS = "stars";
  public static final String COLUMN_FORKS = "forks";
  public static final String COLUMN_HTML_URL = "html_url";
  public static final String COLUMN_UPDATED_AT = "updated_at";

  /**
   * Fully qualified URI for "repository" resources.
   */
  public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
  /**
   * Fully qualified URI for "repository" resources.
   */
  public static final Uri CONTENT_JOIN_USER_URI =
      BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME + JOIN + UserTable.TABLE_NAME).build();
  /**
   * MIME type for lists of repositories.
   */
  public static final String CONTENT_TYPE =
      CURSOR_DIR_BASE_TYPE + PATH_NODE_VND + "." + CONTENT_AUTHORITY + "." + TABLE_NAME;
  /**
   * MIME type for individual repository.
   */
  public static final String CONTENT_ITEM_TYPE =
      CURSOR_ITEM_BASE_TYPE + PATH_NODE_VND + "." + CONTENT_AUTHORITY + "." + TABLE_NAME;

  public static final String TABLE_JOIN_USER = "repository INNER JOIN "
      + UserTable.TABLE_NAME
      + " ON "
      + TABLE_NAME
      + "."
      + COLUMN_OWNER_ID
      //
      + "="
      + UserTable.TABLE_NAME
      + "."
      + UserTable.COLUMN_ID;

  public static RepositoryEntity fromCursor(Cursor cursor) {
    return new RepositoryEntity.Builder() //
        .id(cursor.getString(cursor.getColumnIndex(RepositoryTable.COLUMN_ID)))
        .name(cursor.getString(cursor.getColumnIndex(RepositoryTable.COLUMN_NAME)))
        .owner(cursor.getString(cursor.getColumnIndex(RepositoryTable.COLUMN_OWNER_ID)))
        .description(cursor.getString(cursor.getColumnIndex(RepositoryTable.COLUMN_DESCRIPTION)))
        .stars(cursor.getLong(cursor.getColumnIndex(RepositoryTable.COLUMN_STARS)))
        .forks(cursor.getLong(cursor.getColumnIndex(RepositoryTable.COLUMN_FORKS)))
        .htmlUrl(cursor.getString(cursor.getColumnIndex(RepositoryTable.COLUMN_HTML_URL)))
        .updatedAt(
            new DateTime(cursor.getLong(cursor.getColumnIndex(RepositoryTable.COLUMN_UPDATED_AT))))
        .build();
  }

  public static ContentValues toContentValues(RepositoryEntity entity) {
    ContentValues values = new ContentValues();
    values.put(RepositoryTable.COLUMN_ID, entity.getId());
    values.put(RepositoryTable.COLUMN_NAME, entity.getName());
    values.put(RepositoryTable.COLUMN_OWNER_ID, entity.getOwnerId());
    values.put(RepositoryTable.COLUMN_DESCRIPTION, entity.getDescription());
    values.put(RepositoryTable.COLUMN_STARS, entity.getStars());
    values.put(RepositoryTable.COLUMN_FORKS, entity.getForks());
    values.put(RepositoryTable.COLUMN_HTML_URL, entity.getHtmlUrl());
    values.put(RepositoryTable.COLUMN_UPDATED_AT, entity.getUpdatedAt().getMillis());
    return values;
  }

  private static final String DATABASE_CREATE = "CREATE TABLE " //
      + TABLE_NAME + " (" //
      + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //
      + COLUMN_ID + " TEXT NOT NULL, " //
      + COLUMN_NAME + " TEXT NOT NULL, " //
      + COLUMN_OWNER_ID + " TEXT NOT NULL, " //
      + COLUMN_DESCRIPTION + " TEXT, " //
      + COLUMN_STARS + " INTEGER, " //
      + COLUMN_FORKS + " INTEGER, " //
      + COLUMN_HTML_URL + " TEXT NOT NULL, " //
      + COLUMN_UPDATED_AT + " INTEGER NOT NULL, " //
      + "UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE);";

  /** Build {@link Uri} for requested {@link #COLUMN_ID}. */
  public static Uri buildUri(String id) {
    return CONTENT_URI.buildUpon().appendPath(id).build();
  }

  /** Get {@link #COLUMN_ID} for requested {@link Uri}. */
  public static String getId(Uri uri) {
    return uri.getPathSegments().get(1);
  }

  public static void onCreate(SQLiteDatabase database) {
    Timber.i("Create %s table", TABLE_NAME);
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    Timber.i("Update %s table from version %d to %d", TABLE_NAME, oldVersion, newVersion);
  }
}