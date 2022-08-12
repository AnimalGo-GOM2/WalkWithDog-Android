package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.pixplicity.easyprefs.library.Prefs;

import java.net.URI;

import butterknife.BindView;
import butterknife.OnClick;
import per.wsj.library.AndRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.BuildConfig;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.intro.SigninActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.WithDrawalDialog;
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class SettingActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SettingActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.walk_friends_talk_alarm_toggle_button)
  SwitchCompat mWalkfriendsTalkalarmToggleButton;
  @BindView(R.id.guide_alarm_toggle_button)
  SwitchCompat mguideAlarmToggleButton;
  @BindView(R.id.marketting_alarm_toggle_button)
  SwitchCompat mMarkettingAlarmToggleButton;
  @BindView(R.id.version_info_text)
  AppCompatTextView mVerSionIngoText;



  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private AlarmModel mAlarmResponse = new AlarmModel();
  private String mGuideAlarmState = "";

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_setting;
  }

  @Override
  protected void initLayout() {
    initToolbar("설정");
    mVerSionIngoText.setText(BuildConfig.VERSION_NAME);


  }

  @Override
  protected void initRequest() {
    alarmToggleViewAPI();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 로그아웃 API
   */
  public void memberLogOutAPI() {
    MemberModel memberRequest  = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));

    CommonRouter.api().member_logout(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {

          Prefs.remove(Constants.MEMBER_IDX);
          Prefs.remove(Constants.MEMBER_ID);
          Prefs.remove(Constants.MEMBER_NAME);
          Prefs.remove(Constants.MEMBER_PW);
          Prefs.remove(Constants.MEMBER_REGION);

          removeAllActivity();
          Intent signInActivity = SigninActivity.getStartIntent(mActivity);
          startActivity(signInActivity,TRANS.ZOOM);
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }
  /**
   * 알림 상태
   */
  public void alarmToggle(String value, String  type) {
    AlarmModel alarmRequest = new AlarmModel();
    alarmRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    alarmRequest.setType(type);
    alarmRequest.setValue(value);

    CommonRouter.api().alarm_toggle(Tools.getInstance().getMapper(alarmRequest)).enqueue(new Callback<AlarmModel>() {
      @Override
      public void onResponse(Call<AlarmModel> call, Response<AlarmModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
        }
      }

      @Override
      public void onFailure(Call<AlarmModel> call, Throwable t) {

      }
    });
  }

  /**
   * 알림 설정 보기 API
   */
  public void alarmToggleViewAPI() {
    AlarmModel alarmRequest = new AlarmModel();
    alarmRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));

    CommonRouter.api().alarm_toggle_view(Tools.getInstance().getMapper(alarmRequest)).enqueue(new Callback<AlarmModel>() {
      @Override
      public void onResponse(Call<AlarmModel> call, Response<AlarmModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAlarmResponse = response.body();
          if (mAlarmResponse.getChatting_alarm_yn() != null || mAlarmResponse.getGuide_alarm_yn() != null || mAlarmResponse.getMarketing_agree_yn() != null) {
            if (mAlarmResponse.getChatting_alarm_yn().equals("Y")) {
              mWalkfriendsTalkalarmToggleButton.setChecked(true);
            } else if (mAlarmResponse.getChatting_alarm_yn().equals("N")) {
              mWalkfriendsTalkalarmToggleButton.setChecked(false);
            }
            if (mAlarmResponse.getGuide_alarm_yn().equals("Y")) {
              mguideAlarmToggleButton.setChecked(true);
            } else if (mAlarmResponse.getGuide_alarm_yn().equals("N")) {
              mguideAlarmToggleButton.setChecked(false);
            }
            if (mAlarmResponse.getMarketing_agree_yn().equals("Y")) {
              mMarkettingAlarmToggleButton.setChecked(true);
            } else if (mAlarmResponse.getMarketing_agree_yn().equals("N")) {
              mMarkettingAlarmToggleButton.setChecked(false);
            }
          }

        }
      }

      @Override
      public void onFailure(Call<AlarmModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 알림 디바이스 설정
   */
  @OnClick(R.id.alarm_device_setting)
  public void alarmDeviceSettingTouched() {
    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
    finish();
    startActivity(i);
  }

  /**
   * 회원탈퇴
   */
  @OnClick(R.id.withdrawal_text_view)
  public void withDrawalTouched() {
    WithDrawalDialog withDrawalDialog = new WithDrawalDialog(mActivity);
    withDrawalDialog.show();
  }
  /**
   * 로그아웃
   */
  @OnClick(R.id.logout_layout)
  public void logoutTouched() {
    showConfirmDialog("로그아웃 하시겠습니까?", "취소", "확인", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        memberLogOutAPI();
      }
    });
  }

  /**
   * 알림 버튼
   */
  @OnClick({R.id.walk_friends_talk_alarm_toggle_button, R.id.guide_alarm_toggle_button, R.id.marketting_alarm_toggle_button})
  public void alarmTouched(final View view) {
    switch (view.getId()) {
      case R.id.walk_friends_talk_alarm_toggle_button:
        alarmToggle(mWalkfriendsTalkalarmToggleButton.isChecked() ? "Y" : "N", "0");
        break;
      case R.id.guide_alarm_toggle_button:
        alarmToggle(mguideAlarmToggleButton.isChecked() ? "Y" : "N", "1");
        if (mguideAlarmToggleButton.isChecked()) {
          Prefs.putString(Constants.GUIDE_ALARM_YN,"Y");
        } else {
          Prefs.putString(Constants.GUIDE_ALARM_YN,"N");
        }
        Timber.i( "알림 : " + Prefs.getString(Constants.GUIDE_ALARM_YN,""));
        break;
      case R.id.marketting_alarm_toggle_button:
        alarmToggle(mMarkettingAlarmToggleButton.isChecked() ? "Y" : "N", "2");
        break;
    }
  }
}

