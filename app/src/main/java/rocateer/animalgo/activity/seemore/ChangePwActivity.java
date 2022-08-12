package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

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
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;

public class ChangePwActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, ChangePwActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.pw_edit_text)
  AppCompatEditText mPwEditText;
  @BindView(R.id.new_pw_edit_text)
  AppCompatEditText mNewPwEditText;
  @BindView(R.id.confirm_new_pw_edit_text)
  AppCompatEditText mConfirmNewPwEditText;
  @BindView(R.id.user_id_text_view)
  AppCompatTextView mIdTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  private MemberModel mMemberResponse = new MemberModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_change_pw;
  }

  @Override
  protected void initLayout() {
    initToolbar("계정관리");
    memberPwModViewAPI();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 비밀번호 변경 API
   */
  public void memberPwModUpAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memberRequest.setMember_pw(mPwEditText.getText().toString());
    memberRequest.setNew_member_pw(mNewPwEditText.getText().toString());
    memberRequest.setNew_member_pw_check(mConfirmNewPwEditText.getText().toString());

    CommonRouter.api().member_pw_mod_up(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  // 비밀번호 변경 폼 API
  public void memberPwModViewAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    CommonRouter.api().member_pw_mod_view(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse =response.body();
          mIdTextView.setText(mMemberResponse.getMember_id());

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
  /**
   * 비밀번호 변경
   */
  @OnClick(R.id.change_pw_button)
  public void changePwTouched() {
    memberPwModUpAPI();
  }
}

