package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.lang.reflect.Member;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.commons.AuthActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class SignupActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SignupActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.email_edit_text)
  AppCompatEditText mEmailEditText;
  @BindView(R.id.pw_edit_text)
  AppCompatEditText mPwEditText;
  @BindView(R.id.confirm_pw_edit_text)
  AppCompatEditText mConfirmPwEditText;
  @BindView(R.id.nickname_edit_text)
  AppCompatEditText mNickNameEditText;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemberModel mMemberResponse = new MemberModel();
  private boolean isAuth = false;
  private String mPhone = "";
  private String mBirth = "";
  private String mName = "";
  private String mGender = "";
  private static String mCheck;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_signup;
  }

  @Override
  protected void initLayout() {
    initToolbar("회원가입");
  }

  @Override
  protected void initRequest() {

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 회원가입 API
   */
  private void memberRegIn(String gender, String tel, String birth, String name) {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_id(mEmailEditText.getText().toString());
    memberRequest.setMember_pw(mPwEditText.getText().toString());
    memberRequest.setMember_pw_confirm(mConfirmPwEditText.getText().toString());
    memberRequest.setMember_nickname(mNickNameEditText.getText().toString());
    memberRequest.setMember_gender(gender);
    memberRequest.setMember_birth(birth);
    memberRequest.setMember_name(name);
    memberRequest.setMember_phone(tel);

    CommonRouter.api().member_reg_in(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          showSnackBar("회원가입이 완료되었습니다.");
          Intent welcomeActivity = WelcomeActivity.getStartIntent(mActivity);
          startActivity(welcomeActivity, TRANS.PUSH);
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
   * 본인 인증
   */
  @OnClick(R.id.auth_button)
  public void authTouched() {
    Intent authActivity = AuthActivity.getStartIntent(mActivity, new AuthActivity.AuthListener() {
      @Override
      public void onAuthResult(String name, String tel, String gender, String birth) {
        Timber.i(mBirth,mPhone,mGender,mBirth);
        mName = name;
        mPhone = tel;
        mGender = gender;
        mBirth = birth;
        isAuth = true;
      }
    });
    startActivity(authActivity, TRANS.PRESENT);

  }

  /**
   * 회원가입
   */
  @OnClick(R.id.signup_button)
  public void SignUpTouched() {
    memberRegIn(mGender, mPhone, mBirth, mName);
  }
}