package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;

public final class AccessibilityEventCompat {
   public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
   public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
   public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
   public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
   private static final AccessibilityEventCompat.AccessibilityEventVersionImpl IMPL;
   public static final int TYPES_ALL_MASK = -1;
   public static final int TYPE_ANNOUNCEMENT = 16384;
   public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
   public static final int TYPE_GESTURE_DETECTION_END = 524288;
   public static final int TYPE_GESTURE_DETECTION_START = 262144;
   public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
   public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
   public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
   public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
   public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
   public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
   public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
   public static final int TYPE_VIEW_HOVER_ENTER = 128;
   public static final int TYPE_VIEW_HOVER_EXIT = 256;
   public static final int TYPE_VIEW_SCROLLED = 4096;
   public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
   public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
   public static final int TYPE_WINDOWS_CHANGED = 4194304;
   public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new AccessibilityEventCompat.AccessibilityEventKitKatImpl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityEventCompat.AccessibilityEventJellyBeanImpl();
      } else if (VERSION.SDK_INT >= 14) {
         IMPL = new AccessibilityEventCompat.AccessibilityEventIcsImpl();
      } else {
         IMPL = new AccessibilityEventCompat.AccessibilityEventStubImpl();
      }

   }

   public static void appendRecord(AccessibilityEvent var0, AccessibilityRecordCompat var1) {
      IMPL.appendRecord(var0, var1.getImpl());
   }

   public static AccessibilityRecordCompat asRecord(AccessibilityEvent var0) {
      return new AccessibilityRecordCompat(var0);
   }

   public static int getContentChangeTypes(AccessibilityEvent var0) {
      return IMPL.getContentChangeTypes(var0);
   }

   public static AccessibilityRecordCompat getRecord(AccessibilityEvent var0, int var1) {
      return new AccessibilityRecordCompat(IMPL.getRecord(var0, var1));
   }

   public static int getRecordCount(AccessibilityEvent var0) {
      return IMPL.getRecordCount(var0);
   }

   public static void setContentChangeTypes(AccessibilityEvent var0, int var1) {
      IMPL.setContentChangeTypes(var0, var1);
   }

   public int getAction(AccessibilityEvent var1) {
      return IMPL.getAction(var1);
   }

   public int getMovementGranularity(AccessibilityEvent var1) {
      return IMPL.getMovementGranularity(var1);
   }

   public void setAction(AccessibilityEvent var1, int var2) {
      IMPL.setAction(var1, var2);
   }

   public void setMovementGranularity(AccessibilityEvent var1, int var2) {
      IMPL.setMovementGranularity(var1, var2);
   }

   static class AccessibilityEventIcsImpl extends AccessibilityEventCompat.AccessibilityEventStubImpl {
      public void appendRecord(AccessibilityEvent var1, Object var2) {
         AccessibilityEventCompatIcs.appendRecord(var1, var2);
      }

      public Object getRecord(AccessibilityEvent var1, int var2) {
         return AccessibilityEventCompatIcs.getRecord(var1, var2);
      }

      public int getRecordCount(AccessibilityEvent var1) {
         return AccessibilityEventCompatIcs.getRecordCount(var1);
      }
   }

   static class AccessibilityEventJellyBeanImpl extends AccessibilityEventCompat.AccessibilityEventIcsImpl {
      public int getAction(AccessibilityEvent var1) {
         return AccessibilityEventCompatJellyBean.getAction(var1);
      }

      public int getMovementGranularity(AccessibilityEvent var1) {
         return AccessibilityEventCompatJellyBean.getMovementGranularity(var1);
      }

      public void setAction(AccessibilityEvent var1, int var2) {
         AccessibilityEventCompatJellyBean.setAction(var1, var2);
      }

      public void setMovementGranularity(AccessibilityEvent var1, int var2) {
         AccessibilityEventCompatJellyBean.setMovementGranularity(var1, var2);
      }
   }

   static class AccessibilityEventKitKatImpl extends AccessibilityEventCompat.AccessibilityEventJellyBeanImpl {
      public int getContentChangeTypes(AccessibilityEvent var1) {
         return AccessibilityEventCompatKitKat.getContentChangeTypes(var1);
      }

      public void setContentChangeTypes(AccessibilityEvent var1, int var2) {
         AccessibilityEventCompatKitKat.setContentChangeTypes(var1, var2);
      }
   }

   static class AccessibilityEventStubImpl implements AccessibilityEventCompat.AccessibilityEventVersionImpl {
      public void appendRecord(AccessibilityEvent var1, Object var2) {
      }

      public int getAction(AccessibilityEvent var1) {
         return 0;
      }

      public int getContentChangeTypes(AccessibilityEvent var1) {
         return 0;
      }

      public int getMovementGranularity(AccessibilityEvent var1) {
         return 0;
      }

      public Object getRecord(AccessibilityEvent var1, int var2) {
         return null;
      }

      public int getRecordCount(AccessibilityEvent var1) {
         return 0;
      }

      public void setAction(AccessibilityEvent var1, int var2) {
      }

      public void setContentChangeTypes(AccessibilityEvent var1, int var2) {
      }

      public void setMovementGranularity(AccessibilityEvent var1, int var2) {
      }
   }

   interface AccessibilityEventVersionImpl {
      void appendRecord(AccessibilityEvent var1, Object var2);

      int getAction(AccessibilityEvent var1);

      int getContentChangeTypes(AccessibilityEvent var1);

      int getMovementGranularity(AccessibilityEvent var1);

      Object getRecord(AccessibilityEvent var1, int var2);

      int getRecordCount(AccessibilityEvent var1);

      void setAction(AccessibilityEvent var1, int var2);

      void setContentChangeTypes(AccessibilityEvent var1, int var2);

      void setMovementGranularity(AccessibilityEvent var1, int var2);
   }
}
