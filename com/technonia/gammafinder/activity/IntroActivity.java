package com.technonia.gammafinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.technonia.gammafinder.Preferences;

public class IntroActivity extends Activity {
   private static final int INTRO_DELAY = 1000;
   private static final int PERMISSIONS_REQUEST_CODE = 1;
   private static final String TAG = "IntroActivity";
   private Handler introHandler;
   private Runnable runActivity = new Runnable() {
      public void run() {
         IntroActivity var2 = IntroActivity.this;
         Class var1;
         if (Preferences.instance().getBoolean("IS_INIT_USER")) {
            var1 = UserGuideActivity.class;
         } else {
            var1 = MainActivity.class;
         }

         Intent var3 = new Intent(var2, var1);
         IntroActivity.this.startActivity(var3);
         IntroActivity.this.finish();
         IntroActivity.this.overridePendingTransition(17432576, 17432577);
      }
   };

   private void checkPermission() {
      if (VERSION.SDK_INT >= 23) {
         if (this.checkSelfPermission("android.permission.RECORD_AUDIO") == 0 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            this.introHandler.postDelayed(this.runActivity, 1000L);
         } else {
            this.requestPermissions(new String[]{"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
         }
      }

   }

   public void onBackPressed() {
      super.onBackPressed();
      Log.d("IntroActivity", "onBackPressed()");
      this.introHandler.removeCallbacks(this.runActivity);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903068);
      Preferences.instance().setContext(this);
      this.introHandler = new Handler();
      if (VERSION.SDK_INT >= 23) {
         this.checkPermission();
      } else {
         this.introHandler.postDelayed(this.runActivity, 1000L);
      }

   }

   protected void onDestroy() {
      Log.d("IntroActivity", "onDestroy()");
      super.onDestroy();
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      if (var1 == 1) {
         var1 = 0;

         while(true) {
            if (var1 >= var3.length) {
               this.introHandler.postDelayed(this.runActivity, 1000L);
               break;
            }

            if (var3[var1] != 0) {
               Log.i("IntroActivity", var2[var1] + " is PERMISSION Deny");
               Toast.makeText(this, this.getString(2131099736), 0).show();
               this.finish();
               break;
            }

            Log.i("IntroActivity", var2[var1] + " is PERMISSION_GRANTED");
            ++var1;
         }
      }

   }

   protected void onStart() {
      Log.d("IntroActivity", "onStart()");
      super.onStart();
   }
}
