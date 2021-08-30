package com.technonia.gammafinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.technonia.gammafinder.model.MenuInfo;
import java.util.ArrayList;

public class MenuListAdapter extends ArrayAdapter {
   private ArrayList list;
   private Context mContext;
   private int mLayout;

   public MenuListAdapter(Context var1, int var2, ArrayList var3) {
      super(var1, var2, var3);
      this.mContext = var1;
      this.list = var3;
      this.mLayout = var2;
   }

   public int getCount() {
      return this.list.size();
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   @NonNull
   public View getView(int var1, View var2, @NonNull ViewGroup var3) {
      View var4 = var2;
      var2 = var2;
      if (var4 == null) {
         var2 = ((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(this.mLayout, var3, false);
      }

      MenuInfo var5 = (MenuInfo)this.list.get(var1);
      if (var5 != null) {
         ImageView var6 = (ImageView)var2.findViewById(2131427463);
         TextView var7 = (TextView)var2.findViewById(2131427464);
         if (var6 != null) {
            var6.setImageResource(var5.getmIcon());
         }

         if (var7 != null) {
            var7.setText(var5.getMenu());
         }
      }

      return var2;
   }
}
