package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.util.Log;

public final class NavUtils {
   private static final NavUtils.NavUtilsImpl IMPL;
   public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
   private static final String TAG = "NavUtils";

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new NavUtils.NavUtilsImplJB();
      } else {
         IMPL = new NavUtils.NavUtilsImplBase();
      }

   }

   public static Intent getParentActivityIntent(Activity var0) {
      return IMPL.getParentActivityIntent(var0);
   }

   public static Intent getParentActivityIntent(Context var0, ComponentName var1) throws NameNotFoundException {
      String var2 = getParentActivityName(var0, var1);
      Intent var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var1 = new ComponentName(var1.getPackageName(), var2);
         if (getParentActivityName(var0, var1) == null) {
            var3 = IntentCompat.makeMainActivity(var1);
         } else {
            var3 = (new Intent()).setComponent(var1);
         }
      }

      return var3;
   }

   public static Intent getParentActivityIntent(Context var0, Class var1) throws NameNotFoundException {
      String var3 = getParentActivityName(var0, new ComponentName(var0, var1));
      Intent var2;
      if (var3 == null) {
         var2 = null;
      } else {
         ComponentName var4 = new ComponentName(var0, var3);
         if (getParentActivityName(var0, var4) == null) {
            var2 = IntentCompat.makeMainActivity(var4);
         } else {
            var2 = (new Intent()).setComponent(var4);
         }
      }

      return var2;
   }

   @Nullable
   public static String getParentActivityName(Activity var0) {
      try {
         String var2 = getParentActivityName(var0, var0.getComponentName());
         return var2;
      } catch (NameNotFoundException var1) {
         throw new IllegalArgumentException(var1);
      }
   }

   @Nullable
   public static String getParentActivityName(Context var0, ComponentName var1) throws NameNotFoundException {
      ActivityInfo var2 = var0.getPackageManager().getActivityInfo(var1, 128);
      return IMPL.getParentActivityName(var0, var2);
   }

   public static void navigateUpFromSameTask(Activity var0) {
      Intent var1 = getParentActivityIntent(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Activity " + var0.getClass().getSimpleName() + " does not have a parent activity name specified." + " (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> " + " element in your manifest?)");
      } else {
         navigateUpTo(var0, var1);
      }
   }

   public static void navigateUpTo(Activity var0, Intent var1) {
      IMPL.navigateUpTo(var0, var1);
   }

   public static boolean shouldUpRecreateTask(Activity var0, Intent var1) {
      return IMPL.shouldUpRecreateTask(var0, var1);
   }

   interface NavUtilsImpl {
      Intent getParentActivityIntent(Activity var1);

      String getParentActivityName(Context var1, ActivityInfo var2);

      void navigateUpTo(Activity var1, Intent var2);

      boolean shouldUpRecreateTask(Activity var1, Intent var2);
   }

   static class NavUtilsImplBase implements NavUtils.NavUtilsImpl {
      public Intent getParentActivityIntent(Activity var1) {
         Object var2 = null;
         String var3 = NavUtils.getParentActivityName(var1);
         Intent var6;
         if (var3 == null) {
            var6 = (Intent)var2;
         } else {
            ComponentName var4 = new ComponentName(var1, var3);

            try {
               if (NavUtils.getParentActivityName(var1, var4) == null) {
                  var6 = IntentCompat.makeMainActivity(var4);
               } else {
                  var6 = new Intent();
                  var6 = var6.setComponent(var4);
               }
            } catch (NameNotFoundException var5) {
               Log.e("NavUtils", "getParentActivityIntent: bad parentActivityName '" + var3 + "' in manifest");
               var6 = (Intent)var2;
            }
         }

         return var6;
      }

      public String getParentActivityName(Context var1, ActivityInfo var2) {
         String var4;
         if (var2.metaData == null) {
            var4 = null;
         } else {
            String var3 = var2.metaData.getString("android.support.PARENT_ACTIVITY");
            if (var3 == null) {
               var4 = null;
            } else {
               var4 = var3;
               if (var3.charAt(0) == '.') {
                  var4 = var1.getPackageName() + var3;
               }
            }
         }

         return var4;
      }

      public void navigateUpTo(Activity var1, Intent var2) {
         var2.addFlags(67108864);
         var1.startActivity(var2);
         var1.finish();
      }

      public boolean shouldUpRecreateTask(Activity var1, Intent var2) {
         String var4 = var1.getIntent().getAction();
         boolean var3;
         if (var4 != null && !var4.equals("android.intent.action.MAIN")) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   static class NavUtilsImplJB extends NavUtils.NavUtilsImplBase {
      public Intent getParentActivityIntent(Activity var1) {
         Intent var3 = NavUtilsJB.getParentActivityIntent(var1);
         Intent var2 = var3;
         if (var3 == null) {
            var2 = this.superGetParentActivityIntent(var1);
         }

         return var2;
      }

      public String getParentActivityName(Context var1, ActivityInfo var2) {
         String var4 = NavUtilsJB.getParentActivityName(var2);
         String var3 = var4;
         if (var4 == null) {
            var3 = super.getParentActivityName(var1, var2);
         }

         return var3;
      }

      public void navigateUpTo(Activity var1, Intent var2) {
         NavUtilsJB.navigateUpTo(var1, var2);
      }

      public boolean shouldUpRecreateTask(Activity var1, Intent var2) {
         return NavUtilsJB.shouldUpRecreateTask(var1, var2);
      }

      Intent superGetParentActivityIntent(Activity var1) {
         return super.getParentActivityIntent(var1);
      }
   }
}
