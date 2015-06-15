package com.shansown.androidarchitecture.data.repository;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.ui.model.Repository;
import java.util.List;
import org.joda.time.DateTime;
import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Repository} related data.
 */
public interface RepoRepository {

  /**
   * Get an {@link rx.Observable} which will emit a List of {@link Repository}.
   * Data will be get force from server.
   *
   * @param since
   * @param sort
   * @param order
   * @return {@link rx.Observable} of a List of {@link Repository}
   */
  Observable<List<Repository>> getForce(DateTime since, Sort sort, Order order);

  /**
   * Get an {@link rx.Observable} which will emit a List of {@link Repository}.
   * Data may be taken as from server as from local db as from cache
   * depending on internal realization.
   *
   * @param since
   * @param sort
   * @param order
   * @return {@link rx.Observable} of a List of {@link Repository}
   */
  Observable<List<Repository>> get(DateTime since, Sort sort, Order order);
}
