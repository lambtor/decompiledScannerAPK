package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.view.Display;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import java.lang.reflect.Field;

class ViewCompatBase {
   private static final String TAG = "ViewCompatBase";
   private static Field sMinHeightField;
   private static boolean sMinHeightFieldFetched;
   private static Field sMinWidthField;
   private static boolean sMinWidthFieldFetched;

   static ColorStateList getBackgroundTintList(View var0) {
      ColorStateList var1;
      if (var0 instanceof TintableBackgroundView) {
         var1 = ((TintableBackgroundView)var0).getSupportBackgroundTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   static Mode getBackgroundTintMode(View var0) {
      Mode var1;
      if (var0 instanceof TintableBackgroundView) {
         var1 = ((TintableBackgroundView)var0).getSupportBackgroundTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   static Display getDisplay(View var0) {
      Display var1;
      if (isAttachedToWindow(var0)) {
         var1 = ((WindowManager)var0.getContext().getSystemService("window")).getDefaultDisplay();
      } else {
         var1 = null;
      }

      return var1;
   }

   static int getMinimumHeight(View var0) {
      if (!sMinHeightFieldFetched) {
         try {
            sMinHeightField = View.class.getDeclaredField("mMinHeight");
            sMinHeightField.setAccessible(true);
         } catch (NoSuchFieldException var3) {
            ;
         }

         sMinHeightFieldFetched = true;
      }

      int var1;
      if (sMinHeightField != null) {
         try {
            var1 = ((Integer)sMinHeightField.get(var0)).intValue();
            return var1;
         } catch (Exception var4) {
            ;
         }
      }

      var1 = 0;
      return var1;
   }

   static int getMinimumWidth(View var0) {
      if (!sMinWidthFieldFetched) {
         try {
            sMinWidthField = View.class.getDeclaredField("mMinWidth");
            sMinWidthField.setAccessible(true);
         } catch (NoSuchFieldException var3) {
            ;
         }

         sMinWidthFieldFetched = true;
      }

      int var1;
      if (sMinWidthField != null) {
         try {
            var1 = ((Integer)sMinWidthField.get(var0)).intValue();
            return var1;
         } catch (Exception var4) {
            ;
         }
      }

      var1 = 0;
      return var1;
   }

   static boolean isAttachedToWindow(View var0) {
      boolean var1;
      if (var0.getWindowToken() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   static boolean isLaidOut(View var0) {
      boolean var1;
      if (var0.getWidth() > 0 && var0.getHeight() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   static void offsetLeftAndRight(View var0, int var1) {
      int var2 = var0.getLeft();
      var0.offsetLeftAndRight(var1);
      if (var1 != 0) {
         ViewParent var3 = var0.getParent();
         if (var3 instanceof View) {
            var1 = Math.abs(var1);
            ((View)var3).invalidate(var2 - var1, var0.getTop(), var0.getWidth() + var2 + var1, var0.getBottom());
         } else {
            var0.invalidate();
         }
      }

   }

   static void offsetTopAndBottom(View var0, int var1) {
      int var2 = var0.getTop();
      var0.offsetTopAndBottom(var1);
      if (var1 != 0) {
         ViewParent var3 = var0.getParent();
         if (var3 instanceof View) {
            var1 = Math.abs(var1);
            ((View)var3).invalidate(var0.getLeft(), var2 - var1, var0.getRight(), var0.getHeight() + var2 + var1);
         } else {
            var0.invalidate();
         }
      }

   }

   static void setBackgroundTintList(View var0, ColorStateList var1) {
      if (var0 instanceof TintableBackgroundView) {
         ((TintableBackgroundView)var0).setSupportBackgroundTintList(var1);
      }

   }

   static void setBackgroundTintMode(View var0, Mode var1) {
      if (var0 instanceof TintableBackgroundView) {
         ((TintableBackgroundView)var0).setSupportBackgroundTintMode(var1);
      }

   }
}
