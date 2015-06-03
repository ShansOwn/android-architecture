package com.shansown.androidarchitecture.data.api.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.joda.time.DateTime;

import static com.shansown.androidarchitecture.util.Preconditions.checkNotNull;

@Getter
public final class RepositoryData {
  @NonNull private final String name;
  @NonNull private final UserData owner;
  @Nullable private final String description;

  @SerializedName("watchers")
  private final long stars;
  private final long forks;

  @SerializedName("html_url")
  private final String htmlUrl;

  @SerializedName("updated_at")
  private final DateTime updatedAt;

  private RepositoryData(Builder builder) {
    this.name = checkNotNull(builder.name, "name == null");
    this.owner = checkNotNull(builder.owner, "owner == null");
    this.description = builder.description;
    this.stars = builder.stars;
    this.forks = builder.forks;
    this.htmlUrl = checkNotNull(builder.htmlUrl, "htmlUrl == null");
    this.updatedAt = checkNotNull(builder.updatedAt, "updatedAt == null");
  }

  @Override public String toString() {
    return "RepositoryData{" +
        "name='" + name + '\'' +
        ", owner=" + owner +
        ", description='" + description + '\'' +
        ", stars=" + stars +
        ", forks=" + forks +
        ", htmlUrl='" + htmlUrl + '\'' +
        ", updatedAt=" + updatedAt +
        '}';
  }

  public static final class Builder {
    private String name;
    private UserData owner;
    private String description;
    private long stars;
    private long forks;
    private String htmlUrl;
    private DateTime updatedAt;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder owner(UserData owner) {
      this.owner = owner;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder stars(long stars) {
      this.stars = stars;
      return this;
    }

    public Builder forks(long forks) {
      this.forks = forks;
      return this;
    }

    public Builder htmlUrl(String htmlUrl) {
      this.htmlUrl = htmlUrl;
      return this;
    }

    public Builder updatedAt(DateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public RepositoryData build() {
      return new RepositoryData(this);
    }
  }
}