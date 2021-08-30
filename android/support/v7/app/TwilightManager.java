package android.support.v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
   private static final int SUNRISE = 6;
   private static final int SUNSET = 22;
   private static final String TAG = "TwilightManager";
   private static TwilightManager sInstance;
   private final Context mContext;
   private final LocationManager mLocationManager;
   private final TwilightManager.TwilightState mTwilightState = new TwilightManager.TwilightState();

   @VisibleForTesting
   TwilightManager(@NonNull Context var1, @NonNull LocationManager var2) {
      this.mContext = var1;
      this.mLocationManager = var2;
   }

   static TwilightManager getInstance(@NonNull Context var0) {
      if (sInstance == null) {
         var0 = var0.getApplicationContext();
         sInstance = new TwilightManager(var0, (LocationManager)var0.getSystemService("location"));
      }

      return sInstance;
   }

   private Location getLastKnownLocation() {
      Location var1 = null;
      Location var2 = null;
      if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
         var1 = this.getLastKnownLocationForProvider("network");
      }

      if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
         var2 = this.getLastKnownLocationForProvider("gps");
      }

      Location var3;
      if (var2 != null && var1 != null) {
         if (var2.getTime() > var1.getTime()) {
            var3 = var2;
         } else {
            var3 = var1;
         }
      } else {
         var3 = var2;
         if (var2 == null) {
            var3 = var1;
         }
      }

      return var3;
   }

   private Location getLastKnownLocationForProvider(String var1) {
      Location var3;
      if (this.mLocationManager != null) {
         try {
            if (this.mLocationManager.isProviderEnabled(var1)) {
               var3 = this.mLocationManager.getLastKnownLocation(var1);
               return var3;
            }
         } catch (Exception var2) {
            Log.d("TwilightManager", "Failed to get last known location", var2);
         }
      }

      var3 = null;
      return var3;
   }

   private boolean isStateValid() {
      boolean var1;
      if (this.mTwilightState != null && this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @VisibleForTesting
   static void setInstance(TwilightManager var0) {
      sInstance = var0;
   }

   private void updateState(@NonNull Location var1) {
      TwilightManager.TwilightState var13 = this.mTwilightState;
      long var3 = System.currentTimeMillis();
      TwilightCalculator var14 = TwilightCalculator.getInstance();
      var14.calculateTwilight(var3 - 86400000L, var1.getLatitude(), var1.getLongitude());
      long var9 = var14.sunset;
      var14.calculateTwilight(var3, var1.getLatitude(), var1.getLongitude());
      boolean var2;
      if (var14.state == 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      long var5 = var14.sunrise;
      long var7 = var14.sunset;
      var14.calculateTwilight(86400000L + var3, var1.getLatitude(), var1.getLongitude());
      long var11 = var14.sunrise;
      if (var5 != -1L && var7 != -1L) {
         if (var3 > var7) {
            var3 = 0L + var11;
         } else if (var3 > var5) {
            var3 = 0L + var7;
         } else {
            var3 = 0L + var5;
         }

         var3 += 60000L;
      } else {
         var3 += 43200000L;
      }

      var13.isNight = var2;
      var13.yesterdaySunset = var9;
      var13.todaySunrise = var5;
      var13.todaySunset = var7;
      var13.tomorrowSunrise = var11;
      var13.nextUpdate = var3;
   }

   boolean isNight() {
      TwilightManager.TwilightState var3 = this.mTwilightState;
      boolean var2;
      if (this.isStateValid()) {
         var2 = var3.isNight;
      } else {
         Location var4 = this.getLastKnownLocation();
         if (var4 != null) {
            this.updateState(var4);
            var2 = var3.isNight;
         } else {
            Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
            int var1 = Calendar.getInstance().get(11);
            if (var1 >= 6 && var1 < 22) {
               var2 = false;
            } else {
               var2 = true;
            }
         }
      }

      return var2;
   }

   private static class TwilightState {
      boolean isNight;
      long nextUpdate;
      long todaySunrise;
      long todaySunset;
      long tomorrowSunrise;
      long yesterdaySunset;
   }
}
