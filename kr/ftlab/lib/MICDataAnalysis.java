package kr.ftlab.lib;

import android.util.Log;

public class MICDataAnalysis {
   private static final boolean D = true;
   private static final String TAG = "Calculator";
   Struct.MDI_Data MDI_Data_Ret;
   DataInfo mInfo;
   SmartSensorResultEM mResultEM;
   SmartSensorResultGE mResultGE;
   SmartSensorResultMDI mResultMDI;
   SmartSensorResultUV mResultUV;

   public MICDataAnalysis(DataInfo var1, SmartSensorResultGE var2, SmartSensorResultEM var3, SmartSensorResultUV var4, SmartSensorResultMDI var5) {
      this.mInfo = var1;
      this.mResultGE = var2;
      this.mResultEM = var3;
      this.mResultUV = var4;
      this.mResultMDI = var5;
   }

   private void Average_Process(int var1) {
      int[] var4 = new int[var1];

      int var2;
      for(var2 = 0; var2 < var1; ++var2) {
         var4[var2] = 0;
      }

      for(var2 = 0; var2 < var1 - Struct.UV_Ret.Avg_Step; ++var2) {
         var4[var2] = 0;

         for(int var3 = 0; var3 < Struct.UV_Ret.Avg_Step; ++var3) {
            var4[var2] += Struct.UV_Ret.WaveData[var2 + var3];
         }

         var4[var2] /= Struct.UV_Ret.Avg_Step;
      }

      for(var2 = 0; var2 < var1 - Struct.UV_Ret.Avg_Step; ++var2) {
         Struct.UV_Ret.WaveData[var2] = (short)var4[var2];
      }

      Log.e("Calculator", "2Struct.UV_Ret.WaveData[0] : " + Struct.UV_Ret.WaveData[0]);
      Log.e("Calculator", "2Struct.UV_Ret.WaveData[0] : " + Struct.UV_Ret.WaveData[var1 - 1]);
   }

