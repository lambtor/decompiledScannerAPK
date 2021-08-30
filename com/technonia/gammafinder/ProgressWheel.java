package com.technonia.gammafinder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.technonia.geiger.R;

public class ProgressWheel extends View {
   int MY_DIP_VALUE = 20;
   private int barColor = -1442840576;
   private int barLength = 60;
   private Paint barPaint = new Paint();
   private int barWidth = 20;
   private RectF circleBounds = new RectF();
   private int circleColor = 0;
   private RectF circleInnerContour = new RectF();
   private RectF circleOuterContour = new RectF();
   private Paint circlePaint = new Paint();
   private int circleRadius = 80;
   private int contourColor = -1442840576;
   private Paint contourPaint = new Paint();
   private float contourSize = 0.0F;
   private int delayMillis;
   private int fullRadius = 100;
   boolean isSpinning;
   private int layout_height = 0;
   private int layout_width = 0;
   private int paddingBottom = 5;
   private int paddingLeft = 5;
   private int paddingRight = 5;
   private int paddingTop = 5;
   int pixel;
   float progress;
   private RectF rectBounds = new RectF();
   private int rimColor = -1428300323;
   private Paint rimPaint = new Paint();
   private int rimWidth = 20;
   private Handler spinHandler;
   private int spinSpeed;
   private String[] splitText;
   private String[] splitTextUnit;
   private String text;
   private int textColor = -16777216;
   private Paint textPaint = new Paint();
   private int textSize = 20;
   private Paint textUnitPaint = new Paint();
   private int textUnitSize = 50;

   public ProgressWheel(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.pixel = (int)TypedValue.applyDimension(1, (float)this.MY_DIP_VALUE, this.getResources().getDisplayMetrics());
      this.spinSpeed = 2;
      this.delayMillis = 0;
      this.spinHandler = new Handler() {
         public void handleMessage(Message var1) {
            ProgressWheel.this.invalidate();
            if (ProgressWheel.this.isSpinning) {
               ProgressWheel var2 = ProgressWheel.this;
               var2.progress += (float)ProgressWheel.this.spinSpeed;
               if (ProgressWheel.this.progress > 360.0F) {
                  ProgressWheel.this.progress = 0.0F;
               }

               ProgressWheel.this.spinHandler.sendEmptyMessageDelayed(0, (long)ProgressWheel.this.delayMillis);
            }

         }
      };
      this.progress = 0.0F;
      this.isSpinning = false;
      this.text = "";
      this.splitText = new String[0];
      this.splitTextUnit = new String[0];
      this.parseAttributes(var1.obtainStyledAttributes(var2, R.styleable.ProgressWheel));
   }

   private void parseAttributes(TypedArray var1) {
      this.barWidth = (int)var1.getDimension(13, (float)this.barWidth);
      this.rimWidth = (int)var1.getDimension(8, (float)this.rimWidth);
      this.spinSpeed = (int)var1.getDimension(9, (float)this.spinSpeed);
      this.delayMillis = var1.getInteger(10, this.delayMillis);
      if (this.delayMillis < 0) {
         this.delayMillis = 0;
      }

      this.barColor = var1.getColor(6, this.barColor);
      this.barLength = (int)var1.getDimension(0, (float)this.barLength);
      this.textSize = (int)var1.getDimension(4, (float)this.textSize);
      this.textColor = var1.getColor(3, this.textColor);
      if (var1.hasValue(1)) {
         this.setText(var1.getString(1));
      }

      this.rimColor = var1.getColor(7, this.rimColor);
      this.circleColor = var1.getColor(11, this.circleColor);
      this.contourColor = var1.getColor(14, this.contourColor);
      this.contourSize = var1.getDimension(15, this.contourSize);
      var1.recycle();
   }

   private void setupBounds() {
      int var2 = Math.min(this.layout_width, this.layout_height);
      int var1 = this.layout_width - var2;
      var2 = this.layout_height - var2;
      this.paddingTop = this.getPaddingTop() + var2 / 2;
      this.paddingBottom = this.getPaddingBottom() + var2 / 2;
      this.paddingLeft = this.getPaddingLeft() + var1 / 2;
      this.paddingRight = this.getPaddingRight() + var1 / 2;
      var1 = this.getWidth();
      var2 = this.getHeight();
      this.rectBounds = new RectF((float)this.paddingLeft, (float)this.paddingTop, (float)(var1 - this.paddingRight), (float)(var2 - this.paddingBottom));
      this.circleBounds = new RectF((float)(this.paddingLeft + this.barWidth), (float)(this.paddingTop + this.barWidth), (float)(var1 - this.paddingRight - this.barWidth), (float)(var2 - this.paddingBottom - this.barWidth));
      this.circleInnerContour = new RectF(this.circleBounds.left + (float)this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.top + (float)this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.right - (float)this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.bottom - (float)this.rimWidth / 2.0F - this.contourSize / 2.0F);
      this.circleOuterContour = new RectF(this.circleBounds.left - (float)this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.top - (float)this.rimWidth / 2.0F - this.contourSize / 2.0F, this.circleBounds.right + (float)this.rimWidth / 2.0F + this.contourSize / 2.0F, this.circleBounds.bottom + (float)this.rimWidth / 2.0F + this.contourSize / 2.0F);
      this.fullRadius = (var1 - this.paddingRight - this.barWidth) / 2;
      this.circleRadius = this.fullRadius - this.barWidth + 1;
   }

