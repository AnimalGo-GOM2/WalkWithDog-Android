package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;

import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;

public class FindPwDetailActivity extends RocateerActivity {
    //--------------------------------------------------------------------------------------------
    // MARK : GET START INTENT
    //--------------------------------------------------------------------------------------------
    public static Intent getStartIntent(Context context) {
      Intent intent = new Intent(context, FindPwDetailActivity.class);
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
      return R.layout.activity_find_pw_detail;
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

    //--------------------------------------------------------------------------------------------
    // MARK : Bind Actions
    //--------------------------------------------------------------------------------------------

    @OnClick(R.id.signin_button)
    public void signInTouched() {
      Intent signInActivity = SigninActivity.getStartIntent(mActivity);
      startActivity(signInActivity, TRANS.PUSH);
      removeAllActivity();

    }
}
