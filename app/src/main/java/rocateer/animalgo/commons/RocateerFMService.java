package rocateer.animalgo.commons;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.intro.IntroActivity;
import rocateer.animalgo.activity.intro.IntroAlarmActivity;
import rocateer.animalgo.activity.walk.WalkChatActivity;
import rocateer.animalgo.models.AlarmModel;
import timber.log.Timber;

public class RocateerFMService extends FirebaseMessagingService {
  public static int current_ID = 0;

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);

    if (remoteMessage == null) {
      return;
    }

    Timber.i("Remote message test");

    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
    wakeLock.acquire(3000);

    if (remoteMessage.getData().size() > 0) {
      Timber.i(remoteMessage.getData().toString());
      Bundle bundle = new Bundle();
      bundle.putString("data", remoteMessage.getData().toString());

      // Index 별 이동 처리
      try {
        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        AlarmModel alarmModel = new AlarmModel();
        if (jsonObject.has("title")) {
          alarmModel.setTitle(jsonObject.getString("title"));
        }
        if (jsonObject.has("msg")) {
          alarmModel.setMsg(jsonObject.getString("msg"));
        }

        if (jsonObject.has("index")) {
          alarmModel.setIndex(jsonObject.getString("index"));
        }

        if (jsonObject.has("chatting_room_idx")) {
          alarmModel.setChatting_room_idx(jsonObject.getString("chatting_room_idx"));
        }


        if (alarmModel.getIndex() != null) {
          if (alarmModel.getIndex().equals("900")) {
            Intent chatRefresh = new Intent(Constants.CHAT_REFRESH);
            sendBroadcast(chatRefresh);
          } else if (alarmModel.getIndex().equals("101")) {
            Intent acceptRefresh = new Intent(Constants.ACCEPT_REFRESH);
            sendBroadcast(acceptRefresh);
          } else {
            Intent alarmRefresh = new Intent(Constants.AlARM_REFRESH);
            sendBroadcast(alarmRefresh);
          }
        }

        // Index 별 이동 처리
        if (foregrounded()) {
          sendNotification(alarmModel);
        } else {

          JSONObject json =  new JSONObject(remoteMessage.getData());
          sendBackgroundNotification(alarmModel, json.toString());
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

    } else {
      AlarmModel alarmModel = new AlarmModel();
      alarmModel.setTitle(remoteMessage.getNotification().getTitle());
      alarmModel.setMsg(remoteMessage.getNotification().getBody());
      alarmModel.setIndex("100000");
      sendNotification(alarmModel);
    }
  }

  /**
   * Notification 발송.
   *
   * @param alarmModel
   */
  private void sendNotification(AlarmModel alarmModel) {
    current_ID = (int) System.currentTimeMillis();

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "notify_001");

    Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable._12x512);

    mBuilder.setSmallIcon(getNotificationIcon());
    mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
    mBuilder.setLargeIcon(rawBitmap);
    mBuilder.setContentTitle(alarmModel.getTitle());
    mBuilder.setContentText(alarmModel.getMsg());
    mBuilder.setPriority(Notification.PRIORITY_HIGH);
    mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
    mBuilder.setAutoCancel(true);
    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
    bigText.setBigContentTitle(alarmModel.getTitle());
    bigText.bigText(alarmModel.getMsg());
    mBuilder.setStyle(bigText);

    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    mBuilder.setSound(uri);
    long[] v = {500, 1000};
    mBuilder.setVibrate(v);

    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      @SuppressLint("WrongConstant")
      NotificationChannel channel = new NotificationChannel("notify_001", "알림", NotificationManager.IMPORTANCE_DEFAULT);
      mNotificationManager.createNotificationChannel(channel);
    } else {

    }
    // 펜딩 Intent 세팅
    PendingIntent pendingIntent;
    if (alarmModel.getIndex() != null) {
      switch (alarmModel.getIndex()) {
        case "101": // 산책 수락
          Intent walkchatViewController = WalkChatActivity.getStartIntent(this, alarmModel.getChatting_room_idx());
          pendingIntent = PendingIntent.getActivity(this, 0, walkchatViewController, PendingIntent.FLAG_IMMUTABLE);
          mBuilder.setContentIntent(pendingIntent);
          break;
        case "900": // 채팅
          Intent walkChatViewController = WalkChatActivity.getStartIntent(this, alarmModel.getChatting_room_idx());
          pendingIntent = PendingIntent.getActivity(this, 0, walkChatViewController, PendingIntent.FLAG_IMMUTABLE);
          mBuilder.setContentIntent(pendingIntent);
          break;
      }
    }
    mNotificationManager.notify(current_ID, mBuilder.build());

  }

  /**
   * 백그라운드 Notification
   *
   * @param alarmModel
   */
  private void sendBackgroundNotification(AlarmModel alarmModel, String data) {
    current_ID = (int) System.currentTimeMillis();
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "notify_001");

    Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable._12x512);
//
    mBuilder.setSmallIcon(getNotificationIcon());
    mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
    mBuilder.setLargeIcon(rawBitmap);
    mBuilder.setContentTitle(alarmModel.getTitle());
    mBuilder.setContentText(alarmModel.getMsg());
    mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
    mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
    mBuilder.setAutoCancel(true);
    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
    bigText.setBigContentTitle(alarmModel.getTitle());
    bigText.bigText(alarmModel.getMsg());
    mBuilder.setStyle(bigText);

    Timber.i("백그라운드 알림 =====");
    Timber.i("DATA = " + data);

    Intent introAlarmActivity = IntroAlarmActivity.getStartIntent(this, data);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, introAlarmActivity, PendingIntent.FLAG_IMMUTABLE);
    mBuilder.setContentIntent(pendingIntent);

    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    mBuilder.setSound(uri);
    long[] v = {500, 1000};
    mBuilder.setVibrate(v);

    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      @SuppressLint("WrongConstant")
      NotificationChannel channel = new NotificationChannel("notify_001", "알림", NotificationManager.IMPORTANCE_DEFAULT);
      mNotificationManager.createNotificationChannel(channel);
    } else {

    }

    mNotificationManager.notify(current_ID, mBuilder.build());

  }

  /**
   * 알림 아이콘
   *
   * @return
   */
  private int getNotificationIcon() {
    boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
    return useWhiteIcon ? R.drawable._12x512 : R.drawable._12x512;
  }

  /**
   * 백그라운드 인지 체크
   *
   * @return
   */
  public boolean foregrounded() {
    ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
    ActivityManager.getMyMemoryState(appProcessInfo);
    return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
  }

}