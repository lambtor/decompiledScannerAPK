package android.support.v7.view.menu;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public final class MenuWrapperFactory {
   public static Menu wrapSupportMenu(Context var0, SupportMenu var1) {
      if (VERSION.SDK_INT >= 14) {
         return new MenuWrapperICS(var0, var1);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public static MenuItem wrapSupportMenuItem(Context var0, SupportMenuItem var1) {
      Object var2;
      if (VERSION.SDK_INT >= 16) {
         var2 = new MenuItemWrapperJB(var0, var1);
      } else {
         if (VERSION.SDK_INT < 14) {
            throw new UnsupportedOperationException();
         }

         var2 = new MenuItemWrapperICS(var0, var1);
      }

      return (MenuItem)var2;
   }

   public static SubMenu wrapSupportSubMenu(Context var0, SupportSubMenu var1) {
      if (VERSION.SDK_INT >= 14) {
         return new SubMenuWrapperICS(var0, var1);
      } else {
         throw new UnsupportedOperationException();
      }
   }
}