   private void setupPaints() {
      this.barPaint.setColor(this.barColor);
      this.barPaint.setAntiAlias(true);
      this.barPaint.setStyle(Style.STROKE);
      this.barPaint.setStrokeWidth((float)this.barWidth);
      this.rimPaint.setColor(this.rimColor);
      this.rimPaint.setAntiAlias(true);
      this.rimPaint.setStyle(Style.STROKE);
      this.rimPaint.setStrokeWidth((float)this.rimWidth);
      this.circlePaint.setColor(this.circleColor);
      this.circlePaint.setAntiAlias(true);
      this.circlePaint.setStyle(Style.FILL);
      this.textPaint.setColor(this.textColor);
      this.textPaint.setStyle(Style.FILL);
      this.textPaint.setAntiAlias(true);
      this.textPaint.setTextSize((float)this.textSize);
      this.textUnitPaint.setColor(this.textColor);
      this.textUnitPaint.setStyle(Style.FILL);
      this.textUnitPaint.setAntiAlias(true);
      this.textUnitPaint.setTextSize((float)this.pixel);
      this.contourPaint.setColor(this.contourColor);
      this.contourPaint.setAntiAlias(true);
      this.contourPaint.setStyle(Style.STROKE);
      this.contourPaint.setStrokeWidth(this.contourSize);
   }

   public int getBarColor() {
      return this.barColor;
   }

   public int getBarLength() {
      return this.barLength;
   }

   public int getBarWidth() {
      return this.barWidth;
   }

   public int getCircleColor() {
      return this.circleColor;
   }

   public int getCircleRadius() {
      return this.circleRadius;
   }

   public int getDelayMillis() {
      return this.delayMillis;
   }

   public int getPaddingBottom() {
      return this.paddingBottom;
   }

   public int getPaddingLeft() {
      return this.paddingLeft;
   }

   public int getPaddingRight() {
      return this.paddingRight;
   }

   public int getPaddingTop() {
      return this.paddingTop;
   }

   public int getRimColor() {
      return this.rimColor;
   }

   public Shader getRimShader() {
      return this.rimPaint.getShader();
   }

   public int getRimWidth() {
      return this.rimWidth;
   }

   public int getSpinSpeed() {
      return this.spinSpeed;
   }

   public int getTextColor() {
      return this.textColor;
   }

   public int getTextSize() {
      return this.textSize;
   }

   public void incrementProgress(float var1) {
      this.isSpinning = false;
      this.progress = var1;
      if (this.progress > 1000.0F) {
         this.progress = 1000.0F;
      }

      if (this.progress >= 0.1F && this.progress <= 0.4F) {
         this.barPaint.setColor(-16711936);
      } else if (this.progress > 0.4F && this.progress <= 4.0F) {
         this.barPaint.setColor(Color.parseColor("#FFFF00"));
      } else if (this.progress > 4.0F && this.progress <= 10.0F) {
         this.barPaint.setColor(Color.parseColor("#FFA500"));
      } else if (this.progress > 10.0F && this.progress <= 1000.0F) {
         this.barPaint.setColor(Color.parseColor("#FF0000"));
      }

      this.spinHandler.sendEmptyMessage(0);
   }

