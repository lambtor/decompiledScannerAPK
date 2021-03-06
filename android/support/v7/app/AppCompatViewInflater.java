package android.support.v7.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class AppCompatViewInflater {
   private static final String LOG_TAG = "AppCompatViewInflater";
   private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
   private static final Map sConstructorMap = new ArrayMap();
   private static final Class[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
   private static final int[] sOnClickAttrs = new int[]{16843375};
   private final Object[] mConstructorArgs = new Object[2];

   private void checkOnClickListener(View var1, AttributeSet var2) {
      Context var3 = var1.getContext();
      if (var3 instanceof ContextWrapper && (VERSION.SDK_INT < 15 || ViewCompat.hasOnClickListeners(var1))) {
         TypedArray var5 = var3.obtainStyledAttributes(var2, sOnClickAttrs);
         String var4 = var5.getString(0);
         if (var4 != null) {
            var1.setOnClickListener(new AppCompatViewInflater.DeclaredOnClickListener(var1, var4));
         }

         var5.recycle();
      }

   }

   private View createView(Context param1, String param2, String param3) throws ClassNotFoundException, InflateException {
      // $FF: Couldn't be decompiled
   }

   private View createViewFromTag(Context param1, String param2, AttributeSet param3) {
      // $FF: Couldn't be decompiled
   }

   private static Context themifyContext(Context var0, AttributeSet var1, boolean var2, boolean var3) {
      TypedArray var6 = var0.obtainStyledAttributes(var1, R.styleable.View, 0, 0);
      int var4 = 0;
      if (var2) {
         var4 = var6.getResourceId(R.styleable.View_android_theme, 0);
      }

      int var5 = var4;
      if (var3) {
         var5 = var4;
         if (var4 == 0) {
            var4 = var6.getResourceId(R.styleable.View_theme, 0);
            var5 = var4;
            if (var4 != 0) {
               Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
               var5 = var4;
            }
         }
      }

      var6.recycle();
      Object var7 = var0;
      if (var5 != 0) {
         if (var0 instanceof ContextThemeWrapper) {
            var7 = var0;
            if (((ContextThemeWrapper)var0).getThemeResId() == var5) {
               return (Context)var7;
            }
         }

         var7 = new ContextThemeWrapper(var0, var5);
      }

      return (Context)var7;
   }

   public final View createView(View var1, String var2, @NonNull Context var3, @NonNull AttributeSet var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      Context var10 = var3;
      if (var5) {
         var10 = var3;
         if (var1 != null) {
            var10 = var1.getContext();
         }
      }

      Context var12;
      label92: {
         if (!var6) {
            var12 = var10;
            if (!var7) {
               break label92;
            }
         }

         var12 = themifyContext(var10, var4, var6, var7);
      }

      var10 = var12;
      if (var8) {
         var10 = TintContextWrapper.wrap(var12);
      }

      Object var13 = null;
      byte var9 = -1;
      switch(var2.hashCode()) {
      case -1946472170:
         if (var2.equals("RatingBar")) {
            var9 = 11;
         }
         break;
      case -1455429095:
         if (var2.equals("CheckedTextView")) {
            var9 = 8;
         }
         break;
      case -1346021293:
         if (var2.equals("MultiAutoCompleteTextView")) {
            var9 = 10;
         }
         break;
      case -938935918:
         if (var2.equals("TextView")) {
            var9 = 0;
         }
         break;
      case -937446323:
         if (var2.equals("ImageButton")) {
            var9 = 5;
         }
         break;
      case -658531749:
         if (var2.equals("SeekBar")) {
            var9 = 12;
         }
         break;
      case -339785223:
         if (var2.equals("Spinner")) {
            var9 = 4;
         }
         break;
      case 776382189:
         if (var2.equals("RadioButton")) {
            var9 = 7;
         }
         break;
      case 1125864064:
         if (var2.equals("ImageView")) {
            var9 = 1;
         }
         break;
      case 1413872058:
         if (var2.equals("AutoCompleteTextView")) {
            var9 = 9;
         }
         break;
      case 1601505219:
         if (var2.equals("CheckBox")) {
            var9 = 6;
         }
         break;
      case 1666676343:
         if (var2.equals("EditText")) {
            var9 = 3;
         }
         break;
      case 2001146706:
         if (var2.equals("Button")) {
            var9 = 2;
         }
      }

      switch(var9) {
      case 0:
         var13 = new AppCompatTextView(var10, var4);
         break;
      case 1:
         var13 = new AppCompatImageView(var10, var4);
         break;
      case 2:
         var13 = new AppCompatButton(var10, var4);
         break;
      case 3:
         var13 = new AppCompatEditText(var10, var4);
         break;
      case 4:
         var13 = new AppCompatSpinner(var10, var4);
         break;
      case 5:
         var13 = new AppCompatImageButton(var10, var4);
         break;
      case 6:
         var13 = new AppCompatCheckBox(var10, var4);
         break;
      case 7:
         var13 = new AppCompatRadioButton(var10, var4);
         break;
      case 8:
         var13 = new AppCompatCheckedTextView(var10, var4);
         break;
      case 9:
         var13 = new AppCompatAutoCompleteTextView(var10, var4);
         break;
      case 10:
         var13 = new AppCompatMultiAutoCompleteTextView(var10, var4);
         break;
      case 11:
         var13 = new AppCompatRatingBar(var10, var4);
         break;
      case 12:
         var13 = new AppCompatSeekBar(var10, var4);
      }

      Object var11 = var13;
      if (var13 == null) {
         var11 = var13;
         if (var3 != var10) {
            var11 = this.createViewFromTag(var10, var2, var4);
         }
      }

      if (var11 != null) {
         this.checkOnClickListener((View)var11, var4);
      }

      return (View)var11;
   }

   private static class DeclaredOnClickListener implements OnClickListener {
      private final View mHostView;
      private final String mMethodName;
      private Context mResolvedContext;
      private Method mResolvedMethod;

      public DeclaredOnClickListener(@NonNull View var1, @NonNull String var2) {
         this.mHostView = var1;
         this.mMethodName = var2;
      }

      @NonNull
      private void resolveMethod(@Nullable Context param1, @NonNull String param2) {
         // $FF: Couldn't be decompiled
      }

      public void onClick(@NonNull View var1) {
         if (this.mResolvedMethod == null) {
            this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
         }

         try {
            this.mResolvedMethod.invoke(this.mResolvedContext, var1);
         } catch (IllegalAccessException var2) {
            throw new IllegalStateException("Could not execute non-public method for android:onClick", var2);
         } catch (InvocationTargetException var3) {
            throw new IllegalStateException("Could not execute method for android:onClick", var3);
         }
      }
   }
}
