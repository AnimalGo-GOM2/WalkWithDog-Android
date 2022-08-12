package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;

public class WalkFriendGuideActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordIdx, WalkModel walkModel) {
    Intent intent = new Intent(context, WalkFriendGuideActivity.class);
    mRecordIdx = recordIdx;
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.with_friend_image_view)
  AppCompatImageView mWithFriendImageView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private RequestOptions requestOptions = new RequestOptions();
  private static String mRecordIdx;
  private static WalkModel mWalkModel;
  long now = System.currentTimeMillis();
  Date mDate = new Date(now);
  SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_friend_guide;
  }

  private String getTime() {
    return mFormat.format(mDate);
  }

  @Override
  protected void initLayout() {
    initToolbar("산책 가이드");
    recordGuideAPI();


  }

  @Override
  protected void initRequest() {

  }

  @Override
  public void onBackPressed() {
    //super.onBackPressed();
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 산책 가이드
   */
  private void recordGuideAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_type("1");

    CommonRouter.api().record_guide(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkModel = response.body();
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mWalkModel.getGuide_img())
              .into(mWithFriendImageView);
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 닫기 버튼
   */
  @OnClick(R.id.close_button)
  public void closeTouched() {
    if (getTime() != null ){
      Prefs.putString(Constants.START_DATE,getTime());
    }
    finishWithRemove();
    Intent walkTrackingActivity = WalkTrackingActivity.getStartIntent(mActivity, mRecordIdx, mWalkModel);
    startActivity(walkTrackingActivity,TRANS.PRESENT);
  }
}
