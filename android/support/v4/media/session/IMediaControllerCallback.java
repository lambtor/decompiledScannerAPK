package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;
import java.util.List;

public interface IMediaControllerCallback extends IInterface {
   void onEvent(String var1, Bundle var2) throws RemoteException;

   void onExtrasChanged(Bundle var1) throws RemoteException;

   void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException;

   void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException;

   void onQueueChanged(List var1) throws RemoteException;

   void onQueueTitleChanged(CharSequence var1) throws RemoteException;

   void onSessionDestroyed() throws RemoteException;

   void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaControllerCallback {
      private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaControllerCallback";
      static final int TRANSACTION_onEvent = 1;
      static final int TRANSACTION_onExtrasChanged = 7;
      static final int TRANSACTION_onMetadataChanged = 4;
      static final int TRANSACTION_onPlaybackStateChanged = 3;
      static final int TRANSACTION_onQueueChanged = 5;
      static final int TRANSACTION_onQueueTitleChanged = 6;
      static final int TRANSACTION_onSessionDestroyed = 2;
      static final int TRANSACTION_onVolumeInfoChanged = 8;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaControllerCallback");
      }

      public static IMediaControllerCallback asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (var1 != null && var1 instanceof IMediaControllerCallback) {
               var2 = (IMediaControllerCallback)var1;
            } else {
               var2 = new IMediaControllerCallback.Proxy(var0);
            }
         }

         return (IMediaControllerCallback)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         Bundle var8;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            String var7 = var2.readString();
            if (var2.readInt() != 0) {
               var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var8 = null;
            }

            this.onEvent(var7, var8);
            break;
         case 2:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            this.onSessionDestroyed();
            break;
         case 3:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            PlaybackStateCompat var11;
            if (var2.readInt() != 0) {
               var11 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var2);
            } else {
               var11 = null;
            }

            this.onPlaybackStateChanged(var11);
            break;
         case 4:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            MediaMetadataCompat var10;
            if (var2.readInt() != 0) {
               var10 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
            } else {
               var10 = null;
            }

            this.onMetadataChanged(var10);
            break;
         case 5:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            this.onQueueChanged(var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
            break;
         case 6:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            CharSequence var9;
            if (var2.readInt() != 0) {
               var9 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var2);
            } else {
               var9 = null;
            }

            this.onQueueTitleChanged(var9);
            break;
         case 7:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (var2.readInt() != 0) {
               var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var8 = null;
            }

            this.onExtrasChanged(var8);
            break;
         case 8:
            var2.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            ParcelableVolumeInfo var6;
            if (var2.readInt() != 0) {
               var6 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var2);
            } else {
               var6 = null;
            }

            this.onVolumeInfoChanged(var6);
            break;
         case 1598968902:
            var3.writeString("android.support.v4.media.session.IMediaControllerCallback");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }
   }

   private static class Proxy implements IMediaControllerCallback {
      private IBinder mRemote;

      Proxy(IBinder var1) {
         this.mRemote = var1;
      }

      public IBinder asBinder() {
         return this.mRemote;
      }

      public String getInterfaceDescriptor() {
         return "android.support.v4.media.session.IMediaControllerCallback";
      }

      public void onEvent(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void onExtrasChanged(Bundle param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void onMetadataChanged(MediaMetadataCompat param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void onPlaybackStateChanged(PlaybackStateCompat param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void onQueueChanged(List var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
            var2.writeTypedList(var1);
            this.mRemote.transact(5, var2, (Parcel)null, 1);
         } finally {
            var2.recycle();
         }

      }

      public void onQueueTitleChanged(CharSequence param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void onSessionDestroyed() throws RemoteException {
         Parcel var1 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
            this.mRemote.transact(2, var1, (Parcel)null, 1);
         } finally {
            var1.recycle();
         }

      }

      public void onVolumeInfoChanged(ParcelableVolumeInfo param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }
   }
}