   public boolean isSpinning() {
      boolean var1;
      if (this.isSpinning) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      float var3 = this.progress;
      float var4 = this.progress;
      float var2 = (float)((double)this.progress * 3.6D);
      float var5 = (float)((double)this.progress * 0.36D);
      var1.drawArc(this.circleBounds, 360.0F, 360.0F, false, this.circlePaint);
      var1.drawArc(this.circleBounds, 360.0F, 360.0F, false, this.rimPaint);
      if (this.progress == 0.1F) {
         this.barPaint.setColor(-16711936);
      }

      if (this.isSpinning) {
         var1.drawArc(this.circleBounds, this.progress - 90.0F, (float)this.barLength, false, this.barPaint);
      } else if (this.progress >= 0.1F && this.progress <= 1.0F) {
         var1.drawArc(this.circleBounds, -90.0F, var3 * 360.0F / 4.0F, false, this.barPaint);
      } else if (this.progress >= 1.0F && this.progress <= 10.0F) {
         var1.drawArc(this.circleBounds, 0.0F, -90.0F, false, this.barPaint);
         var1.drawArc(this.circleBounds, -10.0F, var4 * 36.0F / 4.0F, false, this.barPaint);
      } else if (this.progress >= 10.0F && this.progress <= 100.0F) {
         var1.drawArc(this.circleBounds, 1.0F, -91.0F, false, this.barPaint);
         var1.drawArc(this.circleBounds, 0.0F, 90.0F, false, this.barPaint);
         var1.drawArc(this.circleBounds, 80.0F, var2 / 4.0F, false, this.barPaint);
      } else if (this.progress >= 100.0F && this.progress <= 1000.0F) {
         var1.drawArc(this.circleBounds, 0.0F, -90.0F, false, this.barPaint);
         var1.drawArc(this.circleBounds, 0.0F, 180.0F, false, this.barPaint);
         var1.drawArc(this.circleBounds, 170.0F, var5 / 4.0F, false, this.barPaint);
      }

      var3 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
      var4 = this.textPaint.descent();
      String[] var8 = this.splitText;
      int var7 = var8.length;

      int var6;
      for(var6 = 0; var6 < var7; ++var6) {
         String var9 = var8[var6];
         var2 = this.textPaint.measureText(var9) / 2.0F;
         var1.drawText(var9, (float)(this.getWidth() / 2) - var2, (float)(this.getHeight() / 2) + (var3 - var4), this.textPaint);
      }

      String[] var11 = this.splitTextUnit;
      var7 = var11.length;

      for(var6 = 0; var6 < var7; ++var6) {
         String var10 = var11[var6];
         var2 = this.textUnitPaint.measureText(var10) / 2.0F;
         var1.drawText(var10, (float)(this.getWidth() / 2) - var2, (float)this.getHeight() / 1.5F, this.textUnitPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      var1 = this.getMeasuredWidth();
      var2 = this.getMeasuredHeight();
      var1 = var1 - this.getPaddingLeft() - this.getPaddingRight();
      var2 = var2 - this.getPaddingTop() - this.getPaddingBottom();
      if (var1 > var2) {
         var1 = var2;
      }

      this.setMeasuredDimension(this.getPaddingLeft() + var1 + this.getPaddingRight(), this.getPaddingTop() + var1 + this.getPaddingBottom());
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      this.layout_width = var1;
      this.layout_height = var2;
      this.setupBounds();
      this.setupPaints();
      this.invalidate();
   }

   public void resetCount() {
      this.progress = 0.0F;
      this.invalidate();
   }

   public void setBarColor(int var1) {
      this.barColor = var1;
   }

   public void setBarLength(int var1) {
      this.barLength = var1;
   }

   public void setBarWidth(int var1) {
      this.barWidth = var1;
   }

   public void setCircleColor(int var1) {
      this.circleColor = var1;
   }

   public void setCircleRadius(int var1) {
      this.circleRadius = var1;
   }

   public void setDelayMillis(int var1) {
      this.delayMillis = var1;
   }

   public void setPaddingBottom(int var1) {
      this.paddingBottom = var1;
   }

   public void setPaddingLeft(int var1) {
      this.paddingLeft = var1;
   }

   public void setPaddingRight(int var1) {
      this.paddingRight = var1;
   }

   public void setPaddingTop(int var1) {
      this.paddingTop = var1;
   }

   public void setProgress(float var1) {
      this.isSpinning = false;
      this.progress = var1;
      this.spinHandler.sendEmptyMessage(0);
   }

   public void setRimColor(int var1) {
      this.rimColor = var1;
   }

   public void setRimShader(Shader var1) {
      this.rimPaint.setShader(var1);
   }

   public void setRimWidth(int var1) {
      this.rimWidth = var1;
   }

   public void setSpinSpeed(int var1) {
      this.spinSpeed = var1;
   }

   public void setText(String var1) {
      this.text = var1;
      this.splitText = this.text.split("\n");
   }

   public void setTextColor(int var1) {
      this.textColor = var1;
   }

   public void setTextSize(int var1) {
      this.textSize = var1;
   }

   public void setTextUnit(String var1) {
      this.text = var1;
      this.splitTextUnit = this.text.split("\n");
   }

   public void setTextUnitSize(int var1) {
      this.textUnitSize = var1;
   }

   public void spin() {
      this.isSpinning = true;
      this.spinHandler.sendEmptyMessage(0);
   }

   public void stopSpinning() {
      this.isSpinning = false;
      this.progress = 0.0F;
      this.spinHandler.removeMessages(0);
   }
}
