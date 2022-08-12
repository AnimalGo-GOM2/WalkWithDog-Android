package rocateer.animalgo.activity.walk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.RocateerApplication;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.MyPetAdapter;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.BottomDialogApplyWalkFriend;
import rocateer.animalgo.dialog.WalkBottomDialog;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BannerListModel;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WalkFriendDetailActivity extends RocateerActivity implements OnMapReadyCallback {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordIdx, String memberIdx) {
    Intent intent = new Intent(context, WalkFriendDetailActivity.class);
    mRecordIdx = recordIdx;
    mMemberIdx = memberIdx;
//    mPosition = position;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.walk_friend_map_view)
  MapView mWalkFriendMapView;
  @BindView(R.id.pet_view_pager)
  CardSliderViewPager mPetViewPager;
  @BindView(R.id.pet_image_progressbar)
  ProgressBar mPetImageProgressBar;
  @BindView(R.id.walk_date_text_view)
  AppCompatTextView mWalkDateTextView;
  @BindView(R.id.walk_addr_text_view)
  AppCompatTextView mWalkAddrTextView;
  @BindView(R.id.talk_button)
  LinearLayout mTalkButton;
  @BindView(R.id.button_text_view)
  AppCompatTextView mButtonTextView;
  @BindView(R.id.user_profile_layout)
  RoundRectView mUserProfileLayout;
  @BindView(R.id.new_image_view)
  AppCompatImageView mNewIconView;
  @BindView(R.id.user_nickname_text_view)
  AppCompatTextView mUserNickNameTextView;
  @BindView(R.id.user_age_text_view)
  AppCompatTextView mUserAgeTextView;
  @BindView(R.id.user_gender_text_view)
  AppCompatTextView mUserGenderTextView;
  @BindView(R.id.user_image_view)
  AppCompatImageView mUserImageView;
  @BindView(R.id.dot_button)
  AppCompatImageView mDotButton;
  @BindView(R.id.my_pet_recycler_view)
  RecyclerView mMyPetRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private ArrayList<WalkModel> mPetList = new ArrayList<>();
  private PetViewPagerAdapter mPetViewPagerAdapter;
  private WalkModel mWalkResponse = new WalkModel();
  private static String mRecordIdx;
  private static String mMemberIdx;
  //  private static WalkDetailListener mWalkDetailListener;
  private AnimalModel mAnimalResponse = new AnimalModel();
  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도
  private static int mPosition;
  private RequestOptions requestOptions = new RequestOptions();
  private Marker mMarker;
  private ArrayList<WalkModel> mMyPetList = new ArrayList<>();
  private MyAnimalAdapter mMyAnimalAdapter;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_friend_detail;
  }

  @Override
  protected void initLayout() {
    initToolbar("등록한 산책친구 요청");
    mWalkFriendMapView.getMapAsync(this);

  }

  @Override
  protected void initRequest() {
    initBannerAdapter();
    initMyPetListAdapter();


  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {
    mNaverMap = naverMap;
    // 네이버 지도 Ready

    //Camera Move
//    mNaverMap.moveCamera(CameraUpdate.zoomTo(14));
    //Map Type
    mNaverMap.setMapType(NaverMap.MapType.Basic);

    UiSettings uiSettings = naverMap.getUiSettings();
    //zoom button
    uiSettings.setZoomControlEnabled(false);
    //scale bar
    uiSettings.setScaleBarEnabled(false);
    //map tilt
    uiSettings.setTiltGesturesEnabled(false);
    //베어링 degree
    uiSettings.setRotateGesturesEnabled(false);
    //Naver Logo Click
    uiSettings.setLogoClickEnabled(false);
    // 제스쳐
    uiSettings.setAllGesturesEnabled(true);

    registeredRecordDetailAPI();
  }

  @Override
  public void onResume(){
    super.onResume();
    if (mNaverMap != null) {
      registeredRecordDetailAPI();
      mPetList.clear();
      mMyPetList.clear();
    }
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------


  /**
   * 반려견 배너
   **/
  private void initBannerAdapter() {
    mPetViewPagerAdapter = new PetViewPagerAdapter(mActivity, mPetList, mWalkResponse, mWalkResponse.getAnimal_img_path());
    mPetViewPager.setAdapter(mPetViewPagerAdapter);
    mPetViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        int totalPage = mWalkResponse.getRecord_animal_array().size();
        double progress = ((double) position + 1) / (double) totalPage;
        int progressValue = Integer.parseInt(String.valueOf(Math.round(progress * 100)));
        mPetImageProgressBar.setProgress(progressValue);
      }

      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
      }
    });
  }

  /**
   * 내 반려견 리스트
   */
  private void initMyPetListAdapter() {
    mMyAnimalAdapter = new MyAnimalAdapter(R.layout.row_my_pet, mMyPetList);
    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(mMyPetList.size(), -100);
    mMyPetRecyclerView.addItemDecoration(decoration_width);
    mMyPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mMyPetRecyclerView.setAdapter(mMyAnimalAdapter);
  }


  /**
   * 산책친구 등록 상세 API
   */
  private void registeredRecordDetailAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mRecordIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().registered_record_detail(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkResponse = response.body();
          mWalkDateTextView.setText(mWalkResponse.getRecord_date());
          mWalkAddrTextView.setText(mWalkResponse.getRecord_addr());
          mPetList.addAll(mWalkResponse.getRecord_animal_array());
          mPetViewPagerAdapter.setNewData(mPetList);

          try {
            mNaverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(Double.parseDouble(mWalkResponse.getRecord_lat()), Double.parseDouble(mWalkResponse.getRecord_lng()))));
          } catch (Exception e) {
            mNaverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(37.566680, 126.978406)));
          }

          Marker marker = new Marker();
          marker.setPosition(new LatLng(Double.parseDouble(mWalkResponse.getRecord_lat()), Double.parseDouble(mWalkResponse.getRecord_lng())));
          //원금감 표시
          marker.setIconPerspectiveEnabled(true);
          //아이콘 지정
          marker.setIcon(OverlayImage.fromResource(R.drawable.i_location_s2));
          marker.setMap(mNaverMap);

          /**
           * 등록한 산책친구 요청
           */

          if (mWalkResponse.getMember_idx().equals(Prefs.getString(Constants.MEMBER_IDX, ""))) {
            mUserProfileLayout.setVisibility(View.GONE);
            mButtonTextView.setText("대화");
            mToolbarTitle.setText("등록한 산책친구 요청");
              if (mWalkResponse.getNew_chat_cnt().equals("0")) {
                mNewIconView.setVisibility(View.GONE);
              } else {
                mNewIconView.setVisibility(View.VISIBLE);
              }
            mMyPetRecyclerView.setVisibility(View.GONE);
            if (mWalkResponse.getChat_active_yn().equals("Y")) {
              mButtonTextView.setText("대화");
              mTalkButton.setEnabled(true);
              mTalkButton.setBackgroundColor(getColor(R.color.colorAccent));
            } else if (mWalkResponse.getChat_active_yn().equals("N")) {
              mButtonTextView.setText("대화");
              mNewIconView.setVisibility(View.GONE);
              mTalkButton.setEnabled(false);
              mTalkButton.setBackgroundColor(getColor(R.color.color_EAE8E5));
            }
          } else if (mMemberIdx != Prefs.getString(Constants.MEMBER_IDX, "")) {
            mUserProfileLayout.setVisibility(View.VISIBLE);
            mButtonTextView.setText("산책친구 지원");
            mMyPetRecyclerView.setVisibility(View.GONE);
            mTalkButton.setBackgroundColor(getColor(R.color.colorAccent));
            mToolbarTitle.setText("산책친구 찾기");
            mNewIconView.setVisibility(View.GONE);
            if (mWalkResponse.getApply_yn().equals("Y")) {
              mToolbarTitle.setText("지원한 산책친구");
              mTalkButton.setBackgroundColor(getColor(R.color.colorAccent));
              mButtonTextView.setText("대화");
              mMyPetList.addAll(mWalkResponse.getMy_animal_array());
              mMyAnimalAdapter.setNewData(mMyPetList);
              mMyPetRecyclerView.setVisibility(View.VISIBLE);
              mUserProfileLayout.setVisibility(View.VISIBLE);
            } else if (mWalkResponse.getApply_yn().equals("N")) {
              mUserProfileLayout.setVisibility(View.VISIBLE);
              mDotButton.setVisibility(View.GONE);
              mButtonTextView.setText("산책친구 지원");
              mTalkButton.setBackgroundColor(getColor(R.color.colorAccent));
              mToolbarTitle.setText("산책친구 찾기");
              mNewIconView.setVisibility(View.GONE);
            }
          }

          if (mUserProfileLayout.getVisibility() == View.VISIBLE) {
            mUserNickNameTextView.setText(mWalkResponse.getMember_nickname());
            mUserAgeTextView.setText(mWalkResponse.getMember_age() + "대");
            if (mWalkResponse.getMember_gender().equals("0")) {
              mUserGenderTextView.setText("남성");
            } else if (mWalkResponse.getMember_gender().equals("1")) {
              mUserGenderTextView.setText("여성");
            }
            Glide.with(mActivity)
                .load(BaseRouter.BASE_URL + mWalkResponse.getMember_img())
                .apply(requestOptions)
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(mUserImageView);
          }
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 산책 등록 취소
   */
  private void registeredRecordCancelAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mRecordIdx);

    CommonRouter.api().registered_record_cancel(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
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
   * dot 버튼
   */
  @OnClick(R.id.dot_button)
  public void dotTouched() {
    WalkBottomDialog walkBottomDialog = new WalkBottomDialog(mActivity, new WalkBottomDialog.WalkBottomDialogListener() {
      @Override
      public void onCancel() {
        registeredRecordCancelAPI();
      }
    });
    walkBottomDialog.show(getSupportFragmentManager(), "");
  }

  /**
   * 대화 버튼
   */
  @OnClick(R.id.talk_button)
  public void talkTouched() {
    if (mWalkResponse.getMember_idx().equals(Prefs.getString(Constants.MEMBER_IDX, ""))) {
      Intent applyFriendActivity = ApplyFriendActivity.getStartIntent(mActivity, mRecordIdx);
      startActivity(applyFriendActivity, TRANS.PUSH);

    } else if (Prefs.getString(Constants.MEMBER_IDX, "") != mMemberIdx) {
      if (mWalkResponse.getApply_yn().equals("Y")) {
        Intent walkChatAcitivity = WalkChatActivity.getStartIntent(mActivity, mWalkResponse.getChatting_room_idx());
        startActivity(walkChatAcitivity, TRANS.PUSH);
      } else if (mWalkResponse.getApply_yn().equals("N")) {
        BottomDialogApplyWalkFriend bottomDialogApplyWalkFriend = new BottomDialogApplyWalkFriend(mActivity, mWalkResponse, mAnimalResponse, new BottomDialogApplyWalkFriend.ApplyRefresh() {
          @Override
          public void onRefresh() {
            registeredRecordDetailAPI();
          }
        });
        bottomDialogApplyWalkFriend.show(getSupportFragmentManager(), "");

      }
    }
  }

  /**
   * 신고 차단
   */
  @OnClick(R.id.friend_bottom_popup)
  public void friendBottomPopupTouched() {
    BottomDialogReportBlock bottomDialogReportBlock = new BottomDialogReportBlock(mActivity, mWalkResponse.getMember_idx());
    bottomDialogReportBlock.show(getSupportFragmentManager(), "WalkFriednRequstActivity");
  }
}

