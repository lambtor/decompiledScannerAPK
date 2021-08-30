package com.technonia.gammafinder;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;

public class MediaScanner implements MediaScannerConnectionClient {
   private static final String TAG = "MediaScanner";
   private File file;
   private MediaScannerConnection mScannerConn;

   public MediaScanner(Context var1, File var2) {
      this.file = var2;
      this.mScannerConn = new MediaScannerConnection(var1, this);
   }

   public void connect() {
      Log.d("MediaScanner", "MediaScanner : connect()");
      this.mScannerConn.connect();
   }

   public void onMediaScannerConnected() {
      Log.d("MediaScanner", "onMediaScannerConnected()");
      File[] var2 = this.file.listFiles(new FilenameFilter() {
         public boolean accept(File var1, String var2) {
            return var2.endsWith(".png");
         }
      });
      if (var2 != null) {
         for(int var1 = 0; var1 < var2.length; ++var1) {
            this.mScannerConn.scanFile(var2[var1].getAbsolutePath(), (String)null);
         }
      }

   }

   public void onScanCompleted(String var1, Uri var2) {
      Log.i("MediaScanner", "MediaScanner : onScanCompleted(" + var1 + "," + var2.toString() + ")");
      this.mScannerConn.disconnect();
   }
}
