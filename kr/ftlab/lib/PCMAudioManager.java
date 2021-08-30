package kr.ftlab.lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public abstract class PCMAudioManager implements Runnable {
   private static final int AM_Start = 2;
   private static final int AM_Stop = 3;
   private static final boolean D = true;
   private static final int RECORDER_AUDIO_ENCODING = 2;
   private static final int RECORDER_BPS = 16;
   private static final int RECORDER_CHANNELS = 16;
   private static final int RECORDER_SAMPLERATE = 44100;
   private static final String TAG = "PCMA";
   private int BufferSize = 4410;
   public float buff_size = 1.0F;
   private IPCMAEventListener listener;
   protected AudioManager mAm = null;
   protected AudioRecord mAr = null;
   protected AudioTrack mAt = null;
   protected int mByteBufferSize = 8820;
   private Handler mHandler;
   private String mSdPath = "";
   protected boolean m_isRecording = false;
   private Thread recThread = null;
   private PCMAudioManager.RecordOperation recordWork = new PCMAudioManager.RecordOperation();

   PCMAudioManager() {
   }

   public PCMAudioManager(String var1, Context var2) {
      this.mSdPath = var1;
      this.mAm = (AudioManager)var2.getSystemService("audio");
   }

   private void runRecord() {
      // $FF: Couldn't be decompiled
   }

   private void startRecord() {
      this.mAr = new AudioRecord(1, 44100, 16, 2, this.mByteBufferSize);
      Log.d("PCMA", "startRecord");
      this.mAr.startRecording();
      Log.d("PCMA", "startRecord END");
      this.m_isRecording = true;
      this.recThread = new Thread(this.recordWork, "Audio Record Thread");
      this.recThread.start();
      Log.d("PCMA", "start");
   }

   private void stopRecord() {
      this.m_isRecording = false;
   }

   public void quit() {
      this.m_isRecording = false;
      if (this.recThread != null) {
         this.recThread.interrupt();
      }

      this.mHandler.post(new PCMAudioManager.QuitLooper());
   }

   public void run() {
      Looper.prepare();
      this.mHandler = new Handler() {
         public void handleMessage(Message var1) {
            switch(var1.what) {
            case 2:
               PCMAudioManager.this.startRecord();
               break;
            case 3:
               PCMAudioManager.this.stopRecord();
            }

         }
      };
      Looper.loop();
   }

   public void setBufferSize(int var1) {
      this.BufferSize = var1 * 4410;
      this.mByteBufferSize = this.BufferSize * 2;
   }

   public void setListener(IPCMAEventListener var1) {
      this.listener = var1;
   }

   public void start() {
      if (this.mHandler != null) {
         Log.d("PCMA", "Audio Start");
         this.mHandler.sendEmptyMessage(2);
      }

   }

   public void stop() {
      if (this.mHandler != null) {
         Log.d("PCMA", "Audio Stop");
         this.mHandler.sendEmptyMessage(3);
      }

   }

   class QuitLooper implements Runnable {
      public void run() {
         Looper.myLooper().quit();
      }
   }

   class RecordOperation implements Runnable {
      public void run() {
         PCMAudioManager.this.runRecord();
      }
   }
}
