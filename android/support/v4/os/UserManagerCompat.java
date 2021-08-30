package android.support.v4.os;

import android.content.Context;

public class UserManagerCompat {
   @Deprecated
   public static boolean isUserRunningAndLocked(Context var0) {
      boolean var1;
      if (!isUserUnlocked(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Deprecated
   public static boolean isUserRunningAndUnlocked(Context var0) {
      return isUserUnlocked(var0);
   }

   public static boolean isUserUnlocked(Context var0) {
      boolean var1;
      if (BuildCompat.isAtLeastN()) {
         var1 = UserManagerCompatApi24.isUserUnlocked(var0);
      } else {
         var1 = true;
      }

      return var1;
   }
}