   private boolean Checksum_Check(int var1, int[] var2, int var3) {
      int var5 = 1;

      for(int var4 = 0; var4 < Struct.MDI_Para.ChecksumBit; ++var4) {
         var5 *= 2;
      }

      boolean var6;
      if (var3 == (((0 + var1) % var5 + var2[0]) % var5 + var2[1]) % var5) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   private void Data_Clear() {
      Struct.MDI_Det.buffer_count2 = 0;
      Struct.MDI_Det.count = 0;
      Struct.MDI_Det.buffer_count = 0;
      Struct.STATUS_STEP = 0;

      for(int var1 = 0; var1 < Struct.MDI_Para.TotalBit; ++var1) {
         Struct.MDI_Det.Pos[var1] = 0;
         Struct.MDI_Det.Findex[var1] = 0;
         Struct.MDI_Det.Findex_Value[var1] = 0.0F;
      }

   }

   private float Data_Convertion_to_RealValue(int var1, int[] var2) {
      int var4 = Struct.MDI_Ret.d_data[0] * 256 + Struct.MDI_Ret.d_data[1];
      float var3;
      switch(var1) {
      case 0:
         var3 = (float)var4;
         break;
      case 1:
         var3 = -46.85F + 0.0026812744F * (float)(var4 & -64);
         break;
      case 2:
         Log.e("Calculator", "Ret.d_data : " + Struct.MDI_Ret.d_data[0] + " " + Struct.MDI_Ret.d_data[1]);
         Log.e("Calculator", "ret : " + var4);
         var1 = var4 & -64;
         Log.e("Calculator", "&ret : " + var1);
         var3 = -6.0F + 0.0019073486F * (float)var1;
         break;
      case 3:
         this.EM_cal_init();
         var3 = this.Find_Cal_Result(6, (float)var4);
         break;
      case 4:
         this.EM_cal_init();
         var3 = this.Find_Cal_Result(7, (float)var4);
         break;
      case 128:
      case 129:
         var3 = (float)var4;
         Log.e("Calculator", "test type : " + var1);
         break;
      default:
         var3 = (float)var4;
      }

      return var3;
   }

   private void Data_Init(int var1) {
      Struct.MDI_Det.buffer_count2 = 0;
      Struct.MDI_Det.count = 0;
      Struct.MDI_Det.buffer_count = 0;
      Struct.STATUS_STEP = 0;

      for(var1 = 0; var1 < Struct.MDI_Para.TotalBit; ++var1) {
         Struct.MDI_Det.Pos[var1] = 0;
         Struct.MDI_Det.Findex[var1] = 0;
         Struct.MDI_Det.Findex_Value[var1] = 0.0F;
      }

      switch(Struct.MDI_Ret.STATUS) {
      case 0:
         if (Struct.MDI_Ret.dataOK_Flag) {
            ++Struct.MDI_Ret.OK_Count;
            Struct.MDI_Ret.Error_INSTANT = 0;
         } else {
            ++Struct.MDI_Ret.Error_Count;
            ++Struct.MDI_Ret.Error_INSTANT;
         }

         if (Struct.MDI_Ret.Rec_Count >= 10) {
            Struct.MDI_Ret.OK_Rate = (float)Struct.MDI_Ret.OK_Count / (float)Struct.MDI_Ret.Rec_Count * 100.0F;
         } else {
            Struct.MDI_Ret.OK_Rate = 0.0F;
         }

         if (Struct.MDI_Ret.Rec_Count > 100) {
            if (Struct.MDI_Ret.OK_Rate > 90.0F && !Struct.MDI_Ret.Save_Done_Flag) {
               Struct.MDI_Ret.Save_Flag = true;
               Struct.MDI_Ret.Save_Done_Flag = true;
            } else {
               Struct.MDI_Ret.Save_Flag = false;
            }
         }
         break;
      case 1:
         Struct.MDI_Ret.OK_Count = 0;
         Struct.MDI_Ret.Error_Count = 0;
         Struct.MDI_Ret.Rec_Count = 0;
         Struct.MDI_Ret.Error_INSTANT = 0;
      }

   }

   private boolean Detection_Process(int var1, int var2) {
      boolean var3 = false;
      ++Struct.MDI_Det.buffer_count;
      if (Struct.MDI_Det.count > 0) {
         ++Struct.MDI_Det.buffer_count2;
      }

      boolean var4 = var3;
      if (Struct.MDI_Det.count > 0) {
         var4 = var3;
         if ((float)Struct.MDI_Det.buffer_count2 > Struct.MDI_Para.STD_INTV * (float)Struct.MDI_Para.TotalBit * 1.1F) {
            var4 = true;
         }
      }

      switch(Struct.STATUS_STEP) {
      case 0:
         var3 = var4;
         if ((float)var2 >= Struct.MDI_Para.THR) {
            Struct.MDI_Det.sub_Count = 0;
            Struct.STATUS_STEP = 1;
            this.data_Update(var2, Struct.MDI_Det.buffer_count);
            var3 = var4;
         }
         break;
      case 1:
         ++Struct.MDI_Det.sub_Count;
         if (var2 >= Struct.MDI_Det.Det_AMax) {
            Struct.MDI_Det.sub_Count = 0;
            this.data_Update(var2, Struct.MDI_Det.buffer_count);
         }

         var3 = var4;
         if (Struct.MDI_Det.sub_Count > Struct.MDI_Para.DIFF_PULSE_WIDTH) {
            if (Struct.MDI_Det.count == 0 && var1 == 0) {
               ++Struct.MDI_Ret.Rec_Count;
            }

            if (Struct.MDI_Det.count > 33) {
               var3 = true;
            } else {
               var2 = Struct.MDI_Det.count;
               Struct.MDI_Det.DfA[var2] = Struct.MDI_Det.Det_AMax;
               Struct.MDI_Det.Pos[var2] = Struct.MDI_Det.Det_APos;
               Struct.STATUS_STEP = 2;
               ++Struct.MDI_Det.count;
               if (Struct.MDI_Det.count > Struct.MDI_Para.TotalBit) {
                  var3 = true;
               } else {
                  var3 = var4;
                  if (var2 > 0) {
                     var3 = var4;
                     if (this.Final_Data_Check(var2)) {
                        if (this.Validation_Check(var2)) {
                           Struct.MDI_Det.S_Inv = (float)(Struct.MDI_Det.Pos[Struct.MDI_Det.count - 1] - Struct.MDI_Det.Pos[0]) / 33.0F;
                           if (var1 == 0) {
                              this.Parameter_Update();
                           }

                           Struct.MDI_Ret.dataOK_Flag = true;
                           this.Data_Init(0);
                           var3 = var4;
                        } else {
                           var3 = true;
                        }
                     }
                  }
               }
            }
         }
         break;
      case 2:
         ++Struct.MDI_Det.sub_Count;
         var3 = var4;
         if ((float)Struct.MDI_Det.sub_Count >= Struct.MDI_Para.STD_INTV - 20.0F) {
            Struct.STATUS_STEP = 0;
            var3 = var4;
         }
         break;
      default:
         var3 = var4;
      }

      if (var3) {
         this.Data_Init(Struct.MDI_Ret.STATUS);
      }

      return Struct.MDI_Ret.dataOK_Flag;
   }

   private void EM_cal_init() {
      Struct.EM_Fact.X[0][0] = 0.0F;
      float[] var4 = Struct.EM_Fact.Y[0];
      int var1 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[0][var1] = 14.0F;
      var4 = Struct.EM_Fact.Y[0];
      int var2 = var1 + 1;
      var4[var1] = 30.0F;
      Struct.EM_Fact.X[0][var2] = 19.0F;
      var4 = Struct.EM_Fact.Y[0];
      var1 = var2 + 1;
      var4[var2] = 50.0F;
      Struct.EM_Fact.X[0][var1] = 32.0F;
      var4 = Struct.EM_Fact.Y[0];
      var2 = var1 + 1;
      var4[var1] = 105.0F;
      Struct.EM_Fact.X[0][var2] = 57.0F;
      var4 = Struct.EM_Fact.Y[0];
      var1 = var2 + 1;
      var4[var2] = 195.0F;
      Struct.EM_Fact.X[0][var1] = 98.0F;
      var4 = Struct.EM_Fact.Y[0];
      var2 = var1 + 1;
      var4[var1] = 305.0F;
      Struct.EM_Fact.X[0][var2] = 114.0F;
      var4 = Struct.EM_Fact.Y[0];
      int var3 = var2 + 1;
      var4[var2] = 385.0F;
      Struct.EM_Fact.X[0][var3] = 145.0F;
      var4 = Struct.EM_Fact.Y[0];
      var1 = var3 + 1;
      var4[var3] = 510.0F;
      Struct.EM_Fact.X[0][var1] = 186.0F;
      var4 = Struct.EM_Fact.Y[0];
      var2 = var1 + 1;
      var4[var1] = 601.0F;
      Struct.EM_Fact.X[0][var2] = 213.0F;
      var4 = Struct.EM_Fact.Y[0];
      var1 = var2 + 1;
      var4[var2] = 685.0F;
      Struct.EM_Fact.X[0][var1] = 256.0F;
      var4 = Struct.EM_Fact.Y[0];
      var2 = var1 + 1;
      var4[var1] = 798.0F;
      Struct.EM_Fact.X[0][var2] = 400.0F;
      var4 = Struct.EM_Fact.Y[0];
      var1 = var2 + 1;
      var4[var2] = 1000.0F;
      Struct.EM_Fact.X[0][var1] = 900.0F;
      Struct.EM_Fact.Y[0][var1] = 1200.0F;
      Struct.EM_Fact.No[0] = var1 + 1;
      Struct.EM_Fact.X[1][0] = 10.0F;
      var4 = Struct.EM_Fact.Y[1];
      var1 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[1][var1] = 105.0F;
      var4 = Struct.EM_Fact.Y[1];
      var2 = var1 + 1;
      var4[var1] = 56.0F;
      Struct.EM_Fact.X[1][var2] = 166.0F;
      var4 = Struct.EM_Fact.Y[1];
      var1 = var2 + 1;
      var4[var2] = 130.0F;
      Struct.EM_Fact.X[1][var1] = 288.0F;
      var4 = Struct.EM_Fact.Y[1];
      var3 = var1 + 1;
      var4[var1] = 260.0F;
      Struct.EM_Fact.X[1][var3] = 437.0F;
      var4 = Struct.EM_Fact.Y[1];
      var2 = var3 + 1;
      var4[var3] = 390.0F;
      Struct.EM_Fact.X[1][var2] = 602.0F;
      var4 = Struct.EM_Fact.Y[1];
      var1 = var2 + 1;
      var4[var2] = 495.0F;
      Struct.EM_Fact.X[1][var1] = 769.0F;
      var4 = Struct.EM_Fact.Y[1];
      var3 = var1 + 1;
      var4[var1] = 630.0F;
      Struct.EM_Fact.X[1][var3] = 976.0F;
      var4 = Struct.EM_Fact.Y[1];
      var2 = var3 + 1;
      var4[var3] = 757.0F;
      Struct.EM_Fact.X[1][var2] = 1226.0F;
      var4 = Struct.EM_Fact.Y[1];
      var1 = var2 + 1;
      var4[var2] = 833.0F;
      Struct.EM_Fact.X[1][var1] = 7500.0F;
      var4 = Struct.EM_Fact.Y[1];
      var2 = var1 + 1;
      var4[var1] = 1000.0F;
      Struct.EM_Fact.X[1][var2] = 8800.0F;
      Struct.EM_Fact.Y[1][var2] = 1400.0F;
      Struct.EM_Fact.No[1] = var2 + 1;
      Struct.EM_Fact.X[2][0] = 2.8F;
      var4 = Struct.EM_Fact.Y[2];
      var1 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[2][var1] = 5.0F;
      var4 = Struct.EM_Fact.Y[2];
      var2 = var1 + 1;
      var4[var1] = 30.0F;
      Struct.EM_Fact.X[2][var2] = 20.0F;
      var4 = Struct.EM_Fact.Y[2];
      var1 = var2 + 1;
      var4[var2] = 97.0F;
      Struct.EM_Fact.X[2][var1] = 59.0F;
      var4 = Struct.EM_Fact.Y[2];
      var2 = var1 + 1;
      var4[var1] = 210.0F;
      Struct.EM_Fact.X[2][var2] = 139.0F;
      var4 = Struct.EM_Fact.Y[2];
      var1 = var2 + 1;
      var4[var2] = 339.0F;
      Struct.EM_Fact.X[2][var1] = 191.0F;
      var4 = Struct.EM_Fact.Y[2];
      var2 = var1 + 1;
      var4[var1] = 440.0F;
      Struct.EM_Fact.X[2][var2] = 308.0F;
      var4 = Struct.EM_Fact.Y[2];
      var1 = var2 + 1;
      var4[var2] = 575.0F;
      Struct.EM_Fact.X[2][var1] = 394.0F;
      var4 = Struct.EM_Fact.Y[2];
      var2 = var1 + 1;
      var4[var1] = 690.0F;
      Struct.EM_Fact.X[2][var2] = 550.0F;
      var4 = Struct.EM_Fact.Y[2];
      var1 = var2 + 1;
      var4[var2] = 803.0F;
      Struct.EM_Fact.X[2][var1] = 2200.0F;
      var4 = Struct.EM_Fact.Y[2];
      var2 = var1 + 1;
      var4[var1] = 1000.0F;
      Struct.EM_Fact.X[2][var2] = 3300.0F;
      Struct.EM_Fact.Y[2][var2] = 1400.0F;
      Struct.EM_Fact.No[2] = var2 + 1;
      Struct.EM_Fact.X[3][0] = 1.0F;
      var4 = Struct.EM_Fact.Y[3];
      var1 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[3][var1] = 3.0F;
      var4 = Struct.EM_Fact.Y[3];
      var2 = var1 + 1;
      var4[var1] = 55.0F;
      Struct.EM_Fact.X[3][var2] = 10.0F;
      var4 = Struct.EM_Fact.Y[3];
      var1 = var2 + 1;
      var4[var2] = 135.0F;
      Struct.EM_Fact.X[3][var1] = 36.0F;
      var4 = Struct.EM_Fact.Y[3];
      var2 = var1 + 1;
      var4[var1] = 270.0F;
      Struct.EM_Fact.X[3][var2] = 64.0F;
      var4 = Struct.EM_Fact.Y[3];
      var3 = var2 + 1;
      var4[var2] = 380.0F;
      Struct.EM_Fact.X[3][var3] = 124.0F;
      var4 = Struct.EM_Fact.Y[3];
      var1 = var3 + 1;
      var4[var3] = 507.0F;
      Struct.EM_Fact.X[3][var1] = 185.0F;
      var4 = Struct.EM_Fact.Y[3];
      var2 = var1 + 1;
      var4[var1] = 640.0F;
      Struct.EM_Fact.X[3][var2] = 225.0F;
      var4 = Struct.EM_Fact.Y[3];
      var1 = var2 + 1;
      var4[var2] = 740.0F;
      Struct.EM_Fact.X[3][var1] = 331.0F;
      Struct.EM_Fact.Y[3][var1] = 860.0F;
      Struct.EM_Fact.No[3] = var1 + 1;
      Struct.EM_Fact.X[4][0] = 2.0F;
      var4 = Struct.EM_Fact.Y[4];
      var1 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[4][var1] = 3.9F;
      var4 = Struct.EM_Fact.Y[4];
      var2 = var1 + 1;
      var4[var1] = 12.0F;
      Struct.EM_Fact.X[4][var2] = 6.8F;
      var4 = Struct.EM_Fact.Y[4];
      var1 = var2 + 1;
      var4[var2] = 59.0F;
      Struct.EM_Fact.X[4][var1] = 20.7F;
      var4 = Struct.EM_Fact.Y[4];
      var2 = var1 + 1;
      var4[var1] = 100.0F;
      Struct.EM_Fact.X[4][var2] = 123.0F;
      var4 = Struct.EM_Fact.Y[4];
      var1 = var2 + 1;
      var4[var2] = 193.0F;
      Struct.EM_Fact.X[4][var1] = 1072.0F;
      var4 = Struct.EM_Fact.Y[4];
      var2 = var1 + 1;
      var4[var1] = 290.0F;
      Struct.EM_Fact.X[4][var2] = 1392.0F;
      var4 = Struct.EM_Fact.Y[4];
      var3 = var2 + 1;
      var4[var2] = 395.0F;
      Struct.EM_Fact.X[4][var3] = 1776.0F;
      var4 = Struct.EM_Fact.Y[4];
      var1 = var3 + 1;
      var4[var3] = 500.0F;
      Struct.EM_Fact.X[4][var1] = 2080.0F;
      var4 = Struct.EM_Fact.Y[4];
      var2 = var1 + 1;
      var4[var1] = 556.0F;
      Struct.EM_Fact.X[4][var2] = 2400.0F;
      Struct.EM_Fact.Y[4][var2] = 646.0F;
      Struct.EM_Fact.No[4] = var2 + 1;
      Struct.EM_Fact.X[5][0] = 2.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[5][var2] = 3.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var2 + 1;
      var4[var2] = 5.0F;
      Struct.EM_Fact.X[5][var1] = 6.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = var1 + 1;
      var4[var1] = 10.0F;
      Struct.EM_Fact.X[5][var2] = 35.0F;
      var4 = Struct.EM_Fact.Y[5];
      var3 = var2 + 1;
      var4[var2] = 40.0F;
      Struct.EM_Fact.X[5][var3] = 120.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var3 + 1;
      var4[var3] = 80.0F;
      Struct.EM_Fact.X[5][var1] = 320.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = var1 + 1;
      var4[var1] = 150.0F;
      Struct.EM_Fact.X[5][var2] = 650.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var2 + 1;
      var4[var2] = 220.0F;
      Struct.EM_Fact.X[5][var1] = 1000.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = var1 + 1;
      var4[var1] = 300.0F;
      Struct.EM_Fact.X[5][var2] = 1300.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var2 + 1;
      var4[var2] = 400.0F;
      Struct.EM_Fact.X[5][var1] = 1850.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = var1 + 1;
      var4[var1] = 550.0F;
      Struct.EM_Fact.X[5][var2] = 2200.0F;
      var4 = Struct.EM_Fact.Y[5];
      var3 = var2 + 1;
      var4[var2] = 700.0F;
      Struct.EM_Fact.X[5][var3] = 2800.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var3 + 1;
      var4[var3] = 900.0F;
      Struct.EM_Fact.X[5][var1] = 3300.0F;
      var4 = Struct.EM_Fact.Y[5];
      var2 = var1 + 1;
      var4[var1] = 110.0F;
      Struct.EM_Fact.X[5][var2] = 4400.0F;
      var4 = Struct.EM_Fact.Y[5];
      var1 = var2 + 1;
      var4[var2] = 1350.0F;
      Struct.EM_Fact.X[5][var1] = 15000.0F;
      Struct.EM_Fact.Y[5][var1] = 1500.0F;
      Struct.EM_Fact.No[5] = var1 + 1;
      Struct.EM_Fact.X[6][0] = 1.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = 0 + 1;
      var4[0] = 0.0F;
      Struct.EM_Fact.X[6][var2] = 55.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 0.5F;
      Struct.EM_Fact.X[6][var1] = 103.0F;
      var4 = Struct.EM_Fact.Y[6];
      var3 = var1 + 1;
      var4[var1] = 1.0F;
      Struct.EM_Fact.X[6][var3] = 156.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var3 + 1;
      var4[var3] = 1.5F;
      Struct.EM_Fact.X[6][var2] = 204.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 2.0F;
      Struct.EM_Fact.X[6][var1] = 255.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 2.5F;
      Struct.EM_Fact.X[6][var2] = 306.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 3.0F;
      Struct.EM_Fact.X[6][var1] = 353.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 3.5F;
      Struct.EM_Fact.X[6][var2] = 407.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 4.0F;
      Struct.EM_Fact.X[6][var1] = 455.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 4.5F;
      Struct.EM_Fact.X[6][var2] = 509.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 5.0F;
      Struct.EM_Fact.X[6][var1] = 557.0F;
      var4 = Struct.EM_Fact.Y[6];
      var3 = var1 + 1;
      var4[var1] = 5.5F;
      Struct.EM_Fact.X[6][var3] = 607.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var3 + 1;
      var4[var3] = 6.0F;
      Struct.EM_Fact.X[6][var2] = 658.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 6.5F;
      Struct.EM_Fact.X[6][var1] = 706.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 7.0F;
      Struct.EM_Fact.X[6][var2] = 760.0F;
      var4 = Struct.EM_Fact.Y[6];
      var3 = var2 + 1;
      var4[var2] = 7.5F;
      Struct.EM_Fact.X[6][var3] = 807.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var3 + 1;
      var4[var3] = 8.0F;
      Struct.EM_Fact.X[6][var1] = 860.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 8.5F;
      Struct.EM_Fact.X[6][var2] = 908.0F;
      var4 = Struct.EM_Fact.Y[6];
      var1 = var2 + 1;
      var4[var2] = 9.0F;
      Struct.EM_Fact.X[6][var1] = 959.0F;
      var4 = Struct.EM_Fact.Y[6];
      var2 = var1 + 1;
      var4[var1] = 9.5F;
      Struct.EM_Fact.X[6][var2] = 1011.0F;
      Struct.EM_Fact.Y[6][var2] = 10.0F;
      Struct.EM_Fact.No[6] = var2 + 1;
      Struct.EM_Fact.X[7][0] = 72.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = 0 + 1;
      var4[0] = 15.0F;
      Struct.EM_Fact.X[7][var2] = 125.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 13.0F;
      Struct.EM_Fact.X[7][var1] = 180.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = 11.0F;
      Struct.EM_Fact.X[7][var2] = 210.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 10.0F;
      Struct.EM_Fact.X[7][var1] = 230.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = 9.0F;
      Struct.EM_Fact.X[7][var2] = 250.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 8.0F;
      Struct.EM_Fact.X[7][var1] = 270.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = 7.0F;
      Struct.EM_Fact.X[7][var2] = 290.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 6.0F;
      Struct.EM_Fact.X[7][var1] = 310.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = 5.0F;
      Struct.EM_Fact.X[7][var2] = 330.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 4.0F;
      Struct.EM_Fact.X[7][var1] = 350.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = 3.0F;
      Struct.EM_Fact.X[7][var2] = 370.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = 2.0F;
      Struct.EM_Fact.X[7][var1] = 380.0F;
      var4 = Struct.EM_Fact.Y[7];
      var3 = var1 + 1;
      var4[var1] = 1.0F;
      Struct.EM_Fact.X[7][var3] = 395.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var3 + 1;
      var4[var3] = 0.0F;
      Struct.EM_Fact.X[7][var2] = 400.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -1.0F;
      Struct.EM_Fact.X[7][var1] = 420.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -2.0F;
      Struct.EM_Fact.X[7][var2] = 440.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -3.0F;
      Struct.EM_Fact.X[7][var1] = 460.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -4.0F;
      Struct.EM_Fact.X[7][var2] = 490.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -5.0F;
      Struct.EM_Fact.X[7][var1] = 510.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -6.0F;
      Struct.EM_Fact.X[7][var2] = 530.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -7.0F;
      Struct.EM_Fact.X[7][var1] = 550.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -8.0F;
      Struct.EM_Fact.X[7][var2] = 570.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -9.0F;
      Struct.EM_Fact.X[7][var1] = 590.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -10.0F;
      Struct.EM_Fact.X[7][var2] = 610.0F;
      var4 = Struct.EM_Fact.Y[7];
      var1 = var2 + 1;
      var4[var2] = -11.0F;
      Struct.EM_Fact.X[7][var1] = 650.0F;
      var4 = Struct.EM_Fact.Y[7];
      var2 = var1 + 1;
      var4[var1] = -13.0F;
      Struct.EM_Fact.X[7][var2] = 710.0F;
      Struct.EM_Fact.Y[7][var2] = -15.0F;
      Struct.EM_Fact.No[7] = var2 + 1;
   }

   private boolean Final_Data_Check(int var1) {
      boolean var2;
      if ((int)((float)(Struct.MDI_Det.Pos[var1] - Struct.MDI_Det.Pos[0]) / Struct.MDI_Para.STD_INTV + 0.5F) >= Struct.MDI_Para.TotalBit - 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private float Find_Cal_Result(int var1, float var2) {
      float var8 = 0.0F;
      float var7 = 0.0F;
      float var9 = 0.0F;
      float var10 = 0.0F;
      int var12 = Struct.EM_Fact.No[var1];
      float var3;
      float var4;
      float var5;
      float var6;
      if (var2 >= Struct.EM_Fact.X[var1][var12 - 1]) {
         var6 = Struct.EM_Fact.Y[var1][var12 - 1];
         var4 = Struct.EM_Fact.Y[var1][var12 - 2];
         var5 = Struct.EM_Fact.X[var1][var12 - 1];
         var3 = Struct.EM_Fact.X[var1][var12 - 2];
      } else if (var2 <= Struct.EM_Fact.X[var1][0]) {
         var6 = Struct.EM_Fact.Y[var1][1];
         var4 = Struct.EM_Fact.Y[var1][0];
         var5 = Struct.EM_Fact.X[var1][1];
         var3 = Struct.EM_Fact.X[var1][0];
      } else {
         int var11 = 1;

         while(true) {
            var3 = var8;
            var5 = var7;
            var4 = var9;
            var6 = var10;
            if (var11 >= var12) {
               break;
            }

            if (var2 <= Struct.EM_Fact.X[var1][var11]) {
               var6 = Struct.EM_Fact.Y[var1][var11];
               var4 = Struct.EM_Fact.Y[var1][var11 - 1];
               var5 = Struct.EM_Fact.X[var1][var11];
               var3 = Struct.EM_Fact.X[var1][var11 - 1];
               break;
            }

            ++var11;
         }
      }

      var5 = (var6 - var4) / (var5 - var3);
      var3 = var5 * var2 + (var4 - var5 * var3);
      var2 = var3;
      if (var1 != 7) {
         var2 = var3;
         if (var3 < 0.0F) {
            var2 = 0.0F;
         }
      }

      return var2;
   }

   private float Find_Frequency(short[] var1, int var2) {
      short var4 = 0;
      short var5 = 0;

      int var6;
      short var8;
      for(var6 = 0; var6 < var2; var5 = var8) {
         short var7 = var4;
         if (var1[var6] >= var4) {
            var7 = var1[var6];
         }

         var8 = var5;
         if (var1[var6] <= var5) {
            var8 = var1[var6];
         }

         ++var6;
         var4 = var7;
      }

      Log.e("Calculator", "MaxValue : " + var4);
      Log.e("Calculator", "MinValue : " + var5);
      int var10;
      if (Math.abs(var4) > Math.abs(var5)) {
         Struct.UV_Ret.Polarity = 1;
         Log.e("Calculator", "Struct.UV_Ret.Polarity = 1");
      } else {
         Struct.UV_Ret.Polarity = -1;

         for(var10 = 0; var10 < var2; ++var10) {
            var1[var10] = (short)(var1[var10] * -1);
         }

         var4 = (short)(Struct.UV_Ret.Polarity * var5);
         Log.e("Calculator", "Struct.UV_Ret.Polarity = -1");
      }

      int var11;
      for(var11 = 0; var11 < var2; ++var11) {
         if (var1[var11] > (short)((int)((float)var4 * Struct.UV_Ret.MaxValue_Find_Level))) {
            var1[var11] = 1;
         } else {
            var1[var11] = -1;
         }
      }

      int[] var9 = new int[200];
      var11 = 0;

      for(var10 = 0; var11 < var2 - 1; ++var11) {
         if (var1[var11] * var1[var11 + 1] < 0 && var1[var11] < 0) {
            var6 = var10 + 1;
            var9[var10] = var11;
            var11 += 441;
            var10 = var6;
         }
      }

      float var3;
      if (var10 < 2) {
         Log.e("Calculator", "Find_Pivot_No Error, Find_Pivot_No : " + var10);
         var3 = 0.0F;
      } else {
         var3 = 0.0F;

         for(var2 = 1; var2 < var10; ++var2) {
            var3 += (float)(var9[var2] - var9[var2 - 1]);
         }

         var3 = 44100.0F / (var3 / (float)(var10 - 1));
         if (var3 < 1.0F) {
            var3 = Struct.UV_Ret.Frq;
         }
      }

      return var3;
   }

   private Struct.MDI_Data Find_Parameter(short[] var1, int var2) {
      Struct.MDI_Data var10;
      if (var2 < 22050) {
         var10 = this.MDI_Data_Ret;
      } else {
         int[] var7 = new int[(var2 - 22050) * 4];
         int var4 = 22050;

         int var3;
         for(var3 = 0; var4 < var2; ++var3) {
            var7[var3] = var1[var4];
            ++var4;
         }

         this.Find_Polarity_and_MaxValue(var1, var3);
         Struct.MDI_Para.STD_INTV = 113.0F;
         int[] var11 = new int[10];
         float[] var8 = new float[10];
         float[] var9 = new float[10];

         int var5;
         for(var4 = 0; var4 < 10; ++var4) {
            var11[var4] = 0;
            switch(var4) {
            case 0:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxAmp * 0.8F;
               Struct.MDI_Para.WaveType = 0;
               break;
            case 1:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxAmp * 0.6F;
               Struct.MDI_Para.WaveType = 0;
               break;
            case 2:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxAmp * 0.5F;
               Struct.MDI_Para.WaveType = 0;
               break;
            case 3:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxAmp * 0.4F;
               Struct.MDI_Para.WaveType = 0;
               break;
            case 4:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxAmp * 0.3F;
               Struct.MDI_Para.WaveType = 0;
               break;
            case 5:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxDAmp * 0.8F;
               Struct.MDI_Para.WaveType = 1;
               break;
            case 6:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxDAmp * 0.6F;
               Struct.MDI_Para.WaveType = 1;
               break;
            case 7:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxDAmp * 0.4F;
               Struct.MDI_Para.WaveType = 1;
               break;
            case 8:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxDAmp * 0.2F;
               Struct.MDI_Para.WaveType = 1;
               break;
            case 9:
               var8[var4] = (float)Struct.MDI_BasicPara.MaxDAmp * 0.1F;
               Struct.MDI_Para.WaveType = 1;
            }

            Struct.MDI_Para.THR = var8[var4];
            this.Data_Clear();

            for(var5 = 1; var5 < var3; ++var5) {
               int var6;
               if (var4 < 5) {
                  var6 = var7[var5] * Struct.MDI_Para.POL;
                  var2 = var6;
                  if ((float)var6 < Struct.MDI_Para.THR) {
                     var2 = 0;
                  }
               } else if (var7[var5] < 0) {
                  var2 = 0;
               } else {
                  var2 = Math.abs(var7[var5] - var7[var5 - 1]);
               }

               var6 = var2;
               if ((float)var2 < Struct.MDI_Para.THR) {
                  var6 = 0;
               }

               if (this.Detection_Process(1, var6)) {
                  ++var11[var4];
                  var9[var4] = Struct.MDI_Det.S_Inv;
                  Struct.MDI_Ret.dataOK_Flag = false;
               }
            }
         }

         var5 = 100;
         var3 = 0;

         for(var2 = 0; var2 < 10; var3 = var4) {
            var4 = var3;
            if (var11[var2] >= var3) {
               var4 = var11[var2];
               var5 = var2;
            }

            ++var2;
         }

         if (var5 < 100) {
            Struct.MDI_Para.STD_INTV = var9[var5];
            Struct.MDI_Para.WaveType = var5 / 5;
            Struct.MDI_Para.THR = var8[var5];
            if (Struct.MDI_Para.WaveType == 0) {
               Struct.MDI_Para.POL = Struct.MDI_BasicPara.POL;
            } else {
               Struct.MDI_Para.POL = 1;
            }

            Struct.MDI_Data.P_IsParameter_OK = true;
         } else {
            Struct.MDI_Data.P_IsParameter_OK = false;
         }

         Struct.MDI_Ret.OK_Count = 0;
         Struct.MDI_Ret.Rec_Count = 0;
         Struct.MDI_Ret.Error_Count = 0;
         Struct.MDI_Ret.OK_Rate = 0.0F;
         Struct.MDI_Data.P_SINTV = Struct.MDI_Para.STD_INTV;
         Struct.MDI_Data.P_WaveType = Struct.MDI_Para.WaveType;
         Struct.MDI_Data.P_THR = Struct.MDI_Para.THR;
         Struct.MDI_Data.P_POL = Struct.MDI_Para.POL;
         var10 = this.MDI_Data_Ret;
      }

      return var10;
   }

   private void Find_Polarity_and_MaxValue(short[] var1, int var2) {
      short var5 = 0;
      int var4 = 0;

      int var6;
      int var8;
      for(var6 = 0; var6 < var2; var4 = var8) {
         short var7 = var5;
         if (Math.abs(var1[var6]) > var5) {
            var7 = var1[var6];
         }

         var8 = var4;
         if (var6 > 0) {
            int var11 = Math.abs(var1[var6] - var1[var6 - 1]);
            var8 = var4;
            if (var11 > var4) {
               var8 = var11;
            }
         }

         ++var6;
         var5 = var7;
      }

      float var3 = (float)var5 * 0.8F;
      int var12 = 0;
      var8 = 0;

      int var10;
      for(var6 = 0; var6 < var2; var12 = var10) {
         int var9;
         if (var1[var6] > 0) {
            var9 = var8;
            var10 = var12;
            if ((float)var1[var6] > var3) {
               var10 = var12 + 1;
               var9 = var8;
            }
         } else {
            var9 = var8;
            var10 = var12;
            if ((float)Math.abs(var1[var6]) > var3) {
               var9 = var8 + 1;
               var10 = var12;
            }
         }

         ++var6;
         var8 = var9;
      }

      if (var12 >= var8) {
         Struct.MDI_Para.POL = 1;
      } else {
         Struct.MDI_Para.POL = -1;
      }

      Struct.MDI_BasicPara.MaxAmp = var5;
      Struct.MDI_BasicPara.POL = Struct.MDI_Para.POL;
      Struct.MDI_BasicPara.MaxDAmp = var4;
   }

   private float Find_UV_Index(float var1) {
      return (float)((int)var1) / 500.0F;
   }

   private void Find_UV_Lux_Init() {
      switch(Struct.UV_Ret.Type) {
      case 2:
         Struct.UV_Lux_Fact.X[0] = 16.45F;
         float[] var4 = Struct.UV_Lux_Fact.Y;
         int var1 = 0 + 1;
         var4[0] = 0.0F;
         Struct.UV_Lux_Fact.X[var1] = 16.64F;
         var4 = Struct.UV_Lux_Fact.Y;
         int var2 = var1 + 1;
         var4[var1] = 84.0F;
         Struct.UV_Lux_Fact.X[var2] = 16.69F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 103.0F;
         Struct.UV_Lux_Fact.X[var1] = 16.75F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 130.0F;
         Struct.UV_Lux_Fact.X[var2] = 16.85F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 171.0F;
         Struct.UV_Lux_Fact.X[var1] = 17.0F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 234.0F;
         Struct.UV_Lux_Fact.X[var2] = 17.28F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 348.0F;
         Struct.UV_Lux_Fact.X[var1] = 17.49F;
         var4 = Struct.UV_Lux_Fact.Y;
         int var3 = var1 + 1;
         var4[var1] = 440.0F;
         Struct.UV_Lux_Fact.X[var3] = 17.79F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var3 + 1;
         var4[var3] = 561.0F;
         Struct.UV_Lux_Fact.X[var2] = 18.24F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 763.0F;
         Struct.UV_Lux_Fact.X[var1] = 18.98F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 1080.0F;
         Struct.UV_Lux_Fact.X[var2] = 19.66F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 1350.0F;
         Struct.UV_Lux_Fact.X[var1] = 20.42F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 1600.0F;
         Struct.UV_Lux_Fact.X[var2] = 21.42F;
         var4 = Struct.UV_Lux_Fact.Y;
         var3 = var2 + 1;
         var4[var2] = 2050.0F;
         Struct.UV_Lux_Fact.X[var3] = 22.42F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var3 + 1;
         var4[var3] = 2480.0F;
         Struct.UV_Lux_Fact.X[var1] = 22.94F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 2740.0F;
         Struct.UV_Lux_Fact.X[var2] = 23.6F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 3030.0F;
         Struct.UV_Lux_Fact.X[var1] = 24.3F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 3360.0F;
         Struct.UV_Lux_Fact.X[var2] = 25.06F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 3710.0F;
         Struct.UV_Lux_Fact.X[var1] = 25.88F;
         var4 = Struct.UV_Lux_Fact.Y;
         var3 = var1 + 1;
         var4[var1] = 4150.0F;
         Struct.UV_Lux_Fact.X[var3] = 26.78F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var3 + 1;
         var4[var3] = 4520.0F;
         Struct.UV_Lux_Fact.X[var2] = 27.73F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 4950.0F;
         Struct.UV_Lux_Fact.X[var1] = 28.76F;
         var4 = Struct.UV_Lux_Fact.Y;
         var2 = var1 + 1;
         var4[var1] = 5430.0F;
         Struct.UV_Lux_Fact.X[var2] = 29.71F;
         var4 = Struct.UV_Lux_Fact.Y;
         var1 = var2 + 1;
         var4[var2] = 6000.0F;
         Struct.UV_Lux_Fact.X[var1] = 30.3F;
         Struct.UV_Lux_Fact.Y[var1] = 6570.0F;
         Struct.UV_Lux_Fact.No = var1 + 1;
      default:
      }
   }

   private float Find_UV_Lux_Result(float var1) {
      if (var1 <= 16.2F) {
         var1 = 0.0F;
      } else {
         var1 = 372.77F * var1 - 6058.9F;
      }

      float var2 = var1;
      if (var1 >= 40000.0F) {
         var2 = 40000.0F;
      }

      return var2;
   }

   private float Find_UV_Value(float var1) {
      float var2 = 0.0F;
      switch(Struct.UV_Ret.Type) {
      case 0:
         var2 = (Struct.UV_Ret.cfact_UV_slop * var1 + Struct.UV_Ret.cfact_UV_y) * 0.857F;
         var1 = var2;
         if (var2 < 0.0F) {
            var1 = 0.0F;
         }
         break;
      case 1:
         var1 = 192.3077F * (Struct.UV_Ret.Frq * 0.0135F - 0.2F);
         if (Struct.UV_Ret.Frq <= 16.5F) {
            var1 = 0.0F;
         }
         break;
      case 2:
         var1 = 10.0F * (Struct.UV_Ret.Frq * 0.0135F - 0.2F);
         if (Struct.UV_Ret.Frq <= 16.5F) {
            var1 = 0.0F;
         }
         break;
      default:
         var1 = var2;
      }

      return var1;
   }

   private float Find_Voltage(float var1) {
      float var2 = Struct.UV_Ret.cfact_slop * var1 + Struct.UV_Ret.cfact_y;
      var1 = var2;
      if (var2 < 0.0F) {
         var1 = 0.0F;
      }

      return var1;
   }

   private String MDI_Find_Unit(int var1) {
      String var2;
      switch(var1) {
      case 1:
         var2 = "℃";
         break;
      case 2:
         var2 = "%";
         break;
      case 3:
         var2 = "V";
         break;
      default:
         var2 = "";
      }

      return var2;
   }

   private void Parameter_Update() {
      float var1 = 65535.0F;

      float var2;
      for(int var3 = 0; var3 < Struct.MDI_Det.count; var1 = var2) {
         var2 = var1;
         if (var1 > (float)Struct.MDI_Det.DfA[var3]) {
            var2 = (float)Struct.MDI_Det.DfA[var3];
         }

         ++var3;
      }

      var2 = (float)(Struct.MDI_Det.Pos[Struct.MDI_Det.count - 1] - Struct.MDI_Det.Pos[0]) / (float)(Struct.MDI_Para.TotalBit - 1);
      Struct.MDI_Para.STD_INTV = (Struct.MDI_Para.STD_INTV * 9.0F + var2) / 10.0F;
      Struct.MDI_Para.THR = (Struct.MDI_Para.THR * 9.0F + 0.5F * var1) / 10.0F;
   }

   private boolean Validation_Check(int var1) {
      int var3 = Struct.MDI_Para.TotalBit - 2;

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         Struct.MDI_Det.fData[var2] = 0;
         Struct.MDI_Det.Findex[var2] = 0;
         Struct.MDI_Det.Findex_Value[var2] = 0.0F;
      }

      Struct.MDI_Det.Findex_Value[0] = 0.0F;
      var2 = 1;

      boolean var6;
      while(true) {
         int var4;
         if (var2 >= var1) {
            Struct.MDI_Det.Findex_Value[var1] = (float)(Struct.MDI_Det.Pos[var1] - Struct.MDI_Det.Pos[0]) / Struct.MDI_Para.STD_INTV;
            var1 = 0;
            var3 = 0;

            for(var2 = 0; var2 < Struct.MDI_Para.TypeBit; var3 = var4) {
               var4 = var3;
               if (Struct.MDI_Det.fData[var1] == 1) {
                  var4 = var3 + (1 << Struct.MDI_Para.TypeBit - 1 - var2);
               }

               ++var2;
               ++var1;
            }

            Struct.MDI_Ret.d_type = var3;

            for(var2 = 0; var2 < Struct.MDI_Para.DataBit / 8; ++var2) {
               var4 = 0;

               int var5;
               for(var3 = 0; var3 < 8; var4 = var5) {
                  var5 = var4;
                  if (Struct.MDI_Det.fData[var1] == 1) {
                     var5 = var4 + (1 << 7 - var3);
                  }

                  ++var3;
                  ++var1;
               }

               Struct.MDI_Ret.d_data[var2] = var4;
            }

            var3 = 0;

            for(var2 = 0; var2 < Struct.MDI_Para.ChecksumBit; var3 = var4) {
               var4 = var3;
               if (Struct.MDI_Det.fData[var1] == 1) {
                  var4 = var3 + (1 << Struct.MDI_Para.ChecksumBit - 1 - var2);
               }

               ++var2;
               ++var1;
            }

            Struct.MDI_Ret.d_checksum = var3;
            var6 = this.Checksum_Check(Struct.MDI_Ret.d_type, Struct.MDI_Ret.d_data, Struct.MDI_Ret.d_checksum);
            break;
         }

         Struct.MDI_Det.Findex_Value[var2] = (float)(Struct.MDI_Det.Pos[var2] - Struct.MDI_Det.Pos[0]) / Struct.MDI_Para.STD_INTV;
         var4 = Math.round(Struct.MDI_Det.Findex_Value[var2]);
         Struct.MDI_Det.Findex[var2 - 1] = var4;
         if (var4 >= var3 + 1 || var4 == 0) {
            var6 = false;
            break;
         }

         Struct.MDI_Det.fData[var4 - 1] = 1;
         ++var2;
      }

      return var6;
   }

   private void data_Update(int var1, int var2) {
      Struct.MDI_Det.Det_AMax = var1;
      Struct.MDI_Det.Det_APos = var2;
   }

   public float EM_AutoVmin(short[] var1, int var2) {
      float var4 = 0.0F;
      ++Struct.EM_Ret.pack_cnt;
      float var3;
      if (Struct.EM_Ret.pack_cnt >= 10) {
         var3 = 0.0F;

         for(int var5 = 0; var5 < var2; ++var5) {
            var3 += (float)var1[var5] * (float)var1[var5];
         }

         Struct.EM_Ret.avg = (float)Math.sqrt((double)(var3 / (float)var2));
         Struct.EM_Ret.Cal_sum += Struct.EM_Ret.avg;
         var3 = var4;
         if (Struct.EM_Ret.pack_cnt > 200) {
            Struct.EM_Ret.EM_Value = Struct.EM_Ret.Cal_sum / 190.0F;
            Struct.EM_Ret.pack_cnt = 0;
            var3 = Struct.EM_Ret.EM_Value;
         }
      } else {
         Struct.EM_Ret.Cal_sum = 0.0F;
         var3 = var4;
      }

      return var3;
   }

   public void EM_Process(short[] var1, int var2) {
      this.EM_cal_init();
      float var3 = 0.0F;

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var3 += (float)var1[var4] * (float)var1[var4];
      }

      float[] var5 = Struct.EM_Ret.avg_Data;
      var4 = Struct.EM_Ret.pack_cnt;
      Struct.EM_Ret.pack_cnt = var4 + 1;
      var5[var4] = (float)Math.sqrt((double)(var3 / (float)var2));
      Struct.EM_Ret.pack_cnt %= 10;
      var3 = 0.0F;
      if (Struct.EM_Ret.pack_cnt == 0) {
         for(var2 = 0; var2 < 10; ++var2) {
            var3 += Struct.EM_Ret.avg_Data[var2];
         }

         Struct.EM_Ret.avg = var3 / 10.0F;
         switch(Struct.EM_Ret.Type) {
         case 1:
         case 2:
         case 3:
         case 4:
            Struct.EM_Ret.FinalAvg = this.Find_Cal_Result(Struct.EM_Ret.Type, Struct.EM_Ret.avg);
            break;
         default:
            Struct.EM_Ret.avg = (Struct.EM_Ret.avg - Struct.EM_Ret.EM_Value) * (100.0F / Struct.EM_Ret.EM_Value);
            if (Struct.EM_Ret.avg < 0.0F) {
               Struct.EM_Ret.avg = 0.0F;
            }

            Struct.EM_Ret.avg *= Struct.EM_Ret.G_Correct;
            Struct.EM_Ret.FinalAvg = this.Find_Cal_Result(Struct.EM_Ret.Type, Struct.EM_Ret.avg);
         }
      }

   }

   public void EM_setType(int var1) {
      Struct.EM_Ret.Type = var1;
   }

   public Struct.MDI_Data Find_MIC_Digital_Data(Struct.MDI_Data var1, short[] var2, int var3) {
      Struct.MDI_Para.STD_INTV = Struct.MDI_Data.P_SINTV;
      Struct.MDI_Para.THR = Struct.MDI_Data.P_THR;
      Struct.MDI_Para.WaveType = Struct.MDI_Data.P_WaveType;
      Struct.MDI_Para.POL = Struct.MDI_Data.P_POL;
      int var4;
      int var5;
      int var6;
      if (Struct.MDI_Para.WaveType == 0) {
         for(var4 = 0; var4 < var3; ++var4) {
            var6 = var2[var4] * Struct.MDI_Para.POL;
            var5 = var6;
            if ((float)var6 < Struct.MDI_Para.THR) {
               var5 = 0;
            }

            this.Detection_Process(0, var5);
            if (Struct.MDI_Ret.dataOK_Flag) {
               break;
            }
         }
      } else {
         for(var5 = 1; var5 < var3; ++var5) {
            if (var2[var5] > 0) {
               var6 = Math.abs(var2[var5] - var2[var5 - 1]);
               var4 = var6;
               if ((float)var6 < Struct.MDI_Para.THR) {
                  var4 = 0;
               }
            } else {
               var4 = 0;
            }

            this.Detection_Process(0, var4);
            if (Struct.MDI_Ret.dataOK_Flag) {
               break;
            }
         }
      }

      var1 = new Struct.MDI_Data();
      Struct.MDI_Data.P_IsSave_Update = Struct.MDI_Ret.Save_Flag;
      Struct.MDI_Data.P_SINTV = Struct.MDI_Para.STD_INTV;
      Struct.MDI_Data.P_WaveType = Struct.MDI_Para.WaveType;
      Struct.MDI_Data.P_THR = Struct.MDI_Para.THR;
      Struct.MDI_Data.P_POL = Struct.MDI_Para.POL;
      Struct.MDI_Ret.result = this.Data_Convertion_to_RealValue(Struct.MDI_Ret.d_type, Struct.MDI_Ret.d_data);
      Log.v("Calculator", "Struct.MDI_Ret.d_type : " + Struct.MDI_Ret.d_type);
      Struct.MDI_Data.iTYPE = Struct.MDI_Ret.d_type;
      Struct.MDI_Data.cUnit = this.MDI_Find_Unit(Struct.MDI_Data.iTYPE);
      Struct.MDI_Data.fValue = Struct.MDI_Ret.result;
      Log.v("Calculator", "mRP.fValue : " + Struct.MDI_Data.fValue);
      Struct.MDI_Data.UpDate_OK = Struct.MDI_Ret.dataOK_Flag;
      Struct.MDI_Data.D_OK_Count = Struct.MDI_Ret.OK_Count;
      Struct.MDI_Data.D_Rec_Count = Struct.MDI_Ret.Rec_Count;
      Struct.MDI_Data.D_Error_Count = Struct.MDI_Ret.Error_Count;
      Struct.MDI_Data.D_OK_Rate = Struct.MDI_Ret.OK_Rate;
      Struct.MDI_Data.P_IsParameter_OK = Struct.MDI_Data.P_IsParameter_OK;
      Log.e("Calculator", "Find_MIC_Digital_Data");
      Struct.MDI_Ret.dataOK_Flag = false;
      return var1;
   }

   public int GEI_AutoVmin(short[] var1, int var2) {
      byte var4 = 0;
      ++Struct.GE_Ret.Cnt;
      int var3;
      if (Struct.GE_Ret.Cnt >= 10) {
         for(var3 = 0; var3 < var2; ++var3) {
            Struct.GE_Ret.auto_sum += (float)(var1[var3] * var1[var3]);
         }
      }

      var3 = var4;
      if (Struct.GE_Ret.Cnt == 200) {
         Struct.GE_Ret.auto_avg = (float)Math.sqrt((double)(Struct.GE_Ret.auto_sum / (float)(var2 * 190)));
         Struct.GE_Ret.Vmin = (int)(Struct.GE_Ret.auto_avg * Struct.GE_Ret.valpha);
         if (Struct.GE_Ret.Vmin < 20) {
            Struct.GE_Ret.Vmin = 20;
         }

         Struct.GE_Ret.Cnt = 0;
         Struct.GE_Ret.auto_sum = 0.0F;
         Struct.GE_Ret.Vmin *= 2;
         var3 = Struct.GE_Ret.Vmin;
      }

      return var3;
   }

   public float GEI_CPM_to_Sivert(int var1, float var2) {
      float var3;
      if (var1 == 1) {
         var3 = 0.15696F;
      } else {
         var3 = 0.0889F;
      }

      var3 *= var2;
      if (var1 == 1) {
         var2 = var3;
         if (var3 < 0.1F) {
            var2 = 0.1F;
         }
      } else {
         var2 = var3;
         if (var3 < 0.05F) {
            var2 = 0.05F;
         }
      }

      return var2;
   }

   public float GEI_Calculate_CPM() {
      int var4 = Struct.GE_Ret.OLD_AVG_COUNT;
      float var2 = (float)(var4 / 10) / 60.0F;
      float var1 = 0.0F;

      for(int var3 = 0; var3 < var4; ++var3) {
         var1 += (float)Struct.GE_Ret.CPS_Data[var3];
      }

      if (Struct.GE_Ret.fTime < 1.0F) {
         var1 = 0.0F;
      } else if (Struct.GE_Ret.fTime <= 30.0F) {
         var1 = var1 * var2 * 2.0F;
      } else if (Struct.GE_Ret.fTime <= (float)(var4 - 1) / 10.0F) {
         var1 = var1 * var2 * (60.0F / Struct.GE_Ret.fTime);
      } else {
         Log.i("Calculator", "ret : " + var1);
         var1 += (float)Struct.GE_Ret.CPS_Init_Count;
         var2 = Struct.GE_Ret.fTime / 60.0F;
      }

      return var1 / var2;
   }

   public void GEI_Process(int var1, short[] var2, int var3, int var4) {
      float var7 = (float)Struct.GE_Ret.Vmin;
      float var5 = 0.0F;

      int var10;
      for(var10 = 1; var10 < var3; ++var10) {
         var5 += (float)(var2[var10] * var2[var10]);
      }

      float var9 = (float)Math.sqrt((double)(var5 / (float)var3));
      float var8 = Struct.GE_Ret.alpha;

      int var11;
      for(var10 = 1; var10 < var3 - 1; var10 = var11 + 1) {
         Struct.GE_Ret.xPos = var4 * var3 + var10;
         var11 = var10;
         if ((float)Math.abs(var2[var10]) >= var7 + var8 * var9) {
            int var12 = 0;
            short[] var14 = new short[(int)Struct.GE_Ret.shadow_range];

            for(var11 = 0; (float)var11 < Struct.GE_Ret.shadow_range && var10 + var11 < var3 - 1; ++var12) {
               var14[var12] = var2[var10 + var11];
               ++var11;
            }

            float var6 = 0.0F;
            int var13 = 0;

            for(var11 = 0; var11 < var12; var6 = var5) {
               var5 = var6;
               if (Math.abs((float)var14[var11]) > var6) {
                  var5 = Math.abs((float)var14[var11]);
                  var13 = var11;
               }

               ++var11;
            }

            Struct.GE_Para.xPeak = Struct.GE_Ret.xPos;
            Struct.GE_Para.yPeak = var2[var10];
            Struct.GE_Ret.X[Struct.GE_Ret.rCount] = Struct.GE_Para.xPeak;
            Struct.GE_Ret.Y[Struct.GE_Ret.rCount] = Struct.GE_Para.yPeak;
            ++Struct.GE_Ret.rCount;
            ++Struct.GE_Ret.CPS_Count;
            var11 = (int)((float)var10 + (float)var13 + Struct.GE_Ret.shadow_range);
            if (var11 - (var3 - 1) >= 0) {
               Struct.GE_Ret.pre_shadow = (float)(var11 - (var3 - 1));
            } else {
               Struct.GE_Ret.pre_shadow = 0.0F;
            }
         }
      }

      for(var3 = Struct.GE_Ret.CPS_DataIndex - 1; var3 >= 0; --var3) {
         Struct.GE_Ret.CPS_Data[var3 + 1] = Struct.GE_Ret.CPS_Data[var3];
      }

      Struct.GE_Ret.CPS_Data[0] = Struct.GE_Ret.CPS_Count;
      Struct.GE_Ret.CPS_Count = 0;
      Struct.GE_Ret.fTime = (float)Math.floor((double)Struct.GE_Ret.atTime);
      Struct.GE_Ret.atTime += 0.1F;
      Struct.GE_Ret.atTime = (float)Math.round(Struct.GE_Ret.atTime * 10.0F) / 10.0F;
      Struct.GE_Ret.fCPM = this.GEI_Calculate_CPM();
      Struct.GE_Ret.fuSv = this.GEI_CPM_to_Sivert(var1, Struct.GE_Ret.fCPM);
      ++Struct.GE_Ret.CPS_DataIndex;
      if (Struct.GE_Ret.CPS_DataIndex >= Struct.GE_Ret.AVG_COUNT) {
         Struct.GE_Ret.CPS_DataIndex = 0;

         for(var1 = 0; var1 < Struct.GE_Ret.CPS_Data.length; ++var1) {
            Struct.GE_Ret.CPS_Data[var1] = 0;
         }

         Struct.GE_Ret.CPS_Init_Count = Struct.GE_Ret.rCount;
      }

   }

   public void Light_Type(int var1) {
      Struct.UV_Ret.Type = var1;
      if (Struct.UV_Ret.Type == 2) {
         Log.v("Calculator", "Struct.UV_Ret.Type : " + Struct.UV_Ret.Type);
         this.Find_UV_Lux_Init();
      }

   }

   public DataInfo MDI_Find_Parameter(short[] var1, int var2) {
      this.MDI_Data_Ret = this.Find_Parameter(var1, var2);
      this.mInfo.MDI_THR = Struct.MDI_Data.P_THR;
      this.mInfo.MDI_INTV = Struct.MDI_Data.P_SINTV;
      this.mInfo.MDI_Pol = Struct.MDI_Data.P_POL;
      this.mInfo.MDI_WaveType = Struct.MDI_Data.P_WaveType;
      return this.mInfo;
   }

   public void MDI_Process(short[] var1, int var2) {
      if (var2 >= 176400) {
         this.MDI_Data_Ret = this.Find_Parameter(var1, var2);
      } else {
         this.MDI_Data_Ret = this.Find_MIC_Digital_Data(this.MDI_Data_Ret, var1, var2);
      }

   }

   public void UV_Process(short[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         Struct.UV_Ret.WaveData[var3] = var1[var3];
      }

      this.Average_Process(var2);
      Struct.UV_Ret.Frq = this.Find_Frequency(Struct.UV_Ret.WaveData, var2);
      Struct.UV_Ret.Volt = this.Find_Voltage(Struct.UV_Ret.Frq);
      if (Struct.UV_Ret.Type == 2) {
         Struct.UV_Ret.Lux = this.Find_UV_Lux_Result(Struct.UV_Ret.Frq);
      }

      Struct.UV_Ret.UVvalue = this.Find_UV_Value(Struct.UV_Ret.Volt);
      if (Struct.UV_Ret.UVvalue > Struct.UV_Ret.Max_UVvalue) {
         Struct.UV_Ret.Max_UVvalue = Struct.UV_Ret.UVvalue;
      }

      Struct.UV_Ret.UV_Index = this.Find_UV_Index(Struct.UV_Ret.UVvalue);
      if (Struct.UV_Ret.UV_Index > Struct.UV_Ret.Max_UV_Index) {
         Struct.UV_Ret.Max_UV_Index = Struct.UV_Ret.UV_Index;
      }

   }

   public DataInfo getDataInfo() {
      this.mInfo.MDI_Type = Struct.MDI_Data.iTYPE;
      this.mInfo.MDI_Value = Struct.MDI_Data.fValue;
      this.mInfo.MDI_Unit = Struct.MDI_Data.cUnit;
      this.mInfo.MDI_WaveType = Struct.MDI_Data.P_WaveType;
      this.mInfo.MDI_THR = Struct.MDI_Data.P_THR;
      this.mInfo.MDI_Rec_Count = Struct.MDI_Data.D_Rec_Count;
      this.mInfo.MDI_OK_Count = Struct.MDI_Data.D_OK_Count;
      this.mInfo.MDI_Error_Count = Struct.MDI_Data.D_Error_Count;
      this.mInfo.MDI_Pol = Struct.MDI_Data.P_POL;
      this.mInfo.MDI_INTV = Struct.MDI_Data.P_SINTV;
      this.mInfo.MDI_STATUS = Struct.MDI_Data.D_STATUS;
      this.mInfo.IsParameter_OK = Struct.MDI_Data.P_IsParameter_OK;
      this.mInfo.IsUpdate_OK = Struct.MDI_Data.UpDate_OK;
      this.mInfo.IsSave_Update = Struct.MDI_Data.P_IsSave_Update;
      this.mInfo.GE_Auto_Value = Struct.GE_Ret.Vmin;
      this.mInfo.GE_CPM = Struct.GE_Ret.fCPM;
      this.mInfo.GE_Count = Struct.GE_Ret.rCount;
      this.mInfo.GE_uSv = Struct.GE_Ret.fuSv;
      this.mInfo.EM_Auto_Value = (float)((int)Struct.EM_Ret.EM_Value);
      this.mInfo.EM_Value = Struct.EM_Ret.FinalAvg;
      this.mInfo.UV_Value = Struct.UV_Ret.UVvalue;
      this.mInfo.UV_Index = Struct.UV_Ret.UV_Index;
      this.mInfo.UV_Frq = Struct.UV_Ret.Frq;
      this.mInfo.UV_Volt = Struct.UV_Ret.Volt;
      this.mInfo.UV_Lux = Struct.UV_Ret.Lux;
      return this.mInfo;
   }

   public SmartSensorResultEM getResultEM() {
      this.mResultEM.EM_Value = Struct.EM_Ret.FinalAvg;
      return this.mResultEM;
   }

   public SmartSensorResultGE getResultGE() {
      this.mResultGE.GE_CPM = Struct.GE_Ret.fCPM;
      this.mResultGE.GE_uSv = Struct.GE_Ret.fuSv;
      this.mResultGE.GE_CNT = Struct.GE_Ret.rCount;
      return this.mResultGE;
   }

   public SmartSensorResultMDI getResultMDI() {
      this.mResultMDI.MDI_Type = Struct.MDI_Data.iTYPE;
      this.mResultMDI.MDI_Value = Struct.MDI_Data.fValue;
      this.mResultMDI.MDI_Unit = Struct.MDI_Data.cUnit;
      Log.e("Calculator", "mResultMDI.MDI_Type : " + this.mResultMDI.MDI_Type);
      return this.mResultMDI;
   }

   public SmartSensorResultUV getResultUV() {
      this.mResultUV.UV_Value = Struct.UV_Ret.UVvalue;
      this.mResultUV.UV_Index = Struct.UV_Ret.UV_Index;
      return this.mResultUV;
   }

   public void setLocalData(DataInfo var1) {
      Struct.MDI_Data.P_THR = var1.MDI_THR;
      Struct.MDI_Data.P_SINTV = var1.MDI_INTV;
      Struct.MDI_Data.P_POL = var1.MDI_Pol;
      Struct.MDI_Data.P_WaveType = var1.MDI_WaveType;
      Struct.GE_Ret.Vmin = var1.GE_Auto_Value;
      Struct.EM_Ret.EM_Value = var1.EM_Auto_Value;
      Log.v("Calculator", "setLocalData.... mInfo.GE_Auto_Value : " + var1.GE_Auto_Value + " Struct.GE_Ret.Vmin : " + Struct.GE_Ret.Vmin);
   }
}
