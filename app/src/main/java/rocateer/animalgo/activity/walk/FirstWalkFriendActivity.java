package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;

import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;

public class FirstWalkFriendActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, FirstWalkFriendActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_first_walk_friend;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구와 함께");

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------


  /**
   * 내 위치 설정
   */
  @OnClick(R.id.setting_location_button)
  public void settingLocationTouched() {
    finishWithRemove();
    Intent settingLocattionActivity = SettingMyLocationActivity.getStartIntent(mActivity, new SettingMyLocationActivity.SelectLocationListener() {
      @Override
      public void onResult(String location) {

      }
    });
    startActivity(settingLocattionActivity, TRANS.PUSH);
  }
}
