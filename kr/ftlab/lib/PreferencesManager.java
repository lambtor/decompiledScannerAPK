package kr.ftlab.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public enum PreferencesManager {
   private static final boolean D = false;
   INSTANCE;

   private static final String TAG = "PreferencesManager";
   private final String PREF_NAME = "FTLAB_SDK_CONF";
   private Context mContext;
   SharedPreferences sharedPref;

   public DataInfo File_Read(int var1) {
      DataInfo var5 = new DataInfo();
      this.sharedPref = this.mContext.getSharedPreferences("FTLAB_SDK_CONF", 0);
      var5.MDI_WaveType = this.sharedPref.getInt("WaveType", 0);
      var5.MDI_INTV = this.sharedPref.getFloat("STD INTERVAL", 0.0F);
      var5.MDI_THR = this.sharedPref.getFloat("Threshold", 0.0F);
      var5.MDI_Pol = this.sharedPref.getInt("Polarity", 0);
      Log.e("PreferencesManager", "THR : " + var5.MDI_THR + ", INTV :" + var5.MDI_INTV + ", GE : " + var5.GE_Auto_Value + ", EM : " + var5.EM_Auto_Value);
      var5.GE_Auto_Value = this.sharedPref.getInt("GE", 0);
      var5.EM_Auto_Value = this.sharedPref.getFloat("EM", 0.0F);
      byte var4 = 0;
      byte var3 = 0;
      int var2 = var3;
      switch(var1) {
      case 1:
      case 5:
         if (var5.GE_Auto_Value > 0) {
            var2 = var3;
            if (var5.GE_Auto_Value != 200) {
               break;
            }
         }

         var2 = 0 + 1;
         break;
      case 2:
         label50: {
            if (var5.EM_Auto_Value > 0.0F) {
               var2 = var3;
               if (var5.EM_Auto_Value != 10.0F) {
                  break label50;
               }
            }

            var2 = 0 + 1;
         }
      case 3:
         break;
      case 4:
         var2 = var4;
         if (var5.MDI_WaveType > 10) {
            var2 = 0 + 1;
         }

         label44: {
            if (var5.MDI_INTV <= 120.0F) {
               var1 = var2;
               if (var5.MDI_INTV >= 100.0F) {
                  break label44;
               }
            }

            var1 = var2 + 1;
         }

         int var6;
         label39: {
            if (var5.MDI_THR <= 32768.0F && var5.MDI_THR >= 0.0F) {
               var6 = var1;
               if (var5.MDI_THR != 5000.0F) {
                  break label39;
               }
            }

            var6 = var1 + 1;
         }

         if (Math.abs(var5.MDI_Pol) <= 1) {
            var2 = var6;
            if (var5.MDI_Pol != 0) {
               break;
            }
         }

         var2 = var6 + 1;
         break;
      default:
         var2 = var3;
      }

      if (var2 > 0) {
         var5.IsParameter_OK = false;
      } else {
         var5.IsParameter_OK = true;
      }

      return var5;
   }

   public void File_Write(DataInfo var1) {
      this.sharedPref = this.mContext.getSharedPreferences("FTLAB_SDK_CONF", 0);
      Editor var2 = this.sharedPref.edit();
      var2.putInt("WaveType", var1.MDI_WaveType);
      var2.putFloat("STD INTERVAL", var1.MDI_INTV);
      var2.putFloat("Threshold", var1.MDI_THR);
      var2.putInt("Polarity", var1.MDI_Pol);
      var2.putInt("GE", var1.GE_Auto_Value);
      var2.putFloat("EM", var1.EM_Auto_Value);
      var2.commit();
   }

   public void setContext(Context var1) {
      this.mContext = var1;
   }
}
