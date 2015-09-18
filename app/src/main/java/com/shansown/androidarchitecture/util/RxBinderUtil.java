package com.shansown.androidarchitecture.util;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Class designed for views and viewmodels bindings
 *
 * For appropriate work all public methods should be invoked in main thread
 */
public final class RxBinderUtil {

  final private String tag;
  final private CompositeSubscription compositeSubscription = new CompositeSubscription();

  public RxBinderUtil(Object target) {
    this.tag = target.getClass().getCanonicalName();
  }

  public void clear() {
    compositeSubscription.clear();
  }

  public <U> void bindObserver(final Observable<U> observable, final Observer<U> observer) {
    compositeSubscription.add(
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer));
  }

  public <U> void bindProperty(final Observable<U> observable, final Action1<U> setter) {
    compositeSubscription.add(subscribeSetter(observable, setter, tag));
  }

  private static <U> Subscription subscribeSetter(final Observable<U> observable,
      final Action1<U> setter, final String tag) {
    return observable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SetterSubscriber<>(setter, tag));
  }

  static private class SetterSubscriber<U> extends Subscriber<U> {
    final private Action1<U> setter;
    final private String tag;

    public SetterSubscriber(final Action1<U> setter, final String tag) {
      this.setter = setter;
      this.tag = tag;
    }

    @Override public void onCompleted() {
      Timber.d(tag + "." + "onCompleted");
    }

    @Override public void onError(Throwable e) {
      Timber.e(e, tag + "." + "onError");
    }

    @Override public void onNext(U u) {
      setter.call(u);
    }
  }
}