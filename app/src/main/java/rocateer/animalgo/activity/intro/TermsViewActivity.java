package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.commons.terms.TermsActivity;
import rocateer.animalgo.activity.main.MainActivity;

public class TermsViewActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, TermsViewActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.all_checkbox)
  AppCompatCheckBox mAllTermsCheckbox;
  @BindView(R.id.checkbox1)
  AppCompatCheckBox mTerms1Checkbox;
  @BindView(R.id.checkbox2)
  AppCompatCheckBox mTerms2Checkbox;
  @BindView(R.id.checkbox3)
  AppCompatCheckBox mTerms3Checkbox;
  @BindView(R.id.checkbox4)
  AppCompatCheckBox mTerms4Checkbox;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private String mCheck = "";

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_terms_view;
  }

  @Override
  protected void initLayout() {
    initToolbar("약관보기");

    if (mTerms4Checkbox.isChecked()) {
      mCheck = "Y";
    } else {
      mCheck = "N";
    }


  }

  @Override
  protected void initRequest() {
    // 약관 체크박스
    CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
          case R.id.all_checkbox:

            break;
          case R.id.checkbox1:
          case R.id.checkbox2:
          case R.id.checkbox3:
          case R.id.checkbox4:
            if (mTerms1Checkbox.isChecked() && mTerms2Checkbox.isChecked() && mTerms3Checkbox.isChecked() && mTerms4Checkbox.isChecked()) {
              mAllTermsCheckbox.setChecked(true);
            } else {
              mAllTermsCheckbox.setChecked(false);
            }
        }
      }
    };

    mAllTermsCheckbox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mAllTermsCheckbox.isChecked()) {
          mTerms1Checkbox.setChecked(true);
          mTerms2Checkbox.setChecked(true);
          mTerms3Checkbox.setChecked(true);
          mTerms4Checkbox.setChecked(true);
        } else {
          mTerms1Checkbox.setChecked(false);
          mTerms2Checkbox.setChecked(false);
          mTerms3Checkbox.setChecked(false);
          mTerms4Checkbox.setChecked(false);
        }
      }
    });
    mTerms1Checkbox.setOnCheckedChangeListener(checkedListener);
    mTerms2Checkbox.setOnCheckedChangeListener(checkedListener);
    mTerms3Checkbox.setOnCheckedChangeListener(checkedListener);
    mTerms4Checkbox.setOnCheckedChangeListener(checkedListener);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  @OnClick(R.id.next_button)
  public void nextTouched() {
    if (!mTerms1Checkbox.isChecked() || !mTerms2Checkbox.isChecked() || !mTerms3Checkbox.isChecked()) {
      showAlertDialog("약관을 확인해주세요.", "확인", null);
    } else {
      Intent signUpActivity = SignupActivity.getStartIntent(mActivity);
      startActivity(signUpActivity, TRANS.PUSH);
    }
  }

  /**
   * 약관 보기 웹뷰
   */
  @OnClick({R.id.term_button1, R.id.term_button2, R.id.term_button3})
  public void terms1Touched(View view) {
    Intent termsActivity;
    if (view.getId() == R.id.term_button1) {
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms2);
      startActivity(termsActivity, TRANS.PUSH);
    } else if (view.getId() == R.id.term_button2) {
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms3);
      startActivity(termsActivity,TRANS.PUSH);
    } else if (view.getId() == R.id.term_button3) {
      termsActivity = TermsActivity.getStartIntent(mActivity, TermsActivity.TermsType.Terms1);
      startActivity(termsActivity, TRANS.PUSH);
    }


  }

}