package com.shansown.androidarchitecture.data.db.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.shansown.androidarchitecture.data.db.table.RepositoryTable;
import com.shansown.androidarchitecture.data.db.table.UserTable;

public final class AADatabase extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "aa_provider.db";
  private static final int DATABASE_VERSION = 1;

  public AADatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    UserTable.onCreate(db);
    RepositoryTable.onCreate(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    UserTable.onUpgrade(db, oldVersion, newVersion);
    RepositoryTable.onUpgrade(db, oldVersion, newVersion);
  }
}