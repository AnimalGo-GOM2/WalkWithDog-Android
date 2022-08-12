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
import timber.log.Timber;

public class WalkGuideActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordIdx) {
    Intent intent = new Intent(context, WalkGuideActivity.class);
    mRecordIdx = recordIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.guide_image_view)
  AppCompatImageView mGuideImageView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private RequestOptions requestOptions = new RequestOptions();
  private WalkModel mWalkResponse = new WalkModel();
  private static String mRecordIdx;
  long now = System.currentTimeMillis();
  Date mDate = new Date(now);
  SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_guide;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책 가이드");
    recordGuideAPI();


  }

  @Override
  protected void initRequest() {

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 산책 가이드
   */
  private void recordGuideAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_type("0");

    CommonRouter.api().record_guide(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkModel = response.body();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mWalkModel.getGuide_img())
              .apply(requestOptions)
              .into(mGuideImageView);
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  private String getTime() {
    return mFormat.format(mDate);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  /**
   * 산책시작
   */
  @OnClick(R.id.start_walking_button)
  public void startWalkingTouched() {
    Intent withPetTrackingactivity = WithPetTrackingActivity.getStartIntent(mActivity, mRecordIdx);
    startActivity(withPetTrackingactivity,TRANS.PUSH);
    Prefs.putString(Constants.RECORD_TYPE,"0");
    Prefs.putString(Constants.REOCRD_IDX,mRecordIdx);
    if (getTime() != null ){
      Prefs.putString(Constants.START_DATE,getTime());
    }
    Timber.i(Prefs.getString(Constants.RECORD_TYPE,""));
    finishMultiRemoveActivity(2);
  }

  /**
   * 이전 버튼
   */
  @OnClick(R.id.walk_back_button)
  public void backTouched() {
    finishWithRemove();
  }
}
