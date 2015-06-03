package com.shansown.androidarchitecture.data.api.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import static com.shansown.androidarchitecture.util.Preconditions.checkNotNull;

@Getter
public final class UserData {
  @NonNull private final String login;
  @SerializedName("avatar_url") @Nullable private final String avatarUrl;

  public UserData(String login, @Nullable String avatarUrl) {
    this.login = checkNotNull(login, "login == null");
    this.avatarUrl = avatarUrl;
  }

  @Override public String toString() {
    return "UserData{" +
        "login='" + login + '\'' +
        ", avatarUrl='" + avatarUrl + '\'' +
        '}';
  }
}