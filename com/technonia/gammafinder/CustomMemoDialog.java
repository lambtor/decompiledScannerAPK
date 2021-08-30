package com.technonia.gammafinder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CustomMemoDialog extends Dialog {
   private Boolean IsStored = false;
   protected Button dialog_cancel;
   protected EditText dialog_memo;
   protected Button dialog_store;
   private String memoStr;

   public CustomMemoDialog(@NonNull Context var1) {
      super(var1);
   }

   public String getMemoStr() {
      return this.memoStr;
   }

   public Boolean getStored() {
      return this.IsStored;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.requestWindowFeature(1);
      this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      this.setContentView(2130903072);
      this.dialog_memo = (EditText)this.findViewById(2131427457);
      this.dialog_store = (Button)this.findViewById(2131427459);
      this.dialog_cancel = (Button)this.findViewById(2131427460);
      this.dialog_store.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            CustomMemoDialog.this.memoStr = CustomMemoDialog.this.dialog_memo.getText().toString();
            CustomMemoDialog.this.IsStored = true;
            CustomMemoDialog.this.dismiss();
         }
      });
      this.dialog_cancel.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            CustomMemoDialog.this.IsStored = false;
            CustomMemoDialog.this.dismiss();
         }
      });
   }

   public void setDialog_memo(String var1) {
      if (var1 != null) {
         this.dialog_memo.setText(var1);
         this.dialog_memo.setSelection(var1.length());
      }

   }

   public void setMemoStr(String var1) {
      this.memoStr = var1;
   }

   public void setStored(Boolean var1) {
      this.IsStored = var1;
   }
}
