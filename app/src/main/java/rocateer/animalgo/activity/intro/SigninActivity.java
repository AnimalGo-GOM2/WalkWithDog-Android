package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.commons.RocateerPreference;

public class SigninActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SigninActivity.class);
    return intent;
  }

  public static Intent getStartIntent(Context context, boolean pause) {
    Intent intent = new Intent(context, SigninActivity.class);
    isPause = pause;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.email_edit_text)
  AppCompatEditText mIdEditText;
  @BindView(R.id.pw_edit_text)
  AppCompatEditText mPwEditText;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private static boolean isPause = false;
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_signin;
  }

  @Override
  protected void initLayout() {
    if (isPause) {
      showAlertDialog("이용 중지 처리된 계정입니다.\n관리자에 문의해주세요.", "확인", new DialogEventListener() {
        @Override
        public void onReceivedEvent() {
        }
      });
    }
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 회원 로그인 API
   */
  private void memberLoginAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_id(mIdEditText.getText().toString());
    memberRequest.setMember_pw(mPwEditText.getText().toString());
    memberRequest.setDevice_os("A");
    memberRequest.setGcm_key(Prefs.getString(Constants.FCM_TOKEN,""));

    CommonRouter.api().member_login(Tools.getInstance(mActivity).getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemberModel memberResponse = response.body();

          Prefs.putString(Constants.MEMBER_IDX, memberResponse.getMember_idx());
          Prefs.putString(Constants.MEMBER_ID, mIdEditText.getText().toString());
          Prefs.putString(Constants.MEMBER_NAME, memberResponse.getMember_name());
          Prefs.putString(Constants.MEMBER_PW, mPwEditText.getText().toString());
          Prefs.putString(Constants.MEMBER_NICKNAME, memberResponse.getMember_nickname());
          Intent mainActivity = MainActivity.getStartIntent(mActivity);
          startActivity(mainActivity,TRANS.ZOOM);
          removeAllActivity();
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {
        showSnackBar("오류");
      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 아이디 찾기 버튼
   */
  @OnClick(R.id.find_id_button)
  public void findIdTouched() {
    Intent findIdActivity = FindIdActivity.getStartIntent(mActivity);
    startActivity(findIdActivity, TRANS.PUSH);
  }

  /**
   * 비밀번호 찾기 버튼
   */
  @OnClick(R.id.find_pw_button)
  public void findPwTouched() {
    Intent findPwActivity = FindPwActivity.getStartIntent(mActivity);
    startActivity(findPwActivity, TRANS.PUSH);
  }

  /**
   * 회원가입 버튼
   */
  @OnClick(R.id.signup_button)
  public void signUpTouched() {
    Intent termsViewActivity = TermsViewActivity.getStartIntent(mActivity);
    startActivity(termsViewActivity, TRANS.PUSH);
  }
  /**
   * 로그인 버튼
   */
  @OnClick(R.id.login_button)
  public void signInTouched() {
    memberLoginAPI();
  }

}
