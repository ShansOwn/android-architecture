package com.shansown.androidarchitecture.ui.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import lombok.Getter;

import static com.shansown.androidarchitecture.util.Preconditions.checkNotNull;

@Getter
public final class User {
  @NonNull private String id;
  @NonNull private String login;
  @Nullable private String avatarUrl;

  private User(Builder builder) {
    this.id = checkNotNull(builder.id, "id == null");
    this.login = checkNotNull(builder.login, "login == null");
    this.avatarUrl = builder.avatarUrl;
  }

  @Override public String toString() {
    return "User{" +
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

    public User build() {
      return new User(this);
    }
  }
}