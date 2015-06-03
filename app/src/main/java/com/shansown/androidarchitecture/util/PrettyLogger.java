package com.shansown.androidarchitecture.util;

import android.util.Log;
import com.orhanobut.logger.Logger;
import timber.log.Timber;

public class PrettyLogger extends Timber.Tree {

  public PrettyLogger() {
    Logger.init()
        .setMethodCount(3)
        .setMethodOffset(5);
  }

  @Override protected void log(int priority, String tag, String message, Throwable t) {
    switch (priority) {
      case Log.VERBOSE:
        Logger.v(message);
        break;
      case Log.DEBUG:
        Logger.d(message);
        break;
      case Log.INFO:
        Logger.i(message);
        break;
      case Log.WARN:
        Logger.w(message);
        break;
      case Log.ERROR:
        Logger.e(t, message);
        break;
    }
  }
}