package rocateer.animalgo.activity.intro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.RocateerPreference;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.naver.NaverModel;
import rocateer.animalgo.models.naver.NaverRouter;
import timber.log.Timber;

public class IntroActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, IntroActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도
  private String mRegion; // 위치


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_intro;
  }

  @Override
  protected void initLayout() {
    permissionCheck();
  }

  @Override
  protected void initRequest() {
    if (Prefs.getDouble(Constants.MEMBER_LAT, 0.0) == 0.0) {
    }
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 메인 이동 딜레이
   */
  private void delayMain() {
    Handler delayHandler = new Handler();
    delayHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (!Prefs.getBoolean(Constants.TUTORIAL, false)) {
          Intent tutorialActivity = TutorialActivity.getStartIntent(mActivity);
          startActivity(tutorialActivity, TRANS.PRESENT);
        } else if (Prefs.getString(Constants.MEMBER_IDX, "").equals("")) {
          Intent signinActivity = SigninActivity.getStartIntent(mActivity);
          startActivity(signinActivity, TRANS.ZOOM);
          Timber.i(mRegion);
          finishWithRemove();
        } else {
          memberLoginAPI();
        }

      }
    }, 2000);
  }

  /**
   * 권한체크
   */
  private void permissionCheck() {

    // 권한 체크
    int accessFineLocation = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);

    if (accessFineLocation == PackageManager.PERMISSION_DENIED) {
      PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
          delayMain();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
        }
      };

      TedPermission.with(mActivity)
          .setPermissionListener(permissionlistener)
          .setRationaleMessage(R.string.common_auth)
          .setDeniedMessage(R.string.common_auth_denined)
          .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
          .check();
    } else {
      delayMain();
    }
  }

  /**
   * 회원 로그인 API
   */
  private void memberLoginAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_id(Prefs.getString(Constants.MEMBER_ID, ""));
    memberRequest.setMember_pw(Prefs.getString(Constants.MEMBER_PW, ""));
    memberRequest.setDevice_os("A");
    memberRequest.setGcm_key(Prefs.getString(Constants.FCM_TOKEN, ""));

    CommonRouter.api().member_login(Tools.getInstance(mActivity).getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemberModel memberResponse = response.body();

          Prefs.putString(Constants.MEMBER_IDX, memberResponse.getMember_idx());
          Prefs.putString(Constants.MEMBER_NAME, memberResponse.getMember_name());
          Prefs.putString(Constants.MEMBER_NICKNAME, memberResponse.getMember_nickname());
          Prefs.putString(Constants.GUIDE_ALARM_YN, memberResponse.getGuide_alarm_yn());
          Intent mainActivity = MainActivity.getStartIntent(mActivity);
          startActivity(mainActivity, TRANS.ZOOM);
          removeAllActivity();
        } else {
          finishWithRemove();
          Intent signInActivity = SigninActivity.getStartIntent(mActivity);
          startActivity(signInActivity, TRANS.ZOOM);
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {
        showSnackBar("오류");
      }
    });
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------


}
