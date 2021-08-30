package com.technonia.gammafinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.technonia.gammafinder.model.HistoryInfo;
import java.util.ArrayList;

public class HistoryCustomAdapter extends BaseAdapter {
   private LayoutInflater inflater;
   private ArrayList infoList;
   private HistoryCustomAdapter.ViewHolder viewHolder;

   public HistoryCustomAdapter(Context var1, ArrayList var2) {
      this.inflater = LayoutInflater.from(var1);
      this.infoList = var2;
   }

   public int getCount() {
      return this.infoList.size();
   }

   public Object getItem(int var1) {
      return null;
   }

   public long getItemId(int var1) {
      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (var2 == null) {
         this.viewHolder = new HistoryCustomAdapter.ViewHolder();
         var2 = this.inflater.inflate(2130903076, (ViewGroup)null);
         this.viewHolder.date = (TextView)var2.findViewById(2131427469);
         this.viewHolder.cpm = (TextView)var2.findViewById(2131427470);
         this.viewHolder.count = (TextView)var2.findViewById(2131427471);
         this.viewHolder.time = (TextView)var2.findViewById(2131427473);
         this.viewHolder.uSvh = (TextView)var2.findViewById(2131427472);
         this.viewHolder.memo = (TextView)var2.findViewById(2131427474);
         var2.setTag(this.viewHolder);
      } else {
         this.viewHolder = (HistoryCustomAdapter.ViewHolder)var2.getTag();
      }

      this.viewHolder.date.setText(((HistoryInfo)this.infoList.get(var1)).date);
      this.viewHolder.cpm.setText(((HistoryInfo)this.infoList.get(var1)).cpm);
      this.viewHolder.count.setText(((HistoryInfo)this.infoList.get(var1)).count);
      this.viewHolder.time.setText(((HistoryInfo)this.infoList.get(var1)).time);
      this.viewHolder.uSvh.setText(((HistoryInfo)this.infoList.get(var1)).uSvh);
      this.viewHolder.memo.setText(((HistoryInfo)this.infoList.get(var1)).memo);
      return var2;
   }

   class ViewHolder {
      TextView count;
      TextView cpm;
      TextView date;
      TextView memo;
      TextView time;
      TextView uSvh;
   }
}
