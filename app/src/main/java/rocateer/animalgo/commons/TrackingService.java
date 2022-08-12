package rocateer.animalgo.commons;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rocateer.animalgo.R;
import rocateer.animalgo.RocateerApplication;
import rocateer.animalgo.activity.EmptyActivity;
import timber.log.Timber;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

  private final String TAG = "BackgroundLocationUpdateService";
  private final String TAG_LOCATION = "TAG_LOCATION";
  private Context context;
  private boolean stopService = false;

  private KalmanLatLong mKalmanFilter;
  private ArrayList<Location> mLocationList = new ArrayList<>();
  private ArrayList<Location> mOldLocationList;
  private ArrayList<Location> mNoAccuracyLocationList;
  private ArrayList<Location> mInaccurateLocationList;
  private ArrayList<Location> mKalmanNGLocationList;
  private float mCurrentSpeed = 0.0f; // meters/second
  private long mRunStartTimeInMillis; // 시작 시간

  private String mTrackingText;


  /* For Google Fused API */
  protected GoogleApiClient mGoogleApiClient;
  protected LocationSettingsRequest mLocationSettingsRequest;
  private String latitude = "0.0";
  private String longitude = "0.0";
  private FusedLocationProviderClient mFusedLocationClient;
  private SettingsClient mSettingsClient;
  private String mRecordType = Prefs.getString(Constants.RECORD_TYPE, "");
  private LocationCallback mLocationCallback;
  private Random randomSetting = new Random();
  private LocationRequest mLocationRequest;
  private Location mCurrentLocation;
  private String mWalkWithPetAlarmText;

  // 타이머

  Timer mTimer = new Timer(false);

  private Timer mNotificationTimer;
  private TimerTask mNotificationTimerTask;


  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
    initFilter();


  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.i("=============================================");
    Timber.i("ON START COMMAND");

    StartForeground();


    final Handler tickHandler = new Handler();
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {

        tickHandler.post(new Runnable() {
          @Override
          public void run() {
            if (RocateerApplication.rcState == RocateerApplication.RC_STATE.RECORDING) {
              RocateerApplication.recordSeconds += 1;
            }
          }
        });
      }
    };
    mTimer.schedule(timerTask, 0, 1000);

    // 알림 타이머 시작
    setNotificationTimer();

    buildGoogleApiClient();

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    Timber.i("Service Destory");
    mTimer.cancel();
    mNotificationTimer.cancel();
    stopService = true;
    if (mFusedLocationClient != null) {
      mFusedLocationClient.removeLocationUpdates(mLocationCallback);
      Timber.i("Location Update Callback Removed");
    }

    super.onDestroy();
  }


  @Override
  public boolean stopService(Intent name) {
    Timber.i("Service Stopped");
    mTimer.cancel();
    stopService = true;
    if (mFusedLocationClient != null) {
      mFusedLocationClient.removeLocationUpdates(mLocationCallback);
      mGoogleApiClient.disconnect();
      Timber.i("Location Update Callback Removed");
    }

    return super.stopService(name);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  /**
   * 알림 생성 -> 백그라운드에 유지 시키기 위해
   */
  private void StartForeground() {
    Intent intent = new Intent(context, EmptyActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_MUTABLE);

    String CHANNEL_ID = "애니멀고";
    String CHANNEL_NAME = "애니멀고 산책기록";

    NotificationCompat.Builder builder = null;
    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
      channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
      notificationManager.createNotificationChannel(channel);
      builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
      builder.setChannelId(CHANNEL_ID);
      builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
    } else {
      builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
    }

    builder.setContentTitle("애니멀고");
    builder.setContentText("산책 기록중입니다.");
    Uri notificationSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
    builder.setSound(notificationSound);
    builder.setAutoCancel(true);
    builder.setSmallIcon(R.drawable._12x512);
    builder.setContentIntent(pendingIntent);
    Notification notification = builder.build();
    startForeground(101, notification);
  }

  @Override
  public void onLocationChanged(Location location) {
    Timber.i("Location Changed Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());

    latitude = String.valueOf(location.getLatitude());
    longitude = String.valueOf(location.getLongitude());

    if (latitude.equalsIgnoreCase("0.0") && longitude.equalsIgnoreCase("0.0")) {
      requestLocationUpdate();
    } else {
      Timber.i("Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());
      Location filteredLocation = filteredLocation(location);

      if (filteredLocation != null) {
        RocateerApplication.recordingList.add(filteredLocation);

      }

    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(5 * 1000);
    mLocationRequest.setFastestInterval(3 * 1000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(mLocationRequest);
    builder.setAlwaysShow(true);
    mLocationSettingsRequest = builder.build();

    mSettingsClient
        .checkLocationSettings(mLocationSettingsRequest)
        .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
          @Override
          public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            Timber.i("GPS Success");
            requestLocationUpdate();
          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
          case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            try {
              int REQUEST_CHECK_SETTINGS = 214;
              ResolvableApiException rae = (ResolvableApiException) e;
              rae.startResolutionForResult((AppCompatActivity) context, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException sie) {
              Timber.i("Unable to execute request.");
            }
            break;
          case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            Timber.i("Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
        }
      }
    }).addOnCanceledListener(new OnCanceledListener() {
      @Override
      public void onCanceled() {
        Timber.i("checkLocationSettings -> onCanceled");
      }
    });
  }

  @Override
  public void onConnectionSuspended(int i) {
    connectGoogleClient();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    buildGoogleApiClient();
  }

  protected synchronized void buildGoogleApiClient() {
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    mSettingsClient = LocationServices.getSettingsClient(context);

    mGoogleApiClient = new GoogleApiClient.Builder(context)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

    connectGoogleClient();

    mLocationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        Log.e(TAG_LOCATION, "Location Received");
        mCurrentLocation = locationResult.getLastLocation();
        onLocationChanged(mCurrentLocation);
      }
    };
  }

  private void connectGoogleClient() {
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int resultCode = googleAPI.isGooglePlayServicesAvailable(context);
    if (resultCode == ConnectionResult.SUCCESS) {
      mGoogleApiClient.connect();
    }
  }

  @SuppressLint("MissingPermission")
  private void requestLocationUpdate() {
    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
  }

  /**
   * 필터 초기화
   */
  private void initFilter() {
    mRunStartTimeInMillis = (long) (SystemClock.elapsedRealtimeNanos() / 1000000);
    mLocationList = new ArrayList<>();
    mNoAccuracyLocationList = new ArrayList<>();
    mOldLocationList = new ArrayList<>();
    mInaccurateLocationList = new ArrayList<>();
    mKalmanNGLocationList = new ArrayList<>();
    mKalmanFilter = new KalmanLatLong(3);
  }

  /**
   * 필터 로케이션
   */
  private Location filteredLocation(Location location) {


    // 칼만 필터 적용.
    long age = getLocationAge(location);

    if (age > 5 * 1000) { //more than 5 seconds
      Timber.e("Location is old");
      mOldLocationList.add(location);
//      ngLinePathDraw1();
      return null;
    }

    if (location.getAccuracy() <= 0) {
      Timber.e("Latitidue and longitude values are invalid.");
      mNoAccuracyLocationList.add(location);
      return null;
    }

    //setAccuracy(newLocation.getAccuracy());
    float horizontalAccuracy = location.getAccuracy();
    if (horizontalAccuracy > 20) { //10 meter filter
      Timber.e("Accuracy is too low.");
      mInaccurateLocationList.add(location);
      return null;
    }

    /* Kalman Filter */
    float Qvalue;

    long locationTimeInMillis = (long) (location.getElapsedRealtimeNanos() / 1000000);
    long elapsedTimeInMillis = locationTimeInMillis - mRunStartTimeInMillis;

    if (mCurrentSpeed == 0.0f) {
      Qvalue = 3.0f; //3 meters per second
    } else {
      Qvalue = mCurrentSpeed; // meters per second
    }

    mKalmanFilter.Process(location.getLatitude(), location.getLongitude(), location.getAccuracy(), elapsedTimeInMillis, Qvalue);
    double predictedLat = mKalmanFilter.get_lat();
    double predictedLng = mKalmanFilter.get_lng();

    Location predictedLocation = new Location(""); // provider name is unecessary
    predictedLocation.setLatitude(predictedLat); // your coords of course
    predictedLocation.setLongitude(predictedLng);
    float predictedDeltaInMeters = predictedLocation.distanceTo(location);

    if (predictedDeltaInMeters > 60) {
      Timber.i("Kalman Filter detects mal GPS, we should probably remove this from track");
      mKalmanFilter.consecutiveRejectCount += 1;

      if (mKalmanFilter.consecutiveRejectCount > 3) {
        mKalmanFilter = new KalmanLatLong(3); //reset Kalman Filter if it rejects more than 3 times in raw.
      }
      mKalmanNGLocationList.add(location);
      return null;
    } else {
      mKalmanFilter.consecutiveRejectCount = 0;
    }

//    mCurrentSpeed = (float) speed;
    mLocationList.add(location);

    return location;


  }

  private String secTotime(int sec) {
    int seconds = sec % 60;
    int minutes = sec / 60;
    if (minutes >= 60) {
      int hours = minutes / 60;
      minutes %= 60;
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    return String.format("00:%02d:%02d", minutes, seconds);
  }

  /**
   * 시간 계산
   *
   * @return
   */
  private void setNotificationTimer() {
    mNotificationTimer = new Timer();
    mNotificationTimerTask = new TimerTask() {
      @Override
      public void run() {
        timeNotification();
      }
    };

// timer 실행
    mNotificationTimer.schedule(mNotificationTimerTask, 1000 * 60 * 10  , 1000 * 60 * 10);

  }


  @SuppressLint("NewApi")
  private long getLocationAge(Location newLocation) {
    long locationAge;
    if (Build.VERSION.SDK_INT >= 17) {
      long currentTimeInMilli = (long) (SystemClock.elapsedRealtimeNanos() / 1000000);
      long locationTimeInMilli = (long) (newLocation.getElapsedRealtimeNanos() / 1000000);
      locationAge = currentTimeInMilli - locationTimeInMilli;
    } else {
      locationAge = System.currentTimeMillis() - newLocation.getTime();
    }
    return locationAge;
  }

  /**
   * 가이드 알림
   */
  private void timeNotification() {
    if (Prefs.getString(Constants.GUIDE_ALARM_YN, "").equals("Y")) {
      if (RocateerApplication.rcState != RocateerApplication.RC_STATE.RECORDING) {
        return;
      }

      if (mRecordType.equals("0")) {
        String[] pushText = getResources().getStringArray(R.array.tracking_push_text);
        Random rand = new Random();
        int n = rand.nextInt(pushText.length - 1);
        mTrackingText = pushText[n];
      } else if (mRecordType.equals("1")) {
        String[] withFriendText = getResources().getStringArray(R.array.friend_tracking_push_text);
        Random random = new Random();
        int n = random.nextInt(withFriendText.length - 1);
        mTrackingText = withFriendText[n];
      }

      NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      Intent emptyActivity = EmptyActivity.getStartIntent(getApplicationContext());
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyActivity, PendingIntent.FLAG_IMMUTABLE);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100001")
          .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable._12x512))
          .setContentTitle("애니멀고 산책")
          .setContentText(mTrackingText)
          .setStyle(new NotificationCompat.BigTextStyle().bigText(mTrackingText))
          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
          .setContentIntent(pendingIntent)
          .setAutoCancel(true);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        builder.setSmallIcon(R.drawable._12x512);
        CharSequence channelName = "애니멀고 산책";
        String description = mTrackingText;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel("100001", channelName, importance);
        channel.setDescription(description);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);

      } else {
        builder.setSmallIcon(R.mipmap.ic_launcher);
      }

      assert notificationManager != null;
      notificationManager.notify(1234, builder.build());
    } else if (Prefs.getString(Constants.GUIDE_ALARM_YN,"").equals("N")) {

    }
  }
}
