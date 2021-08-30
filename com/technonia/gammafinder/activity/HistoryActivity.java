package com.technonia.gammafinder.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.technonia.gammafinder.CustomMemoDialog;
import com.technonia.gammafinder.adapter.GeigerDBAdapter;
import com.technonia.gammafinder.adapter.HistoryCustomAdapter;
import com.technonia.gammafinder.model.HistoryInfo;
import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {
   private static String TAG = "HistoryActivity";
   private Boolean IsMemo;
   private HistoryCustomAdapter historyAdapter;
   private ArrayList infoList;
   private ListView listViewForHistoryData;
   private int mPosition = 0;
   private GeigerDBAdapter mdb;
   private String strMemo;

   private void showDataList() {
      Cursor var2 = this.mdb.getAllColumns();
      Log.d(TAG, "Data COUNT : " + var2.getCount());

      while(var2.moveToNext()) {
         HistoryInfo var1 = new HistoryInfo(var2.getInt(var2.getColumnIndex("id")), var2.getString(var2.getColumnIndex("date")), var2.getFloat(var2.getColumnIndex("latitude")), var2.getFloat(var2.getColumnIndex("longitude")), var2.getString(var2.getColumnIndex("cpm")), var2.getString(var2.getColumnIndex("count")), var2.getString(var2.getColumnIndex("uSvh")), var2.getString(var2.getColumnIndex("time")), var2.getString(var2.getColumnIndex("memo")));
         this.infoList.add(var1);
      }

      var2.close();
   }

   public void DialogShow(final int var1) {
      final CustomMemoDialog var2 = new CustomMemoDialog(this);
      var2.setOnShowListener(new OnShowListener() {
         public void onShow(DialogInterface var1x) {
            var2.setDialog_memo(((HistoryInfo)HistoryActivity.this.infoList.get(var1)).memo);
         }
      });
      var2.setOnDismissListener(new OnDismissListener() {
         public void onDismiss(DialogInterface var1x) {
            HistoryActivity.this.IsMemo = var2.getStored();
            if (HistoryActivity.this.IsMemo.booleanValue()) {
               HistoryActivity.this.strMemo = var2.getMemoStr();
               HistoryActivity.this.mdb.updateDataInDB(((HistoryInfo)HistoryActivity.this.infoList.get(var1)).id, HistoryActivity.this.strMemo);
               HistoryInfo var2x = new HistoryInfo(((HistoryInfo)HistoryActivity.this.infoList.get(var1)).id, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).date, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).latitude, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).longitude, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).cpm, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).count, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).uSvh, ((HistoryInfo)HistoryActivity.this.infoList.get(var1)).time, HistoryActivity.this.strMemo);
               HistoryActivity.this.infoList.set(var1, var2x);
               HistoryActivity.this.historyAdapter.notifyDataSetChanged();
               HistoryActivity.this.strMemo = "";
            }

         }
      });
      var2.show();
   }

   protected void onCreate(Bundle var1) {
      Log.d(TAG, "onCreate()");
      this.setCurActivity(BaseActivity.Activity.HISTORY.ordinal());
      super.onCreate(var1);
      this.mdb = new GeigerDBAdapter(this);
      this.mdb.open();
      this.infoList = new ArrayList();
      this.listViewForHistoryData = (ListView)this.findViewById(2131427425);
      this.showDataList();
      this.historyAdapter = new HistoryCustomAdapter(this, this.infoList);
      this.listViewForHistoryData.setAdapter(this.historyAdapter);
      this.listViewForHistoryData.setSelection(this.historyAdapter.getCount() - 1);
      this.listViewForHistoryData.setChoiceMode(1);
      this.listViewForHistoryData.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            HistoryActivity.this.mPosition = var3;
         }
      });
      this.listViewForHistoryData.setOnItemLongClickListener(new OnItemLongClickListener() {
         public boolean onItemLongClick(AdapterView var1, View var2, final int var3, long var4) {
            Builder var6 = new Builder(HistoryActivity.this);
            ArrayAdapter var7 = new ArrayAdapter(HistoryActivity.this, 17367057);
            var7.add(HistoryActivity.this.getResources().getString(2131099694));
            var7.add(HistoryActivity.this.getResources().getString(2131099692));
            var6.setAdapter(var7, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  Builder var3x;
                  if (var2 == 0) {
                     var3x = new Builder(HistoryActivity.this);
                     var3x.setMessage(HistoryActivity.this.getString(2131099735));
                     var3x.setPositiveButton(2131099713, new OnClickListener() {
                        public void onClick(DialogInterface var1, int var2) {
                           HistoryActivity.this.DialogShow(var3);
                        }
                     }).setNegativeButton(2131099686, new OnClickListener() {
                        public void onClick(DialogInterface var1, int var2) {
                        }
                     });
                     var3x.show();
                  } else if (var2 == 1) {
                     var3x = new Builder(HistoryActivity.this);
                     var3x.setMessage(HistoryActivity.this.getString(2131099734));
                     var3x.setPositiveButton(2131099713, new OnClickListener() {
                        public void onClick(DialogInterface var1, int var2) {
                           boolean var3x = HistoryActivity.this.mdb.deleteDB_Data(((HistoryInfo)HistoryActivity.this.infoList.get(var3)).id);
                           Log.d(HistoryActivity.TAG, "Delete Success : " + var3x);
                           if (var3x) {
                              HistoryActivity.this.infoList.remove(var3);
                              HistoryActivity.this.historyAdapter.notifyDataSetChanged();
                           }

                        }
                     }).setNegativeButton(2131099686, new OnClickListener() {
                        public void onClick(DialogInterface var1, int var2) {
                        }
                     });
                     var3x.show();
                  }

               }
            });
            var6.show();
            return true;
         }
      });
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      MenuItem var2 = var1.add(0, 2131427334, 0, "");
      MenuItem var3 = var1.add(0, 2131427331, 0, "");
      var3.setIcon(2130837611);
      var3.setShowAsAction(2);
      var2.setIcon(2130837621);
      var2.setShowAsAction(1);
      return true;
   }

   protected void onDestroy() {
      Log.d(TAG, "onDestroy()");
      super.onDestroy();
      Log.d(TAG, "DataBaseClose");
      this.mdb.close();
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case 2131427331:
         if (this.mDrawerLayout.isDrawerOpen(8388613)) {
            this.mDrawerLayout.closeDrawer(8388613);
         } else {
            this.mDrawerLayout.openDrawer(8388613);
         }
      case 2131427332:
      case 2131427333:
      default:
         break;
      case 2131427334:
         if (this.infoList.size() == 0) {
            Toast.makeText(this.getApplicationContext(), this.getString(2131099710), 0).show();
         }

         if (!this.listViewForHistoryData.isItemChecked(this.mPosition)) {
            Toast.makeText(this.getApplicationContext(), this.getString(2131099709), 0).show();
         } else {
            this.home_close_flag = false;
            Intent var2 = new Intent("android.intent.action.SEND");
            var2.addCategory("android.intent.category.DEFAULT");
            var2.putExtra("android.intent.extra.SUBJECT", this.getString(2131099690));
            var2.putExtra("android.intent.extra.TEXT", this.getString(2131099691) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).date + "\n" + this.getString(2131099689) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).cpm + "\n" + this.getString(2131099688) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).count + "\n" + this.getString(2131099722) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).uSvh + "\n" + this.getString(2131099704) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).time + "\n" + this.getString(2131099706) + " : " + ((HistoryInfo)this.infoList.get(this.mPosition)).memo);
            var2.setType("text/plain");
            this.startActivity(var2);
         }
      }

      return super.onOptionsItemSelected(var1);
   }

   protected void onPause() {
      Log.d(TAG, "onPause()");
      super.onPause();
   }

   protected void onRestart() {
      Log.d(TAG, "onRestart()");
      super.onRestart();
   }

   protected void onResume() {
      Log.d(TAG, "onResume()");
      super.onResume();
   }

   protected void onStart() {
      Log.d(TAG, "onStart()");
      super.onStart();
   }

   protected void onStop() {
      Log.d(TAG, "onStop()");
      super.onStop();
   }

   protected void onUserLeaveHint() {
      Log.d(TAG, "Home key");
      if (this.home_close_flag) {
         this.finish();
         System.exit(0);
      }

      super.onUserLeaveHint();
   }

   public void selectedMenu(int var1) {
      this.home_close_flag = false;
      if (var1 == HistoryActivity.HISTORY_MENU.MAIN.ordinal()) {
         this.startActivity(MainActivity.class);
      } else if (var1 == HistoryActivity.HISTORY_MENU.SETTING.ordinal()) {
         this.startActivity(SettingActivity.class);
      } else if (var1 == HistoryActivity.HISTORY_MENU.OTHER_APPS.ordinal()) {
         this.showWebPage("market://search?q=technonia");
      } else if (var1 == HistoryActivity.HISTORY_MENU.WEB_SITE.ordinal()) {
         this.showWebPage("http://www.allsmartlab.com");
      } else if (var1 == HistoryActivity.HISTORY_MENU.NOTICE.ordinal()) {
         this.showWebPage("http://allsmartlab.com/notice/");
      } else if (var1 == HistoryActivity.HISTORY_MENU.FAQ.ordinal()) {
         this.showWebPage("http://allsmartlab.com/online-qa/");
      } else if (var1 == HistoryActivity.HISTORY_MENU.TECHNONIA_URL.ordinal()) {
         this.showWebPage("http://www.technonia.co.kr");
      }

      this.mMenuListView.setItemChecked(var1, false);
      this.mDrawerLayout.closeDrawer(this.mMenuListView);
   }

   private static enum HISTORY_MENU {
      FAQ,
      MAIN,
      NOTICE,
      OTHER_APPS,
      SETTING,
      TECHNONIA_URL,
      WEB_SITE;
   }
}
