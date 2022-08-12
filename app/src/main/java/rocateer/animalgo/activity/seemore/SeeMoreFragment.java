package rocateer.animalgo.activity.seemore;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.database.core.view.Change;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.activity.history.HistoryWithFriendAdapter;
import rocateer.animalgo.activity.history.HistoryWithFriendDetailActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;

public class SeeMoreFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.walk_ready_star_text_view)
  AppCompatTextView mWalkReadyStarTextView;
  @BindView(R.id.walk_text_view)
  AppCompatTextView mWalkTextView;
  @BindView(R.id.time_star_text_view)
  AppCompatTextView mTimeStarTextView;
  @BindView(R.id.friendly_text_view)
  AppCompatTextView mFriendlytextView;
  @BindView(R.id.total_record_cnt_text_view)
  AppCompatTextView mTotalRecordCntTextView;
  @BindView(R.id.total_record_distant_text_view)
  AppCompatTextView mTotalRecordDistantTextView;
  @BindView(R.id.member_nickname_text_view)
  AppCompatTextView mMemberNickNameTextView;
  @BindView(R.id.mypage_user_image_view)
  AppCompatImageView mMyPageUserImageView;
  @BindView(R.id.app_link_recycler_view)
  RecyclerView mAppLinkRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MemberModel mMemberResponse = new MemberModel();
  private RequestOptions requestOptions = new RequestOptions();
  private ArrayList<MemberModel> mAppLinkList = new ArrayList<>();
  private AppLinkAdapter mAppLinkAdapter;
  private MemberModel mMemberModel = new MemberModel();
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_see_more;
  }

  @Override
  protected void initLayout() {
    myPageDetailAPI();

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mMemberModel.resetPage();
        mAppLinkList.clear();
        linkListAPI();
      }
    });

  }

  @Override
  protected void initRequest() {
    initAppLinkAdapter();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 더보기 상세
   */
  public void myPageDetailAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().mypage_detail(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();
          requestOptions.centerCrop();
          requestOptions.placeholder(R.drawable.default_profile);
          Glide.with(mContext)
              .load(BaseRouter.BASE_URL + mMemberResponse.getMember_img())
              .apply(requestOptions).dontAnimate()
              .into(mMyPageUserImageView);
          mTotalRecordCntTextView.setText(mMemberResponse.getTotal_record_cnt() + "회");
          mTotalRecordDistantTextView.setText(mMemberResponse.getTotal_record_distant() + "km");
          mWalkReadyStarTextView.setText(mMemberResponse.getReview_0());
          if (mMemberResponse.getReview_0() == null) {
            mWalkReadyStarTextView.setText("0.0");
          } else {
            mWalkReadyStarTextView.setText(mMemberResponse.getReview_0());
          }
          if (mMemberResponse.getReview_1() == null) {
            mWalkTextView.setText("0.0");
          } else {
            mWalkTextView.setText(mMemberResponse.getReview_1());
          }
          if (mMemberResponse.getReview_2() == null) {
            mTimeStarTextView.setText("0.0");
          } else {
            mTimeStarTextView.setText(mMemberResponse.getReview_2());
          }
          if (mMemberResponse.getReview_3() == null) {
            mFriendlytextView.setText("0.0");
          } else {
            mFriendlytextView.setText(mMemberResponse.getReview_3());
          }
          mMemberNickNameTextView.setText(mMemberResponse.getMember_nickname());

        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  /**
   * 앱 바로가기 링크
   */
  private void initAppLinkAdapter() {
    mAppLinkAdapter = new AppLinkAdapter(R.layout.row_applink_list, mAppLinkList);
    mAppLinkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAppLinkList.get(position).getAndroid_url() != null) {
          Tools.getInstance(mActivity).openBrowser(mAppLinkList.get(position).getAndroid_url());
        }
      }
    });
    mAppLinkRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mAppLinkRecyclerView.setAdapter(mAppLinkAdapter);
    linkListAPI();
  }

  /**
   * 앱 링크 바로가기 API
   */
  private void linkListAPI() {
    MemberModel memberRequest = new MemberModel();
    CommonRouter.api().link_list(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberModel = response.body();
          mAppLinkList.addAll(mMemberModel.getData_array());
          mAppLinkAdapter.setNewData(mAppLinkList);
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 의견 보내기
   */
  @OnClick(R.id.send_feedback_layout)
  public void sendFeedbackLayoutTouched() {
    Intent sendFeedbackActivity = SendFeedbackActivity.getStartIntent(mActivity);
    startActivity(sendFeedbackActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 내 프로필
   */
  @OnClick(R.id.my_profile_view)
  public void myProfileTouched() {
    Intent myProfileActivity = MyProfileActivity.getStartIntent(mActivity);
    startActivity(myProfileActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 고객센터
   */
  @OnClick(R.id.service_center_layout)
  public void serviceCenterTouched0() {
    Intent serviceCenterActivity = ServiceCenterActivity.getStartIntent(mActivity);
    startActivity(serviceCenterActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 설정
   */
  @OnClick(R.id.setting_button)
  public void settingTouched() {
    Intent settingAcitivty = SettingActivity.getStartIntent(mActivity);
    startActivity(settingAcitivty, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 애니멀고 정보
   */
  @OnClick(R.id.animalgo_info_layout)
  public void annimalgoInfoTouched() {
    Intent animalgoInfoAcitivity = AnimalgoInfoActivity.getStartIntent(mActivity);
    startActivity(animalgoInfoAcitivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 차단 현황
   */
  @OnClick(R.id.block_manage_button)
  public void blockManageTouched() {
    Intent blockActivity = BlockActivity.getStartIntent(mActivity);
    startActivity(blockActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 계정관리
   */
  @OnClick(R.id.account_management_layout)
  public void accountManageMentTouched() {
    Intent changePwAcitivty = ChangePwActivity.getStartIntent(mActivity);
    startActivity(changePwAcitivty, RocateerActivity.TRANS.PUSH);
  }
}