package com.technonia.gammafinder.model;

public class MenuInfo {
   public int mIcon;
   public String menu;

   public MenuInfo(int var1, String var2) {
      this.mIcon = var1;
      this.menu = var2;
   }

   public String getMenu() {
      return this.menu;
   }

   public int getmIcon() {
      return this.mIcon;
   }
}
