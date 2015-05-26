package com.shansown.androidarchitecture.ui.debug;

import android.app.Activity;
import android.app.Application;
import android.os.PowerManager;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.madge.MadgeFrameLayout;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.mattprecious.telescope.TelescopeLayout;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.data.annotation.PixelGridEnabled;
import com.shansown.androidarchitecture.data.annotation.PixelRatioEnabled;
import com.shansown.androidarchitecture.data.annotation.ScalpelEnabled;
import com.shansown.androidarchitecture.data.annotation.ScalpelWireframeEnabled;
import com.shansown.androidarchitecture.ui.AppContainer;
import com.shansown.androidarchitecture.ui.bugreport.BugReportLens;
import com.shansown.androidarchitecture.util.EmptyActivityLifecycleCallbacks;
import com.shansown.androidarchitecture.util.LumberYard;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

/**
 * An {@link AppContainer} for debug builds which wrap the content view with a sliding drawer on
 * the right that holds all of the debug information and settings.
 */
@Singleton
public final class DebugAppContainer implements AppContainer {
  private final LumberYard lumberYard;
  private final Observable<Boolean> pixelGridEnabled;
  private final Observable<Boolean> pixelRatioEnabled;
  private final Observable<Boolean> scalpelEnabled;
  private final Observable<Boolean> scalpelWireframeEnabled;

  static class ViewHolder {
    @InjectView(R.id.telescope_container) TelescopeLayout telescopeLayout;
    @InjectView(R.id.madge_container) MadgeFrameLayout madgeFrameLayout;
    @InjectView(R.id.debug_content) ScalpelFrameLayout content;
  }

  @Inject
  public DebugAppContainer(LumberYard lumberYard,
      @PixelGridEnabled Observable<Boolean> pixelGridEnabled,
      @PixelRatioEnabled Observable<Boolean> pixelRatioEnabled,
      @ScalpelEnabled Observable<Boolean> scalpelEnabled,
      @ScalpelWireframeEnabled Observable<Boolean> scalpelWireframeEnabled) {
    this.lumberYard = lumberYard;
    this.pixelGridEnabled = pixelGridEnabled;
    this.pixelRatioEnabled = pixelRatioEnabled;
    this.scalpelEnabled = scalpelEnabled;
    this.scalpelWireframeEnabled = scalpelWireframeEnabled;
  }

  @Override public ViewGroup bind(final Activity activity) {
    activity.setContentView(R.layout.debug_activity_frame);

    final ViewHolder viewHolder = new ViewHolder();
    ButterKnife.inject(viewHolder, activity);

    TelescopeLayout.cleanUp(activity); // Clean up any old screenshots.
    viewHolder.telescopeLayout.setLens(new BugReportLens(activity, lumberYard));

    final CompositeSubscription subscriptions = new CompositeSubscription();
    setupMadge(viewHolder, subscriptions);
    setupScalpel(viewHolder, subscriptions);

    final Application app = activity.getApplication();
    app.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
      @Override public void onActivityDestroyed(Activity lifecycleActivity) {
        if (lifecycleActivity == activity) {
          subscriptions.unsubscribe();
          app.unregisterActivityLifecycleCallbacks(this);
        }
      }
    });

    riseAndShine(activity);
    return viewHolder.content;
  }

  private void setupMadge(final ViewHolder viewHolder, CompositeSubscription subscriptions) {
    subscriptions.add(pixelGridEnabled.subscribe(viewHolder.madgeFrameLayout::setOverlayEnabled));
    subscriptions.add(
        pixelRatioEnabled.subscribe(viewHolder.madgeFrameLayout::setOverlayRatioEnabled));
  }

  private void setupScalpel(final ViewHolder viewHolder, CompositeSubscription subscriptions) {
    subscriptions.add(scalpelEnabled.subscribe(viewHolder.content::setLayerInteractionEnabled));
    subscriptions.add(scalpelWireframeEnabled.subscribe(enabled ->
        viewHolder.content.setDrawViews(!enabled)));
  }

  /**
   * Show the activity over the lock-screen and wake up the device. If you launched the app
   * manually
   * both of these conditions are already true. If you deployed from the IDE, however, this will
   * save you from hundreds of power button presses and pattern swiping per day!
   */
  public static void riseAndShine(Activity activity) {
    activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

    PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
    PowerManager.WakeLock lock =
        power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
    lock.acquire();
    lock.release();
  }
}
