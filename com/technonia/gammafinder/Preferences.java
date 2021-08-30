package com.technonia.gammafinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
   private static final String PREF_NAME = "SmartGeiger_Pref";
   private static Preferences instance;
   private Editor editor;
   private SharedPreferences pref;

   public static Preferences instance() {
      synchronized(Preferences.class){}

      Preferences var0;
      try {
         if (instance == null) {
            var0 = new Preferences();
            instance = var0;
         }

         var0 = instance;
      } finally {
         ;
      }

      return var0;
   }

   public boolean getBoolean(String var1) {
      boolean var2;
      if (var1.equals("IS_INIT_USER") | var1.equals("IS_INIT_AUTO_CALIBRATION")) {
         var2 = this.pref.getBoolean(var1, true);
      } else {
         var2 = this.pref.getBoolean(var1, false);
      }

      return var2;
   }

   public int getInt(String var1) {
      return this.pref.getInt(var1, 0);
   }

   public void putBoolean(String var1, boolean var2) {
      this.editor.putBoolean(var1, var2);
      this.editor.apply();
   }

   public void putInt(String var1, int var2) {
      this.editor.putInt(var1, var2);
      this.editor.apply();
   }

   public void setContext(Context var1) {
      this.pref = var1.getSharedPreferences("SmartGeiger_Pref", 0);
      this.editor = this.pref.edit();
      this.editor.apply();
   }
}
