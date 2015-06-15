package com.shansown.androidarchitecture.data.db.table;

import android.net.Uri;
import android.provider.BaseColumns;
import com.shansown.androidarchitecture.data.db.provider.AAProvider;

public abstract class BaseTable implements BaseColumns {
  public static final String CONTENT_AUTHORITY = AAProvider.CONTENT_AUTHORITY;
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String QUERY_PARAMETER_LIMIT = "limit";
  public static final String QUERY_PARAMETER_OFFSET = "offset";
  public static final String JOIN = "_join_";

  public static final String PATH_NODE_VND = "/vnd";
  public static final String PATH_NODE_ANY_STR = "/*";
  public static final String PATH_NODE_ANY_NUM = "/#";
}