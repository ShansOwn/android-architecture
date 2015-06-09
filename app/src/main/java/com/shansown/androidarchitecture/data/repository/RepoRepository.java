package com.shansown.androidarchitecture.data.repository;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.List;
import org.joda.time.DateTime;
import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link RepositoryData} related data.
 */
public interface RepoRepository {

  /**
   * Get an {@link rx.Observable} which will emit a List of {@link RepositoryData}.
   */
  Observable<List<RepositoryData>> getRepositories(DateTime since, Sort sort, Order order);
}
