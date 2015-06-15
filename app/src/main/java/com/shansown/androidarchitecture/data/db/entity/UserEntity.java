package com.shansown.androidarchitecture.data.db.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import lombok.Getter;

import static com.shansown.androidarchitecture.util.Preconditions.checkNotNull;

@Getter
public final class UserEntity {
  @NonNull private final String id;
  @NonNull private final String login;
  @Nullable private final String avatarUrl;

  private UserEntity(Builder builder) {
    this.id = checkNotNull(builder.id, "id == null");
    this.login = checkNotNull(builder.login, "login == null");
    this.avatarUrl = builder.avatarUrl;
  }

  @Override public String toString() {
    return "UserEntity{" +
        "id='" + id + '\'' +
        ", login='" + login + '\'' +
        ", avatarUrl='" + avatarUrl + '\'' +
        '}';
  }

  public static final class Builder {
    private String id;
    private String login;
    private String avatarUrl;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder login(String login) {
      this.login = login;
      return this;
    }

    public Builder avatarUrl(String avatarUrl) {
      this.avatarUrl = avatarUrl;
      return this;
    }

    public UserEntity build() {
      return new UserEntity(this);
    }
  }
}