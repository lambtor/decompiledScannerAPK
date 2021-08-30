package com.technonia.gammafinder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import com.technonia.gammafinder.CustomMemoDialog;
import com.technonia.gammafinder.Preferences;
import com.technonia.gammafinder.ProgressWheel;
import com.technonia.gammafinder.adapter.GeigerDBAdapter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kr.ftlab.lib.DataInfo;
import kr.ftlab.lib.SmartSensor;
import kr.ftlab.lib.SmartSensorEventListener;

public class MainActivity extends BaseActivity implements SmartSensorEventListener {
   private static final int BACK_KEY = 4;
   private static final int COMPLETED_CALIBRATION = 3;
   private static final int GONE_LAYOUT = 5;
   private static final int Geiger = 1;
   private static final int START_MEASUREMENT = 2;
   private static final String TAG = "MainActivity";
   private static boolean back_close_flag = false;
   private static boolean isShowToastMsg = false;
   private Boolean IsMemo;
   private Dialog alertNoticeDialog;
   private Animation anim = null;
   private Button btnStart;
   private int count = 0;
   private MainActivity.EventHandler eventHandler = new MainActivity.EventHandler(this);
   private float f_Sivert = 0.0F;
   private boolean isAutoCalibration = false;
   private boolean isInitStart = true;
   private boolean isStartStatus = false;
   private ImageView ivLogo;
   private BroadcastReceiver mHeadSetConnectReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         if (var2.getAction().equalsIgnoreCase("android.intent.action.HEADSET_PLUG") && var2.hasExtra("state")) {
            Log.i("MainActivity", "BroadcastReceiver onReceive");
            if (var2.getIntExtra("state", 0) == 0) {
               BaseActivity.isSensorConnected = false;
               MainActivity.this.stopProcess();
               if (!Preferences.instance().getBoolean("IS_SKIP_NOTICE")) {
                  MainActivity.this.alertNoticeDialog.show();
               }

               MainActivity.isShowToastMsg = false;
               MainActivity.this.btnStart.setEnabled(false);
               Toast.makeText(MainActivity.this, 2131099671, 0).show();
            } else if (var2.getIntExtra("state", 0) == 1) {
               BaseActivity.isSensorConnected = true;
               if (Preferences.instance().getBoolean("IS_INIT_AUTO_CALIBRATION")) {
                  MainActivity.this.runAutoCalibration();
               } else {
                  if (!MainActivity.isShowToastMsg) {
                     Toast.makeText(MainActivity.this, 2131099672, 0).show();
                     MainActivity.isShowToastMsg = true;
                  }

                  MainActivity.this.btnStart.setEnabled(true);
                  if (BaseActivity.isClickCalibrationButton) {
                     MainActivity.this.stopProcess();
                     (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                           MainActivity.this.runAutoCalibration();
                        }
                     }, 200L);
                     BaseActivity.isClickCalibrationButton = false;
                  }
               }
            }
         }

      }
   };
   private SmartSensor mMI = null;
   private GeigerDBAdapter mdb;
   private LinearLayout over_LinearLayout;
   private int pre_count = 0;
   private ProgressDialog progress;
   private ProgressWheel progressWheel;
   private Runnable run_InitStart = new Runnable() {
      public void run() {
         if (MainActivity.this.progress.isShowing()) {
            MainActivity.this.progress.dismiss();
            MainActivity.this.tvStartMsg.setVisibility(0);
            MainActivity.this.mMI.start();
            MainActivity.this.startTime = System.currentTimeMillis();
            MainActivity.this.ivLogo.startAnimation(MainActivity.this.anim);
            Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(2131099719), 0).show();
         }

      }
   };
   private long startTime = 0L;
   private String strCPM;
   private String strMeasureTime;
   private String strMemo;
   private String strReverseMeasureTime;
   private String strTempMeasureTime;
   private String strValue;
   private TextView tvCPM;
   private TextView tvCount;
   private TextView tvDebugModeVmin;
   private TextView tvStartMsg;
   private TextView tvTimer;

   private void DialogCall() {
      final CustomMemoDialog var1 = new CustomMemoDialog(this);
      var1.setOnShowListener(new OnShowListener() {
         public void onShow(DialogInterface var1) {
            MainActivity.this.strTempMeasureTime = MainActivity.this.strMeasureTime;
            Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.strTempMeasureTime, 0).show();
         }
      });
      var1.setOnDismissListener(new OnDismissListener() {
         public void onDismiss(DialogInterface var1x) {
            MainActivity.this.IsMemo = var1.getStored();
            if (MainActivity.this.IsMemo.booleanValue()) {
               MainActivity.this.strMemo = var1.getMemoStr();
               String var2;
               if (!MainActivity.this.systemLanguage.equals("es") && !MainActivity.this.systemLanguage.equals("de") && !MainActivity.this.systemLanguage.equals("en") && !MainActivity.this.systemLanguage.equals("ru")) {
                  var2 = (new SimpleDateFormat("yyyy. MM. dd\nHH:mm:ss", Locale.getDefault())).format(new Date(System.currentTimeMillis()));
               } else {
                  var2 = (new SimpleDateFormat("dd. MM. yyyy\nHH:mm:ss", Locale.getDefault())).format(new Date(System.currentTimeMillis()));
               }

               MainActivity.this.mdb.insertDataInDB(var2, (String)null, (String)null, MainActivity.this.strCPM, String.valueOf(MainActivity.this.count), MainActivity.this.strValue, MainActivity.this.strTempMeasureTime, MainActivity.this.strMemo);
               Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(2131099717), 0).show();
               MainActivity.this.strMemo = "";
            }

         }
      });
      var1.show();
   }

   private void handleMessage(Message var1) {
      switch(var1.what) {
      case 2:
         if (!this.isAutoCalibration) {
            this.serviceLoop();
         }
         break;
      case 3:
         this.isStartStatus = false;
         this.isAutoCalibration = false;
         this.btnStart.setEnabled(true);
         if (!Preferences.instance().getBoolean("IS_SKIP_NOTICE") && Preferences.instance().getBoolean("IS_INIT_AUTO_CALIBRATION")) {
            this.alertNoticeDialog.show();
         }

         if (Preferences.instance().getBoolean("IS_INIT_AUTO_CALIBRATION")) {
            Preferences.instance().putBoolean("IS_INIT_AUTO_CALIBRATION", false);
         }
         break;
      case 4:
         back_close_flag = false;
         break;
      case 5:
         this.over_LinearLayout.setVisibility(8);
         break;
      case 100:
         this.eventHandler.sendEmptyMessageDelayed(100, 1000L);
      }

   }

   private void runAutoCalibration() {
      Log.d("MainActivity", "runAutoCalibration");
      this.isAutoCalibration = true;
      this.btnStart.setEnabled(false);
      this.mMI.registerSelfConfiguration();
   }

   private void screenShot(Bitmap param1) {
      // $FF: Couldn't be decompiled
   }

   private void serviceLoop() {
      DataInfo var1 = this.mMI.getDataInfo();
      Log.i("MainActivity", "Count : " + var1.GE_Count);
      this.count = var1.GE_Count;
      this.strCPM = String.format(Locale.getDefault(), "%3.1f", var1.GE_CPM);
      this.f_Sivert = var1.GE_uSv;
      this.tvCount.setText(String.valueOf(this.count));
      this.tvCPM.setText(this.strCPM);
      if (this.f_Sivert > 99.0F) {
         this.strValue = String.format(Locale.getDefault(), "%3.1f", this.f_Sivert);
      } else {
         this.strValue = String.format(Locale.getDefault(), "%3.2f", this.f_Sivert);
      }

      this.progressWheel.incrementProgress(this.f_Sivert);
      this.progressWheel.setText(this.strValue);
      Log.i("MainActivity", String.format(Locale.getDefault(), "Sivert : %3.2f\nCPM : %3.1f\nCount : %d", var1.GE_uSv, var1.GE_CPM, var1.GE_Count));
      this.setTimerText();
      if (this.count != this.pre_count) {
         this.pre_count = this.count;
         this.over_LinearLayout.setVisibility(0);
         this.eventHandler.sendEmptyMessageDelayed(5, 300L);
      }

   }

   private void setTimerText() {
      long var4 = (System.currentTimeMillis() - this.startTime) / 1000L;
      Log.i("MainActivity", "Measured Time : " + var4 + " sec");
      int var3 = Preferences.instance().getInt("COUNTDOWN_MIN") * 60;
      int var2 = (int)((long)var3 - var4);
      int var1 = (int)((long)var3 - var4) / 60;
      var3 = (int)((long)var3 - var4) / 3600;
      this.strReverseMeasureTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", var3 % 24, var1 % 60, var2 % 60);
      var3 = (int)var4;
      var1 = (int)var4 / 60;
      var2 = (int)var4 / 3600;
      this.strMeasureTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", var2 % 24, var1 % 60, var3 % 60);
      if (!Preferences.instance().getBoolean("COUNTDOWN_MODE")) {
         this.tvTimer.setText(this.strMeasureTime);
      } else {
         this.tvTimer.setText(this.strReverseMeasureTime);
         if (this.strReverseMeasureTime.equals("00:00:00")) {
            this.stopProcess();
         }
      }

   }

   private void startProcess() {
      if (this.isStartStatus) {
         this.stopProcess();
      } else if (isSensorConnected) {
         this.isStartStatus = true;
         this.btnStart.setBackgroundResource(2130837592);
         this.mMI.reset();
         this.anim.setFillEnabled(true);
         this.anim.setFillAfter(true);
         this.anim.setRepeatMode(1);
         this.anim.setRepeatCount(-1);
         if (this.isInitStart) {
            this.progress.setTitle(this.getString(2131099677));
            this.progress.setMessage(this.getString(2131099714));
            this.progress.setProgressStyle(0);
            this.progress.setCancelable(false);
            this.progress.show();
            this.isInitStart = false;
            (new Handler()).postDelayed(this.run_InitStart, 3000L);
         } else {
            this.tvStartMsg.setVisibility(0);
            this.ivLogo.startAnimation(this.anim);
            this.mMI.start();
            this.startTime = System.currentTimeMillis();
         }
      }

   }

   private void stopProcess() {
      Log.d("MainActivity", "stopProcess");
      this.isStartStatus = false;
      this.anim.cancel();
      this.btnStart.setBackgroundResource(2130837591);
      this.over_LinearLayout.setVisibility(8);
      this.tvStartMsg.setVisibility(4);
      this.count = 0;
      this.pre_count = 0;
      this.mMI.stop();
   }

   protected void onCreate(Bundle var1) {
      Log.d("MainActivity", "onCreate");
      this.setCurActivity(BaseActivity.Activity.MAIN.ordinal());
      super.onCreate(var1);
      Window var3 = this.getWindow();
      this.mMI = new SmartSensor(this, this);
      this.mMI.selectDevice(1);
      this.tvTimer = (TextView)this.findViewById(2131427438);
      this.tvCount = (TextView)this.findViewById(2131427439);
      this.tvCPM = (TextView)this.findViewById(2131427437);
      this.btnStart = (Button)this.findViewById(2131427441);
      this.ivLogo = (ImageView)this.findViewById(2131427444);
      this.tvDebugModeVmin = (TextView)this.findViewById(2131427432);
      this.tvStartMsg = (TextView)this.findViewById(2131427443);
      this.tvStartMsg.setVisibility(4);
      this.anim = AnimationUtils.loadAnimation(this, 2130968586);
      LayoutInflater var4 = LayoutInflater.from(this);
      this.over_LinearLayout = (LinearLayout)var4.inflate(2130903092, (ViewGroup)null);
      LayoutParams var2 = new LayoutParams(-1, -1);
      var3.addContentView(this.over_LinearLayout, var2);
      this.over_LinearLayout.setVisibility(8);
      this.mdb = new GeigerDBAdapter(this);
      this.progress = new ProgressDialog(this);
      Builder var6 = new Builder(this);
      View var7 = var4.inflate(2130903071, (ViewGroup)null);
      final CheckBox var5 = (CheckBox)var7.findViewById(2131427391);
      var6.setTitle(this.getString(2131099677));
      var6.setView(var7);
      var6.setPositiveButton(this.getString(2131099713), new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            Preferences.instance().putBoolean("IS_SKIP_NOTICE", var5.isChecked());
         }
      });
      this.alertNoticeDialog = var6.create();
      if (!Preferences.instance().getBoolean("IS_SKIP_NOTICE") && !Preferences.instance().getBoolean("IS_INIT_AUTO_CALIBRATION")) {
         this.alertNoticeDialog.show();
      }

      this.progressWheel = (ProgressWheel)this.findViewById(2131427430);
      this.progressWheel.setTextUnit(this.getString(2131099722));
      this.progressWheel.setText("0.10");
      this.btnStart.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            MainActivity.this.startProcess();
         }
      });
      ((RelativeLayout)this.findViewById(2131427431)).setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            if (var2.getAction() == 0) {
               Builder var3 = new Builder(MainActivity.this);
               var3.setTitle(2131099675);
               var3.setMessage(2131099673);
               var3.setPositiveButton(2131099713, new OnClickListener() {
                  public void onClick(DialogInterface var1, int var2) {
                     var1.dismiss();
                  }
               });
               var3.show();
            }

            return false;
         }
      });
      if (Preferences.instance().getBoolean("IS_INIT_USER")) {
         Toast.makeText(this, 2131099671, 0).show();
      }

   }

   public boolean onCreateOptionsMenu(Menu var1) {
      MenuItem var2 = var1.add(0, 2131427330, 0, "");
      MenuItem var3 = var1.add(0, 2131427331, 0, "");
      var3.setIcon(2130837611);
      var3.setShowAsAction(1);
      var2.setIcon(2130837607);
      var2.setShowAsAction(1);
      return true;
   }

   protected void onDestroy() {
      Log.d("MainActivity", "onDestroy");
      this.unregisterReceiver(this.mHeadSetConnectReceiver);
      if (this.isStartStatus) {
         this.stopProcess();
      }

      System.gc();
      super.onDestroy();
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3 = false;
      if (var2.getKeyCode() == 4) {
         if (!back_close_flag) {
            Toast.makeText(this, 2131099682, 0).show();
            back_close_flag = true;
            this.eventHandler.sendEmptyMessageDelayed(4, 2000L);
            return var3;
         }

         this.finish();
         System.exit(0);
      }

      var3 = super.onKeyUp(var1, var2);
      return var3;
   }

   public void onMeasured() {
      Log.i("MainActivity", "onMeasured()");
      this.eventHandler.sendEmptyMessage(2);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      View var2 = this.getWindow().getDecorView();
      switch(var1.getItemId()) {
      case 2131427330:
         try {
            var2.setDrawingCacheEnabled(true);
            this.screenShot(var2.getDrawingCache());
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         Toast.makeText(this, this.getString(2131099687), 0).show();
         break;
      case 2131427331:
         if (this.mDrawerLayout.isDrawerOpen(8388613)) {
            this.mDrawerLayout.closeDrawer(8388613);
         } else {
            this.mDrawerLayout.openDrawer(8388613);
         }
      }

      return super.onOptionsItemSelected(var1);
   }

   protected void onPause() {
      Log.d("MainActivity", "onPause()");
      super.onPause();
      this.progressWheel.stopSpinning();
   }

   protected void onRestart() {
      this.home_close_flag = true;
      Log.d("MainActivity", "onRestart()");
      super.onRestart();
   }

   protected void onResume() {
      Log.d("MainActivity", "onResume");
      super.onResume();
      IntentFilter var1 = new IntentFilter();
      var1.addAction("android.intent.action.HEADSET_PLUG");
      this.registerReceiver(this.mHeadSetConnectReceiver, var1);
      if (0.0F == this.f_Sivert) {
         this.progressWheel.incrementProgress(0.1F);
      }

   }

   public void onSelfConfigurated() {
      Log.i("MainActivity", "onSelfConfigurated()");
      this.eventHandler.sendEmptyMessage(3);
   }

   protected void onStart() {
      Log.d("MainActivity", "onStart()");
      super.onStart();
   }

   protected void onStop() {
      Log.d("MainActivity", "onStop()");
      super.onStop();
   }

   protected void onUserLeaveHint() {
      Log.d("MainActivity", "Home key System.exit");
      if (this.home_close_flag) {
         Toast.makeText(this, 2131099678, 0).show();
         this.finish();
         System.exit(0);
      }

      super.onUserLeaveHint();
   }

   public void selectedMenu(int var1) {
      super.selectedMenu(var1);
      if (var1 == BaseActivity.MENU.SAVE_DATA.ordinal()) {
         this.mdb.open();
         if (this.strMeasureTime != null) {
            this.DialogCall();
         } else {
            Toast.makeText(this.getApplicationContext(), this.getString(2131099708), 0).show();
         }
      }

   }

   private static class EventHandler extends Handler {
      WeakReference weakReference;

      EventHandler(MainActivity var1) {
         this.weakReference = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         MainActivity var2 = (MainActivity)this.weakReference.get();
         if (var2 != null) {
            var2.handleMessage(var1);
         }

      }
   }
}
