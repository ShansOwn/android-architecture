package com.shansown.androidarchitecture.ui;

import android.support.annotation.Nullable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Base view model created to be extended by every view model in this application.
 * This class provides some methods common to every view model.
 */
public abstract class BaseVM {

  private final Queue<Action0> actionsQueue = new LinkedBlockingQueue<>(getActionsQueueSize());

  @Nullable private CompositeSubscription compositeSubscription;

  private volatile boolean bound;

  /**
   * You should call this after view creating
   *
   * Only after/in this method VM should start any requests
   */
  public void init() {
    if (compositeSubscription == null) {
      compositeSubscription = new CompositeSubscription();
    }
  }

  /**
   * You should call this when view is bound to view model
   */
  public void onBind() {
    if (bound) return;
    bound = true;
    while (!actionsQueue.isEmpty()) {
      actionsQueue.poll().call();
    }
  }

  /**
   * You should call this before unbind view from view model
   */
  public void onUnbind() {
    bound = false;
  }

  /**
   * You should call this before view destroying
   */
  public void dispose() {
    if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
      compositeSubscription.unsubscribe();
      compositeSubscription = null;
    }
  }

  /**
   * If action which has side effect to view can be called when view is unbound
   * put this action here.
   *
   * @param action which has side effect to the view
   * @return <tt>true</tt> if view model bounded to the view
   * and this action will be called immediately
   */
  protected boolean viewAction(Action0 action) {
    if (bound) {
      action.call();
      return true;
    } else {
      actionsQueue.offer(action);
      return false;
    }
  }

  /**
   * Add subscription of infinite/long-running observables or cancellable subscription which can be
   * unsubscribed/cancelled manually by invoking {@link #removeSubscription(Subscription)} or
   * automatically on viewmodel disposing
   *
   * @param subscription which can be unsubscribed/cancelled
   * @return param subscription
   */
  protected Subscription addSubscription(Subscription subscription) {
    if (compositeSubscription != null) {
      compositeSubscription.add(subscription);
    }
    return subscription;
  }

  /**
   * Unsubscribe/cancel subscription
   *
   * @param subscription which will be unsubscribed/cancelled
   */
  protected void removeSubscription(Subscription subscription) {
    if (compositeSubscription != null) {
      compositeSubscription.remove(subscription);
    }
  }

  /**
   * If you need another size of actions queue
   * just override this method.
   * Default value is {@link Integer#MAX_VALUE}
   *
   * @return size of the actions queue
   */
  protected int getActionsQueueSize() {
    return Integer.MAX_VALUE;
  }
}