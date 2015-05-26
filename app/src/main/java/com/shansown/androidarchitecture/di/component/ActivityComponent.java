package com.shansown.androidarchitecture.di.component;

import com.shansown.androidarchitecture.di.annotation.PerActivity;
import com.shansown.androidarchitecture.ui.BaseActivity;
import com.shansown.androidarchitecture.ui.BaseFragment;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link PerActivity}
 */
public interface ActivityComponent extends AppComponent {
  void inject(BaseActivity baseActivity);
  void inject(BaseFragment baseFragment);
}