package com.technonia.gammafinder.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.technonia.gammafinder.Preferences;
import com.technonia.gammafinder.adapter.MenuListAdapter;
import com.technonia.gammafinder.model.MenuInfo;
import java.util.ArrayList;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
   public static final String FAQ_URL = "http://allsmartlab.com/online-qa/";
   public static final String FTLAB_URL = "http://www.allsmartlab.com";
   public static final String NOTICE_URL = "http://allsmartlab.com/notice/";
   private static final String TAG = "BaseActivity";
   public static final String TECHNONIA_URL = "http://www.technonia.co.kr";
   protected static boolean isClickCalibrationButton = false;
   protected static boolean isSensorConnected = false;
   private int curActivity;
   protected boolean home_close_flag = true;
   protected DrawerLayout mDrawerLayout;
   protected ListView mMenuListView;
   private OrientationEventListener orientationEventListener;
   String systemLanguage;

   private void addMenuList(ArrayList var1) {
      if (this.getCurActivity() == BaseActivity.Activity.MAIN.ordinal()) {
         var1.add(new MenuInfo(2130837619, this.getString(2131099718)));
         var1.add(new MenuInfo(2130837617, this.getString(2131099703)));
         var1.add(new MenuInfo(2130837614, this.getString(2131099701)));
         var1.add(new MenuInfo(2130837618, this.getString(2131099716)));
      } else if (this.getCurActivity() == BaseActivity.Activity.SETTING.ordinal()) {
         var1.add(new MenuInfo(2130837615, this.getString(2131099702)));
         var1.add(new MenuInfo(2130837617, this.getString(2131099703)));
         var1.add(new MenuInfo(2130837614, this.getString(2131099701)));
      } else if (this.getCurActivity() == BaseActivity.Activity.HISTORY.ordinal()) {
         var1.add(new MenuInfo(2130837615, this.getString(2131099702)));
         var1.add(new MenuInfo(2130837619, this.getString(2131099718)));
         var1.add(new MenuInfo(2130837617, this.getString(2131099703)));
      }

      var1.add(new MenuInfo(2130837620, this.getString(2131099723)));
      var1.add(new MenuInfo(2130837616, this.getString(2131099711)));
      var1.add(new MenuInfo(2130837613, this.getString(2131099695)));
      if (this.systemLanguage.equals("ko")) {
         var1.add(new MenuInfo(2130837612, this.getString(2131099715)));
      }

   }

   private int getCurActivity() {
      return this.curActivity;
   }

   private void setActionBarTitle() {
      TextView var1 = (TextView)this.findViewById(2131427416);
      ((ImageView)this.findViewById(2131427415)).setImageResource(2130837608);
      if (this.getCurActivity() == BaseActivity.Activity.MAIN.ordinal()) {
         var1.setText(2131099679);
      } else if (this.getCurActivity() == BaseActivity.Activity.SETTING.ordinal()) {
         var1.setText(2131099718);
      } else if (this.getCurActivity() == BaseActivity.Activity.HISTORY.ordinal()) {
         var1.setText(2131099701);
      }

   }

   protected void onCreate(@Nullable Bundle var1) {
      super.onCreate(var1);
      this.getWindow().addFlags(128);
      if (this.getCurActivity() == BaseActivity.Activity.MAIN.ordinal()) {
         this.setContentView(2130903069);
      } else if (this.getCurActivity() == BaseActivity.Activity.SETTING.ordinal()) {
         this.setContentView(2130903070);
      } else if (this.getCurActivity() == BaseActivity.Activity.HISTORY.ordinal()) {
         this.setContentView(2130903067);
      }

      ActionBar var2 = this.getSupportActionBar();
      if (var2 != null) {
         var2.setCustomView(2130903066);
         var2.setDisplayShowCustomEnabled(true);
         var2.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffc947")));
         this.setActionBarTitle();
      }

      Preferences.instance().setContext(this);
      Preferences.instance().putBoolean("IS_INIT_USER", false);
      this.systemLanguage = Locale.getDefault().getLanguage();
      Log.i("BaseActivity", "systemLanguage : " + this.systemLanguage);
      this.mDrawerLayout = (DrawerLayout)this.findViewById(2131427417);
      this.mMenuListView = (ListView)this.findViewById(2131427426);
      ArrayList var3 = new ArrayList();
      this.addMenuList(var3);
      MenuListAdapter var4 = new MenuListAdapter(this, 2130903074, var3);
      this.mMenuListView.setAdapter(var4);
      this.mMenuListView.setOnItemClickListener(new BaseActivity.DrawerItemClickListener(null));
      this.orientationEventListener = new OrientationEventListener(this, 2) {
         public void onOrientationChanged(int var1) {
            if (var1 != -1) {
               if (var1 < 255 && (var1 < 0 || var1 > 105)) {
                  BaseActivity.this.setRequestedOrientation(9);
               } else {
                  BaseActivity.this.setRequestedOrientation(1);
               }
            }

         }
      };
   }

   protected void onDestroy() {
      super.onDestroy();
      this.orientationEventListener.disable();
   }

   protected void onResume() {
      super.onResume();
      this.orientationEventListener.enable();
   }

   public void selectedMenu(int var1) {
      this.home_close_flag = false;
      if (var1 == BaseActivity.MENU.SETTING.ordinal()) {
         this.startActivity(SettingActivity.class);
      } else if (var1 == BaseActivity.MENU.OTHER_APP.ordinal()) {
         this.showWebPage("market://search?q=technonia");
      } else if (var1 == BaseActivity.MENU.HISTORY.ordinal()) {
         this.startActivity(HistoryActivity.class);
      } else if (var1 == BaseActivity.MENU.WEB_SITE.ordinal()) {
         this.showWebPage("http://www.allsmartlab.com");
      } else if (var1 == BaseActivity.MENU.NOTICE.ordinal()) {
         this.showWebPage("http://allsmartlab.com/notice/");
      } else if (var1 == BaseActivity.MENU.FAQ.ordinal()) {
         this.showWebPage("http://allsmartlab.com/online-qa/");
      } else if (var1 == BaseActivity.MENU.TECHNONIA_URL.ordinal()) {
         this.showWebPage("http://www.technonia.co.kr");
      }

      this.mMenuListView.setItemChecked(var1, false);
      this.mDrawerLayout.closeDrawer(this.mMenuListView);
   }

   public void setCurActivity(int var1) {
      this.curActivity = var1;
   }

   protected void showWebPage(String var1) {
      this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(var1)));
   }

   protected void startActivity(Class var1) {
      Intent var2 = new Intent(this, var1);
      var2.addFlags(603979776);
      this.startActivity(var2);
      this.overridePendingTransition(17432578, 17432579);
   }

   static enum Activity {
      HISTORY,
      MAIN,
      SETTING;
   }

   private class DrawerItemClickListener implements OnItemClickListener {
      private DrawerItemClickListener() {
      }

      // $FF: synthetic method
      DrawerItemClickListener(Object var2) {
         this();
      }

      public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
         BaseActivity.this.selectedMenu(var3);
      }
   }

   static enum MENU {
      FAQ,
      HISTORY,
      NOTICE,
      OTHER_APP,
      SAVE_DATA,
      SETTING,
      TECHNONIA_URL,
      WEB_SITE;
   }
}
