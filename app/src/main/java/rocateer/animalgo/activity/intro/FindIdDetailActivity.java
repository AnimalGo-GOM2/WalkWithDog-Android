package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatTextView;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.models.MemberModel;

public class FindIdDetailActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, MemberModel memberModel) {
    Intent intent = new Intent(context, FindIdDetailActivity.class);
    mMemberModel = memberModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.find_id_text_view)
  AppCompatTextView mFindIdTextView;
  @BindView(R.id.ins_date_text_view)
  AppCompatTextView mInsDateTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemberModel mMemberResponse = new MemberModel();
  private static MemberModel mMemberModel;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_find_id_detail;
  }

  @Override
  protected void initLayout() {
    initToolbar("아이디 찾기");

    mFindIdTextView.setText(mMemberModel.getMember_id());
    mInsDateTextView.setText(mMemberModel.getIns_date());


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
   * 로그인 버튼
   */
  @OnClick(R.id.signin_button)
  public void signInTouched() {
    Intent signinActivity = SigninActivity.getStartIntent(mActivity);
    startActivity(signinActivity,TRANS.ZOOM);
    removeAllActivity();
  }

  /**
   * 비밀번호 찾기 버튼
   */
  @OnClick(R.id.find_pw_button)
  public void findPwTouched() {
    Intent findPwActivity = FindPwActivity.getStartIntent(mActivity);
    startActivity(findPwActivity, TRANS.PUSH);
  }
}
