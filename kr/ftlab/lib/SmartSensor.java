package kr.ftlab.lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class SmartSensor extends PCMAudioManager implements IPCMAEventListener {
   private static final boolean D = true;
   public static final int EM = 2;
   public static final int GE = 1;
   public static final int GE_PRO = 5;
   public static final int MDI = 4;
   public static final int MDI_HUMI = 2;
   public static final int MDI_NONE = 0;
   public static final int MDI_TEMP = 1;
   public static final int MDI_UVC = 128;
   public static final int MDI_UVI = 129;
   public static final int MDI_V = 3;
   private static final String TAG = "MDI";
   public static final int UV = 3;
   private boolean EM_Init = false;
   private boolean GE_Init = false;
   private boolean MDI_Init = false;
   private int Mode = 0;
   private MICDataAnalysis mCalculator;
   protected int mCntBuffer = 0;
   Context mContext;
   public DataInfo mDataInfo = new DataInfo();
   private DisplayProg mDisplayProg;
   private SmartSensorEventListener mListener;
   public SmartSensorResultEM mResultEM = new SmartSensorResultEM();
   public SmartSensorResultGE mResultGE = new SmartSensorResultGE();
   public SmartSensorResultMDI mResultMDI = new SmartSensorResultMDI();
   public SmartSensorResultUV mResultUV = new SmartSensorResultUV();
   private Thread mThread4PCMAudio = null;
   ProgressDialog progress;

   public SmartSensor(Context var1, SmartSensorEventListener var2) {
      Log.i("MDI", "in MDIDetector");
      this.setListener(this);
      this.mContext = var1;
      this.mListener = var2;
      this.progress = new ProgressDialog(var1);
      this.mDisplayProg = new DisplayProg(var1);
      PreferencesManager.INSTANCE.setContext(var1);
      this.mCalculator = new MICDataAnalysis(this.mDataInfo, this.mResultGE, this.mResultEM, this.mResultUV, this.mResultMDI);
      this.mThread4PCMAudio = new Thread(this, "PCMAudioMgr");
      this.mThread4PCMAudio.start();
   }

   private void Auto_Progress() {
      this.progress.setTitle((CharSequence)null);
      this.progress.setProgressStyle(0);
      this.progress.setCancelable(false);
      this.progress.show();
   }

   private void Process(short[] var1, int var2) {
      Log.i("MDI", "Process Mode : " + this.Mode);
      int var3;
      switch(this.Mode) {
      case 1:
      case 5:
         if (this.GE_Init) {
            this.mDataInfo.GE_Auto_Value = this.mCalculator.GEI_AutoVmin(var1, var2);
            if (this.mDataInfo.GE_Auto_Value != 0) {
               Log.i("MDI", "auto_vmin ret vmin : " + this.mDataInfo.GE_Auto_Value);
               this.GE_Init = false;
               this.mDisplayProg.onPostExecute(Integer.valueOf(1));
               Log.i("MDI", "alistener_ : " + this.mListener);
               if (this.mListener != null) {
                  Log.i("MDI", "do write..");
                  PreferencesManager.INSTANCE.File_Write(this.mDataInfo);
                  this.onCompletedFind_test();
                  this.progress.dismiss();
               }

               this.stop();
            }
         } else {
            MICDataAnalysis var5 = this.mCalculator;
            var3 = this.Mode;
            int var4 = this.mCntBuffer;
            this.mCntBuffer = var4 + 1;
            var5.GEI_Process(var3, var1, var2, var4);
         }
         break;
      case 2:
         if (this.EM_Init) {
            this.mDataInfo.EM_Auto_Value = this.mCalculator.EM_AutoVmin(var1, var2);
            if (0.0F != this.mDataInfo.EM_Auto_Value) {
               Log.i("MDI", "auto_vmin ret vmin : " + this.mDataInfo.EM_Auto_Value);
               this.EM_Init = false;
               this.mDisplayProg.onPostExecute(Integer.valueOf(1));
               if (this.mListener != null) {
                  PreferencesManager.INSTANCE.File_Write(this.mDataInfo);
                  this.onCompletedFind_test();
                  this.progress.dismiss();
               }

               this.stop();
            }
         } else {
            this.mCalculator.EM_Process(var1, var2);
         }
         break;
      case 3:
         for(var3 = 0; var3 < var2; ++var3) {
            Struct.UV_Ret.TotalData[Struct.UV_Ret.Inc] = var1[var3];
            ++Struct.UV_Ret.Inc;
            if (Struct.UV_Ret.Inc == 44000) {
               break;
            }
         }

         Log.e("MDI", "Struct.UV_Ret.Inc : " + Struct.UV_Ret.Inc);
         if (Struct.UV_Ret.Inc == 44000) {
            this.mCalculator.UV_Process(Struct.UV_Ret.TotalData, Struct.UV_Ret.Inc);
            Struct.UV_Ret.Inc = 0;
         }
         break;
      case 4:
         if (this.MDI_Init) {
            this.mDataInfo = this.mCalculator.MDI_Find_Parameter(var1, var2);
            Log.i("MDI", "size : " + var2);
            if (0.0F != this.mDataInfo.MDI_THR) {
               Log.i("MDI", "auto_vmin ret vmin : " + this.mDataInfo.MDI_THR + ", " + this.mDataInfo.MDI_INTV + ", " + this.mDataInfo.MDI_Type);
               this.MDI_Init = false;
               if (this.mListener != null) {
                  this.setBufferSize(1);
                  PreferencesManager.INSTANCE.File_Write(this.mDataInfo);
               }
            }
         } else {
            this.mCalculator.MDI_Process(var1, var2);
         }
      }

   }

   private void onCompletedFind_test() {
      ((Activity)this.mContext).runOnUiThread(new Runnable() {
         public void run() {
            SmartSensor.this.mListener.onSelfConfigurated();
         }
      });
   }

   private void setAutoFind(int var1) {
      switch(var1) {
      case 1:
      case 5:
         this.GE_Init = true;
         break;
      case 2:
         this.EM_Init = true;
      case 3:
      default:
         break;
      case 4:
         this.setBufferSize(40);
         this.MDI_Init = true;
      }

   }

   private void setDataInfo(DataInfo var1) {
      Log.d("MDI", "DATA INFO ");
      Log.e("MDI", "THR : " + var1.MDI_THR + ", INTV :" + var1.MDI_INTV + ", GE : " + var1.GE_Auto_Value + ", EM : " + var1.EM_Auto_Value);
      this.mCalculator.setLocalData(var1);
   }

   public void Geiger_Data_Initialize() {
      Struct.GE_Ret.CPS_Count = 0;
      Struct.GE_Ret.CPS_DataIndex = 0;
      this.mCntBuffer = 0;

      for(int var1 = 0; var1 < Struct.GE_Ret.AVG_COUNT; ++var1) {
         Struct.GE_Ret.CPS_Data[var1] = 0;
      }

      Struct.GE_Ret.rCount = 0;
      Struct.GE_Ret.xPos = 0;
      Struct.GE_Ret.atTime = 0.0F;
   }

   public DataInfo getDataInfo() {
      return this.mCalculator.getDataInfo();
   }

   public SmartSensorResultEM getResultEM() {
      return this.mCalculator.getResultEM();
   }

   public SmartSensorResultGE getResultGE() {
      return this.mCalculator.getResultGE();
   }

   public SmartSensorResultMDI getResultMDI() {
      return this.mCalculator.getResultMDI();
   }

   public SmartSensorResultUV getResultUV() {
      return this.mCalculator.getResultUV();
   }

   public void onAudioDataByte(short[] var1, int var2) {
      this.Process(var1, var2);
      this.mListener.onMeasured();
   }

   public void quit() {
   }

   public void registerSelfConfiguration() {
      switch(this.Mode) {
      case 1:
      case 5:
         this.GE_Init = true;
         this.start();
         break;
      case 2:
         this.EM_Init = true;
         this.start();
      case 3:
      default:
         break;
      case 4:
         Log.i("MDI", "Mode : " + String.format("%d", this.Mode));
         this.setBufferSize(40);
         this.MDI_Init = true;
      }

   }

   public void reset() {
   }

   public void selectDevice(int var1) {
      this.Mode = var1;
      this.mDataInfo = PreferencesManager.INSTANCE.File_Read(var1);
      Log.i("MDI", "IsParameter_OK :" + this.mDataInfo.IsParameter_OK + "GE_Auto_Value : " + this.mDataInfo.GE_Auto_Value);
      if (!this.mDataInfo.IsParameter_OK) {
         Log.e("MDI", "NO Para find ," + var1);
         this.setAutoFind(this.Mode);
      } else {
         Log.e("MDI", "Para find ," + var1);
         this.setDataInfo(this.mDataInfo);
      }

   }

   public void setEM_Type(int var1) {
      this.mCalculator.EM_setType(var1);
   }

   public void setLight_Type(int var1) {
      this.mCalculator.Light_Type(var1);
   }

   public void start() {
      this.mDisplayProg = new DisplayProg(this.mContext);
      switch(this.Mode) {
      case 1:
      case 5:
         this.Geiger_Data_Initialize();
         if (this.GE_Init) {
            this.mDisplayProg.execute(new Integer[]{Integer.valueOf(270)});
         }
         break;
      case 2:
         if (this.EM_Init) {
            this.mDisplayProg.execute(new Integer[]{Integer.valueOf(200)});
         }
      case 3:
      case 4:
      }

      super.start();
   }

   public void stop() {
      super.stop();
   }
}
