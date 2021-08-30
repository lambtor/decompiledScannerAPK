package kr.ftlab.lib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DisplayProg extends AsyncTask {
   private static final boolean D = false;
   private static final String TAG = "Prog";
   private Context mContext;
   private ProgressDialog mDlg;

   public DisplayProg(Context var1) {
      this.mContext = var1;
   }

   protected Integer doInBackground(Integer... var1) {
      int var3 = var1[0].intValue();
      this.publishProgress(new String[]{"max", Integer.toString(var3)});

      for(int var2 = 0; var2 < var3; ++var2) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         }

         this.publishProgress(new String[]{"progress", Integer.toString(var2), Integer.toString(var3)});
      }

      return var3;
   }

   protected void onPostExecute(Integer var1) {
      this.mDlg.dismiss();
   }

   protected void onPreExecute() {
      this.mDlg = new ProgressDialog(this.mContext);
      this.mDlg.setProgressStyle(0);
      this.mDlg.setMessage("Calibration.....");
      this.mDlg.setCancelable(false);
      this.mDlg.show();
      super.onPreExecute();
   }

   protected void onProgressUpdate(String... var1) {
      if (var1[0].equals("progress")) {
         this.mDlg.setMessage("Calibration....");
      } else if (var1[0].equals("max")) {
         this.mDlg.setMax(Integer.parseInt(var1[1]));
      }

   }
}
