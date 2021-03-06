package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.RestrictTo;
import android.support.v4.os.BuildCompat;

public final class PointerIconCompat {
   static final PointerIconCompat.PointerIconCompatImpl IMPL;
   public static final int TYPE_ALIAS = 1010;
   public static final int TYPE_ALL_SCROLL = 1013;
   public static final int TYPE_ARROW = 1000;
   public static final int TYPE_CELL = 1006;
   public static final int TYPE_CONTEXT_MENU = 1001;
   public static final int TYPE_COPY = 1011;
   public static final int TYPE_CROSSHAIR = 1007;
   public static final int TYPE_DEFAULT = 1000;
   public static final int TYPE_GRAB = 1020;
   public static final int TYPE_GRABBING = 1021;
   public static final int TYPE_HAND = 1002;
   public static final int TYPE_HELP = 1003;
   public static final int TYPE_HORIZONTAL_DOUBLE_ARROW = 1014;
   public static final int TYPE_NO_DROP = 1012;
   public static final int TYPE_NULL = 0;
   public static final int TYPE_TEXT = 1008;
   public static final int TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW = 1017;
   public static final int TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = 1016;
   public static final int TYPE_VERTICAL_DOUBLE_ARROW = 1015;
   public static final int TYPE_VERTICAL_TEXT = 1009;
   public static final int TYPE_WAIT = 1004;
   public static final int TYPE_ZOOM_IN = 1018;
   public static final int TYPE_ZOOM_OUT = 1019;
   private Object mPointerIcon;

   static {
      if (BuildCompat.isAtLeastN()) {
         IMPL = new PointerIconCompat.Api24PointerIconCompatImpl();
      } else {
         IMPL = new PointerIconCompat.BasePointerIconCompatImpl();
      }

   }

   private PointerIconCompat(Object var1) {
      this.mPointerIcon = var1;
   }

   public static PointerIconCompat create(Bitmap var0, float var1, float var2) {
      return new PointerIconCompat(IMPL.create(var0, var1, var2));
   }

   public static PointerIconCompat getSystemIcon(Context var0, int var1) {
      return new PointerIconCompat(IMPL.getSystemIcon(var0, var1));
   }

   public static PointerIconCompat load(Resources var0, int var1) {
      return new PointerIconCompat(IMPL.load(var0, var1));
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public Object getPointerIcon() {
      return this.mPointerIcon;
   }

   static class Api24PointerIconCompatImpl extends PointerIconCompat.BasePointerIconCompatImpl {
      public Object create(Bitmap var1, float var2, float var3) {
         return PointerIconCompatApi24.create(var1, var2, var3);
      }

      public Object getSystemIcon(Context var1, int var2) {
         return PointerIconCompatApi24.getSystemIcon(var1, var2);
      }

      public Object load(Resources var1, int var2) {
         return PointerIconCompatApi24.load(var1, var2);
      }
   }

   static class BasePointerIconCompatImpl implements PointerIconCompat.PointerIconCompatImpl {
      public Object create(Bitmap var1, float var2, float var3) {
         return null;
      }

      public Object getSystemIcon(Context var1, int var2) {
         return null;
      }

      public Object load(Resources var1, int var2) {
         return null;
      }
   }

   interface PointerIconCompatImpl {
      Object create(Bitmap var1, float var2, float var3);

      Object getSystemIcon(Context var1, int var2);

      Object load(Resources var1, int var2);
   }
}
