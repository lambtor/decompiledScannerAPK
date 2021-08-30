package com.technonia.gammafinder.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class GeigerDBAdapter {
   public static int ACCESS_COUNT = 0;
   public static final String COUNT = "count";
   public static final String CPM = "cpm";
   public static final String DATABASE_FILE = "geigerhistory.db";
   public static final int DATABASE_VERSION = 2;
   public static final String DATE = "date";
   public static final String ID = "id";
   public static final String LATITUDE = "latitude";
   public static final String LONGITUDE = "longitude";
   public static final String MEMO = "memo";
   public static final String TABLE_NAME = "geiger_log";
   private static String TAG = "GeigerDBAdapter";
   public static final String TIME = "time";
   public static final String μSvh = "uSvh";
   private Context mCtx;
   private SQLiteDatabase mDb;
   private GeigerDBAdapter.DatabaseHelper mDbHelper;

   public GeigerDBAdapter(Context var1) {
      this.mCtx = var1;
   }

   public void close() {
      this.mDbHelper.close();
   }

   public boolean deleteDB_Data(int var1) {
      boolean var2;
      if (this.mDb.delete("geiger_log", "id=" + var1, (String[])null) > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Cursor getAllColumns() {
      return this.mDb.query("geiger_log", (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);
   }

   public void insertDataInDB(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      ContentValues var9 = new ContentValues();
      var9.put("date", var1);
      var9.put("latitude", var2);
      var9.put("longitude", var3);
      var9.put("cpm", var4);
      var9.put("count", var5);
      var9.put("uSvh", var6);
      var9.put("time", var7);
      var9.put("memo", var8);
      this.mDb.insert("geiger_log", (String)null, var9);
   }

   public GeigerDBAdapter open() throws SQLException {
      this.mDbHelper = new GeigerDBAdapter.DatabaseHelper(this.mCtx);
      this.mDb = this.mDbHelper.getWritableDatabase();
      ++ACCESS_COUNT;
      Log.d(TAG, "ACCESS_COUNT : " + ACCESS_COUNT);
      return this;
   }

   public void updateDataInDB(int var1, String var2) {
      ContentValues var3 = new ContentValues();
      var3.put("memo", var2);
      this.mDb.update("geiger_log", var3, "id= ?", new String[]{String.valueOf(var1)});
   }

   private class DatabaseHelper extends SQLiteOpenHelper {
      public DatabaseHelper(Context var2) {
         super(var2, "geigerhistory.db", (CursorFactory)null, 2);
      }

      public void onCreate(SQLiteDatabase var1) {
         var1.execSQL("create table geiger_log(id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT not null, latitude FLOAT,longitude FLOAT, cpm INTEGER not null, count INTEGER not null, uSvh FLOAT not null, time TEXT not null,memo TEXT);");
      }

      public void onUpgrade(SQLiteDatabase var1, int var2, int var3) {
         switch(var2) {
         case 1:
            try {
               var1.beginTransaction();
               var1.execSQL("ALTER TABLE geiger_log ADD COLUMN memoTEXT");
               var1.setTransactionSuccessful();
            } catch (IllegalStateException var7) {
               ;
            } finally {
               var1.endTransaction();
               Log.i("DB Upgrade Log", "version upgrade");
            }
         default:
         }
      }
   }
}
