package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.commons.terms.TermsActivity;

public class AnimalgoInfoActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, AnimalgoInfoActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.terms_of_use_layout)
  LinearLayout mTermsOfUseLayout;
  @BindView(R.id.privacy_policy_terms_layout)
  LinearLayout mPrivacyPolicytermsLayout;
  @BindView(R.id.location_information_layout)
  LinearLayout mLocattionInfomationLayout;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_animalgo_info;
  }

  @Override
  protected void initLayout() {
    initToolbar("애니멀고 정보");

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
   * 약관 보기 웹뷰
   */
  @OnClick({R.id.terms_of_use_layout, R.id.privacy_policy_terms_layout, R.id.location_information_layout})
  public void terms1Touched(View view) {
    Intent termsActivity;
    if (view.getId() == R.id.terms_of_use_layout) {
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms2);
    } else if (view.getId() == R.id.privacy_policy_terms_layout){
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms3);
    } else {
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms4);
    }

    startActivity(termsActivity, TRANS.PUSH);
  }

  /**
   *  회사 소개
   */
  @OnClick(R.id.company_info)
  public void companyInfoTouched() {
    Intent companyInfoActivity = CompanyInfoActivity.getStartIntent(mActivity);
    startActivity(companyInfoActivity,TRANS.PUSH );
  }
}
