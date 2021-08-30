package kr.ftlab.lib;

public final class Utils {
   public static final int HeaderSize = 44;
   public static final int Samples = 44100;
   public static final int SonarDataSize = 176400;

   public static int BooleanToInt(boolean var0) {
      byte var1;
      if (var0) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      return var1;
   }

   public static boolean IntToBoolean(int var0) {
      boolean var1;
      if (var0 == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean StringToBoolean(String var0) {
      boolean var1 = true;
      if (Integer.parseInt(var0) != 1) {
         var1 = false;
      }

      return var1;
   }

   public static float arr2float(byte[] var0, int var1) {
      int var3 = 0;
      byte[] var4 = new byte[4];

      int var2;
      for(var2 = var1; var2 < var1 + 4; ++var2) {
         var4[var3] = var0[var2];
         ++var3;
      }

      var3 = 0;
      var2 = 0;

      for(var1 = 0; var1 < 32; var1 += 8) {
         var3 = (int)((long)var3 | (long)(var4[var2] & 255) << var1);
         ++var2;
      }

      return Float.intBitsToFloat(var3);
   }

   public static byte[] float2bytes(float var0) {
      int var1 = Float.floatToIntBits(var0);
      return new byte[]{(byte)((var1 & 255) >> 0), (byte)(('\uff00' & var1) >> 8), (byte)((16711680 & var1) >> 16), (byte)((-16777216 & var1) >> 24)};
   }

   public static boolean floatToBoolean(double var0) {
      boolean var2;
      if (var0 == 0.0D) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public static int readInt(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   public static short readShort(byte[] var0, int var1) {
      return (short)(var0[var1] & 255 | (var0[var1 + 1] & 255) << 8);
   }

   public static byte[] shortToByteArray(short var0) {
      return new byte[]{(byte)(('\uff00' & var0) >> 8), (byte)(var0 & 255)};
   }
}
