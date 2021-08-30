package com.technonia.gammafinder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class UserGuideActivity extends AppCompatActivity {
   private static final String TAG = "UserGuideActivity";
   private UserGuideActivity.ViewPagerAdapter adapter;
   private Button btnSkip;
   private Button btnStart;
   private int countIndex = 4;
   private ImageView[] pageMark;
   private ViewPager vPager;

   private void pageMarkUpdate() {
      for(int var1 = 0; var1 < this.countIndex; ++var1) {
         if (var1 == this.vPager.getCurrentItem()) {
            this.pageMark[var1].setBackgroundResource(2130837597);
         } else {
            this.pageMark[var1].setBackgroundResource(2130837595);
         }
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903075);
      LinearLayout var3 = (LinearLayout)this.findViewById(2131427468);
      this.btnSkip = (Button)this.findViewById(2131427467);
      this.btnStart = (Button)this.findViewById(2131427466);
      this.vPager = (ViewPager)this.findViewById(2131427465);
      this.pageMark = new ImageView[this.countIndex];
      this.adapter = new UserGuideActivity.ViewPagerAdapter(this);
      this.vPager.setAdapter(this.adapter);
      this.vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         public void onPageScrollStateChanged(int var1) {
         }

         public void onPageScrolled(int var1, float var2, int var3) {
         }

         public void onPageSelected(int var1) {
            if (var1 == UserGuideActivity.this.adapter.getCount() - 1) {
               UserGuideActivity.this.btnStart.setVisibility(0);
               UserGuideActivity.this.btnSkip.setVisibility(8);
               UserGuideActivity.this.pageMarkUpdate();
            } else if (var1 == UserGuideActivity.this.adapter.getCount() - 2) {
               UserGuideActivity.this.btnSkip.setVisibility(0);
               UserGuideActivity.this.btnStart.setVisibility(8);
               UserGuideActivity.this.pageMarkUpdate();
            } else if (var1 == UserGuideActivity.this.adapter.getCount() - 3) {
               UserGuideActivity.this.btnSkip.setVisibility(0);
               UserGuideActivity.this.btnStart.setVisibility(8);
               UserGuideActivity.this.pageMarkUpdate();
            } else if (var1 == UserGuideActivity.this.adapter.getCount() - 4) {
               UserGuideActivity.this.btnSkip.setVisibility(0);
               UserGuideActivity.this.btnStart.setVisibility(8);
               UserGuideActivity.this.pageMarkUpdate();
            }

         }
      });
      this.btnStart.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Intent var2 = new Intent(UserGuideActivity.this, MainActivity.class);
            UserGuideActivity.this.startActivity(var2);
            UserGuideActivity.this.finish();
            UserGuideActivity.this.overridePendingTransition(17432576, 17432577);
         }
      });
      this.btnSkip.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Intent var2 = new Intent(UserGuideActivity.this, MainActivity.class);
            UserGuideActivity.this.startActivity(var2);
            UserGuideActivity.this.finish();
            UserGuideActivity.this.overridePendingTransition(17432576, 17432577);
         }
      });

      for(int var2 = 0; var2 < this.countIndex; ++var2) {
         this.pageMark[var2] = new ImageView(this);
         if (var2 == this.vPager.getCurrentItem()) {
            this.pageMark[var2].setBackgroundResource(2130837597);
         } else {
            this.pageMark[var2].setBackgroundResource(2130837595);
         }

         var3.addView(this.pageMark[var2]);
      }

   }

   protected void onDestroy() {
      Log.d("UserGuideActivity", "onDestroy()");
      super.onDestroy();
   }

   private class ViewPagerAdapter extends PagerAdapter {
      Context context;
      private int[] guideImage = new int[]{2130837600, 2130837601, 2130837602, 2130837603};
      private String[] guideText = new String[]{UserGuideActivity.this.getString(2131099697), UserGuideActivity.this.getString(2131099698), UserGuideActivity.this.getString(2131099699), UserGuideActivity.this.getString(2131099700)};

      ViewPagerAdapter(Context var2) {
         this.context = var2;
      }

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)var3);
      }

      public int getCount() {
         return this.guideImage.length;
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         View var3 = LayoutInflater.from(UserGuideActivity.this).inflate(2130903073, (ViewGroup)null);
         ImageView var4 = (ImageView)var3.findViewById(2131427461);
         var4.setScaleType(ScaleType.FIT_XY);
         var4.setImageResource(this.guideImage[var2]);
         ((TextView)var3.findViewById(2131427462)).setText(this.guideText[var2]);
         var1.addView(var3, 0);
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
