package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;

public class FindPwActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, FindPwActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.member_id_edit_text)
  AppCompatEditText mMemberIdEditText;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemberModel mMemberResponse = new MemberModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_find_pw;
  }

  @Override
  protected void initLayout() {
    initToolbar("비밀번호 찾기");
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 비밀번호 찾기 API
   */
  public void memeberPwResetSendEmailAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_id(mMemberIdEditText.getText().toString());
    CommonRouter.api().member_pw_reset_send_email(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();
          Intent findPwDetailActivity = FindPwDetailActivity.getStartIntent(mActivity);
          startActivity(findPwDetailActivity, TRANS.PUSH);
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  @OnClick(R.id.find_pw_button)
  public void findPwDetailTouched() {
    memeberPwResetSendEmailAPI();

  }

}