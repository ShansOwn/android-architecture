package com.shansown.androidarchitecture.ui.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.shansown.androidarchitecture.R;

public final class DrawerHeaderView extends FrameLayout {

  public DrawerHeaderView(Context context) {
    super(context);
    init(context);
  }

  public DrawerHeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public DrawerHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DrawerHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.drawer_header, this);
  }
}