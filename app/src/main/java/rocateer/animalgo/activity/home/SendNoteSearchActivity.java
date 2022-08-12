package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

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
import rocateer.animalgo.models.api.MemoModel;
import timber.log.Timber;

public class SendNoteSearchActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SendNoteSearchActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.note_male_button)
  RoundRectView mNoteMaleButton;
  @BindView(R.id.note_person_female_button)
  RoundRectView mNoteFeMaleButton;
  @BindView(R.id.note_person_gender_no_problem)
  RoundRectView mNoteGenderNoProblem;
  @BindView(R.id.note_age_button1)
  RoundRectView mNoteAgeButton1;
  @BindView(R.id.note_age_button2)
  RoundRectView mNoteAgeButton2;
  @BindView(R.id.note_age_button3)
  RoundRectView mNoteAgeButton3;
  @BindView(R.id.note_age_button4)
  RoundRectView mNoteAgeButton4;
  @BindView(R.id.note_age_button5)
  RoundRectView mNoteAgeButton5;
  @BindView(R.id.search_edit_text)
  AppCompatEditText mSearchEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ArrayList<RoundRectView> mNoteGenderButtonList = new ArrayList<>();
  private ArrayList<RoundRectView> mNoteAgeButtonList = new ArrayList<>();
  private MemoModel mMemoResponse = new MemoModel();
  private String mMemberGender;
  private String mMemberAge;


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_send_search_note;
  }

  @Override
  protected void initLayout() {
    initToolbar("쪽지 보내기");

    mNoteGenderButtonList = new ArrayList<>(Arrays.asList(mNoteMaleButton, mNoteFeMaleButton, mNoteGenderNoProblem));
    mNoteAgeButtonList = new ArrayList<>(Arrays.asList(mNoteAgeButton1, mNoteAgeButton2, mNoteAgeButton3, mNoteAgeButton4, mNoteAgeButton5));

    if (mNoteMaleButton.isSelected()) {
      mMemberGender = "0";
    } else if (mNoteFeMaleButton.isSelected()) {
      mMemberGender = "1";
    } else if (mNoteGenderNoProblem.isSelected()) {
      mMemberGender = "";
    }

    if (mNoteAgeButton1.isSelected()) {
      mMemberAge = "20";
    } else if (mNoteAgeButton2.isSelected()) {
      mMemberAge = "30";
    } else if (mNoteAgeButton3.isSelected()) {
      mMemberAge = "40";
    } else if (mNoteAgeButton4.isSelected()) {
      mMemberAge = "50";
    } else if (mNoteAgeButton5.isSelected()) {
      mMemberAge ="";
    }

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 회원 리스트 API
   */
  public void memberListAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setMember_nickname(mSearchEditText.getText().toString());
    memoRequest.setMember_age(mMemberAge);
    memoRequest.setMember_gender(mMemberGender);
    memoRequest.setPage_num(memoRequest.getNextPage());

    CommonRouter.api().member_list(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemoModel mMemoResponse = new MemoModel();
          mMemoResponse.setMember_nickname(mSearchEditText.getText().toString());
          mMemoResponse.setMember_age(mMemberAge);
          mMemoResponse.setMember_gender(mMemberGender);
          mMemoResponse.setPage_num(mMemoResponse.getNextPage());
          mMemoResponse.setData_array(mMemoResponse.getData_array());
          Intent sendNoteActivity = SendNoteActivity.getStartIntent(mActivity, mMemoResponse);
          startActivity(sendNoteActivity, TRANS.PUSH);
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<MemoModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 유저 검색
   */
  @OnClick(R.id.user_search_button)
  public void userSearchButton() {
    memberListAPI();
  }

  /**
   * 검색 버튼
   */
  @OnClick(R.id.search_button)
  public void searchTouched() {
    memberListAPI();
  }

  /**
   * 성별
   */
  @OnClick({R.id.note_male_button, R.id.note_person_female_button, R.id.note_person_gender_no_problem})
  public void noteGenderTouched(RoundRectView button) {
    for (RoundRectView value : mNoteGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);
    Timber.i("member_gender :" + mMemberGender );
    if (mNoteMaleButton.isSelected()) {
      mMemberGender = "0";
    } else if (mNoteFeMaleButton.isSelected()) {
      mMemberGender = "1";
    } else if (mNoteGenderNoProblem.isSelected()) {
      mMemberGender = "";
    }

  }
  /**
   * 나이
   */
  @OnClick({R.id.note_age_button1, R.id.note_age_button2, R.id.note_age_button3, R.id.note_age_button4, R.id.note_age_button5})
  public void noteAgeTouched(RoundRectView button) {
    for (RoundRectView value : mNoteAgeButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

    Timber.i(mMemberAge);

    if (mNoteAgeButton1.isSelected()) {
      mMemberAge = "20";
    } else if (mNoteAgeButton2.isSelected()) {
      mMemberAge = "30";
    } else if (mNoteAgeButton3.isSelected()) {
      mMemberAge = "40";
    } else if (mNoteAgeButton4.isSelected()) {
      mMemberAge = "50";
    } else if (mNoteAgeButton5.isSelected()) {
      mMemberAge = "";
    }
  }
}

