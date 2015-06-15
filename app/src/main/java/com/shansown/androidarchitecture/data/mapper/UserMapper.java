package com.shansown.androidarchitecture.data.mapper;

import com.shansown.androidarchitecture.data.api.dto.UserData;
import com.shansown.androidarchitecture.data.db.entity.UserEntity;
import com.shansown.androidarchitecture.ui.model.User;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class UserMapper {

  @Inject public UserMapper() {
  }

  public UserEntity toUserEntity(User user) {
    return new UserEntity.Builder() //
        .id(user.getId()) //
        .login(user.getLogin()) //
        .avatarUrl(user.getAvatarUrl()) //
        .build();
  }

  public UserEntity toUserEntity(UserData data) {
    return new UserEntity.Builder() //
        .id(data.getId()) //
        .login(data.getLogin()) //
        .avatarUrl(data.getAvatarUrl()) //
        .build();
  }

  public User toUser(UserData data) {
    return new User.Builder() //
        .id(data.getId()) //
        .login(data.getLogin()) //
        .avatarUrl(data.getAvatarUrl()) //
        .build();
  }

  public User toUser(UserEntity entity) {
    return new User.Builder() //
        .id(entity.getId()) //
        .login(entity.getLogin()) //
        .avatarUrl(entity.getAvatarUrl()) //
        .build();
  }
}