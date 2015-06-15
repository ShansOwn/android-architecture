package com.shansown.androidarchitecture.data.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import timber.log.Timber;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE;

public final class UserTable extends BaseTable {
  public static final String TABLE_NAME = "user";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_LOGIN = "login";
  public static final String COLUMN_AVATAR_URL = "avatar_url";

  /**
   * Fully qualified URI for "user" resources.
   */
  public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
  /**
   * MIME type for lists of users.
   */
  public static final String CONTENT_TYPE =
      CURSOR_DIR_BASE_TYPE + PATH_NODE_VND + "." + CONTENT_AUTHORITY + "." + TABLE_NAME;
  /**
   * MIME type for individual user.
   */
  public static final String CONTENT_ITEM_TYPE =
      CURSOR_ITEM_BASE_TYPE + PATH_NODE_VND + "." + CONTENT_AUTHORITY + "." + TABLE_NAME;

  public static UserEntity fromCursor(Cursor cursor) {
    return new UserEntity.Builder() //
        .id(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_ID)))
        .login(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_LOGIN)))
        .avatarUrl(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_AVATAR_URL)))
        .build();
  }

  public static ContentValues toContentValues(UserEntity entity) {
    ContentValues values = new ContentValues();
    values.put(UserTable.COLUMN_ID, entity.getId());
    values.put(UserTable.COLUMN_LOGIN, entity.getLogin());
    values.put(UserTable.COLUMN_AVATAR_URL, entity.getAvatarUrl());
    return values;
  }

  private static final String DATABASE_CREATE = "CREATE TABLE " //
      + TABLE_NAME + " (" //
      + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //
      + COLUMN_ID + " TEXT NOT NULL, " //
      + COLUMN_LOGIN + " TEXT NOT NULL, " //
      + COLUMN_AVATAR_URL + " TEXT NOT NULL, " //
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