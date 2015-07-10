package com.shansown.androidarchitecture.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class) //
@PrepareForTest(AndroidSchedulers.class) //
public class RxBinderUtilTest {

  RxBinderUtil binder;

  @Mock Action1<String> action;

  @Before public void setup() {
    // Mocking AndroidSchedulers.mainThread() as loopers are not mocked by android unit tests
    PowerMockito.stub(PowerMockito.method(AndroidSchedulers.class, "mainThread"))
        .toReturn(Schedulers.immediate());
    binder = new RxBinderUtil(this);
  }

  @Test public void testSetterIsNotifiedAfterBinding() throws Exception {
    binder.bindProperty(Observable.just("String"), action);

    verify(action, only()).call(anyString());
  }

  @Test public void testBinderIsReusableAfterClearing() throws Exception {
    binder.bindProperty(Observable.just("String"), action);
    binder.clear();
    binder.bindProperty(Observable.just("String"), action);

    verify(action, times(2)).call(anyString());
  }
}