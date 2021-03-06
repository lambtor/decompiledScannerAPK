package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
   private static final int CLOSE_ENOUGH = 2;
   private static final Comparator COMPARATOR = new Comparator() {
      public int compare(ViewPager.ItemInfo var1, ViewPager.ItemInfo var2) {
         return var1.position - var2.position;
      }
   };
   private static final boolean DEBUG = false;
   private static final int DEFAULT_GUTTER_SIZE = 16;
   private static final int DEFAULT_OFFSCREEN_PAGES = 1;
   private static final int DRAW_ORDER_DEFAULT = 0;
   private static final int DRAW_ORDER_FORWARD = 1;
   private static final int DRAW_ORDER_REVERSE = 2;
   private static final int INVALID_POINTER = -1;
   static final int[] LAYOUT_ATTRS = new int[]{16842931};
   private static final int MAX_SETTLE_DURATION = 600;
   private static final int MIN_DISTANCE_FOR_FLING = 25;
   private static final int MIN_FLING_VELOCITY = 400;
   public static final int SCROLL_STATE_DRAGGING = 1;
   public static final int SCROLL_STATE_IDLE = 0;
   public static final int SCROLL_STATE_SETTLING = 2;
   private static final String TAG = "ViewPager";
   private static final boolean USE_CACHE = false;
   private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float var1) {
         --var1;
         return var1 * var1 * var1 * var1 * var1 + 1.0F;
      }
   };
   private static final ViewPager.ViewPositionComparator sPositionComparator = new ViewPager.ViewPositionComparator();
   private int mActivePointerId = -1;
   PagerAdapter mAdapter;
   private List mAdapterChangeListeners;
   private int mBottomPageBounds;
   private boolean mCalledSuper;
   private int mChildHeightMeasureSpec;
   private int mChildWidthMeasureSpec;
   private int mCloseEnough;
   int mCurItem;
   private int mDecorChildCount;
   private int mDefaultGutterSize;
   private int mDrawingOrder;
   private ArrayList mDrawingOrderedChildren;
   private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
         ViewPager.this.setScrollState(0);
         ViewPager.this.populate();
      }
   };
   private int mExpectedAdapterCount;
   private long mFakeDragBeginTime;
   private boolean mFakeDragging;
   private boolean mFirstLayout = true;
   private float mFirstOffset = -3.4028235E38F;
   private int mFlingDistance;
   private int mGutterSize;
   private boolean mInLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private ViewPager.OnPageChangeListener mInternalPageChangeListener;
   private boolean mIsBeingDragged;
   private boolean mIsScrollStarted;
   private boolean mIsUnableToDrag;
   private final ArrayList mItems = new ArrayList();
   private float mLastMotionX;
   private float mLastMotionY;
   private float mLastOffset = Float.MAX_VALUE;
   private EdgeEffectCompat mLeftEdge;
   private Drawable mMarginDrawable;
   private int mMaximumVelocity;
   private int mMinimumVelocity;
   private boolean mNeedCalculatePageOffsets = false;
   private ViewPager.PagerObserver mObserver;
   private int mOffscreenPageLimit = 1;
   private ViewPager.OnPageChangeListener mOnPageChangeListener;
   private List mOnPageChangeListeners;
   private int mPageMargin;
   private ViewPager.PageTransformer mPageTransformer;
   private int mPageTransformerLayerType;
   private boolean mPopulatePending;
   private Parcelable mRestoredAdapterState = null;
   private ClassLoader mRestoredClassLoader = null;
   private int mRestoredCurItem = -1;
   private EdgeEffectCompat mRightEdge;
   private int mScrollState = 0;
   private Scroller mScroller;
   private boolean mScrollingCacheEnabled;
   private Method mSetChildrenDrawingOrderEnabled;
   private final ViewPager.ItemInfo mTempItem = new ViewPager.ItemInfo();
   private final Rect mTempRect = new Rect();
   private int mTopPageBounds;
   private int mTouchSlop;
   private VelocityTracker mVelocityTracker;

   public ViewPager(Context var1) {
      super(var1);
      this.initViewPager();
   }

   public ViewPager(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initViewPager();
   }

   private void calculatePageOffsets(ViewPager.ItemInfo var1, int var2, ViewPager.ItemInfo var3) {
      int var10 = this.mAdapter.getCount();
      int var7 = this.getClientWidth();
      float var5;
      if (var7 > 0) {
         var5 = (float)this.mPageMargin / (float)var7;
      } else {
         var5 = 0.0F;
      }

      float var4;
      float var6;
      int var8;
      int var9;
      if (var3 != null) {
         var7 = var3.position;
         if (var7 < var1.position) {
            var8 = 0;
            var4 = var3.offset + var3.widthFactor + var5;
            ++var7;

            while(var7 <= var1.position && var8 < this.mItems.size()) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var8);

               while(true) {
                  var6 = var4;
                  var9 = var7;
                  if (var7 <= var3.position) {
                     break;
                  }

                  var6 = var4;
                  var9 = var7;
                  if (var8 >= this.mItems.size() - 1) {
                     break;
                  }

                  ++var8;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var8);
               }

               while(var9 < var3.position) {
                  var6 += this.mAdapter.getPageWidth(var9) + var5;
                  ++var9;
               }

               var3.offset = var6;
               var4 = var6 + var3.widthFactor + var5;
               var7 = var9 + 1;
            }
         } else if (var7 > var1.position) {
            var8 = this.mItems.size() - 1;
            var4 = var3.offset;
            --var7;

            while(var7 >= var1.position && var8 >= 0) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var8);

               while(true) {
                  var6 = var4;
                  var9 = var7;
                  if (var7 >= var3.position) {
                     break;
                  }

                  var6 = var4;
                  var9 = var7;
                  if (var8 <= 0) {
                     break;
                  }

                  --var8;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var8);
               }

               while(var9 > var3.position) {
                  var6 -= this.mAdapter.getPageWidth(var9) + var5;
                  --var9;
               }

               var4 = var6 - (var3.widthFactor + var5);
               var3.offset = var4;
               var7 = var9 - 1;
            }
         }
      }

      var9 = this.mItems.size();
      var6 = var1.offset;
      var7 = var1.position - 1;
      if (var1.position == 0) {
         var4 = var1.offset;
      } else {
         var4 = -3.4028235E38F;
      }

      this.mFirstOffset = var4;
      if (var1.position == var10 - 1) {
         var4 = var1.offset + var1.widthFactor - 1.0F;
      } else {
         var4 = Float.MAX_VALUE;
      }

      this.mLastOffset = var4;
      var8 = var2 - 1;

      for(var4 = var6; var8 >= 0; --var7) {
         for(var3 = (ViewPager.ItemInfo)this.mItems.get(var8); var7 > var3.position; --var7) {
            var4 -= this.mAdapter.getPageWidth(var7) + var5;
         }

         var4 -= var3.widthFactor + var5;
         var3.offset = var4;
         if (var3.position == 0) {
            this.mFirstOffset = var4;
         }

         --var8;
      }

      var4 = var1.offset + var1.widthFactor + var5;
      var8 = var1.position + 1;
      var7 = var2 + 1;

      for(var2 = var8; var7 < var9; ++var2) {
         for(var1 = (ViewPager.ItemInfo)this.mItems.get(var7); var2 < var1.position; ++var2) {
            var4 += this.mAdapter.getPageWidth(var2) + var5;
         }

         if (var1.position == var10 - 1) {
            this.mLastOffset = var1.widthFactor + var4 - 1.0F;
         }

         var1.offset = var4;
         var4 += var1.widthFactor + var5;
         ++var7;
      }

      this.mNeedCalculatePageOffsets = false;
   }

   private void completeScroll(boolean var1) {
      boolean var3 = true;
      boolean var2;
      if (this.mScrollState == 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         this.setScrollingCacheEnabled(false);
         if (this.mScroller.isFinished()) {
            var3 = false;
         }

         if (var3) {
            this.mScroller.abortAnimation();
            int var6 = this.getScrollX();
            int var9 = this.getScrollY();
            int var4 = this.mScroller.getCurrX();
            int var5 = this.mScroller.getCurrY();
            if (var6 != var4 || var9 != var5) {
               this.scrollTo(var4, var5);
               if (var4 != var6) {
                  this.pageScrolled(var4);
               }
            }
         }
      }

      this.mPopulatePending = false;
      byte var10 = 0;
      var3 = var2;

      for(int var8 = var10; var8 < this.mItems.size(); ++var8) {
         ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
         if (var7.scrolling) {
            var3 = true;
            var7.scrolling = false;
         }
      }

      if (var3) {
         if (var1) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
         } else {
            this.mEndScrollRunnable.run();
         }
      }

   }

   private int determineTargetPage(int var1, float var2, int var3, int var4) {
      if (Math.abs(var4) > this.mFlingDistance && Math.abs(var3) > this.mMinimumVelocity) {
         if (var3 <= 0) {
            ++var1;
         }
      } else {
         float var5;
         if (var1 >= this.mCurItem) {
            var5 = 0.4F;
         } else {
            var5 = 0.6F;
         }

         var1 += (int)(var2 + var5);
      }

      var3 = var1;
      if (this.mItems.size() > 0) {
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(0);
         ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
         var3 = Math.max(var6.position, Math.min(var1, var7.position));
      }

      return var3;
   }

   private void dispatchOnPageScrolled(int var1, float var2, int var3) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageScrolled(var1, var2, var3);
      }

      if (this.mOnPageChangeListeners != null) {
         int var4 = 0;

         for(int var5 = this.mOnPageChangeListeners.size(); var4 < var5; ++var4) {
            ViewPager.OnPageChangeListener var6 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var4);
            if (var6 != null) {
               var6.onPageScrolled(var1, var2, var3);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageScrolled(var1, var2, var3);
      }

   }

   private void dispatchOnPageSelected(int var1) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageSelected(var1);
      }

      if (this.mOnPageChangeListeners != null) {
         int var2 = 0;

         for(int var3 = this.mOnPageChangeListeners.size(); var2 < var3; ++var2) {
            ViewPager.OnPageChangeListener var4 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var2);
            if (var4 != null) {
               var4.onPageSelected(var1);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageSelected(var1);
      }

   }

   private void dispatchOnScrollStateChanged(int var1) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageScrollStateChanged(var1);
      }

      if (this.mOnPageChangeListeners != null) {
         int var2 = 0;

         for(int var3 = this.mOnPageChangeListeners.size(); var2 < var3; ++var2) {
            ViewPager.OnPageChangeListener var4 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var2);
            if (var4 != null) {
               var4.onPageScrollStateChanged(var1);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageScrollStateChanged(var1);
      }

   }

   private void enableLayers(boolean var1) {
      int var4 = this.getChildCount();

      for(int var2 = 0; var2 < var4; ++var2) {
         int var3;
         if (var1) {
            var3 = this.mPageTransformerLayerType;
         } else {
            var3 = 0;
         }

         ViewCompat.setLayerType(this.getChildAt(var2), var3, (Paint)null);
      }

   }

   private void endDrag() {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   private Rect getChildRectInPagerCoordinates(Rect var1, View var2) {
      Rect var3 = var1;
      if (var1 == null) {
         var3 = new Rect();
      }

      if (var2 == null) {
         var3.set(0, 0, 0, 0);
      } else {
         var3.left = var2.getLeft();
         var3.right = var2.getRight();
         var3.top = var2.getTop();
         var3.bottom = var2.getBottom();

         ViewGroup var5;
         for(ViewParent var4 = var2.getParent(); var4 instanceof ViewGroup && var4 != this; var4 = var5.getParent()) {
            var5 = (ViewGroup)var4;
            var3.left += var5.getLeft();
            var3.right += var5.getRight();
            var3.top += var5.getTop();
            var3.bottom += var5.getBottom();
         }
      }

      return var3;
   }

   private int getClientWidth() {
      return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
   }

   private ViewPager.ItemInfo infoForCurrentScrollPosition() {
      float var2 = 0.0F;
      int var5 = this.getClientWidth();
      float var1;
      if (var5 > 0) {
         var1 = (float)this.getScrollX() / (float)var5;
      } else {
         var1 = 0.0F;
      }

      if (var5 > 0) {
         var2 = (float)this.mPageMargin / (float)var5;
      }

      int var8 = -1;
      float var4 = 0.0F;
      float var3 = 0.0F;
      boolean var6 = true;
      ViewPager.ItemInfo var10 = null;
      var5 = 0;

      ViewPager.ItemInfo var11;
      while(true) {
         var11 = var10;
         if (var5 >= this.mItems.size()) {
            break;
         }

         var11 = (ViewPager.ItemInfo)this.mItems.get(var5);
         int var7 = var5;
         ViewPager.ItemInfo var9 = var11;
         if (!var6) {
            var7 = var5;
            var9 = var11;
            if (var11.position != var8 + 1) {
               var9 = this.mTempItem;
               var9.offset = var4 + var3 + var2;
               var9.position = var8 + 1;
               var9.widthFactor = this.mAdapter.getPageWidth(var9.position);
               var7 = var5 - 1;
            }
         }

         var4 = var9.offset;
         var3 = var9.widthFactor;
         if (!var6) {
            var11 = var10;
            if (var1 < var4) {
               break;
            }
         }

         if (var1 < var3 + var4 + var2 || var7 == this.mItems.size() - 1) {
            var11 = var9;
            break;
         }

         var6 = false;
         var8 = var9.position;
         var3 = var9.widthFactor;
         var5 = var7 + 1;
         var10 = var9;
      }

      return var11;
   }

   private static boolean isDecorView(@NonNull View var0) {
      boolean var1;
      if (var0.getClass().getAnnotation(ViewPager.DecorView.class) != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isGutterDrag(float var1, float var2) {
      boolean var3;
      if ((var1 >= (float)this.mGutterSize || var2 <= 0.0F) && (var1 <= (float)(this.getWidth() - this.mGutterSize) || var2 >= 0.0F)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private void onSecondaryPointerUp(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionIndex(var1);
      if (var1.getPointerId(var2) == this.mActivePointerId) {
         byte var3;
         if (var2 == 0) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         this.mLastMotionX = var1.getX(var3);
         this.mActivePointerId = var1.getPointerId(var3);
         if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
         }
      }

   }

   private boolean pageScrolled(int var1) {
      boolean var6 = false;
      if (this.mItems.size() == 0) {
         if (!this.mFirstLayout) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper) {
               throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
         }
      } else {
         ViewPager.ItemInfo var7 = this.infoForCurrentScrollPosition();
         int var5 = this.getClientWidth();
         int var4 = this.mPageMargin;
         float var2 = (float)this.mPageMargin / (float)var5;
         int var3 = var7.position;
         var2 = ((float)var1 / (float)var5 - var7.offset) / (var7.widthFactor + var2);
         var1 = (int)((float)(var5 + var4) * var2);
         this.mCalledSuper = false;
         this.onPageScrolled(var3, var2, var1);
         if (!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
         }

         var6 = true;
      }

      return var6;
   }

   private boolean performDrag(float var1) {
      boolean var9 = false;
      boolean var8 = false;
      boolean var7 = false;
      float var2 = this.mLastMotionX;
      this.mLastMotionX = var1;
      float var3 = (float)this.getScrollX() + (var2 - var1);
      int var6 = this.getClientWidth();
      var1 = (float)var6 * this.mFirstOffset;
      var2 = (float)var6 * this.mLastOffset;
      boolean var4 = true;
      boolean var5 = true;
      ViewPager.ItemInfo var11 = (ViewPager.ItemInfo)this.mItems.get(0);
      ViewPager.ItemInfo var10 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
      if (var11.position != 0) {
         var4 = false;
         var1 = var11.offset * (float)var6;
      }

      if (var10.position != this.mAdapter.getCount() - 1) {
         var5 = false;
         var2 = var10.offset * (float)var6;
      }

      if (var3 < var1) {
         if (var4) {
            var7 = this.mLeftEdge.onPull(Math.abs(var1 - var3) / (float)var6);
         }
      } else {
         var7 = var9;
         var1 = var3;
         if (var3 > var2) {
            var7 = var8;
            if (var5) {
               var7 = this.mRightEdge.onPull(Math.abs(var3 - var2) / (float)var6);
            }

            var1 = var2;
         }
      }

      this.mLastMotionX += var1 - (float)((int)var1);
      this.scrollTo((int)var1, this.getScrollY());
      this.pageScrolled((int)var1);
      return var7;
   }

   private void recomputeScrollPosition(int var1, int var2, int var3, int var4) {
      float var5;
      if (var2 > 0 && !this.mItems.isEmpty()) {
         if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
         } else {
            int var6 = this.getPaddingLeft();
            int var7 = this.getPaddingRight();
            int var9 = this.getPaddingLeft();
            int var8 = this.getPaddingRight();
            var5 = (float)this.getScrollX() / (float)(var2 - var9 - var8 + var4);
            this.scrollTo((int)((float)(var1 - var6 - var7 + var3) * var5), this.getScrollY());
         }
      } else {
         ViewPager.ItemInfo var10 = this.infoForPosition(this.mCurItem);
         if (var10 != null) {
            var5 = Math.min(var10.offset, this.mLastOffset);
         } else {
            var5 = 0.0F;
         }

         var1 = (int)((float)(var1 - this.getPaddingLeft() - this.getPaddingRight()) * var5);
         if (var1 != this.getScrollX()) {
            this.completeScroll(false);
            this.scrollTo(var1, this.getScrollY());
         }
      }

   }

   private void removeNonDecorViews() {
      int var2;
      for(int var1 = 0; var1 < this.getChildCount(); var1 = var2 + 1) {
         var2 = var1;
         if (!((ViewPager.LayoutParams)this.getChildAt(var1).getLayoutParams()).isDecor) {
            this.removeViewAt(var1);
            var2 = var1 - 1;
         }
      }

   }

   private void requestParentDisallowInterceptTouchEvent(boolean var1) {
      ViewParent var2 = this.getParent();
      if (var2 != null) {
         var2.requestDisallowInterceptTouchEvent(var1);
      }

   }

   private boolean resetTouch() {
      this.mActivePointerId = -1;
      this.endDrag();
      return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
   }

   private void scrollToItem(int var1, boolean var2, int var3, boolean var4) {
      ViewPager.ItemInfo var6 = this.infoForPosition(var1);
      int var5 = 0;
      if (var6 != null) {
         var5 = (int)((float)this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(var6.offset, this.mLastOffset)));
      }

      if (var2) {
         this.smoothScrollTo(var5, 0, var3);
         if (var4) {
            this.dispatchOnPageSelected(var1);
         }
      } else {
         if (var4) {
            this.dispatchOnPageSelected(var1);
         }

         this.completeScroll(false);
         this.scrollTo(var5, 0);
         this.pageScrolled(var5);
      }

   }

   private void setScrollingCacheEnabled(boolean var1) {
      if (this.mScrollingCacheEnabled != var1) {
         this.mScrollingCacheEnabled = var1;
      }

   }

   private void sortChildDrawingOrder() {
      if (this.mDrawingOrder != 0) {
         if (this.mDrawingOrderedChildren == null) {
            this.mDrawingOrderedChildren = new ArrayList();
         } else {
            this.mDrawingOrderedChildren.clear();
         }

         int var2 = this.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            View var3 = this.getChildAt(var1);
            this.mDrawingOrderedChildren.add(var3);
         }

         Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      int var5 = var1.size();
      int var6 = this.getDescendantFocusability();
      if (var6 != 393216) {
         for(int var4 = 0; var4 < this.getChildCount(); ++var4) {
            View var7 = this.getChildAt(var4);
            if (var7.getVisibility() == 0) {
               ViewPager.ItemInfo var8 = this.infoForChild(var7);
               if (var8 != null && var8.position == this.mCurItem) {
                  var7.addFocusables(var1, var2, var3);
               }
            }
         }
      }

      if ((var6 != 262144 || var5 == var1.size()) && this.isFocusable() && ((var3 & 1) != 1 || !this.isInTouchMode() || this.isFocusableInTouchMode()) && var1 != null) {
         var1.add(this);
      }

   }

   ViewPager.ItemInfo addNewItem(int var1, int var2) {
      ViewPager.ItemInfo var3 = new ViewPager.ItemInfo();
      var3.position = var1;
      var3.object = this.mAdapter.instantiateItem((ViewGroup)this, var1);
      var3.widthFactor = this.mAdapter.getPageWidth(var1);
      if (var2 >= 0 && var2 < this.mItems.size()) {
         this.mItems.add(var2, var3);
      } else {
         this.mItems.add(var3);
      }

      return var3;
   }

   public void addOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener var1) {
      if (this.mAdapterChangeListeners == null) {
         this.mAdapterChangeListeners = new ArrayList();
      }

      this.mAdapterChangeListeners.add(var1);
   }

   public void addOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      if (this.mOnPageChangeListeners == null) {
         this.mOnPageChangeListeners = new ArrayList();
      }

      this.mOnPageChangeListeners.add(var1);
   }

   public void addTouchables(ArrayList var1) {
      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3.getVisibility() == 0) {
            ViewPager.ItemInfo var4 = this.infoForChild(var3);
            if (var4 != null && var4.position == this.mCurItem) {
               var3.addTouchables(var1);
            }
         }
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      android.view.ViewGroup.LayoutParams var4 = var3;
      if (!this.checkLayoutParams(var3)) {
         var4 = this.generateLayoutParams(var3);
      }

      ViewPager.LayoutParams var5 = (ViewPager.LayoutParams)var4;
      var5.isDecor |= isDecorView(var1);
      if (this.mInLayout) {
         if (var5 != null && var5.isDecor) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
         }

         var5.needsMeasure = true;
         this.addViewInLayout(var1, var2, var4);
      } else {
         super.addView(var1, var2, var4);
      }

   }

   public boolean arrowScroll(int var1) {
      View var6 = this.findFocus();
      View var5;
      if (var6 == this) {
         var5 = null;
      } else {
         var5 = var6;
         if (var6 != null) {
            boolean var3 = false;
            ViewParent var10 = var6.getParent();

            boolean var2;
            while(true) {
               var2 = var3;
               if (!(var10 instanceof ViewGroup)) {
                  break;
               }

               if (var10 == this) {
                  var2 = true;
                  break;
               }

               var10 = var10.getParent();
            }

            var5 = var6;
            if (!var2) {
               StringBuilder var7 = new StringBuilder();
               var7.append(var6.getClass().getSimpleName());

               for(var10 = var6.getParent(); var10 instanceof ViewGroup; var10 = var10.getParent()) {
                  var7.append(" => ").append(var10.getClass().getSimpleName());
               }

               Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + var7.toString());
               var5 = null;
            }
         }
      }

      boolean var4 = false;
      var6 = FocusFinder.getInstance().findNextFocus(this, var5, var1);
      if (var6 != null && var6 != var5) {
         int var8;
         int var9;
         if (var1 == 17) {
            var8 = this.getChildRectInPagerCoordinates(this.mTempRect, var6).left;
            var9 = this.getChildRectInPagerCoordinates(this.mTempRect, var5).left;
            if (var5 != null && var8 >= var9) {
               var4 = this.pageLeft();
            } else {
               var4 = var6.requestFocus();
            }
         } else if (var1 == 66) {
            var9 = this.getChildRectInPagerCoordinates(this.mTempRect, var6).left;
            var8 = this.getChildRectInPagerCoordinates(this.mTempRect, var5).left;
            if (var5 != null && var9 <= var8) {
               var4 = this.pageRight();
            } else {
               var4 = var6.requestFocus();
            }
         }
      } else if (var1 != 17 && var1 != 1) {
         if (var1 == 66 || var1 == 2) {
            var4 = this.pageRight();
         }
      } else {
         var4 = this.pageLeft();
      }

      if (var4) {
         this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(var1));
      }

      return var4;
   }

   public boolean beginFakeDrag() {
      boolean var3 = false;
      if (!this.mIsBeingDragged) {
         this.mFakeDragging = true;
         this.setScrollState(1);
         this.mLastMotionX = 0.0F;
         this.mInitialMotionX = 0.0F;
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         } else {
            this.mVelocityTracker.clear();
         }

         long var1 = SystemClock.uptimeMillis();
         MotionEvent var4 = MotionEvent.obtain(var1, var1, 0, 0.0F, 0.0F, 0);
         this.mVelocityTracker.addMovement(var4);
         var4.recycle();
         this.mFakeDragBeginTime = var1;
         var3 = true;
      }

      return var3;
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var10 = (ViewGroup)var1;
         int var7 = var1.getScrollX();
         int var8 = var1.getScrollY();

         for(int var6 = var10.getChildCount() - 1; var6 >= 0; --var6) {
            View var9 = var10.getChildAt(var6);
            if (var4 + var7 >= var9.getLeft() && var4 + var7 < var9.getRight() && var5 + var8 >= var9.getTop() && var5 + var8 < var9.getBottom() && this.canScroll(var9, true, var3, var4 + var7 - var9.getLeft(), var5 + var8 - var9.getTop())) {
               var2 = true;
               return var2;
            }
         }
      }

      if (var2 && ViewCompat.canScrollHorizontally(var1, -var3)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canScrollHorizontally(int var1) {
      boolean var5 = true;
      boolean var6 = true;
      boolean var4 = false;
      if (this.mAdapter != null) {
         int var2 = this.getClientWidth();
         int var3 = this.getScrollX();
         if (var1 < 0) {
            if (var3 > (int)((float)var2 * this.mFirstOffset)) {
               var4 = var6;
            } else {
               var4 = false;
            }
         } else if (var1 > 0) {
            if (var3 < (int)((float)var2 * this.mLastOffset)) {
               var4 = var5;
            } else {
               var4 = false;
            }
         }
      }

      return var4;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof ViewPager.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void clearOnPageChangeListeners() {
      if (this.mOnPageChangeListeners != null) {
         this.mOnPageChangeListeners.clear();
      }

   }

   public void computeScroll() {
      this.mIsScrollStarted = true;
      if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
         int var3 = this.getScrollX();
         int var1 = this.getScrollY();
         int var2 = this.mScroller.getCurrX();
         int var4 = this.mScroller.getCurrY();
         if (var3 != var2 || var1 != var4) {
            this.scrollTo(var2, var4);
            if (!this.pageScrolled(var2)) {
               this.mScroller.abortAnimation();
               this.scrollTo(0, var4);
            }
         }

         ViewCompat.postInvalidateOnAnimation(this);
      } else {
         this.completeScroll(true);
      }

   }

   void dataSetChanged() {
      int var9 = this.mAdapter.getCount();
      this.mExpectedAdapterCount = var9;
      boolean var1;
      if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < var9) {
         var1 = true;
      } else {
         var1 = false;
      }

      int var2 = this.mCurItem;
      boolean var3 = false;

      int var4;
      for(int var5 = 0; var5 < this.mItems.size(); var2 = var4) {
         ViewPager.ItemInfo var10 = (ViewPager.ItemInfo)this.mItems.get(var5);
         int var8 = this.mAdapter.getItemPosition(var10.object);
         boolean var6;
         int var7;
         if (var8 == -1) {
            var4 = var2;
            var6 = var3;
            var7 = var5;
         } else if (var8 == -2) {
            this.mItems.remove(var5);
            var8 = var5 - 1;
            boolean var13 = var3;
            if (!var3) {
               this.mAdapter.startUpdate((ViewGroup)this);
               var13 = true;
            }

            this.mAdapter.destroyItem((ViewGroup)this, var10.position, var10.object);
            var1 = true;
            var7 = var8;
            var6 = var13;
            var4 = var2;
            if (this.mCurItem == var10.position) {
               var4 = Math.max(0, Math.min(this.mCurItem, var9 - 1));
               var1 = true;
               var7 = var8;
               var6 = var13;
            }
         } else {
            var7 = var5;
            var6 = var3;
            var4 = var2;
            if (var10.position != var8) {
               if (var10.position == this.mCurItem) {
                  var2 = var8;
               }

               var10.position = var8;
               var1 = true;
               var7 = var5;
               var6 = var3;
               var4 = var2;
            }
         }

         var5 = var7 + 1;
         var3 = var6;
      }

      if (var3) {
         this.mAdapter.finishUpdate((ViewGroup)this);
      }

      Collections.sort(this.mItems, COMPARATOR);
      if (var1) {
         int var12 = this.getChildCount();

         for(int var11 = 0; var11 < var12; ++var11) {
            ViewPager.LayoutParams var14 = (ViewPager.LayoutParams)this.getChildAt(var11).getLayoutParams();
            if (!var14.isDecor) {
               var14.widthFactor = 0.0F;
            }
         }

         this.setCurrentItemInternal(var2, false, true);
         this.requestLayout();
      }

   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var2;
      if (!super.dispatchKeyEvent(var1) && !this.executeKeyEvent(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      boolean var4;
      if (var1.getEventType() == 4096) {
         var4 = super.dispatchPopulateAccessibilityEvent(var1);
      } else {
         int var3 = this.getChildCount();
         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               var4 = false;
               break;
            }

            View var6 = this.getChildAt(var2);
            if (var6.getVisibility() == 0) {
               ViewPager.ItemInfo var5 = this.infoForChild(var6);
               if (var5 != null && var5.position == this.mCurItem && var6.dispatchPopulateAccessibilityEvent(var1)) {
                  var4 = true;
                  break;
               }
            }

            ++var2;
         }
      }

      return var4;
   }

   float distanceInfluenceForSnapDuration(float var1) {
      return (float)Math.sin((double)((float)((double)(var1 - 0.5F) * 0.4712389167638204D)));
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      boolean var3 = false;
      boolean var2 = false;
      int var4 = this.getOverScrollMode();
      if (var4 != 0 && (var4 != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
         this.mLeftEdge.finish();
         this.mRightEdge.finish();
      } else {
         int var9;
         if (!this.mLeftEdge.isFinished()) {
            var9 = var1.save();
            var4 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            int var8 = this.getWidth();
            var1.rotate(270.0F);
            var1.translate((float)(-var4 + this.getPaddingTop()), this.mFirstOffset * (float)var8);
            this.mLeftEdge.setSize(var4, var8);
            var2 = false | this.mLeftEdge.draw(var1);
            var1.restoreToCount(var9);
         }

         var3 = var2;
         if (!this.mRightEdge.isFinished()) {
            var4 = var1.save();
            int var6 = this.getWidth();
            int var7 = this.getHeight();
            var9 = this.getPaddingTop();
            int var5 = this.getPaddingBottom();
            var1.rotate(90.0F);
            var1.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float)var6);
            this.mRightEdge.setSize(var7 - var9 - var5, var6);
            var3 = var2 | this.mRightEdge.draw(var1);
            var1.restoreToCount(var4);
         }
      }

      if (var3) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      Drawable var1 = this.mMarginDrawable;
      if (var1 != null && var1.isStateful()) {
         var1.setState(this.getDrawableState());
      }

   }

   public void endFakeDrag() {
      if (!this.mFakeDragging) {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
      } else {
         if (this.mAdapter != null) {
            VelocityTracker var4 = this.mVelocityTracker;
            var4.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int var1 = (int)VelocityTrackerCompat.getXVelocity(var4, this.mActivePointerId);
            this.mPopulatePending = true;
            int var2 = this.getClientWidth();
            int var3 = this.getScrollX();
            ViewPager.ItemInfo var5 = this.infoForCurrentScrollPosition();
            this.setCurrentItemInternal(this.determineTargetPage(var5.position, ((float)var3 / (float)var2 - var5.offset) / var5.widthFactor, var1, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, var1);
         }

         this.endDrag();
         this.mFakeDragging = false;
      }
   }

   public boolean executeKeyEvent(KeyEvent var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1.getAction() == 0) {
         switch(var1.getKeyCode()) {
         case 21:
            var2 = this.arrowScroll(17);
            break;
         case 22:
            var2 = this.arrowScroll(66);
            break;
         case 61:
            var2 = var3;
            if (VERSION.SDK_INT >= 11) {
               if (KeyEventCompat.hasNoModifiers(var1)) {
                  var2 = this.arrowScroll(2);
               } else {
                  var2 = var3;
                  if (KeyEventCompat.hasModifiers(var1, 1)) {
                     var2 = this.arrowScroll(1);
                  }
               }
            }
            break;
         default:
            var2 = var3;
         }
      }

      return var2;
   }

   public void fakeDragBy(float var1) {
      if (!this.mFakeDragging) {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
      } else {
         if (this.mAdapter != null) {
            this.mLastMotionX += var1;
            float var3 = (float)this.getScrollX() - var1;
            int var4 = this.getClientWidth();
            var1 = (float)var4 * this.mFirstOffset;
            float var2 = (float)var4 * this.mLastOffset;
            ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(0);
            ViewPager.ItemInfo var8 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            if (var7.position != 0) {
               var1 = var7.offset * (float)var4;
            }

            if (var8.position != this.mAdapter.getCount() - 1) {
               var2 = var8.offset * (float)var4;
            }

            if (var3 >= var1) {
               var1 = var3;
               if (var3 > var2) {
                  var1 = var2;
               }
            }

            this.mLastMotionX += var1 - (float)((int)var1);
            this.scrollTo((int)var1, this.getScrollY());
            this.pageScrolled((int)var1);
            long var5 = SystemClock.uptimeMillis();
            MotionEvent var9 = MotionEvent.obtain(this.mFakeDragBeginTime, var5, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(var9);
            var9.recycle();
         }

      }
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new ViewPager.LayoutParams();
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ViewPager.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return this.generateDefaultLayoutParams();
   }

   public PagerAdapter getAdapter() {
      return this.mAdapter;
   }

   protected int getChildDrawingOrder(int var1, int var2) {
      if (this.mDrawingOrder == 2) {
         var1 = var1 - 1 - var2;
      } else {
         var1 = var2;
      }

      return ((ViewPager.LayoutParams)((View)this.mDrawingOrderedChildren.get(var1)).getLayoutParams()).childIndex;
   }

   public int getCurrentItem() {
      return this.mCurItem;
   }

   public int getOffscreenPageLimit() {
      return this.mOffscreenPageLimit;
   }

   public int getPageMargin() {
      return this.mPageMargin;
   }

   ViewPager.ItemInfo infoForAnyChild(View var1) {
      while(true) {
         ViewParent var2 = var1.getParent();
         ViewPager.ItemInfo var3;
         if (var2 != this) {
            if (var2 != null && var2 instanceof View) {
               var1 = (View)var2;
               continue;
            }

            var3 = null;
         } else {
            var3 = this.infoForChild(var1);
         }

         return var3;
      }
   }

   ViewPager.ItemInfo infoForChild(View var1) {
      int var2 = 0;

      ViewPager.ItemInfo var4;
      while(true) {
         if (var2 >= this.mItems.size()) {
            var4 = null;
            break;
         }

         ViewPager.ItemInfo var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (this.mAdapter.isViewFromObject(var1, var3.object)) {
            var4 = var3;
            break;
         }

         ++var2;
      }

      return var4;
   }

   ViewPager.ItemInfo infoForPosition(int var1) {
      int var2 = 0;

      ViewPager.ItemInfo var3;
      while(true) {
         if (var2 >= this.mItems.size()) {
            var3 = null;
            break;
         }

         var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (var3.position == var1) {
            break;
         }

         ++var2;
      }

      return var3;
   }

   void initViewPager() {
      this.setWillNotDraw(false);
      this.setDescendantFocusability(262144);
      this.setFocusable(true);
      Context var3 = this.getContext();
      this.mScroller = new Scroller(var3, sInterpolator);
      ViewConfiguration var2 = ViewConfiguration.get(var3);
      float var1 = var3.getResources().getDisplayMetrics().density;
      this.mTouchSlop = var2.getScaledPagingTouchSlop();
      this.mMinimumVelocity = (int)(400.0F * var1);
      this.mMaximumVelocity = var2.getScaledMaximumFlingVelocity();
      this.mLeftEdge = new EdgeEffectCompat(var3);
      this.mRightEdge = new EdgeEffectCompat(var3);
      this.mFlingDistance = (int)(25.0F * var1);
      this.mCloseEnough = (int)(2.0F * var1);
      this.mDefaultGutterSize = (int)(16.0F * var1);
      ViewCompat.setAccessibilityDelegate(this, new ViewPager.MyAccessibilityDelegate());
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         private final Rect mTempRect = new Rect();

         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            WindowInsetsCompat var6 = ViewCompat.onApplyWindowInsets(var1, var2);
            if (!var6.isConsumed()) {
               Rect var5 = this.mTempRect;
               var5.left = var6.getSystemWindowInsetLeft();
               var5.top = var6.getSystemWindowInsetTop();
               var5.right = var6.getSystemWindowInsetRight();
               var5.bottom = var6.getSystemWindowInsetBottom();
               int var3 = 0;

               for(int var4 = ViewPager.this.getChildCount(); var3 < var4; ++var3) {
                  var2 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(var3), var6);
                  var5.left = Math.min(var2.getSystemWindowInsetLeft(), var5.left);
                  var5.top = Math.min(var2.getSystemWindowInsetTop(), var5.top);
                  var5.right = Math.min(var2.getSystemWindowInsetRight(), var5.right);
                  var5.bottom = Math.min(var2.getSystemWindowInsetBottom(), var5.bottom);
               }

               var6 = var6.replaceSystemWindowInsets(var5.left, var5.top, var5.right, var5.bottom);
            }

            return var6;
         }
      });
   }

   public boolean isFakeDragging() {
      return this.mFakeDragging;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      this.removeCallbacks(this.mEndScrollRunnable);
      if (this.mScroller != null && !this.mScroller.isFinished()) {
         this.mScroller.abortAnimation();
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
         int var11 = this.getScrollX();
         int var10 = this.getWidth();
         float var4 = (float)this.mPageMargin / (float)var10;
         int var7 = 0;
         ViewPager.ItemInfo var12 = (ViewPager.ItemInfo)this.mItems.get(0);
         float var2 = var12.offset;
         int var9 = this.mItems.size();
         int var6 = var12.position;

         for(int var8 = ((ViewPager.ItemInfo)this.mItems.get(var9 - 1)).position; var6 < var8; ++var6) {
            while(var6 > var12.position && var7 < var9) {
               ArrayList var13 = this.mItems;
               ++var7;
               var12 = (ViewPager.ItemInfo)var13.get(var7);
            }

            float var3;
            if (var6 == var12.position) {
               var3 = (var12.offset + var12.widthFactor) * (float)var10;
               var2 = var12.offset + var12.widthFactor + var4;
            } else {
               float var5 = this.mAdapter.getPageWidth(var6);
               var3 = (var2 + var5) * (float)var10;
               var2 += var5 + var4;
            }

            if ((float)this.mPageMargin + var3 > (float)var11) {
               this.mMarginDrawable.setBounds(Math.round(var3), this.mTopPageBounds, Math.round((float)this.mPageMargin + var3), this.mBottomPageBounds);
               this.mMarginDrawable.draw(var1);
            }

            if (var3 > (float)(var11 + var10)) {
               break;
            }
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var7 = var1.getAction() & 255;
      boolean var8;
      if (var7 != 3 && var7 != 1) {
         if (var7 != 0) {
            if (this.mIsBeingDragged) {
               var8 = true;
               return var8;
            }

            if (this.mIsUnableToDrag) {
               var8 = false;
               return var8;
            }
         }

         float var2;
         switch(var7) {
         case 0:
            var2 = var1.getX();
            this.mInitialMotionX = var2;
            this.mLastMotionX = var2;
            var2 = var1.getY();
            this.mInitialMotionY = var2;
            this.mLastMotionY = var2;
            this.mActivePointerId = var1.getPointerId(0);
            this.mIsUnableToDrag = false;
            this.mIsScrollStarted = true;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
               this.mScroller.abortAnimation();
               this.mPopulatePending = false;
               this.populate();
               this.mIsBeingDragged = true;
               this.requestParentDisallowInterceptTouchEvent(true);
               this.setScrollState(1);
            } else {
               this.completeScroll(false);
               this.mIsBeingDragged = false;
            }
            break;
         case 2:
            var7 = this.mActivePointerId;
            if (var7 != -1) {
               var7 = var1.findPointerIndex(var7);
               float var3 = var1.getX(var7);
               var2 = var3 - this.mLastMotionX;
               float var6 = Math.abs(var2);
               float var4 = var1.getY(var7);
               float var5 = Math.abs(var4 - this.mInitialMotionY);
               if (var2 != 0.0F && !this.isGutterDrag(this.mLastMotionX, var2) && this.canScroll(this, false, (int)var2, (int)var3, (int)var4)) {
                  this.mLastMotionX = var3;
                  this.mLastMotionY = var4;
                  this.mIsUnableToDrag = true;
                  var8 = false;
                  return var8;
               }

               if (var6 > (float)this.mTouchSlop && 0.5F * var6 > var5) {
                  this.mIsBeingDragged = true;
                  this.requestParentDisallowInterceptTouchEvent(true);
                  this.setScrollState(1);
                  if (var2 > 0.0F) {
                     var2 = this.mInitialMotionX + (float)this.mTouchSlop;
                  } else {
                     var2 = this.mInitialMotionX - (float)this.mTouchSlop;
                  }

                  this.mLastMotionX = var2;
                  this.mLastMotionY = var4;
                  this.setScrollingCacheEnabled(true);
               } else if (var5 > (float)this.mTouchSlop) {
                  this.mIsUnableToDrag = true;
               }

               if (this.mIsBeingDragged && this.performDrag(var3)) {
                  ViewCompat.postInvalidateOnAnimation(this);
               }
            }
            break;
         case 6:
            this.onSecondaryPointerUp(var1);
         }

         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         var8 = this.mIsBeingDragged;
      } else {
         this.resetTouch();
         var8 = false;
      }

      return var8;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var13 = this.getChildCount();
      int var15 = var4 - var2;
      int var14 = var5 - var3;
      var3 = this.getPaddingLeft();
      var2 = this.getPaddingTop();
      int var7 = this.getPaddingRight();
      var5 = this.getPaddingBottom();
      int var16 = this.getScrollX();
      int var8 = 0;

      int var6;
      for(int var9 = 0; var9 < var13; var2 = var4) {
         View var18 = this.getChildAt(var9);
         int var12 = var8;
         int var11 = var5;
         var6 = var3;
         int var10 = var7;
         var4 = var2;
         if (var18.getVisibility() != 8) {
            ViewPager.LayoutParams var17 = (ViewPager.LayoutParams)var18.getLayoutParams();
            var12 = var8;
            var11 = var5;
            var6 = var3;
            var10 = var7;
            var4 = var2;
            if (var17.isDecor) {
               var4 = var17.gravity;
               var10 = var17.gravity;
               switch(var4 & 7) {
               case 1:
                  var4 = Math.max((var15 - var18.getMeasuredWidth()) / 2, var3);
                  var6 = var3;
                  break;
               case 2:
               case 4:
               default:
                  var4 = var3;
                  var6 = var3;
                  break;
               case 3:
                  var4 = var3;
                  var6 = var3 + var18.getMeasuredWidth();
                  break;
               case 5:
                  var4 = var15 - var7 - var18.getMeasuredWidth();
                  var7 += var18.getMeasuredWidth();
                  var6 = var3;
               }

               switch(var10 & 112) {
               case 16:
                  var3 = Math.max((var14 - var18.getMeasuredHeight()) / 2, var2);
                  break;
               case 48:
                  var3 = var2;
                  var2 += var18.getMeasuredHeight();
                  break;
               case 80:
                  var3 = var14 - var5 - var18.getMeasuredHeight();
                  var5 += var18.getMeasuredHeight();
                  break;
               default:
                  var3 = var2;
               }

               var4 += var16;
               var18.layout(var4, var3, var18.getMeasuredWidth() + var4, var18.getMeasuredHeight() + var3);
               var12 = var8 + 1;
               var4 = var2;
               var10 = var7;
               var11 = var5;
            }
         }

         ++var9;
         var8 = var12;
         var5 = var11;
         var3 = var6;
         var7 = var10;
      }

      var6 = var15 - var3 - var7;

      for(var4 = 0; var4 < var13; ++var4) {
         View var20 = this.getChildAt(var4);
         if (var20.getVisibility() != 8) {
            ViewPager.LayoutParams var21 = (ViewPager.LayoutParams)var20.getLayoutParams();
            if (!var21.isDecor) {
               ViewPager.ItemInfo var19 = this.infoForChild(var20);
               if (var19 != null) {
                  var7 = var3 + (int)((float)var6 * var19.offset);
                  if (var21.needsMeasure) {
                     var21.needsMeasure = false;
                     var20.measure(MeasureSpec.makeMeasureSpec((int)((float)var6 * var21.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec(var14 - var2 - var5, 1073741824));
                  }

                  var20.layout(var7, var2, var20.getMeasuredWidth() + var7, var20.getMeasuredHeight() + var2);
               }
            }
         }
      }

      this.mTopPageBounds = var2;
      this.mBottomPageBounds = var14 - var5;
      this.mDecorChildCount = var8;
      if (this.mFirstLayout) {
         this.scrollToItem(this.mCurItem, false, 0, false);
      }

      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(getDefaultSize(0, var1), getDefaultSize(0, var2));
      var1 = this.getMeasuredWidth();
      this.mGutterSize = Math.min(var1 / 10, this.mDefaultGutterSize);
      var1 = var1 - this.getPaddingLeft() - this.getPaddingRight();
      var2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
      int var12 = this.getChildCount();

      int var3;
      for(int var5 = 0; var5 < var12; var1 = var3) {
         View var13 = this.getChildAt(var5);
         int var4 = var2;
         var3 = var1;
         if (var13.getVisibility() != 8) {
            ViewPager.LayoutParams var14 = (ViewPager.LayoutParams)var13.getLayoutParams();
            var4 = var2;
            var3 = var1;
            if (var14 != null) {
               var4 = var2;
               var3 = var1;
               if (var14.isDecor) {
                  var4 = var14.gravity & 7;
                  int var6 = var14.gravity & 112;
                  int var8 = Integer.MIN_VALUE;
                  var3 = Integer.MIN_VALUE;
                  boolean var15;
                  if (var6 != 48 && var6 != 80) {
                     var15 = false;
                  } else {
                     var15 = true;
                  }

                  boolean var7;
                  if (var4 != 3 && var4 != 5) {
                     var7 = false;
                  } else {
                     var7 = true;
                  }

                  if (var15) {
                     var4 = 1073741824;
                  } else {
                     var4 = var8;
                     if (var7) {
                        var3 = 1073741824;
                        var4 = var8;
                     }
                  }

                  int var9 = var1;
                  int var11;
                  if (var14.width != -2) {
                     var11 = 1073741824;
                     var4 = var11;
                     var9 = var1;
                     if (var14.width != -1) {
                        var9 = var14.width;
                        var4 = var11;
                     }
                  }

                  int var10 = var3;
                  var3 = var2;
                  if (var14.height != -2) {
                     var11 = 1073741824;
                     var10 = var11;
                     var3 = var2;
                     if (var14.height != -1) {
                        var3 = var14.height;
                        var10 = var11;
                     }
                  }

                  var13.measure(MeasureSpec.makeMeasureSpec(var9, var4), MeasureSpec.makeMeasureSpec(var3, var10));
                  if (var15) {
                     var4 = var2 - var13.getMeasuredHeight();
                     var3 = var1;
                  } else {
                     var4 = var2;
                     var3 = var1;
                     if (var7) {
                        var3 = var1 - var13.getMeasuredWidth();
                        var4 = var2;
                     }
                  }
               }
            }
         }

         ++var5;
         var2 = var4;
      }

      this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(var1, 1073741824);
      this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(var2, 1073741824);
      this.mInLayout = true;
      this.populate();
      this.mInLayout = false;
      var3 = this.getChildCount();

      for(var2 = 0; var2 < var3; ++var2) {
         View var17 = this.getChildAt(var2);
         if (var17.getVisibility() != 8) {
            ViewPager.LayoutParams var16 = (ViewPager.LayoutParams)var17.getLayoutParams();
            if (var16 == null || !var16.isDecor) {
               var17.measure(MeasureSpec.makeMeasureSpec((int)((float)var1 * var16.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
            }
         }
      }

   }

   @CallSuper
   protected void onPageScrolled(int var1, float var2, int var3) {
      int var4;
      if (this.mDecorChildCount > 0) {
         int var11 = this.getScrollX();
         var4 = this.getPaddingLeft();
         int var6 = this.getPaddingRight();
         int var9 = this.getWidth();
         int var10 = this.getChildCount();

         int var5;
         for(int var7 = 0; var7 < var10; var6 = var5) {
            View var14 = this.getChildAt(var7);
            ViewPager.LayoutParams var13 = (ViewPager.LayoutParams)var14.getLayoutParams();
            int var8;
            if (!var13.isDecor) {
               var5 = var6;
               var8 = var4;
            } else {
               switch(var13.gravity & 7) {
               case 1:
                  var5 = Math.max((var9 - var14.getMeasuredWidth()) / 2, var4);
                  break;
               case 2:
               case 4:
               default:
                  var5 = var4;
                  break;
               case 3:
                  var5 = var4;
                  var4 += var14.getWidth();
                  break;
               case 5:
                  var5 = var9 - var6 - var14.getMeasuredWidth();
                  var6 += var14.getMeasuredWidth();
               }

               int var12 = var5 + var11 - var14.getLeft();
               var8 = var4;
               var5 = var6;
               if (var12 != 0) {
                  var14.offsetLeftAndRight(var12);
                  var8 = var4;
                  var5 = var6;
               }
            }

            ++var7;
            var4 = var8;
         }
      }

      this.dispatchOnPageScrolled(var1, var2, var3);
      if (this.mPageTransformer != null) {
         var4 = this.getScrollX();
         var3 = this.getChildCount();

         for(var1 = 0; var1 < var3; ++var1) {
            View var15 = this.getChildAt(var1);
            if (!((ViewPager.LayoutParams)var15.getLayoutParams()).isDecor) {
               var2 = (float)(var15.getLeft() - var4) / (float)this.getClientWidth();
               this.mPageTransformer.transformPage(var15, var2);
            }
         }
      }

      this.mCalledSuper = true;
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      int var5 = this.getChildCount();
      int var3;
      byte var4;
      if ((var1 & 2) != 0) {
         var3 = 0;
         var4 = 1;
      } else {
         var3 = var5 - 1;
         var4 = -1;
         var5 = -1;
      }

      boolean var6;
      while(true) {
         if (var3 == var5) {
            var6 = false;
            break;
         }

         View var8 = this.getChildAt(var3);
         if (var8.getVisibility() == 0) {
            ViewPager.ItemInfo var7 = this.infoForChild(var8);
            if (var7 != null && var7.position == this.mCurItem && var8.requestFocus(var1, var2)) {
               var6 = true;
               break;
            }
         }

         var3 += var4;
      }

      return var6;
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof ViewPager.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         ViewPager.SavedState var2 = (ViewPager.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if (this.mAdapter != null) {
            this.mAdapter.restoreState(var2.adapterState, var2.loader);
            this.setCurrentItemInternal(var2.position, false, true);
         } else {
            this.mRestoredCurItem = var2.position;
            this.mRestoredAdapterState = var2.adapterState;
            this.mRestoredClassLoader = var2.loader;
         }
      }

   }

   public Parcelable onSaveInstanceState() {
      ViewPager.SavedState var1 = new ViewPager.SavedState(super.onSaveInstanceState());
      var1.position = this.mCurItem;
      if (this.mAdapter != null) {
         var1.adapterState = this.mAdapter.saveState();
      }

      return var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         this.recomputeScrollPosition(var1, var3, this.mPageMargin, this.mPageMargin);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var9;
      if (this.mFakeDragging) {
         var9 = true;
      } else if (var1.getAction() == 0 && var1.getEdgeFlags() != 0) {
         var9 = false;
      } else if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         int var6 = var1.getAction();
         boolean var10 = false;
         var9 = var10;
         float var2;
         switch(var6 & 255) {
         case 0:
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            this.populate();
            var2 = var1.getX();
            this.mInitialMotionX = var2;
            this.mLastMotionX = var2;
            var2 = var1.getY();
            this.mInitialMotionY = var2;
            this.mLastMotionY = var2;
            this.mActivePointerId = var1.getPointerId(0);
            var9 = var10;
            break;
         case 1:
            var9 = var10;
            if (this.mIsBeingDragged) {
               VelocityTracker var12 = this.mVelocityTracker;
               var12.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
               int var8 = (int)VelocityTrackerCompat.getXVelocity(var12, this.mActivePointerId);
               this.mPopulatePending = true;
               int var7 = this.getClientWidth();
               var6 = this.getScrollX();
               ViewPager.ItemInfo var13 = this.infoForCurrentScrollPosition();
               var2 = (float)this.mPageMargin / (float)var7;
               this.setCurrentItemInternal(this.determineTargetPage(var13.position, ((float)var6 / (float)var7 - var13.offset) / (var13.widthFactor + var2), var8, (int)(var1.getX(var1.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, var8);
               var9 = this.resetTouch();
            }
            break;
         case 2:
            if (!this.mIsBeingDragged) {
               var6 = var1.findPointerIndex(this.mActivePointerId);
               if (var6 == -1) {
                  var9 = this.resetTouch();
                  break;
               }

               float var4 = var1.getX(var6);
               var2 = Math.abs(var4 - this.mLastMotionX);
               float var3 = var1.getY(var6);
               float var5 = Math.abs(var3 - this.mLastMotionY);
               if (var2 > (float)this.mTouchSlop && var2 > var5) {
                  this.mIsBeingDragged = true;
                  this.requestParentDisallowInterceptTouchEvent(true);
                  if (var4 - this.mInitialMotionX > 0.0F) {
                     var2 = this.mInitialMotionX + (float)this.mTouchSlop;
                  } else {
                     var2 = this.mInitialMotionX - (float)this.mTouchSlop;
                  }

                  this.mLastMotionX = var2;
                  this.mLastMotionY = var3;
                  this.setScrollState(1);
                  this.setScrollingCacheEnabled(true);
                  ViewParent var11 = this.getParent();
                  if (var11 != null) {
                     var11.requestDisallowInterceptTouchEvent(true);
                  }
               }
            }

            var9 = var10;
            if (this.mIsBeingDragged) {
               var9 = false | this.performDrag(var1.getX(var1.findPointerIndex(this.mActivePointerId)));
            }
            break;
         case 3:
            var9 = var10;
            if (this.mIsBeingDragged) {
               this.scrollToItem(this.mCurItem, true, 0, false);
               var9 = this.resetTouch();
            }
         case 4:
            break;
         case 5:
            var6 = MotionEventCompat.getActionIndex(var1);
            this.mLastMotionX = var1.getX(var6);
            this.mActivePointerId = var1.getPointerId(var6);
            var9 = var10;
            break;
         case 6:
            this.onSecondaryPointerUp(var1);
            this.mLastMotionX = var1.getX(var1.findPointerIndex(this.mActivePointerId));
            var9 = var10;
            break;
         default:
            var9 = var10;
         }

         if (var9) {
            ViewCompat.postInvalidateOnAnimation(this);
         }

         var9 = true;
      } else {
         var9 = false;
      }

      return var9;
   }

   boolean pageLeft() {
      boolean var1 = true;
      if (this.mCurItem > 0) {
         this.setCurrentItem(this.mCurItem - 1, true);
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean pageRight() {
      boolean var1 = true;
      if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
         this.setCurrentItem(this.mCurItem + 1, true);
      } else {
         var1 = false;
      }

      return var1;
   }

   void populate() {
      this.populate(this.mCurItem);
   }

   void populate(int var1) {
      ViewPager.ItemInfo var14 = null;
      if (this.mCurItem != var1) {
         var14 = this.infoForPosition(this.mCurItem);
         this.mCurItem = var1;
      }

      if (this.mAdapter == null) {
         this.sortChildDrawingOrder();
      } else if (this.mPopulatePending) {
         this.sortChildDrawingOrder();
      } else if (this.getWindowToken() != null) {
         this.mAdapter.startUpdate((ViewGroup)this);
         var1 = this.mOffscreenPageLimit;
         int var11 = Math.max(0, this.mCurItem - var1);
         int var9 = this.mAdapter.getCount();
         int var10 = Math.min(var9 - 1, this.mCurItem + var1);
         if (var9 != this.mExpectedAdapterCount) {
            String var23;
            try {
               var23 = this.getResources().getResourceName(this.getId());
            } catch (NotFoundException var17) {
               var23 = Integer.toHexString(this.getId());
            }

            throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + var9 + " Pager id: " + var23 + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
         }

         ViewPager.ItemInfo var15 = null;
         var1 = 0;

         ViewPager.ItemInfo var13;
         ViewPager.ItemInfo var16;
         while(true) {
            var13 = var15;
            if (var1 >= this.mItems.size()) {
               break;
            }

            var16 = (ViewPager.ItemInfo)this.mItems.get(var1);
            if (var16.position >= this.mCurItem) {
               var13 = var15;
               if (var16.position == this.mCurItem) {
                  var13 = var16;
               }
               break;
            }

            ++var1;
         }

         var15 = var13;
         if (var13 == null) {
            var15 = var13;
            if (var9 > 0) {
               var15 = this.addNewItem(this.mCurItem, var1);
            }
         }

         int var5;
         if (var15 != null) {
            float var4 = 0.0F;
            int var8 = var1 - 1;
            if (var8 >= 0) {
               var13 = (ViewPager.ItemInfo)this.mItems.get(var8);
            } else {
               var13 = null;
            }

            int var12 = this.getClientWidth();
            float var3;
            if (var12 <= 0) {
               var3 = 0.0F;
            } else {
               var3 = 2.0F - var15.widthFactor + (float)this.getPaddingLeft() / (float)var12;
            }

            int var7 = this.mCurItem - 1;
            var16 = var13;

            float var2;
            int var6;
            for(var6 = var1; var7 >= 0; var8 = var5) {
               if (var4 >= var3 && var7 < var11) {
                  if (var16 == null) {
                     break;
                  }

                  var1 = var6;
                  var2 = var4;
                  var13 = var16;
                  var5 = var8;
                  if (var7 == var16.position) {
                     var1 = var6;
                     var2 = var4;
                     var13 = var16;
                     var5 = var8;
                     if (!var16.scrolling) {
                        this.mItems.remove(var8);
                        this.mAdapter.destroyItem((ViewGroup)this, var7, var16.object);
                        var5 = var8 - 1;
                        var1 = var6 - 1;
                        if (var5 >= 0) {
                           var13 = (ViewPager.ItemInfo)this.mItems.get(var5);
                           var2 = var4;
                        } else {
                           var13 = null;
                           var2 = var4;
                        }
                     }
                  }
               } else if (var16 != null && var7 == var16.position) {
                  var2 = var4 + var16.widthFactor;
                  var5 = var8 - 1;
                  if (var5 >= 0) {
                     var13 = (ViewPager.ItemInfo)this.mItems.get(var5);
                  } else {
                     var13 = null;
                  }

                  var1 = var6;
               } else {
                  var2 = var4 + this.addNewItem(var7, var8 + 1).widthFactor;
                  var1 = var6 + 1;
                  if (var8 >= 0) {
                     var13 = (ViewPager.ItemInfo)this.mItems.get(var8);
                  } else {
                     var13 = null;
                  }

                  var5 = var8;
               }

               --var7;
               var6 = var1;
               var4 = var2;
               var16 = var13;
            }

            var4 = var15.widthFactor;
            var7 = var6 + 1;
            if (var4 < 2.0F) {
               if (var7 < this.mItems.size()) {
                  var13 = (ViewPager.ItemInfo)this.mItems.get(var7);
               } else {
                  var13 = null;
               }

               if (var12 <= 0) {
                  var3 = 0.0F;
               } else {
                  var3 = (float)this.getPaddingRight() / (float)var12 + 2.0F;
               }

               var5 = this.mCurItem + 1;

               for(var16 = var13; var5 < var9; var7 = var1) {
                  if (var4 >= var3 && var5 > var10) {
                     if (var16 == null) {
                        break;
                     }

                     var2 = var4;
                     var13 = var16;
                     var1 = var7;
                     if (var5 == var16.position) {
                        var2 = var4;
                        var13 = var16;
                        var1 = var7;
                        if (!var16.scrolling) {
                           this.mItems.remove(var7);
                           this.mAdapter.destroyItem((ViewGroup)this, var5, var16.object);
                           if (var7 < this.mItems.size()) {
                              var13 = (ViewPager.ItemInfo)this.mItems.get(var7);
                              var1 = var7;
                              var2 = var4;
                           } else {
                              var13 = null;
                              var2 = var4;
                              var1 = var7;
                           }
                        }
                     }
                  } else if (var16 != null && var5 == var16.position) {
                     var2 = var4 + var16.widthFactor;
                     var1 = var7 + 1;
                     if (var1 < this.mItems.size()) {
                        var13 = (ViewPager.ItemInfo)this.mItems.get(var1);
                     } else {
                        var13 = null;
                     }
                  } else {
                     var13 = this.addNewItem(var5, var7);
                     var1 = var7 + 1;
                     var2 = var4 + var13.widthFactor;
                     if (var1 < this.mItems.size()) {
                        var13 = (ViewPager.ItemInfo)this.mItems.get(var1);
                     } else {
                        var13 = null;
                     }
                  }

                  ++var5;
                  var4 = var2;
                  var16 = var13;
               }
            }

            this.calculatePageOffsets(var15, var6, var14);
         }

         PagerAdapter var18 = this.mAdapter;
         var1 = this.mCurItem;
         Object var20;
         if (var15 != null) {
            var20 = var15.object;
         } else {
            var20 = null;
         }

         var18.setPrimaryItem((ViewGroup)this, var1, var20);
         this.mAdapter.finishUpdate((ViewGroup)this);
         var5 = this.getChildCount();

         View var19;
         for(var1 = 0; var1 < var5; ++var1) {
            var19 = this.getChildAt(var1);
            ViewPager.LayoutParams var21 = (ViewPager.LayoutParams)var19.getLayoutParams();
            var21.childIndex = var1;
            if (!var21.isDecor && var21.widthFactor == 0.0F) {
               var14 = this.infoForChild(var19);
               if (var14 != null) {
                  var21.widthFactor = var14.widthFactor;
                  var21.position = var14.position;
               }
            }
         }

         this.sortChildDrawingOrder();
         if (this.hasFocus()) {
            View var22 = this.findFocus();
            if (var22 != null) {
               var13 = this.infoForAnyChild(var22);
            } else {
               var13 = null;
            }

            if (var13 == null || var13.position != this.mCurItem) {
               for(var1 = 0; var1 < this.getChildCount(); ++var1) {
                  var19 = this.getChildAt(var1);
                  var13 = this.infoForChild(var19);
                  if (var13 != null && var13.position == this.mCurItem && var19.requestFocus(2)) {
                     break;
                  }
               }
            }
         }
      }

   }

   public void removeOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener var1) {
      if (this.mAdapterChangeListeners != null) {
         this.mAdapterChangeListeners.remove(var1);
      }

   }

   public void removeOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      if (this.mOnPageChangeListeners != null) {
         this.mOnPageChangeListeners.remove(var1);
      }

   }

   public void removeView(View var1) {
      if (this.mInLayout) {
         this.removeViewInLayout(var1);
      } else {
         super.removeView(var1);
      }

   }

   public void setAdapter(PagerAdapter var1) {
      int var2;
      if (this.mAdapter != null) {
         this.mAdapter.setViewPagerObserver((DataSetObserver)null);
         this.mAdapter.startUpdate((ViewGroup)this);

         for(var2 = 0; var2 < this.mItems.size(); ++var2) {
            ViewPager.ItemInfo var5 = (ViewPager.ItemInfo)this.mItems.get(var2);
            this.mAdapter.destroyItem((ViewGroup)this, var5.position, var5.object);
         }

         this.mAdapter.finishUpdate((ViewGroup)this);
         this.mItems.clear();
         this.removeNonDecorViews();
         this.mCurItem = 0;
         this.scrollTo(0, 0);
      }

      PagerAdapter var6 = this.mAdapter;
      this.mAdapter = var1;
      this.mExpectedAdapterCount = 0;
      if (this.mAdapter != null) {
         if (this.mObserver == null) {
            this.mObserver = new ViewPager.PagerObserver();
         }

         this.mAdapter.setViewPagerObserver(this.mObserver);
         this.mPopulatePending = false;
         boolean var4 = this.mFirstLayout;
         this.mFirstLayout = true;
         this.mExpectedAdapterCount = this.mAdapter.getCount();
         if (this.mRestoredCurItem >= 0) {
            this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
            this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
            this.mRestoredCurItem = -1;
            this.mRestoredAdapterState = null;
            this.mRestoredClassLoader = null;
         } else if (!var4) {
            this.populate();
         } else {
            this.requestLayout();
         }
      }

      if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
         var2 = 0;

         for(int var3 = this.mAdapterChangeListeners.size(); var2 < var3; ++var2) {
            ((ViewPager.OnAdapterChangeListener)this.mAdapterChangeListeners.get(var2)).onAdapterChanged(this, var6, var1);
         }
      }

   }

   void setChildrenDrawingOrderEnabledCompat(boolean var1) {
      if (VERSION.SDK_INT >= 7) {
         if (this.mSetChildrenDrawingOrderEnabled == null) {
            try {
               this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            } catch (NoSuchMethodException var4) {
               Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", var4);
            }
         }

         try {
            this.mSetChildrenDrawingOrderEnabled.invoke(this, var1);
         } catch (Exception var3) {
            Log.e("ViewPager", "Error changing children drawing order", var3);
         }
      }

   }

   public void setCurrentItem(int var1) {
      this.mPopulatePending = false;
      boolean var2;
      if (!this.mFirstLayout) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setCurrentItemInternal(var1, var2, false);
   }

   public void setCurrentItem(int var1, boolean var2) {
      this.mPopulatePending = false;
      this.setCurrentItemInternal(var1, var2, false);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3) {
      this.setCurrentItemInternal(var1, var2, var3, 0);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3, int var4) {
      boolean var6 = true;
      if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
         if (!var3 && this.mCurItem == var1 && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
         } else {
            int var5;
            if (var1 < 0) {
               var5 = 0;
            } else {
               var5 = var1;
               if (var1 >= this.mAdapter.getCount()) {
                  var5 = this.mAdapter.getCount() - 1;
               }
            }

            var1 = this.mOffscreenPageLimit;
            if (var5 > this.mCurItem + var1 || var5 < this.mCurItem - var1) {
               for(var1 = 0; var1 < this.mItems.size(); ++var1) {
                  ((ViewPager.ItemInfo)this.mItems.get(var1)).scrolling = true;
               }
            }

            if (this.mCurItem != var5) {
               var3 = var6;
            } else {
               var3 = false;
            }

            if (this.mFirstLayout) {
               this.mCurItem = var5;
               if (var3) {
                  this.dispatchOnPageSelected(var5);
               }

               this.requestLayout();
            } else {
               this.populate(var5);
               this.scrollToItem(var5, var2, var4, var3);
            }
         }
      } else {
         this.setScrollingCacheEnabled(false);
      }

   }

   ViewPager.OnPageChangeListener setInternalPageChangeListener(ViewPager.OnPageChangeListener var1) {
      ViewPager.OnPageChangeListener var2 = this.mInternalPageChangeListener;
      this.mInternalPageChangeListener = var1;
      return var2;
   }

   public void setOffscreenPageLimit(int var1) {
      int var2 = var1;
      if (var1 < 1) {
         Log.w("ViewPager", "Requested offscreen page limit " + var1 + " too small; defaulting to " + 1);
         var2 = 1;
      }

      if (var2 != this.mOffscreenPageLimit) {
         this.mOffscreenPageLimit = var2;
         this.populate();
      }

   }

   @Deprecated
   public void setOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      this.mOnPageChangeListener = var1;
   }

   public void setPageMargin(int var1) {
      int var3 = this.mPageMargin;
      this.mPageMargin = var1;
      int var2 = this.getWidth();
      this.recomputeScrollPosition(var2, var2, var1, var3);
      this.requestLayout();
   }

   public void setPageMarginDrawable(@DrawableRes int var1) {
      this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setPageMarginDrawable(Drawable var1) {
      this.mMarginDrawable = var1;
      if (var1 != null) {
         this.refreshDrawableState();
      }

      boolean var2;
      if (var1 == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setPageTransformer(boolean var1, ViewPager.PageTransformer var2) {
      this.setPageTransformer(var1, var2, 2);
   }

   public void setPageTransformer(boolean var1, ViewPager.PageTransformer var2, int var3) {
      byte var5 = 1;
      if (VERSION.SDK_INT >= 11) {
         boolean var6;
         if (var2 != null) {
            var6 = true;
         } else {
            var6 = false;
         }

         boolean var7;
         if (this.mPageTransformer != null) {
            var7 = true;
         } else {
            var7 = false;
         }

         boolean var4;
         if (var6 != var7) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.mPageTransformer = var2;
         this.setChildrenDrawingOrderEnabledCompat(var6);
         if (var6) {
            if (var1) {
               var5 = 2;
            }

            this.mDrawingOrder = var5;
            this.mPageTransformerLayerType = var3;
         } else {
            this.mDrawingOrder = 0;
         }

         if (var4) {
            this.populate();
         }
      }

   }

   void setScrollState(int var1) {
      if (this.mScrollState != var1) {
         this.mScrollState = var1;
         if (this.mPageTransformer != null) {
            boolean var2;
            if (var1 != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.enableLayers(var2);
         }

         this.dispatchOnScrollStateChanged(var1);
      }

   }

   void smoothScrollTo(int var1, int var2) {
      this.smoothScrollTo(var1, var2, 0);
   }

   void smoothScrollTo(int var1, int var2, int var3) {
      if (this.getChildCount() == 0) {
         this.setScrollingCacheEnabled(false);
      } else {
         boolean var7;
         if (this.mScroller != null && !this.mScroller.isFinished()) {
            var7 = true;
         } else {
            var7 = false;
         }

         int var11;
         if (var7) {
            if (this.mIsScrollStarted) {
               var11 = this.mScroller.getCurrX();
            } else {
               var11 = this.mScroller.getStartX();
            }

            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
         } else {
            var11 = this.getScrollX();
         }

         int var8 = this.getScrollY();
         int var9 = var1 - var11;
         var2 -= var8;
         if (var9 == 0 && var2 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
         } else {
            this.setScrollingCacheEnabled(true);
            this.setScrollState(2);
            var1 = this.getClientWidth();
            int var10 = var1 / 2;
            float var6 = Math.min(1.0F, 1.0F * (float)Math.abs(var9) / (float)var1);
            float var5 = (float)var10;
            float var4 = (float)var10;
            var6 = this.distanceInfluenceForSnapDuration(var6);
            var3 = Math.abs(var3);
            if (var3 > 0) {
               var1 = Math.round(1000.0F * Math.abs((var5 + var4 * var6) / (float)var3)) * 4;
            } else {
               var5 = (float)var1;
               var4 = this.mAdapter.getPageWidth(this.mCurItem);
               var1 = (int)((1.0F + (float)Math.abs(var9) / ((float)this.mPageMargin + var5 * var4)) * 100.0F);
            }

            var1 = Math.min(var1, 600);
            this.mIsScrollStarted = false;
            this.mScroller.startScroll(var11, var8, var9, var2, var1);
            ViewCompat.postInvalidateOnAnimation(this);
         }
      }

   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (!super.verifyDrawable(var1) && var1 != this.mMarginDrawable) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface DecorView {
   }

   static class ItemInfo {
      Object object;
      float offset;
      int position;
      boolean scrolling;
      float widthFactor;
   }

   public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
      int childIndex;
      public int gravity;
      public boolean isDecor;
      boolean needsMeasure;
      int position;
      float widthFactor = 0.0F;

      public LayoutParams() {
         super(-1, -1);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, ViewPager.LAYOUT_ATTRS);
         this.gravity = var3.getInteger(0, 48);
         var3.recycle();
      }
   }

   class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
      private boolean canScroll() {
         boolean var1 = true;
         if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1) {
            var1 = false;
         }

         return var1;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(ViewPager.class.getName());
         AccessibilityRecordCompat var3 = AccessibilityEventCompat.asRecord(var2);
         var3.setScrollable(this.canScroll());
         if (var2.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
            var3.setItemCount(ViewPager.this.mAdapter.getCount());
            var3.setFromIndex(ViewPager.this.mCurItem);
            var3.setToIndex(ViewPager.this.mCurItem);
         }

      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setClassName(ViewPager.class.getName());
         var2.setScrollable(this.canScroll());
         if (ViewPager.this.canScrollHorizontally(1)) {
            var2.addAction(4096);
         }

         if (ViewPager.this.canScrollHorizontally(-1)) {
            var2.addAction(8192);
         }

      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         boolean var4 = true;
         if (!super.performAccessibilityAction(var1, var2, var3)) {
            switch(var2) {
            case 4096:
               if (ViewPager.this.canScrollHorizontally(1)) {
                  ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
               } else {
                  var4 = false;
               }
               break;
            case 8192:
               if (ViewPager.this.canScrollHorizontally(-1)) {
                  ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
               } else {
                  var4 = false;
               }
               break;
            default:
               var4 = false;
            }
         }

         return var4;
      }
   }

   public interface OnAdapterChangeListener {
      void onAdapterChanged(@NonNull ViewPager var1, @Nullable PagerAdapter var2, @Nullable PagerAdapter var3);
   }

   public interface OnPageChangeListener {
      void onPageScrollStateChanged(int var1);

      void onPageScrolled(int var1, float var2, int var3);

      void onPageSelected(int var1);
   }

   public interface PageTransformer {
      void transformPage(View var1, float var2);
   }

   private class PagerObserver extends DataSetObserver {
      public void onChanged() {
         ViewPager.this.dataSetChanged();
      }

      public void onInvalidated() {
         ViewPager.this.dataSetChanged();
      }
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public ViewPager.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new ViewPager.SavedState(var1, var2);
         }

         public ViewPager.SavedState[] newArray(int var1) {
            return new ViewPager.SavedState[var1];
         }
      });
      Parcelable adapterState;
      ClassLoader loader;
      int position;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         ClassLoader var3 = var2;
         if (var2 == null) {
            var3 = this.getClass().getClassLoader();
         }

         this.position = var1.readInt();
         this.adapterState = var1.readParcelable(var3);
         this.loader = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.position);
         var1.writeParcelable(this.adapterState, var2);
      }
   }

   public static class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
      public void onPageScrollStateChanged(int var1) {
      }

      public void onPageScrolled(int var1, float var2, int var3) {
      }

      public void onPageSelected(int var1) {
      }
   }

   static class ViewPositionComparator implements Comparator {
      public int compare(View var1, View var2) {
         ViewPager.LayoutParams var4 = (ViewPager.LayoutParams)var1.getLayoutParams();
         ViewPager.LayoutParams var5 = (ViewPager.LayoutParams)var2.getLayoutParams();
         int var3;
         if (var4.isDecor != var5.isDecor) {
            if (var4.isDecor) {
               var3 = 1;
            } else {
               var3 = -1;
            }
         } else {
            var3 = var4.position - var5.position;
         }

         return var3;
      }
   }
}
