package com.shansown.androidarchitecture.data.db.dao.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.shansown.androidarchitecture.data.db.dao.UserDao;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.data.db.table.UserTable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

@Singleton
public final class UserProviderDao extends GenericProviderDao<UserEntity, String> implements
    UserDao {

  @Inject public UserProviderDao(ContentResolver contentResolver) {
    super(contentResolver);
  }

  public Observable<List<UserEntity>> get(List<String> ids) {
    int count = ids.size();
    String selection = generateMultipleSelection(UserTable.COLUMN_ID, count);
    String[] selectArgs = new String[count];
    return get(getContentUri(), null, selection, ids.toArray(selectArgs), null);
  }

  protected Uri buildUriForId(String id) {
    return UserTable.buildUri(id);
  }

  protected String getIdFromEntity(UserEntity entity) {
    return entity.getId();
  }

  protected UserEntity fromCursor(Cursor cursor) {
    return new UserEntity.Builder() //
        .id(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_ID)))
        .login(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_LOGIN)))
        .avatarUrl(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_AVATAR_URL)))
        .build();
  }

  protected ContentValues toContentValues(UserEntity entity) {
    ContentValues values = new ContentValues();
    values.put(UserTable.COLUMN_ID, entity.getId());
    values.put(UserTable.COLUMN_LOGIN, entity.getLogin());
    values.put(UserTable.COLUMN_AVATAR_URL, entity.getAvatarUrl());
    return values;
  }

  protected Uri getContentUri() {
    return UserTable.CONTENT_URI;
  }
}