package android.support.v4.util;

class ContainerHelpers {
   static final int[] EMPTY_INTS = new int[0];
   static final long[] EMPTY_LONGS = new long[0];
   static final Object[] EMPTY_OBJECTS = new Object[0];

   static int binarySearch(int[] var0, int var1, int var2) {
      byte var4 = 0;
      int var3 = var1 - 1;
      var1 = var4;

      while(true) {
         if (var1 > var3) {
            var3 = ~var1;
            break;
         }

         int var6 = var1 + var3 >>> 1;
         int var5 = var0[var6];
         if (var5 < var2) {
            var1 = var6 + 1;
         } else {
            var3 = var6;
            if (var5 <= var2) {
               break;
            }

            var3 = var6 - 1;
         }
      }

      return var3;
   }

   static int binarySearch(long[] var0, int var1, long var2) {
      byte var4 = 0;
      int var5 = var1 - 1;
      var1 = var4;
      int var8 = var5;

      while(true) {
         if (var1 > var8) {
            var8 = ~var1;
            break;
         }

         var5 = var1 + var8 >>> 1;
         long var6 = var0[var5];
         if (var6 < var2) {
            var1 = var5 + 1;
         } else {
            var8 = var5;
            if (var6 <= var2) {
               break;
            }

            var8 = var5 - 1;
         }
      }

      return var8;
   }

   public static boolean equal(Object var0, Object var1) {
      boolean var2;
      if (var0 != var1 && (var0 == null || !var0.equals(var1))) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public static int idealByteArraySize(int var0) {
      int var2 = 4;

      int var1;
      while(true) {
         var1 = var0;
         if (var2 >= 32) {
            break;
         }

         if (var0 <= (1 << var2) - 12) {
            var1 = (1 << var2) - 12;
            break;
         }

         ++var2;
      }

      return var1;
   }

   public static int idealIntArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   public static int idealLongArraySize(int var0) {
      return idealByteArraySize(var0 * 8) / 8;
   }
}
