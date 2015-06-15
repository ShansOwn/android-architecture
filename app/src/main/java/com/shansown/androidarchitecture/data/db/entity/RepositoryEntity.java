package com.shansown.androidarchitecture.data.db.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import lombok.Getter;
import org.joda.time.DateTime;

import static com.shansown.androidarchitecture.util.Preconditions.checkNotNull;

@Getter
public final class RepositoryEntity {
  @NonNull private final String id;
  @NonNull private final String name;
  @NonNull private final String ownerId;
  @Nullable private final String description;
  private final long stars;
  private final long forks;
  private final String htmlUrl;
  private final DateTime updatedAt;

  private RepositoryEntity(Builder builder) {
    this.id = checkNotNull(builder.id, "id == null");
    this.name = checkNotNull(builder.name, "name == null");
    this.ownerId = checkNotNull(builder.ownerId, "ownerId == null");
    this.description = builder.description;
    this.stars = builder.stars;
    this.forks = builder.forks;
    this.htmlUrl = checkNotNull(builder.htmlUrl, "htmlUrl == null");
    this.updatedAt = checkNotNull(builder.updatedAt, "updatedAt == null");
  }

  @Override public String toString() {
    return "RepositoryEntity{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", ownerId=" + ownerId +
        ", description='" + description + '\'' +
        ", stars=" + stars +
        ", forks=" + forks +
        ", htmlUrl='" + htmlUrl + '\'' +
        ", updatedAt=" + updatedAt +
        '}';
  }

  public static final class Builder {
    private String id;
    private String name;
    private String ownerId;
    private String description;
    private long stars;
    private long forks;
    private String htmlUrl;
    private DateTime updatedAt;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder owner(String ownerId) {
      this.ownerId = ownerId;
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

    public RepositoryEntity build() {
      return new RepositoryEntity(this);
    }
  }
}
