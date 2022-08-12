package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class WalkFilterActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, WalkFilterActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.filter_male_button)
  RoundRectView mFilterMaleButton;
  @BindView(R.id.filter_female_button)
  RoundRectView mFilterFemaleButton;
  @BindView(R.id.filter_gender_no_problem)
  RoundRectView mFilterGenderNoProblemButton;
  @BindView(R.id.filter_trans_button)
  RoundRectView mFilterTransButton;
  @BindView(R.id.filter_trans_no_button)
  RoundRectView mFilterTransNoButton;
  @BindView(R.id.filter_trans_no_problem)
  RoundRectView mFilterTransNoProblem;
  @BindView(R.id.filter_char1_button)
  RoundRectView mFilterChar1Button;
  @BindView(R.id.filter_char2_button)
  RoundRectView mFilterChar2Button;
  @BindView(R.id.filter_char3_button)
  RoundRectView mFilterChar3Button;
  @BindView(R.id.filter_char4_button)
  RoundRectView mFilterChar4Button;
  @BindView(R.id.filter_char5_button)
  RoundRectView mFilterChar5Button;
  @BindView(R.id.filter_person_male_button)
  RoundRectView mFilterPersonMaleButton;
  @BindView(R.id.filter_person_female_button)
  RoundRectView mFilterPersonFemaleButton;
  @BindView(R.id.filter_person_gender_no_problem)
  RoundRectView mFilterPersonGenderNoProblemButton;
  @BindView(R.id.filter_age_button1)
  RoundRectView mFilterAgeButton1;
  @BindView(R.id.filter_age_button2)
  RoundRectView mFilterAgeButton2;
  @BindView(R.id.filter_age_button3)
  RoundRectView mFilterAgeButton3;
  @BindView(R.id.filter_age_button4)
  RoundRectView mFilterAgeButton4;
  @BindView(R.id.filter_age_button5)
  RoundRectView mFilterAgeButton5;
  @BindView(R.id.all_pet)
  AppCompatCheckBox mAllPet;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  ArrayList<RoundRectView> mFilterPersonGenderButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mFilterAgeButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mFilterGenderButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mFilterTransButtonList = new ArrayList<>();
  ArrayList<RoundRectView> mFilterCharButtonList = new ArrayList<>();
  private WalkModel mWalkResponse = new WalkModel();
  private static String mMemberAge;
  private static String mMemberGender;
  private static String mAnimalGender;
  private static String mAnimalChar;
  private static String mAnimalNeuter;


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_filter;
  }

  @Override
  protected void initLayout() {
    initToolbar("필터 설정");
    mFilterPersonGenderButtonList = new ArrayList<>(Arrays.asList(mFilterPersonMaleButton, mFilterPersonFemaleButton, mFilterPersonGenderNoProblemButton));
    mFilterAgeButtonList = new ArrayList<>(Arrays.asList(mFilterAgeButton1, mFilterAgeButton2, mFilterAgeButton3, mFilterAgeButton4, mFilterAgeButton5));
    mFilterGenderButtonList = new ArrayList<>(Arrays.asList(mFilterMaleButton, mFilterFemaleButton, mFilterGenderNoProblemButton));
    mFilterTransButtonList = new ArrayList<>(Arrays.asList(mFilterTransButton, mFilterTransNoButton, mFilterTransNoProblem));
    mFilterCharButtonList = new ArrayList<>(Arrays.asList(mFilterChar1Button, mFilterChar2Button, mFilterChar3Button, mFilterChar4Button, mFilterChar5Button));

    mAllPet.setChecked(true);

    mFilterPersonGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mFilterPersonGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
    mFilterPersonGenderNoProblemButton.setSelected(true);
    mFilterAgeButton5.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mFilterAgeButton5.setBorderColor(getColor(R.color.colorAccent));
    mFilterAgeButton5.setSelected(true);
    mFilterChar5Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mFilterChar5Button.setBorderColor(getColor(R.color.colorAccent));
    mFilterChar5Button.setSelected(true);
    mFilterTransNoProblem.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mFilterTransNoProblem.setBorderColor(getColor(R.color.colorAccent));
    mFilterTransNoProblem.setSelected(true);
    mFilterGenderNoProblemButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    mFilterGenderNoProblemButton.setBorderColor(getColor(R.color.colorAccent));
    mFilterGenderNoProblemButton.setSelected(true);

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 산책친구와 함께
   */
  private void recordTogetherListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    walkRequest.setMember_age(mWalkResponse.getMember_age());
    walkRequest.setMember_gender(mWalkResponse.getMember_gender());
    walkRequest.setAnimal_neuter(mWalkResponse.getAnimal_neuter());
    walkRequest.setAnimal_character(mWalkResponse.getAnimal_character());
    walkRequest.setAnimal_gender(mWalkResponse.getAnimal_gender());
    walkRequest.setPage_num(mWalkResponse.getNextPage());
    CommonRouter.api().record_together_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          mWalkResponse.setMember_gender(mMemberGender);
          mWalkResponse.setMember_age(mMemberAge);
          mWalkResponse.setAnimal_gender(mAnimalGender);
          mWalkResponse.setAnimal_neuter(mAnimalNeuter);
          mWalkResponse.setAnimal_character(mAnimalChar);
          mWalkResponse.setData_array(mWalkResponse.getData_array());
          Intent withFriendActivity = WithFriendsActivity.getStartIntent(mActivity,"", mWalkResponse);
          startActivity(withFriendActivity,TRANS.PUSH);
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
   * 견종필터
   */
  @OnClick(R.id.search_pet)
  public void searchPetTouched() {
    Intent searchPetActivity = SearchPetActivity.getStartIntent(mActivity);
    startActivity(searchPetActivity, TRANS.PUSH);
  }

  /**
   * 사람 성별
   */
  @OnClick({R.id.filter_person_male_button, R.id.filter_person_female_button, R.id.filter_person_gender_no_problem})
  public void personGenderTouched(RoundRectView button) {
    for (RoundRectView value : mFilterPersonGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mFilterPersonMaleButton.isSelected()) {
      mWalkResponse.setMember_gender("0");
    } else if (mFilterPersonFemaleButton.isSelected()) {
      mWalkResponse.setMember_gender("1");
    } else if (mFilterGenderNoProblemButton.isSelected()) {
      mWalkResponse.setMember_gender("");
    }
  }

  /**
   * 사람 나이
   */
  @OnClick({R.id.filter_age_button1, R.id.filter_age_button2, R.id.filter_age_button3, R.id.filter_age_button4, R.id.filter_age_button5})
  public void personAgeTouched(RoundRectView button) {
    for (RoundRectView value : mFilterAgeButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mFilterAgeButton1.isSelected()) {
      mWalkResponse.setMember_age("20");
    } else if (mFilterAgeButton2.isSelected()) {
      mWalkResponse.setMember_age("30");
    } else if (mFilterAgeButton3.isSelected()) {
      mWalkResponse.setMember_age("40");
    } else if (mFilterAgeButton4.isSelected()) {
      mWalkResponse.setMember_age("50");
    } else if (mFilterAgeButton5.isSelected()) {
      mWalkResponse.setMember_age("");
    }
  }

  /**
   * 성별
   */
  @OnClick({R.id.filter_male_button, R.id.filter_female_button, R.id.filter_gender_no_problem})
  public void genderTouched(RoundRectView button) {
    for (RoundRectView value : mFilterGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mFilterMaleButton.isSelected()) {
      mWalkResponse.setAnimal_gender("0");
    } else if (mFilterFemaleButton.isSelected()) {
      mWalkResponse.setAnimal_gender("1");
    } else if (mFilterGenderNoProblemButton.isSelected()) {
      mWalkResponse.setAnimal_gender("");
    }

  }

  /**
   * 중성화
   */
  @OnClick({R.id.filter_trans_button, R.id.filter_trans_no_button, R.id.filter_trans_no_problem})
  public void transTouched(RoundRectView button) {
    for (RoundRectView value : mFilterTransButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mFilterTransButton.isSelected()) {
      mWalkResponse.setAnimal_neuter("Y");
    } else if (mFilterTransNoButton.isSelected()) {
      mWalkResponse.setAnimal_neuter("N");
    } else if (mFilterTransNoProblem.isSelected()) {
      mWalkResponse.setAnimal_neuter("");
    }

  }

  /**
   * 성격
   */
  @OnClick({R.id.filter_char1_button, R.id.filter_char2_button, R.id.filter_char3_button, R.id.filter_char4_button, R.id.filter_char5_button})
  public void charTouched(RoundRectView button) {
    for (RoundRectView value : mFilterCharButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    if (mFilterChar1Button.isSelected()) {
      mWalkResponse.setAnimal_character("0");
    } else if (mFilterChar2Button.isSelected()) {
      mWalkResponse.setAnimal_character("1");
    } else if (mFilterChar3Button.isSelected()) {
      mWalkResponse.setAnimal_character("2");
    } else if (mFilterChar4Button.isSelected()) {
      mWalkResponse.setAnimal_character("3");
    } else if (mFilterChar5Button.isSelected()) {
      mWalkResponse.setAnimal_character("");
    }

  }

  /**
   * 필터
   */
  @OnClick(R.id.filter_button)
  public void filterTouched() {
    recordTogetherListAPI();
  }

  /**
   * 새로고침
   */
  @OnClick(R.id.refresh_button)
  public void refreshTouched() {
//    Intent filterActivity = WalkFilterActivity.getStartIntent(mActivity,);
//    startActivity(filterActivity);
//    finishWithRemove();
  }
}
