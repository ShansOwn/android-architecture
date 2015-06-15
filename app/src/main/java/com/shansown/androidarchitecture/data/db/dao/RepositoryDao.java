package com.shansown.androidarchitecture.data.db.dao;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.db.entity.RepositoryEntity;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.util.Pair;
import java.util.List;
import org.joda.time.DateTime;
import rx.Observable;

public interface RepositoryDao extends BaseDao<RepositoryEntity> {

  Observable<List<RepositoryEntity>> get(DateTime since, Sort sort, Order order);

  Observable<List<Pair<RepositoryEntity, UserEntity>>> getJoinOwners(DateTime since, Sort sort,
      Order order);
}