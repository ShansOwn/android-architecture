package com.shansown.androidarchitecture.di.annotation;

import java.lang.annotation.Retention;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to signed/unsigned user to be memorized in the
 * correct component.
 */
@Scope
@Retention(RUNTIME)
public @interface PerUser {}
