package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.walk.BottomDialogReportBlock;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;

public class ReceivedNoteActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String memoIdx) {
    Intent intent = new Intent(context, ReceivedNoteActivity.class);
    mMemoIdx = memoIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.memo_user_age_text_view)
  AppCompatTextView mMemoUserAgeTextView;
  @BindView(R.id.memo_user_contents_text_view)
  AppCompatTextView mMemoUserContentstTextView;
  @BindView(R.id.memo_user_date_text_view)
  AppCompatTextView mMemeDateTextView;
  @BindView(R.id.memo_user_name_text_view)
  AppCompatTextView mMemoUserNameTextView;
  @BindView(R.id.memo_user_image_view)
  AppCompatImageView mMemoUserImageView;
  @BindView(R.id.memo_user_gender_text_view)
  AppCompatTextView mMemoUserGenderTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemoModel mMemoResponse = new MemoModel();
  private static String mMemoIdx;
  private RequestOptions requestOptions = new RequestOptions();
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_received_note;
  }

  @Override
  protected void initLayout() {
    initToolbar("받은 쪽지");

    memoDetailAPI();
  }

  @Override
  protected void initRequest() {

  }





  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 쪽지 상세
   */
  public void memoDetailAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setMemo_idx(mMemoIdx);

    CommonRouter.api().memo_detail(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemoResponse = response.body();
          mMemoUserContentstTextView.setText(mMemoResponse.getContents());
          mMemoUserNameTextView.setText(mMemoResponse.getMember_nickname());
          mMemeDateTextView.setText(mMemoResponse.getIns_date());
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMemoResponse.getMember_img())
              .placeholder(R.drawable.default_profile)
              .into(mMemoUserImageView);
          if (mMemoResponse.getMember_gender().equals("0")) {
            mMemoUserGenderTextView.setText("남성");
          } else if (mMemoResponse.getMember_gender().equals("1")) {
            mMemoUserGenderTextView.setText("여성");
          }
          if (mMemoResponse.getMember_age().equals("20")) {
            mMemoUserAgeTextView.setText("20대");
          } else if (mMemoResponse.getMember_age().equals("30")) {
            mMemoUserAgeTextView.setText("30대");
          } else if (mMemoResponse.getMember_age().equals("40")) {
            mMemoUserAgeTextView.setText("40대");
          } else if (mMemoResponse.getMember_age().equals("50")) {
            mMemoUserAgeTextView.setText("50대이상");
          }
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
   * 쪽지 보내기
   */
  @OnClick(R.id.input_note_button)
  public void inPutNoteTouched() {
    Intent inputNoteActivity = InPutNoteActivity.getStartIntent(mActivity, mMemoResponse.getMember_idx());
    startActivity(inputNoteActivity, TRANS.PUSH);
  }

  /**
   * 신고 차단
   */
  @OnClick(R.id.dot_button)
  public void dotTouched() {
    BottomDialogReportBlock bottomDialogReportBlock = new BottomDialogReportBlock(mActivity, mMemoResponse.getMember_idx());
    bottomDialogReportBlock.show(getSupportFragmentManager(), "ReceivedNoteActivity");
  }

}

