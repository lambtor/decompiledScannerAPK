package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
   static final int GENERATED_ITEM_PADDING = 4;
   static final int MIN_CELL_SIZE = 56;
   private static final String TAG = "ActionMenuView";
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   private boolean mFormatItems;
   private int mFormatItemsWidth;
   private int mGeneratedItemPadding;
   private MenuBuilder mMenu;
   MenuBuilder.Callback mMenuBuilderCallback;
   private int mMinCellSize;
   ActionMenuView.OnMenuItemClickListener mOnMenuItemClickListener;
   private Context mPopupContext;
   private int mPopupTheme;
   private ActionMenuPresenter mPresenter;
   private boolean mReserveOverflow;

   public ActionMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setBaselineAligned(false);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mMinCellSize = (int)(56.0F * var3);
      this.mGeneratedItemPadding = (int)(4.0F * var3);
      this.mPopupContext = var1;
      this.mPopupTheme = 0;
   }

   static int measureChildForCells(View var0, int var1, int var2, int var3, int var4) {
      ActionMenuView.LayoutParams var9 = (ActionMenuView.LayoutParams)var0.getLayoutParams();
      int var6 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var3) - var4, MeasureSpec.getMode(var3));
      ActionMenuItemView var8;
      if (var0 instanceof ActionMenuItemView) {
         var8 = (ActionMenuItemView)var0;
      } else {
         var8 = null;
      }

      boolean var10;
      if (var8 != null && var8.hasText()) {
         var10 = true;
      } else {
         var10 = false;
      }

      byte var5 = 0;
      var3 = var5;
      if (var2 > 0) {
         label47: {
            if (var10) {
               var3 = var5;
               if (var2 < 2) {
                  break label47;
               }
            }

            var0.measure(MeasureSpec.makeMeasureSpec(var1 * var2, Integer.MIN_VALUE), var6);
            int var11 = var0.getMeasuredWidth();
            var3 = var11 / var1;
            var2 = var3;
            if (var11 % var1 != 0) {
               var2 = var3 + 1;
            }

            var3 = var2;
            if (var10) {
               var3 = var2;
               if (var2 < 2) {
                  var3 = 2;
               }
            }
         }
      }

      boolean var7;
      if (!var9.isOverflowButton && var10) {
         var7 = true;
      } else {
         var7 = false;
      }

      var9.expandable = var7;
      var9.cellsUsed = var3;
      var0.measure(MeasureSpec.makeMeasureSpec(var3 * var1, 1073741824), var6);
      return var3;
   }

   private void onMeasureExactFormat(int var1, int var2) {
      int var18 = MeasureSpec.getMode(var2);
      int var6 = MeasureSpec.getSize(var1);
      int var17 = MeasureSpec.getSize(var2);
      int var7 = this.getPaddingLeft();
      var1 = this.getPaddingRight();
      int var23 = this.getPaddingTop() + this.getPaddingBottom();
      int var19 = getChildMeasureSpec(var2, var23, -2);
      int var20 = var6 - (var7 + var1);
      var2 = var20 / this.mMinCellSize;
      var1 = this.mMinCellSize;
      if (var2 == 0) {
         this.setMeasuredDimension(var20, 0);
      } else {
         int var21 = this.mMinCellSize + var20 % var1 / var2;
         var6 = 0;
         int var9 = 0;
         int var8 = 0;
         int var10 = 0;
         boolean var36 = false;
         long var26 = 0L;
         int var22 = this.getChildCount();

         int var13;
         int var14;
         int var15;
         int var16;
         long var28;
         ActionMenuView.LayoutParams var32;
         View var33;
         for(int var11 = 0; var11 < var22; var26 = var28) {
            var33 = this.getChildAt(var11);
            boolean var12;
            if (var33.getVisibility() == 8) {
               var28 = var26;
               var12 = var36;
            } else {
               boolean var25 = var33 instanceof ActionMenuItemView;
               var13 = var10 + 1;
               if (var25) {
                  var33.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
               }

               var32 = (ActionMenuView.LayoutParams)var33.getLayoutParams();
               var32.expanded = false;
               var32.extraPixels = 0;
               var32.cellsUsed = 0;
               var32.expandable = false;
               var32.leftMargin = 0;
               var32.rightMargin = 0;
               if (var25 && ((ActionMenuItemView)var33).hasText()) {
                  var25 = true;
               } else {
                  var25 = false;
               }

               var32.preventEdgeOffset = var25;
               if (var32.isOverflowButton) {
                  var1 = 1;
               } else {
                  var1 = var2;
               }

               int var24 = measureChildForCells(var33, var21, var1, var19, var23);
               var14 = Math.max(var9, var24);
               var1 = var8;
               if (var32.expandable) {
                  var1 = var8 + 1;
               }

               if (var32.isOverflowButton) {
                  var36 = true;
               }

               var15 = var2 - var24;
               var16 = Math.max(var6, var33.getMeasuredHeight());
               var2 = var15;
               var8 = var1;
               var12 = var36;
               var9 = var14;
               var6 = var16;
               var28 = var26;
               var10 = var13;
               if (var24 == 1) {
                  var28 = var26 | (long)(1 << var11);
                  var2 = var15;
                  var8 = var1;
                  var12 = var36;
                  var9 = var14;
                  var6 = var16;
                  var10 = var13;
               }
            }

            ++var11;
            var36 = var12;
         }

         boolean var37;
         if (var36 && var10 == 2) {
            var37 = true;
         } else {
            var37 = false;
         }

         boolean var34 = false;
         int var38 = var2;

         View var39;
         ActionMenuView.LayoutParams var40;
         while(true) {
            var28 = var26;
            if (var8 <= 0) {
               break;
            }

            var28 = var26;
            if (var38 <= 0) {
               break;
            }

            var13 = Integer.MAX_VALUE;
            long var30 = 0L;
            var16 = 0;

            for(var15 = 0; var15 < var22; var30 = var28) {
               var32 = (ActionMenuView.LayoutParams)this.getChildAt(var15).getLayoutParams();
               if (!var32.expandable) {
                  var28 = var30;
                  var2 = var16;
                  var14 = var13;
               } else if (var32.cellsUsed < var13) {
                  var14 = var32.cellsUsed;
                  var28 = (long)(1 << var15);
                  var2 = 1;
               } else {
                  var14 = var13;
                  var2 = var16;
                  var28 = var30;
                  if (var32.cellsUsed == var13) {
                     var28 = var30 | (long)(1 << var15);
                     var2 = var16 + 1;
                     var14 = var13;
                  }
               }

               ++var15;
               var13 = var14;
               var16 = var2;
            }

            var26 |= var30;
            if (var16 > var38) {
               var28 = var26;
               break;
            }

            for(var1 = 0; var1 < var22; var26 = var28) {
               var39 = this.getChildAt(var1);
               var40 = (ActionMenuView.LayoutParams)var39.getLayoutParams();
               if (((long)(1 << var1) & var30) == 0L) {
                  var2 = var38;
                  var28 = var26;
                  if (var40.cellsUsed == var13 + 1) {
                     var28 = var26 | (long)(1 << var1);
                     var2 = var38;
                  }
               } else {
                  if (var37 && var40.preventEdgeOffset && var38 == 1) {
                     var39.setPadding(this.mGeneratedItemPadding + var21, 0, this.mGeneratedItemPadding, 0);
                  }

                  ++var40.cellsUsed;
                  var40.expanded = true;
                  var2 = var38 - 1;
                  var28 = var26;
               }

               ++var1;
               var38 = var2;
            }

            var34 = true;
         }

         boolean var35;
         if (!var36 && var10 == 1) {
            var35 = true;
         } else {
            var35 = false;
         }

         var36 = var34;
         if (var38 > 0) {
            var36 = var34;
            if (var28 != 0L) {
               label230: {
                  if (var38 >= var10 - 1 && !var35) {
                     var36 = var34;
                     if (var9 <= 1) {
                        break label230;
                     }
                  }

                  float var5 = (float)Long.bitCount(var28);
                  float var4 = var5;
                  if (!var35) {
                     float var3 = var5;
                     if ((1L & var28) != 0L) {
                        var3 = var5;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                           var3 = var5 - 0.5F;
                        }
                     }

                     var4 = var3;
                     if (((long)(1 << var22 - 1) & var28) != 0L) {
                        var4 = var3;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(var22 - 1).getLayoutParams()).preventEdgeOffset) {
                           var4 = var3 - 0.5F;
                        }
                     }
                  }

                  if (var4 > 0.0F) {
                     var7 = (int)((float)(var38 * var21) / var4);
                  } else {
                     var7 = 0;
                  }

                  var8 = 0;

                  for(var35 = var34; var8 < var22; var35 = var34) {
                     if (((long)(1 << var8) & var28) == 0L) {
                        var34 = var35;
                     } else {
                        var39 = this.getChildAt(var8);
                        var40 = (ActionMenuView.LayoutParams)var39.getLayoutParams();
                        if (var39 instanceof ActionMenuItemView) {
                           var40.extraPixels = var7;
                           var40.expanded = true;
                           if (var8 == 0 && !var40.preventEdgeOffset) {
                              var40.leftMargin = -var7 / 2;
                           }

                           var34 = true;
                        } else if (var40.isOverflowButton) {
                           var40.extraPixels = var7;
                           var40.expanded = true;
                           var40.rightMargin = -var7 / 2;
                           var34 = true;
                        } else {
                           if (var8 != 0) {
                              var40.leftMargin = var7 / 2;
                           }

                           var34 = var35;
                           if (var8 != var22 - 1) {
                              var40.rightMargin = var7 / 2;
                              var34 = var35;
                           }
                        }
                     }

                     ++var8;
                  }

                  var36 = var35;
               }
            }
         }

         if (var36) {
            for(var1 = 0; var1 < var22; ++var1) {
               var33 = this.getChildAt(var1);
               var32 = (ActionMenuView.LayoutParams)var33.getLayoutParams();
               if (var32.expanded) {
                  var33.measure(MeasureSpec.makeMeasureSpec(var32.cellsUsed * var21 + var32.extraPixels, 1073741824), var19);
               }
            }
         }

         var1 = var17;
         if (var18 != 1073741824) {
            var1 = var6;
         }

         this.setMeasuredDimension(var20, var1);
      }

   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 != null && var1 instanceof ActionMenuView.LayoutParams) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void dismissPopupMenus() {
      if (this.mPresenter != null) {
         this.mPresenter.dismissPopupMenus();
      }

   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      return false;
   }

   protected ActionMenuView.LayoutParams generateDefaultLayoutParams() {
      ActionMenuView.LayoutParams var1 = new ActionMenuView.LayoutParams(-2, -2);
      var1.gravity = 16;
      return var1;
   }

   public ActionMenuView.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ActionMenuView.LayoutParams(this.getContext(), var1);
   }

   protected ActionMenuView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      ActionMenuView.LayoutParams var2;
      if (var1 != null) {
         ActionMenuView.LayoutParams var3;
         if (var1 instanceof ActionMenuView.LayoutParams) {
            var3 = new ActionMenuView.LayoutParams((ActionMenuView.LayoutParams)var1);
         } else {
            var3 = new ActionMenuView.LayoutParams(var1);
         }

         var2 = var3;
         if (var3.gravity <= 0) {
            var3.gravity = 16;
            var2 = var3;
         }
      } else {
         var2 = this.generateDefaultLayoutParams();
      }

      return var2;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public ActionMenuView.LayoutParams generateOverflowButtonLayoutParams() {
      ActionMenuView.LayoutParams var1 = this.generateDefaultLayoutParams();
      var1.isOverflowButton = true;
      return var1;
   }

   public Menu getMenu() {
      if (this.mMenu == null) {
         Context var1 = this.getContext();
         this.mMenu = new MenuBuilder(var1);
         this.mMenu.setCallback(new ActionMenuView.MenuBuilderCallback());
         this.mPresenter = new ActionMenuPresenter(var1);
         this.mPresenter.setReserveOverflow(true);
         ActionMenuPresenter var2 = this.mPresenter;
         Object var3;
         if (this.mActionMenuPresenterCallback != null) {
            var3 = this.mActionMenuPresenterCallback;
         } else {
            var3 = new ActionMenuView.ActionMenuPresenterCallback();
         }

         var2.setCallback((MenuPresenter.Callback)var3);
         this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
         this.mPresenter.setMenuView(this);
      }

      return this.mMenu;
   }

   @Nullable
   public Drawable getOverflowIcon() {
      this.getMenu();
      return this.mPresenter.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public int getWindowAnimations() {
      return 0;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   protected boolean hasSupportDividerBeforeChildAt(int var1) {
      boolean var3;
      if (var1 == 0) {
         var3 = false;
      } else {
         View var5 = this.getChildAt(var1 - 1);
         View var4 = this.getChildAt(var1);
         var3 = false;
         boolean var2 = var3;
         if (var1 < this.getChildCount()) {
            var2 = var3;
            if (var5 instanceof ActionMenuView.ActionMenuChildView) {
               var2 = false | ((ActionMenuView.ActionMenuChildView)var5).needsDividerAfter();
            }
         }

         var3 = var2;
         if (var1 > 0) {
            var3 = var2;
            if (var4 instanceof ActionMenuView.ActionMenuChildView) {
               var3 = var2 | ((ActionMenuView.ActionMenuChildView)var4).needsDividerBefore();
            }
         }
      }

      return var3;
   }

   public boolean hideOverflowMenu() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.hideOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean isOverflowMenuShowPending() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isOverflowMenuShowing() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (this.mPresenter != null) {
         this.mPresenter.updateMenuView(false);
         if (this.mPresenter.isOverflowMenuShowing()) {
            this.mPresenter.hideOverflowMenu();
            this.mPresenter.showOverflowMenu();
         }
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.dismissPopupMenus();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (!this.mFormatItems) {
         super.onLayout(var1, var2, var3, var4, var5);
      } else {
         int var12 = this.getChildCount();
         int var11 = (var5 - var3) / 2;
         int var13 = this.getDividerWidth();
         int var7 = 0;
         var5 = 0;
         var3 = var4 - var2 - this.getPaddingRight() - this.getPaddingLeft();
         boolean var8 = false;
         var1 = ViewUtils.isLayoutRtl(this);

         int var6;
         for(var6 = 0; var6 < var12; ++var6) {
            View var17 = this.getChildAt(var6);
            if (var17.getVisibility() != 8) {
               ActionMenuView.LayoutParams var16 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
               int var9;
               if (var16.isOverflowButton) {
                  var9 = var17.getMeasuredWidth();
                  int var19 = var9;
                  if (this.hasSupportDividerBeforeChildAt(var6)) {
                     var19 = var9 + var13;
                  }

                  int var14 = var17.getMeasuredHeight();
                  int var10;
                  if (var1) {
                     var9 = this.getPaddingLeft() + var16.leftMargin;
                     var10 = var9 + var19;
                  } else {
                     var10 = this.getWidth() - this.getPaddingRight() - var16.rightMargin;
                     var9 = var10 - var19;
                  }

                  int var15 = var11 - var14 / 2;
                  var17.layout(var9, var15, var10, var15 + var14);
                  var3 -= var19;
                  var8 = true;
               } else {
                  var9 = var17.getMeasuredWidth() + var16.leftMargin + var16.rightMargin;
                  var7 += var9;
                  var9 = var3 - var9;
                  var3 = var7;
                  if (this.hasSupportDividerBeforeChildAt(var6)) {
                     var3 = var7 + var13;
                  }

                  ++var5;
                  var7 = var3;
                  var3 = var9;
               }
            }
         }

         View var20;
         if (var12 == 1 && !var8) {
            var20 = this.getChildAt(0);
            var5 = var20.getMeasuredWidth();
            var3 = var20.getMeasuredHeight();
            var2 = (var4 - var2) / 2 - var5 / 2;
            var4 = var11 - var3 / 2;
            var20.layout(var2, var4, var2 + var5, var4 + var3);
         } else {
            byte var18;
            if (var8) {
               var18 = 0;
            } else {
               var18 = 1;
            }

            var2 = var5 - var18;
            if (var2 > 0) {
               var2 = var3 / var2;
            } else {
               var2 = 0;
            }

            var5 = Math.max(0, var2);
            ActionMenuView.LayoutParams var21;
            if (var1) {
               var4 = this.getWidth() - this.getPaddingRight();

               for(var2 = 0; var2 < var12; var4 = var3) {
                  var20 = this.getChildAt(var2);
                  var21 = (ActionMenuView.LayoutParams)var20.getLayoutParams();
                  var3 = var4;
                  if (var20.getVisibility() != 8) {
                     if (var21.isOverflowButton) {
                        var3 = var4;
                     } else {
                        var7 = var4 - var21.rightMargin;
                        var3 = var20.getMeasuredWidth();
                        var6 = var20.getMeasuredHeight();
                        var4 = var11 - var6 / 2;
                        var20.layout(var7 - var3, var4, var7, var4 + var6);
                        var3 = var7 - (var21.leftMargin + var3 + var5);
                     }
                  }

                  ++var2;
               }
            } else {
               var4 = this.getPaddingLeft();

               for(var2 = 0; var2 < var12; var4 = var3) {
                  var20 = this.getChildAt(var2);
                  var21 = (ActionMenuView.LayoutParams)var20.getLayoutParams();
                  var3 = var4;
                  if (var20.getVisibility() != 8) {
                     if (var21.isOverflowButton) {
                        var3 = var4;
                     } else {
                        var6 = var4 + var21.leftMargin;
                        var3 = var20.getMeasuredWidth();
                        var7 = var20.getMeasuredHeight();
                        var4 = var11 - var7 / 2;
                        var20.layout(var6, var4, var6 + var3, var4 + var7);
                        var3 = var6 + var21.rightMargin + var3 + var5;
                     }
                  }

                  ++var2;
               }
            }
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      boolean var6 = this.mFormatItems;
      boolean var5;
      if (MeasureSpec.getMode(var1) == 1073741824) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mFormatItems = var5;
      if (var6 != this.mFormatItems) {
         this.mFormatItemsWidth = 0;
      }

      int var3 = MeasureSpec.getSize(var1);
      if (this.mFormatItems && this.mMenu != null && var3 != this.mFormatItemsWidth) {
         this.mFormatItemsWidth = var3;
         this.mMenu.onItemsChanged(true);
      }

      int var4 = this.getChildCount();
      if (this.mFormatItems && var4 > 0) {
         this.onMeasureExactFormat(var1, var2);
      } else {
         for(var3 = 0; var3 < var4; ++var3) {
            ActionMenuView.LayoutParams var7 = (ActionMenuView.LayoutParams)this.getChildAt(var3).getLayoutParams();
            var7.rightMargin = 0;
            var7.leftMargin = 0;
         }

         super.onMeasure(var1, var2);
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public MenuBuilder peekMenu() {
      return this.mMenu;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mPresenter.setExpandedActionViewsExclusive(var1);
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
   }

   public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(@Nullable Drawable var1) {
      this.getMenu();
      this.mPresenter.setOverflowIcon(var1);
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPopupTheme(@StyleRes int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
         } else {
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
      this.mPresenter.setMenuView(this);
   }

   public boolean showOverflowMenu() {
      boolean var1;
      if (this.mPresenter != null && this.mPresenter.showOverflowMenu()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public interface ActionMenuChildView {
      boolean needsDividerAfter();

      boolean needsDividerBefore();
   }

   private class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         return false;
      }
   }

   public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
      @ExportedProperty
      public int cellsUsed;
      @ExportedProperty
      public boolean expandable;
      boolean expanded;
      @ExportedProperty
      public int extraPixels;
      @ExportedProperty
      public boolean isOverflowButton;
      @ExportedProperty
      public boolean preventEdgeOffset;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.isOverflowButton = false;
      }

      LayoutParams(int var1, int var2, boolean var3) {
         super(var1, var2);
         this.isOverflowButton = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.isOverflowButton = var1.isOverflowButton;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }
   }

   private class MenuBuilderCallback implements MenuBuilder.Callback {
      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         boolean var3;
         if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (ActionMenuView.this.mMenuBuilderCallback != null) {
            ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(var1);
         }

      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
