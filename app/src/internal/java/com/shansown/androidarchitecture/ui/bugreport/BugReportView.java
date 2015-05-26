package com.shansown.androidarchitecture.ui.bugreport;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.shansown.androidarchitecture.R;
import com.shansown.androidarchitecture.ui.misc.EmptyTextWatcher;
import com.shansown.androidarchitecture.util.Strings;

public final class BugReportView extends LinearLayout {
  @InjectView(R.id.title) EditText titleView;
  @InjectView(R.id.description) EditText descriptionView;
  @InjectView(R.id.screenshot) CheckBox screenshotView;
  @InjectView(R.id.logs) CheckBox logsView;

  public interface ReportDetailsListener {
    void onStateChanged(boolean valid);
  }

  private ReportDetailsListener listener;

  public BugReportView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);

    titleView.setOnFocusChangeListener((v, hasFocus) -> {
      if (!hasFocus) {
        String error = getContext().getString(R.string.bugreport_title_error);
        titleView.setError(Strings.isBlank(titleView.getText()) ? error : null);
      }
    });
    titleView.addTextChangedListener(new EmptyTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        if (listener != null) {
          listener.onStateChanged(!Strings.isBlank(s));
        }
      }
    });

    screenshotView.setChecked(true);
    logsView.setChecked(true);
  }

  public void setBugReportListener(ReportDetailsListener listener) {
    this.listener = listener;
  }

  public Report getReport() {
    return new Report(String.valueOf(titleView.getText()),
        String.valueOf(descriptionView.getText()), screenshotView.isChecked(),
        logsView.isChecked());
  }

  public static final class Report {
    public final String title;
    public final String description;
    public final boolean includeScreenshot;
    public final boolean includeLogs;

    public Report(String title, String description, boolean includeScreenshot,
        boolean includeLogs) {
      this.title = title;
      this.description = description;
      this.includeScreenshot = includeScreenshot;
      this.includeLogs = includeLogs;
    }
  }
}
