package com.shansown.androidarchitecture.data.repository.datasource;

import com.shansown.androidarchitecture.data.api.Order;
import com.shansown.androidarchitecture.data.api.Sort;
import com.shansown.androidarchitecture.data.api.dto.RepositoryData;
import java.util.List;
import org.joda.time.DateTime;
import rx.Observable;

public interface RepoDataStore {
  Observable<List<RepositoryData>> getRepositories(DateTime since, Sort sort, Order order);
}