package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.QnaModel;
import rocateer.animalgo.models.api.CommonRouter;

public class SendFeedbackActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SendFeedbackActivity.class);
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
    return R.layout.activity_send_feedback;
  }

  @Override
  protected void initLayout() {
    initToolbar("");
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * ????????? ????????? API
   */
  public void qaRegInAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    qnaRequest.setQa_contents("");
    qnaRequest.setQa_type("1");
    qnaRequest.setQa_category("10");

    CommonRouter.api().qa_reg_in(Tools.getInstance().getMapper(qnaRequest)).enqueue(new Callback<QnaModel>() {
      @Override
      public void onResponse(Call<QnaModel> call, Response<QnaModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<QnaModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * ????????? ?????????
   */
  @OnClick(R.id.good_button)
  public void goodTouched() {
    showAlertDialog("?????????! ?????????????????? ???????????????.", "??????", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        qaRegInAPI();
      }
    });
  }


  /**
   * ????????? ????????? ????????????
   */
  @OnClick(R.id.new_function_button)
  public void newFunctionTouched() {
    Intent newfunctionActivity = NewFunctionActivity.getStartIntent(mActivity);
    startActivity(newfunctionActivity, TRANS.PUSH);
  }

  /**
   * ????????? ????????????
   */
  @OnClick(R.id.improving_button)
  public void improvingTouched() {
    Intent improvingActivity = ImprovingActivity.getStartIntent(mActivity);
    startActivity(improvingActivity,TRANS.PUSH);
  }
}
