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

public class FindIdActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, FindIdActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.find_id_name_edit_text)
  AppCompatEditText mFindIdNameEditText;
  @BindView(R.id.find_id_phone_edit_text)
  AppCompatEditText mFindIdPhoneEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemberModel mMemberResponse = new MemberModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_find_id;
  }

  @Override
  protected void initLayout() {
    initToolbar("아이디 찾기");
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 아이디 찾기 API
   */
  public void memberIdFindAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_name(mFindIdNameEditText.getText().toString());
    memberRequest.setMember_phone(mFindIdPhoneEditText.getText().toString());

    CommonRouter.api().member_id_find(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemberModel mMemberModel = response.body();
          mMemberModel.setMember_id(mMemberModel.getMember_id());
          mMemberModel.setIns_date(mMemberModel.getIns_date());
          Intent findIdDetailActivity = FindIdDetailActivity.getStartIntent(mActivity, mMemberModel);
          startActivity(findIdDetailActivity,TRANS.PUSH);
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
  @OnClick(R.id.complete_button)
  public void completeTouched() {
   memberIdFindAPI();
  }
}
