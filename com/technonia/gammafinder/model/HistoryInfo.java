package com.technonia.gammafinder.model;

public class HistoryInfo {
   public String count;
   public String cpm;
   public String date;
   public int id;
   public float latitude;
   public float longitude;
   public String memo;
   public String time;
   public String uSvh;

   public HistoryInfo() {
   }

   public HistoryInfo(int var1, String var2, float var3, float var4, String var5, String var6, String var7, String var8, String var9) {
      this.id = var1;
      this.date = var2;
      this.latitude = var3;
      this.longitude = var4;
      this.cpm = var5;
      this.count = var6;
      this.uSvh = var7;
      this.time = var8;
      this.memo = var9;
   }
}
