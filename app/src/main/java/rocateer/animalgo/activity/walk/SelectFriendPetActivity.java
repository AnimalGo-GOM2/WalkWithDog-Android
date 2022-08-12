package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.models.WalkModel;
import timber.log.Timber;

public class SelectFriendPetActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, WalkModel walkModel) {
    Intent intent = new Intent(context, SelectFriendPetActivity.class);
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.back_button)
  AppCompatImageView mBackButton;
  @BindView(R.id.pet_male_button)
  RoundRectView mPetMaleButton;
  @BindView(R.id.pet_female_button)
  RoundRectView mPetFemaleButton;
  @BindView(R.id.gender_no_problem)
  RoundRectView mGenderNoProblemButton;
  @BindView(R.id.pet_trans_button)
  RoundRectView mPetTransButton;
  @BindView(R.id.pet_trans_no_button)
  RoundRectView mPetTransNoButton;
  @BindView(R.id.trans_no_problem)
  RoundRectView mTransNoProblem;
  @BindView(R.id.pet_char1_button)
  RoundRectView mPetChar1Button;
  @BindView(R.id.pet_char2_button)
  RoundRectView mPetChar2Button;
  @BindView(R.id.pet_char3_button)
  RoundRectView mPetChar3Button;
  @BindView(R.id.pet_char4_button)
  RoundRectView mPetChar4Button;
  @BindView(R.id.pet_char5_button)
  RoundRectView mPetChar5Button;
  @BindView(R.id.all_pet)
  AppCompatCheckBox mAllPet;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  ArrayList<RoundRectView> mPetGenderButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mPetTransButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mPetCharButtonList = new ArrayList<>();
  private static WalkModel mWalkModel;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_select_friend_pet;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 등록");
    mBackButton.setVisibility(View.GONE);

    mPetGenderButtonList = new ArrayList<>(Arrays.asList(mPetMaleButton, mPetFemaleButton, mGenderNoProblemButton));
    mPetTransButtonList = new ArrayList<>(Arrays.asList(mPetTransButton, mPetTransNoButton, mTransNoProblem));
    mPetCharButtonList = new ArrayList<>(Arrays.asList(mPetChar1Button, mPetChar2Button, mPetChar3Button, mPetChar4Button, mPetChar5Button));
    mAllPet.setChecked(true);
    if (mAllPet.isChecked()) {
      mGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
      mGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
      mGenderNoProblemButton.setSelected(true);
      mPetChar5Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
      mPetChar5Button.setBorderColor(getColor(R.color.colorAccent));
      mPetChar5Button.setSelected(true);
      mTransNoProblem.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
      mTransNoProblem.setBorderColor(getColor(R.color.colorAccent));
      mTransNoProblem.setSelected(true);
    }






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
   * 다음 버튼
   */
  @OnClick(R.id.next_button)
  public void NextTouched() {
    Intent selectFriendConditionActivity = SelectFriendConditionActivity.getStartIntent(mActivity, mWalkModel);
    startActivity(selectFriendConditionActivity, TRANS.PUSH);
    finishWithRemove();

  }

  /**
   * 성별
   */
  @OnClick({R.id.pet_male_button, R.id.pet_female_button, R.id.gender_no_problem})
  public void genderTouched(RoundRectView button) {
    for (RoundRectView value : mPetGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);
    if (mPetMaleButton.isSelected()) {
      mWalkModel.setAnimal_gender("0");
    } else if (mPetFemaleButton.isSelected()) {
      mWalkModel.setAnimal_gender("1");
    } else if (mGenderNoProblemButton.isSelected()) {
      mWalkModel.setAnimal_gender("");
    }


  }

  /**
   * 중성화
   */
  @OnClick({R.id.pet_trans_button, R.id.pet_trans_no_button, R.id.trans_no_problem})
  public void transTouched(RoundRectView button) {
    for (RoundRectView value : mPetTransButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mPetTransButton.isSelected()) {
      mWalkModel.setAnimal_neuter("Y");
    } else if (mPetTransNoButton.isSelected()) {
      mWalkModel.setAnimal_neuter("N");
    } else if (mTransNoProblem.isSelected()) {
      mWalkModel.setAnimal_neuter("");
    }


  }

  /**
   * 성격
   */
  @OnClick({R.id.pet_char1_button, R.id.pet_char2_button, R.id.pet_char3_button, R.id.pet_char4_button, R.id.pet_char5_button})
  public void charTouched(RoundRectView button) {
    for (RoundRectView value : mPetCharButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mPetChar1Button.isSelected()) {
      mWalkModel.setAnimal_character("0");
    } else if (mPetChar2Button.isSelected()) {
      mWalkModel.setAnimal_character("1");
    } else if (mPetChar3Button.isSelected()) {
      mWalkModel.setAnimal_character("2");
    } else if (mPetChar4Button.isSelected()) {
      mWalkModel.setAnimal_character("3");
    } else if (mPetChar5Button.isSelected()) {
      mWalkModel.setAnimal_character("");
    }


  }

  /**
   * 모두 상관없어요
   */
  @OnClick(R.id.all_pet)
  public void allPetTouched() {

    mGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
    mGenderNoProblemButton.setSelected(true);
    mPetChar5Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mPetChar5Button.setBorderColor(getColor(R.color.colorAccent));
    mPetChar5Button.setSelected(true);
    mTransNoProblem.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mTransNoProblem.setBorderColor(getColor(R.color.colorAccent));
    mTransNoProblem.setSelected(true);
  }


  /**
   * 견종 찾기
   */
  @OnClick(R.id.search_pet_text_view)
  public void searchPetTouched() {
    Intent searchPetActivity = SearchPetActivity.getStartIntent(mActivity);
    startActivity(searchPetActivity, TRANS.PRESENT);
  }

  /**
   * 취소
   */
  @OnClick(R.id.walk_cancle_button)
  public void walkCancleTouched() {
    finishWithRemove();
  }
}
