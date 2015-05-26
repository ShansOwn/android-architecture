package com.shansown.androidarchitecture.di.module;

import dagger.Module;

/**
 * Module that provides objects which will live during the application lifecycle.
 */
@Module(includes = {ProductionUiModule.class, ReleaseDataModule.class})
public final class ProductionAppModule {
}