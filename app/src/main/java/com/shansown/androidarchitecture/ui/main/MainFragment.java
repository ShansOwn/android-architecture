package com.shansown.androidarchitecture.ui.main;

import android.view.MenuItem;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.ui.BaseFragment;
import timber.log.Timber;

public final class MainFragment extends BaseFragment {

  @Override protected int getLayoutId() {
    return R.layout.main_fragment;
  }

  @Override protected int getMenuId() {
    return R.menu.menu_main;
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_settings:
        Timber.d("Settings action selected");
        return true;
    }
    return super.onMenuItemClick(item);
  }
}