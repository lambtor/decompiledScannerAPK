package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;

class LayoutInflaterCompatBase {
   static LayoutInflaterFactory getFactory(LayoutInflater var0) {
      Factory var1 = var0.getFactory();
      LayoutInflaterFactory var2;
      if (var1 instanceof LayoutInflaterCompatBase.FactoryWrapper) {
         var2 = ((LayoutInflaterCompatBase.FactoryWrapper)var1).mDelegateFactory;
      } else {
         var2 = null;
      }

      return var2;
   }

   static void setFactory(LayoutInflater var0, LayoutInflaterFactory var1) {
      LayoutInflaterCompatBase.FactoryWrapper var2;
      if (var1 != null) {
         var2 = new LayoutInflaterCompatBase.FactoryWrapper(var1);
      } else {
         var2 = null;
      }

      var0.setFactory(var2);
   }

   static class FactoryWrapper implements Factory {
      final LayoutInflaterFactory mDelegateFactory;

      FactoryWrapper(LayoutInflaterFactory var1) {
         this.mDelegateFactory = var1;
      }

      public View onCreateView(String var1, Context var2, AttributeSet var3) {
         return this.mDelegateFactory.onCreateView((View)null, var1, var2, var3);
      }

      public String toString() {
         return this.getClass().getName() + "{" + this.mDelegateFactory + "}";
      }
   }
}
