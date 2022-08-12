package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
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

public class ImprovingActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, ImprovingActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.contents_edit_text)
  AppCompatEditText mContentsEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_improving;
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
   * 개선이 필요해요 API
   */
  public void qaRegInAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    qnaRequest.setQa_contents(mContentsEditText.getText().toString());
    qnaRequest.setQa_type("1");
    qnaRequest.setQa_category("12");

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
   * 보내기
   */
  @OnClick(R.id.send_button)
  public void sendTouched() {
    qaRegInAPI();
  }
}

