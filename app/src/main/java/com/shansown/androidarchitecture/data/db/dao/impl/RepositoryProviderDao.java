package com.shansown.androidarchitecture.data.db.dao.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.db.dao.RepositoryDao;
import com.shansown.androidarchitecture.data.db.entity.RepositoryEntity;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.data.db.table.RepositoryTable;
import com.shansown.androidarchitecture.data.db.table.UserTable;
import com.shansown.androidarchitecture.util.Pair;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import rx.Observable;

import static com.shansown.androidarchitecture.data.db.table.BaseTable.JOIN;

@Singleton //
public final class RepositoryProviderDao extends GenericProviderDao<RepositoryEntity, String>
    implements RepositoryDao {

  @Inject public RepositoryProviderDao(ContentResolver contentResolver) {
    super(contentResolver);
  }

  public Observable<List<RepositoryEntity>> get(DateTime since, Sort sort, Order order) {
    return get(getContentUri(), since, sort, order);
  }

  public Observable<List<Pair<RepositoryEntity, UserEntity>>> getJoinOwners(DateTime since,
      Sort sort, Order order) {
    Uri uri = RepositoryTable.CONTENT_JOIN_USER_URI;
    String select = RepositoryTable.COLUMN_UPDATED_AT + " > ?";
    String[] selctArgs = { String.valueOf(since.getMillis()) };
    String orderBy = sort.toString() + " " + order.toString();
    return getCursor(uri, null, select, selctArgs, orderBy) //
        .map(c -> parseJoinListCursor(c, UserTable::fromCursor));
  }

  private Observable<List<RepositoryEntity>> get(Uri uri, DateTime since, Sort sort, Order order) {
    String select = RepositoryTable.COLUMN_UPDATED_AT + " > ?";
    String[] selctArgs = { String.valueOf(since.getMillis()) };
    String orderBy = sort.toString() + " " + order.toString();
    return get(uri, null, select, selctArgs, orderBy);
  }

  protected Uri buildUriForId(String id) {
    return RepositoryTable.buildUri(id);
  }

  protected String getIdFromEntity(RepositoryEntity entity) {
    return entity.getId();
  }

  protected RepositoryEntity fromCursor(Cursor cursor) {
    return RepositoryTable.fromCursor(cursor);
  }

  protected ContentValues toContentValues(RepositoryEntity entity) {
    return RepositoryTable.toContentValues(entity);
  }

  protected Uri getContentUri() {
    return RepositoryTable.CONTENT_URI;
  }
}