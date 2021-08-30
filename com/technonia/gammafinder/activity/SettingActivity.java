package com.technonia.gammafinder.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.technonia.gammafinder.Preferences;
import java.util.Locale;

public class SettingActivity extends BaseActivity {
   private static final String TAG = "SettingActivity";
   private OnCheckedChangeListener mRadioCheck = new OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup var1, int var2) {
         if (var1.getId() == 2131427447) {
            switch(var2) {
            case 2131427448:
               Preferences.instance().putInt("COUNTDOWN_MIN", 0);
               Preferences.instance().putBoolean("COUNTDOWN_MODE", false);
               break;
            case 2131427449:
               Preferences.instance().putInt("COUNTDOWN_MIN", 3);
               Preferences.instance().putBoolean("COUNTDOWN_MODE", true);
               break;
            case 2131427450:
               Preferences.instance().putInt("COUNTDOWN_MIN", 5);
               Preferences.instance().putBoolean("COUNTDOWN_MODE", true);
               break;
            case 2131427451:
               Preferences.instance().putInt("COUNTDOWN_MIN", 10);
               Preferences.instance().putBoolean("COUNTDOWN_MODE", true);
               break;
            case 2131427452:
               Preferences.instance().putInt("COUNTDOWN_MIN", 30);
               Preferences.instance().putBoolean("COUNTDOWN_MODE", true);
            }
         }

      }
   };
   private OnClickListener onClickListener = new OnClickListener() {
      public void onClick(View var1) {
         Builder var2;
         if (BaseActivity.isSensorConnected) {
            var2 = new Builder(SettingActivity.this);
            var2.setTitle(2131099685).setMessage(2131099684);
            var2.setPositiveButton(2131099713, new android.content.DialogInterface.OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  Log.d("SettingActivity", "==Calibration==");
                  BaseActivity.isClickCalibrationButton = true;
                  SettingActivity.this.finish();
               }
            }).setNegativeButton(2131099686, new android.content.DialogInterface.OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  var1.dismiss();
               }
            });
            var2.show();
         } else {
            var2 = new Builder(SettingActivity.this);
            var2.setTitle(2131099685);
            var2.setMessage(2131099681);
            var2.setPositiveButton(2131099713, new android.content.DialogInterface.OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  var1.dismiss();
               }
            });
            var2.show();
         }

      }
   };

   private void updateUI() {
      RadioGroup var2 = (RadioGroup)this.findViewById(2131427447);
      int var1 = Preferences.instance().getInt("COUNTDOWN_MIN");
      if (var1 == 3) {
         var2.check(2131427449);
      } else if (var1 == 5) {
         var2.check(2131427450);
      } else if (var1 == 10) {
         var2.check(2131427451);
      } else if (var1 == 30) {
         var2.check(2131427452);
      } else {
         var2.check(2131427448);
      }

   }

   protected void onCreate(Bundle var1) {
      Log.d("SettingActivity", "onCreate");
      this.setCurActivity(BaseActivity.Activity.SETTING.ordinal());
      super.onCreate(var1);
      TextView var3 = (TextView)this.findViewById(2131427454);
      String var5 = "";

      label13: {
         String var2;
         try {
            var2 = this.getApplicationContext().getPackageManager().getPackageInfo(this.getApplicationContext().getPackageName(), 0).versionName;
         } catch (NameNotFoundException var4) {
            var4.printStackTrace();
            break label13;
         }

         var5 = var2;
      }

      var3.setText(String.format(Locale.getDefault(), "v%s", var5));
      this.updateUI();
      ((RadioGroup)this.findViewById(2131427447)).setOnCheckedChangeListener(this.mRadioCheck);
      ((Button)this.findViewById(2131427453)).setOnClickListener(this.onClickListener);
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      MenuItem var2 = var1.add(0, 2131427331, 0, "");
      var2.setIcon(2130837611);
      var2.setShowAsAction(1);
      return true;
   }

   protected void onDestroy() {
      Log.d("SettingActivity", "onDestroy");
      super.onDestroy();
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      if (var2.getKeyCode() == 4) {
         Log.d("SettingActivity", "Back");
         this.finish();
      }

      return super.onKeyUp(var1, var2);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case 2131427331:
         if (this.mDrawerLayout.isDrawerOpen(8388613)) {
            this.mDrawerLayout.closeDrawer(8388613);
         } else {
            this.mDrawerLayout.openDrawer(8388613);
         }
      default:
         return super.onOptionsItemSelected(var1);
      }
   }

   protected void onPause() {
      Log.d("SettingActivity", "onPause()");
      super.onPause();
   }

   protected void onResume() {
      Log.d("SettingActivity", "onResume()");
      super.onResume();
   }

   protected void onUserLeaveHint() {
      Log.d("SettingActivity", "Home key");
      if (this.home_close_flag) {
         this.finish();
         System.exit(0);
      }

      super.onUserLeaveHint();
   }

   public void selectedMenu(int var1) {
      this.home_close_flag = false;
      if (var1 == SettingActivity.SETTING_MENU.MAIN.ordinal()) {
         this.startActivity(MainActivity.class);
      } else if (var1 == SettingActivity.SETTING_MENU.OTHER_APPS.ordinal()) {
         this.showWebPage("market://search?q=technonia");
      } else if (var1 == SettingActivity.SETTING_MENU.HISTORY.ordinal()) {
         this.startActivity(HistoryActivity.class);
      } else if (var1 == SettingActivity.SETTING_MENU.WEB_SITE.ordinal()) {
         this.showWebPage("http://www.allsmartlab.com");
      } else if (var1 == SettingActivity.SETTING_MENU.NOTICE.ordinal()) {
         this.showWebPage("http://allsmartlab.com/notice/");
      } else if (var1 == SettingActivity.SETTING_MENU.FAQ.ordinal()) {
         this.showWebPage("http://allsmartlab.com/online-qa/");
      } else if (var1 == SettingActivity.SETTING_MENU.TECHNONIA_URL.ordinal()) {
         this.showWebPage("http://www.technonia.co.kr");
      }

      this.mMenuListView.setItemChecked(var1, false);
      this.mDrawerLayout.closeDrawer(this.mMenuListView);
   }

   private static enum SETTING_MENU {
      FAQ,
      HISTORY,
      MAIN,
      NOTICE,
      OTHER_APPS,
      TECHNONIA_URL,
      WEB_SITE;
   }
}
