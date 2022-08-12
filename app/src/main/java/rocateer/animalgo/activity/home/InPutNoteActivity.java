package rocateer.animalgo.activity.home;


import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatEditText;
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
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;

public class InPutNoteActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String partnerMemberIdx) {
    Intent intent = new Intent(context, InPutNoteActivity.class);
    mPartnerMemberIdx = partnerMemberIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.partner_gender_text_view)
  AppCompatTextView mParterGenderTextView;
  @BindView(R.id.partner_user_age_text_view)
  AppCompatTextView mPartnerUserAgeTextView;
  @BindView(R.id.partner_user_name_text_view)
  AppCompatTextView mParterUserNameTextView;
  @BindView(R.id.parter_user_image_view)
  AppCompatImageView mPartnerUserImageView;
  @BindView(R.id.note_contents_edit_text)
  AppCompatEditText mNoteContentsEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private static String mPartnerMemberIdx;
  private RequestOptions requestOptions = new RequestOptions();
  private MemoModel mMemoResponse = new MemoModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_in_put_note;
  }

  @Override
  protected void initLayout() {
    initToolbar("쪽지 보내기");
    memoRegViewAPI();

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 쪽지 보내기 폼 API
   */
  public void memoRegViewAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setPartner_member_idx(mPartnerMemberIdx);

    CommonRouter.api().memo_reg_view(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemoModel mMemoResponse = response.body();
          if (mMemoResponse.getMember_gender().equals("0")) {
            mParterGenderTextView.setText("남성");
          } else if (mMemoResponse.getMember_gender().equals("1")) {
            mParterGenderTextView.setText("여성");
          }
          if (mMemoResponse.getMember_age().equals("20")) {
            mPartnerUserAgeTextView.setText("20대");
          } else if (mMemoResponse.getMember_age().equals("30")) {
            mPartnerUserAgeTextView.setText("30대");
          } else if (mMemoResponse.getMember_age().equals("40")) {
            mPartnerUserAgeTextView.setText("40대");
          } else if (mMemoResponse.getMember_age().equals("50")) {
            mPartnerUserAgeTextView.setText("50대이상");
          }
          mParterUserNameTextView.setText(mMemoResponse.getMember_nickname());
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMemoResponse.getMember_img())
              .placeholder(R.drawable.default_profile)
              .into(mPartnerUserImageView);
        }
      }

      @Override
      public void onFailure(Call<MemoModel> call, Throwable t) {

      }
    });
  }

  /**
   * 쪽지 보내기 API
   */
  public void memoRegInAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setPartner_member_idx(mPartnerMemberIdx);
    memoRequest.setContents(mNoteContentsEditText.getText().toString());

    CommonRouter.api().memo_reg_in(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if(Tools.getInstance(mActivity).isSuccessResponse(response)) {
          Intent memoRefresh = new Intent(Constants.MEMO_REFRESH);
          mActivity.sendBroadcast(memoRefresh);
          finishMultiRemoveActivity(2);


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
  @OnClick(R.id.send_note_button)
  public void sendNoteTouched() {
    memoRegInAPI();
  }
}

