package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

class MediaControllerCompatApi21 {
   public static void adjustVolume(Object var0, int var1, int var2) {
      ((MediaController)var0).adjustVolume(var1, var2);
   }

   public static Object createCallback(MediaControllerCompatApi21.Callback var0) {
      return new MediaControllerCompatApi21.CallbackProxy(var0);
   }

   public static boolean dispatchMediaButtonEvent(Object var0, KeyEvent var1) {
      return ((MediaController)var0).dispatchMediaButtonEvent(var1);
   }

   public static Object fromToken(Context var0, Object var1) {
      return new MediaController(var0, (Token)var1);
   }

   public static Bundle getExtras(Object var0) {
      return ((MediaController)var0).getExtras();
   }

   public static long getFlags(Object var0) {
      return ((MediaController)var0).getFlags();
   }

   public static Object getMetadata(Object var0) {
      return ((MediaController)var0).getMetadata();
   }

   public static String getPackageName(Object var0) {
      return ((MediaController)var0).getPackageName();
   }

   public static Object getPlaybackInfo(Object var0) {
      return ((MediaController)var0).getPlaybackInfo();
   }

   public static Object getPlaybackState(Object var0) {
      return ((MediaController)var0).getPlaybackState();
   }

   public static List getQueue(Object var0) {
      List var1 = ((MediaController)var0).getQueue();
      ArrayList var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = new ArrayList(var1);
      }

      return var2;
   }

   public static CharSequence getQueueTitle(Object var0) {
      return ((MediaController)var0).getQueueTitle();
   }

   public static int getRatingType(Object var0) {
      return ((MediaController)var0).getRatingType();
   }

   public static PendingIntent getSessionActivity(Object var0) {
      return ((MediaController)var0).getSessionActivity();
   }

   public static Object getTransportControls(Object var0) {
      return ((MediaController)var0).getTransportControls();
   }

   public static void registerCallback(Object var0, Object var1, Handler var2) {
      ((MediaController)var0).registerCallback((android.media.session.MediaController.Callback)var1, var2);
   }

   public static void sendCommand(Object var0, String var1, Bundle var2, ResultReceiver var3) {
      ((MediaController)var0).sendCommand(var1, var2, var3);
   }

   public static void setVolumeTo(Object var0, int var1, int var2) {
      ((MediaController)var0).setVolumeTo(var1, var2);
   }

   public static void unregisterCallback(Object var0, Object var1) {
      ((MediaController)var0).unregisterCallback((android.media.session.MediaController.Callback)var1);
   }

   public interface Callback {
      void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5);

      void onExtrasChanged(Bundle var1);

      void onMetadataChanged(Object var1);

      void onPlaybackStateChanged(Object var1);

      void onQueueChanged(List var1);

      void onQueueTitleChanged(CharSequence var1);

      void onSessionDestroyed();

      void onSessionEvent(String var1, Bundle var2);
   }

   static class CallbackProxy extends android.media.session.MediaController.Callback {
      protected final MediaControllerCompatApi21.Callback mCallback;

      public CallbackProxy(MediaControllerCompatApi21.Callback var1) {
         this.mCallback = var1;
      }

      public void onAudioInfoChanged(android.media.session.MediaController.PlaybackInfo var1) {
         this.mCallback.onAudioInfoChanged(var1.getPlaybackType(), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(var1), var1.getVolumeControl(), var1.getMaxVolume(), var1.getCurrentVolume());
      }

      public void onExtrasChanged(Bundle var1) {
         this.mCallback.onExtrasChanged(var1);
      }

      public void onMetadataChanged(MediaMetadata var1) {
         this.mCallback.onMetadataChanged(var1);
      }

      public void onPlaybackStateChanged(PlaybackState var1) {
         this.mCallback.onPlaybackStateChanged(var1);
      }

      public void onQueueChanged(List var1) {
         this.mCallback.onQueueChanged(var1);
      }

      public void onQueueTitleChanged(CharSequence var1) {
         this.mCallback.onQueueTitleChanged(var1);
      }

      public void onSessionDestroyed() {
         this.mCallback.onSessionDestroyed();
      }

      public void onSessionEvent(String var1, Bundle var2) {
         this.mCallback.onSessionEvent(var1, var2);
      }
   }

   public static class PlaybackInfo {
      private static final int FLAG_SCO = 4;
      private static final int STREAM_BLUETOOTH_SCO = 6;
      private static final int STREAM_SYSTEM_ENFORCED = 7;

      public static AudioAttributes getAudioAttributes(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getAudioAttributes();
      }

      public static int getCurrentVolume(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getCurrentVolume();
      }

      public static int getLegacyAudioStream(Object var0) {
         return toLegacyStreamType(getAudioAttributes(var0));
      }

      public static int getMaxVolume(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getMaxVolume();
      }

      public static int getPlaybackType(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getPlaybackType();
      }

      public static int getVolumeControl(Object var0) {
         return ((android.media.session.MediaController.PlaybackInfo)var0).getVolumeControl();
      }

      private static int toLegacyStreamType(AudioAttributes var0) {
         byte var2 = 3;
         byte var1;
         if ((var0.getFlags() & 1) == 1) {
            var1 = 7;
         } else if ((var0.getFlags() & 4) == 4) {
            var1 = 6;
         } else {
            var1 = var2;
            switch(var0.getUsage()) {
            case 1:
            case 11:
            case 12:
            case 14:
               break;
            case 2:
               var1 = 0;
               break;
            case 3:
               var1 = 8;
               break;
            case 4:
               var1 = 4;
               break;
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
               var1 = 5;
               break;
            case 6:
               var1 = 2;
               break;
            case 13:
               var1 = 1;
               break;
            default:
               var1 = var2;
            }
         }

         return var1;
      }
   }

   public static class TransportControls {
      public static void fastForward(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).fastForward();
      }

      public static void pause(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).pause();
      }

      public static void play(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).play();
      }

      public static void playFromMediaId(Object var0, String var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).playFromMediaId(var1, var2);
      }

      public static void playFromSearch(Object var0, String var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).playFromSearch(var1, var2);
      }

      public static void rewind(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).rewind();
      }

      public static void seekTo(Object var0, long var1) {
         ((android.media.session.MediaController.TransportControls)var0).seekTo(var1);
      }

      public static void sendCustomAction(Object var0, String var1, Bundle var2) {
         ((android.media.session.MediaController.TransportControls)var0).sendCustomAction(var1, var2);
      }

      public static void setRating(Object var0, Object var1) {
         ((android.media.session.MediaController.TransportControls)var0).setRating((Rating)var1);
      }

      public static void skipToNext(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).skipToNext();
      }

      public static void skipToPrevious(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).skipToPrevious();
      }

      public static void skipToQueueItem(Object var0, long var1) {
         ((android.media.session.MediaController.TransportControls)var0).skipToQueueItem(var1);
      }

      public static void stop(Object var0) {
         ((android.media.session.MediaController.TransportControls)var0).stop();
      }
   }
}
