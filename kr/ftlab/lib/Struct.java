package kr.ftlab.lib;

public class Struct {
   public static int ERROR_WAIT_COUNT = 0;
   public static int NextWaitCount = 0;
   public static int STATUS_STEP = 0;
   public static final int sPEAK_FIND = 1;
   public static final int sWAIT = 0;
   public static final int sWAIT_TERM = 2;

   public static class EM_Fact {
      public static int[] No = new int[8];
      public static float[][] X = new float[8][100];
      public static float[][] Y = new float[8][100];
   }

   public static class EM_Ret {
      public static float Cal_sum = 0.0F;
      public static float EM_Value = 10.0F;
      public static float FinalAvg = 0.0F;
      public static float G_Correct = 1.0F;
      public static int No;
      public static int Type;
      public static int[] X = new int[10000];
      public static short[] Y = new short[10000];
      public static float avg = 0.0F;
      public static float[] avg_Data = new float[10];
      public static int pack_cnt = 0;
   }

   public static class ES_Fact {
      public static float[] X = new float[21];
      public static float[] Y = new float[21];
   }

   public static class GE_Para {
      public static float dev1 = 0.0F;
      public static float dev2 = 0.0F;
      public static int status = 0;
      public static int xBase = 0;
      public static int xPeak = 0;
      public static short yPeak = 0;
   }

   public static class GE_Ret {
      public static int AVG_COUNT = 1200;
      public static int CPS_Count = 0;
      public static int[] CPS_Data = new int[1200];
      public static int CPS_DataIndex = 0;
      public static int CPS_Init_Count = 0;
      public static int Cnt = 0;
      public static int No = 0;
      public static int OLD_AVG_COUNT = 1200;
      public static int OLD_CPS_DataIndex = 0;
      public static int Vmin = 200;
      public static int[] X = new int[100000];
      public static short[] Y = new short[100000];
      public static float alpha = 6.0F;
      public static float atTime = 0.0F;
      public static float auto_avg = 0.0F;
      public static float auto_sum = 0.0F;
      public static float fCPM;
      public static float fTime = 0.0F;
      public static float fuSv;
      public static float pre_shadow = 0.0F;
      public static int rCount = 0;
      public static float shadow_range = 125.0F;
      public static float valpha = 3.0F;
      public static int xPos = 0;
   }

   public static class MDI_BasicPara {
      public static int MaxAmp = 0;
      public static int MaxDAmp = 0;
      public static int POL = 1;
   }

   public static class MDI_Data {
      public static int D_Error_Count = 0;
      public static int D_OK_Count = 0;
      public static float D_OK_Rate = 0.0F;
      public static int D_Rec_Count = 0;
      public static int D_STATUS = 0;
      public static boolean P_IsParameter_OK = false;
      public static boolean P_IsSave_Update = false;
      public static int P_POL = 1;
      public static float P_SINTV = 113.0F;
      public static float P_THR = 5000.0F;
      public static int P_WaveType = 1;
      public static boolean UpDate_OK;
      public static String cUnit;
      public static float fValue;
      public static int iTYPE;
   }

   public static class MDI_Det {
      public static int Det_AMax;
      public static int Det_APos;
      public static int[] DfA = new int[34];
      public static int[] Findex = new int[34];
      public static float[] Findex_Value = new float[34];
      public static int[] Pos = new int[34];
      public static float S_Inv;
      public static int buffer_count = 0;
      public static int buffer_count2 = 0;
      public static int count = 0;
      public static int[] fData = new int[34];
      public static int sub_Count = 0;
   }

   public static class MDI_Para {
      public static int AckBit = 2;
      public static int ChecksumBit = 8;
      public static float DFA_THR_RATIO = 0.5F;
      public static int DIFF_PULSE_WIDTH = 10;
      public static int DataBit = 16;
      public static int DataByte = 2;
      public static int POL = 1;
      public static float STD_INTV = 112.0F;
      public static float THR = 5000.0F;
      public static float THR_INIT = 50.0F;
      public static float THR_MAX = 10000.0F;
      public static float THR_Min = 200.0F;
      public static int TotalBit = 34;
      public static int TypeBit = 8;
      public static int WaveType = 0;
   }

   public static class MDI_Ret {
      public static float DFA_AVG;
      public static float DFA_SUM;
      public static float DFA_Value;
      public static int Error_Count = 0;
      public static int Error_INSTANT = 0;
      public static int OK_Count = 0;
      public static float OK_Rate = 100.0F;
      public static int Rec_Count = 0;
      public static int STATUS = 0;
      public static boolean Save_Done_Flag = false;
      public static int Save_Error_Count = 0;
      public static boolean Save_Flag = false;
      public static int Save_OK_Count = 0;
      public static int UpDateCount;
      public static int d_checksum;
      public static int[] d_data = new int[2];
      public static int d_type;
      public static int d_type1;
      public static int d_type2;
      public static boolean dataERROR_Copy = false;
      public static boolean dataOK_Flag = false;
      public static float fSTD_INV;
      public static float fSTD_INV_AVG;
      public static float fSTD_INV_SUM;
      public static float result;
   }

   public static class UV_Lux_Fact {
      public static int No;
      public static float[] X = new float[100];
      public static float[] Y = new float[100];
   }

   public static class UV_Ret {
      public static int Avg_Step = 10;
      public static float Frq = 0.0F;
      public static int Inc = 0;
      public static float Lux = 0.0F;
      public static float MaxValue_Find_Level = 0.5F;
      public static float Max_UV_Index = 0.0F;
      public static float Max_UVvalue = 0.0F;
      public static boolean Pol_Found = false;
      public static short Polarity = 1;
      public static short[] TotalData = new short['걄'];
      public static int Type = 0;
      public static float UV_Index = 0.0F;
      public static float UVvalue = 0.0F;
      public static float Volt = 0.0F;
      public static short[] WaveData = new short[441000];
      public static float cfact_UV_slop = 5000.0F;
      public static float cfact_UV_y = -1250.0F;
      public static float cfact_slop = 0.01586F;
      public static float cfact_y = -0.0194F;
   }

   public static class V_Fact {
      public static float[] X = new float[21];
      public static float[] Y = new float[21];
   }
}
