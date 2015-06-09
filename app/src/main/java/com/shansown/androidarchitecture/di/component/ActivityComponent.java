package com.shansown.androidarchitecture.di.component;

import com.shansown.androidarchitecture.di.annotation.PerActivity;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link PerActivity}
 */
public interface ActivityComponent extends AppComponent {
}