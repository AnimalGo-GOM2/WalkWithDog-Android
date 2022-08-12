package rocateer.animalgo;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pixplicity.easyprefs.library.Prefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import rocateer.animalgo.commons.Constants;
import timber.log.Timber;

public class RocateerApplication extends Application {

  // 기록 상태
  public enum RC_STATE {
    NONE, RECORDING, PAUSE
  }

  // 기록 관련
  public static RC_STATE rcState = RC_STATE.NONE;
  public static ArrayList<Location> recordingList = new ArrayList<>();
  public static int recordSeconds = 0;
  public static double recordDistance = 0.0;


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new RocateerDebugTree());
    }

    Timber.i("=================================================");
    Timber.i(" _____ _____ _____ _____ _____ _____ _____ _____ ");
    Timber.i("| __  |     |     |  _  |_   _|   __|   __| __  |");
    Timber.i("|    -|  |  |   --|     | | | |   __|   __|    -|");
    Timber.i("|__|__|_____|_____|__|__| |_| |_____|_____|__|__|");
    Timber.i("                                                 ");
    Timber.i("=================================================");
    Timber.v("VERBOSE");
    Timber.d("DEBUG");
    Timber.i("INFO");
    Timber.w("WARNNING");
    Timber.e("ERROR");
    Timber.wtf("WHAT THE FUCK");
    getKeyHash(this);

    // Shared Preference settging
    new Prefs.Builder()
        .setContext(this)
        .setMode(ContextWrapper.MODE_PRIVATE)
        .setPrefsName(getPackageName())
        .setUseDefaultSharedPreference(true)
        .build();

    // Firebase 초기화
    FirebaseApp.initializeApp(this);
    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
      @Override
      public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
          Timber.e("getInstanceId failed : " + task.getException());
          return;
        }
        String token = task.getResult().getToken();
        Timber.i("FCM KEY ===============================");
        Prefs.putString(Constants.FCM_TOKEN, token);
        Timber.i(Prefs.getString(Constants.FCM_TOKEN, ""));
        Timber.i("FCM KEY ===============================");
      }
    });


//    NaverMapSdk.getInstance(this).setClient(new NaverMapSdk.NaverCloudPlatformClient("um5pfkx9jv"));
  }

  //-------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------*/

  /**
   * 키 해시 가져오기
   *
   * @param context
   */
  public void getKeyHash(final Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
      if (packageInfo == null)
        return;

      for (android.content.pm.Signature signature : packageInfo.signatures) {
        try {
          MessageDigest md = MessageDigest.getInstance("SHA");
          md.update(signature.toByteArray());

          Timber.i(Base64.encodeToString(md.digest(), Base64.NO_WRAP));


        } catch (NoSuchAlgorithmException e) {
          Timber.e("MessageDigest를 가져올 수 없습니다. Signature : " + signature);
          Timber.e(e.getLocalizedMessage());
        }
      }
    } catch (PackageManager.NameNotFoundException e) {
      Timber.e(e.getLocalizedMessage());
    } catch (Exception e) {
      Timber.e(e.getLocalizedMessage());
    }

    return;
  }

  /**
   * Timber Debug tree
   */
  public class RocateerDebugTree extends Timber.DebugTree {
    @Override
    protected String createStackElementTag(StackTraceElement element) {
      return String.format("ROCATEER_LOG - (%s:%s) [%s] ", element.getFileName(), element.getLineNumber(), element.getMethodName());
    }
  }

}
