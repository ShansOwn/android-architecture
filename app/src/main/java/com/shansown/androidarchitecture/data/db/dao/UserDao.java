package com.shansown.androidarchitecture.data.db.dao;

import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import java.util.List;
import rx.Observable;

public interface UserDao extends BaseDao<UserEntity> {

  Observable<UserEntity> get(String id);

  Observable<List<UserEntity>> get(List<String> ids);
}