package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Member;

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
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;

public class SentInBoxDetailActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String memoIdx) {
    Intent intent = new Intent(context, SentInBoxDetailActivity.class);
    mMemoIdx = memoIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.memo_user_image_view)
  AppCompatImageView mMemoUserImageView;
  @BindView(R.id.memo_age_text_view)
  AppCompatTextView mMemoAgeTextView;
  @BindView(R.id.memo_user_nickname_text_view)
  AppCompatTextView mMemoUserNickNameTextView;
  @BindView(R.id.memo_contents_text_view)
  AppCompatTextView mMemoContentsTextView;
  @BindView(R.id.memo_date_text_view)
  AppCompatTextView mMemoDateTextView;
  @BindView(R.id.memo_gender_text_view)
  AppCompatTextView mMemoGenderTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private static String mMemoIdx;
  private MemoModel mMemoResponse = new MemoModel();
  private RequestOptions requestOptions = new RequestOptions();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_sent_inbox_detail;
  }

  @Override
  protected void initLayout() {
    initToolbar("보낸 쪽지");

    memoDetailAPI();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   *  보낸쪽지 상세 API
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
          mMemoUserNickNameTextView.setText(mMemoResponse.getMember_nickname());
          mMemoDateTextView.setText(mMemoResponse.getIns_date());
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMemoResponse.getMember_img())
              .placeholder(R.drawable.default_profile)
              .into(mMemoUserImageView);
          mMemoContentsTextView.setText(mMemoResponse.getContents());
          if (mMemoResponse.getMember_gender().equals("0")) {
            mMemoGenderTextView.setText("남성");
          } else if (mMemoResponse.getMember_gender().equals("1")) {
            mMemoGenderTextView.setText("여성");
          }
          if (mMemoResponse.getMember_age().equals("20")) {
            mMemoAgeTextView.setText("20대");
          } else if (mMemoResponse.getMember_age().equals("30")) {
            mMemoAgeTextView.setText("30대");
          } else if (mMemoResponse.getMember_age().equals("40")) {
            mMemoAgeTextView.setText("40대");
          } else if (mMemoResponse.getMember_age().equals("50")) {
            mMemoAgeTextView.setText("50대이상");
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
  @OnClick(R.id.input_note_button)
  public void inPutNoteTouched() {
    Intent inputNoteActivity = InPutNoteActivity.getStartIntent(mActivity, mMemoResponse.getMember_idx());
    startActivity(inputNoteActivity,TRANS.PUSH);
  }
}


