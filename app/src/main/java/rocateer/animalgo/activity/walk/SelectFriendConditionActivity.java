package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;

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
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class SelectFriendConditionActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, WalkModel walkModel) {
    Intent intent = new Intent(context, SelectFriendConditionActivity.class);
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.back_button)
  AppCompatImageView mBackButton;
  @BindView(R.id.person_male_button)
  RoundRectView mPersonMaleButton;
  @BindView(R.id.person_female_button)
  RoundRectView mPersonFemaleButton;
  @BindView(R.id.person_gender_no_problem)
  RoundRectView mPersonGenderNoProblemButton;
  @BindView(R.id.age_button1)
  RoundRectView mAgeButton1;
  @BindView(R.id.age_button2)
  RoundRectView mAgeButton2;
  @BindView(R.id.age_button3)
  RoundRectView mAgeButton3;
  @BindView(R.id.age_button4)
  RoundRectView mAgeButton4;
  @BindView(R.id.age_button5)
  RoundRectView mAgeButton5;
  @BindView(R.id.all_person)
  AppCompatCheckBox mAllPerson;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  ArrayList<RoundRectView> mPersonGenderButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mAgeButtonList = new ArrayList<>();
  private static WalkModel mWalkModel;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_select_friend_condition;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 등록");
    mBackButton.setVisibility(View.GONE);

    mPersonGenderButtonList = new ArrayList<>(Arrays.asList(mPersonMaleButton, mPersonFemaleButton, mPersonGenderNoProblemButton));
    mAgeButtonList = new ArrayList<>(Arrays.asList(mAgeButton1, mAgeButton2, mAgeButton3, mAgeButton4, mAgeButton5));

    mAllPerson.setChecked(true);
    if (mAllPerson.isChecked()) {
      mPersonGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
      mPersonGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
      mPersonGenderNoProblemButton.setSelected(true);
      mAgeButton5.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
      mAgeButton5.setBorderColor(getColor(R.color.colorAccent));
      mAgeButton5.setSelected(true);
    }

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 산책등록 API
   */
  public void recordRegInAPI() {
    mWalkModel.setRecord_type("1");
    mWalkModel.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    mWalkModel.setRecord_addr(mWalkModel.getRecord_addr());
    mWalkModel.setRecord_lat(mWalkModel.getRecord_lat());
    mWalkModel.setRecord_lng(mWalkModel.getRecord_lng());
    mWalkModel.setMember_animal_idxs(mWalkModel.getMember_animal_idxs());
    mWalkModel.setRecord_date(mWalkModel.getRecord_date());
    mWalkModel.setAnimal_gender(mWalkModel.getAnimal_gender());
    mWalkModel.setAnimal_character(mWalkModel.getAnimal_character());
    mWalkModel.setAnimal_neuter(mWalkModel.getAnimal_neuter());
    if (mPersonMaleButton.isSelected()) {
      mWalkModel.setGuardian_gender("0");
    } else if (mPersonFemaleButton.isSelected()) {
      mWalkModel.setGuardian_gender("1");
    } else if (mPersonGenderNoProblemButton.isSelected()) {
      mWalkModel.setGuardian_gender("");
    }

    if (mAgeButton1.isSelected()) {
      mWalkModel.setGuardian_age("20");
    } if (mAgeButton2.isSelected()) {
      mWalkModel.setGuardian_age("30");
    } else if (mAgeButton3.isSelected()) {
      mWalkModel.setGuardian_age("40");
    } else if (mAgeButton4.isSelected()) {
      mWalkModel.setGuardian_age("50");
    } else if (mAgeButton5.isSelected()) {
      mWalkModel.setGuardian_age("");
    }

    CommonRouter.api().record_reg_in(Tools.getInstance().getMapper(mWalkModel)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkModel = response.body();
          Intent walkRefresh = new Intent(Constants.WALK_REFRESH);
          mActivity.sendBroadcast(walkRefresh);
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });


  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 등록 버튼
   */
  @OnClick(R.id.add_walk_button)
  public void enrollmentTouched() {
    recordRegInAPI();
  }

  /**
   * 모두 상관없어요
   */
  @OnClick(R.id.all_person)
  public void allPersonTouched() {

    mPersonGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mPersonGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
    mPersonGenderNoProblemButton.setSelected(true);
    mAgeButton5.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mAgeButton5.setBorderColor(getColor(R.color.colorAccent));
    mAgeButton5.setSelected(true);
  }

  /**
   * 성별
   */
  @OnClick({R.id.person_male_button, R.id.person_female_button, R.id.person_gender_no_problem})
  public void genderTouched(RoundRectView button) {
    for (RoundRectView value : mPersonGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 나이
   */
  @OnClick({R.id.age_button1, R.id.age_button2, R.id.age_button3, R.id.age_button4, R.id.age_button5})
  public void ageTouched(RoundRectView button) {
    for (RoundRectView value : mAgeButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 취소
   */
  @OnClick(R.id.walk_cancle_button)
  public void walkCancleTouched() {
    finishWithRemove();
  }

}
