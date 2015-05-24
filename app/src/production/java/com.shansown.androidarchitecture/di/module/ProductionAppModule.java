package com.shansown.androidarchitecture.di.module;

import com.shansown.androidarchitecture.di.module.ReleaseNetworkModule;
import dagger.Module;

/**
 * Module that provides objects which will live during the application lifecycle.
 */
@Module(includes = {ReleaseNetworkModule.class})
public final class ProductionAppModule {
}