package rocateer.animalgo.activity.home;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.pixplicity.easyprefs.library.Prefs;
import com.thedeanda.lorem.LoremIpsum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import android.app.NotificationManager;
import co.lujun.androidtagview.TagContainerLayout;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.activity.commons.alarm.AlarmActivity;
import rocateer.animalgo.activity.mypet.AddMyPetActivity;
import rocateer.animalgo.activity.mypet.EditMyPetActivity;
import rocateer.animalgo.activity.walk.WalkFriendDetailActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.SpacingItemDecoration;
import rocateer.animalgo.commons.SwipeViewPager;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class HomeFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.home_recycler_view)
  RecyclerView mHomeRecyclerView;
  @BindView(R.id.my_pet_recycler_view)
  RecyclerView mMyPetRecyclerView;
  @BindView(R.id.compliment_recycler_view)
  RecyclerView mComplimentRecyclerView;
  @BindView(R.id.user_image_view)
  AppCompatImageView mUserImageView;
  @BindView(R.id.user_age_text_view)
  AppCompatTextView mUserAgeTextView;
  @BindView(R.id.user_gender_text_view)
  AppCompatTextView mUserGenderTextView;
  @BindView(R.id.user_name_text_view)
  AppCompatTextView mUserNameTextView;
  @BindView(R.id.record_date_text_view)
  AppCompatTextView mRecordDateTextView;
  @BindView(R.id.distant_text_view)
  AppCompatTextView mDistantTextView;
  @BindView(R.id.animal_age_text_view)
  AppCompatTextView mAnimalAgeTextView;
  @BindView(R.id.animal_image_view)
  AppCompatImageView mAnimalImageView;
  @BindView(R.id.animal_category_text_view)
  AppCompatTextView mAnimalCategoryTextView;
  @BindView(R.id.animal_gender_text_view)
  AppCompatTextView mAnimalGenderTextView;
  @BindView(R.id.animal_name_text_view)
  AppCompatTextView mAnimalNameTextView;
  @BindView(R.id.member_nickname_text_view)
  AppCompatTextView mMemberNickNameTextView;
  @BindView(R.id.home_banner_layout)
  RoundRectView mHomeBannerLayout;
  @BindView(R.id.home_banner_view_pager)
  CardSliderViewPager mHomeBannerViewPager;
  @BindView(R.id.new_alarm_count_text_view)
  AppCompatTextView mNewAlarmCountTextView;
  @BindView(R.id.new_memo_count_text_view)
  AppCompatTextView mNewMemoCountTextView;
  @BindView(R.id.train_text_View)
  AppCompatTextView mTrainTextView;
  @BindView(R.id.pet_character_text_view)
  AppCompatTextView mPetCharcaterTextView;
  @BindView(R.id.pet_neuter_text_view)
  AppCompatTextView mNeuterTextView;
  @BindView(R.id.animalgo_walk_king_layout)
  RelativeLayout mAnimalgoWalkKingLayout;
  @BindView(R.id.start_date_text_view)
  AppCompatTextView mStartDateTextView;
  @BindView(R.id.end_date_text_view)
  AppCompatTextView mEndDateTextView;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private HomeListAdapter mHomeListAdapter;
  private ArrayList<MainModel> mHomeList = new ArrayList<>();

  private MyPetAdapter mMyPetAdapter;
  private ArrayList<MainModel> mMyPetList = new ArrayList<>();

  private ComplimentAdapter mComplimentAdapter;
  private ArrayList<MainModel> mComplimentList = new ArrayList<>();

  private HomeBannerAdapter mHomeBannerAdapter;
  private ArrayList<MainModel> mHomeBannerList = new ArrayList<>();

  private RequestOptions requestOptions = new RequestOptions();
  private MainModel mMainResponse = new MainModel();
  private WalkModel mWalkResponse = new WalkModel();

  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도


  private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      newAlarmCountAPI();
    }
  };

  private BroadcastReceiver mMainReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mMainResponse.resetPage();
      mHomeBannerList.clear();
      mMyPetList.clear();
      mComplimentList.clear();
      mHomeList.clear();
      mainAPI(String.valueOf(mLatitude), String.valueOf(mLongitude));
    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_home;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mAlarmReceiver, new IntentFilter(Constants.AlARM_REFRESH));
    mActivity.registerReceiver(mMainReceiver, new IntentFilter(Constants.MAIN_REFRESH));


    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mMainResponse.resetPage();
        mHomeBannerList.clear();
        mMyPetList.clear();
        mComplimentList.clear();
        mHomeList.clear();
        mainAPI(String.valueOf(mLatitude), String.valueOf(mLongitude));
      }
    });

    mSkeletonLayout.showSkeleton();
  }

  @Override
  protected void initRequest() {
    initHomeAdapter();
    initMyPetAdapter();
    initComplimentAdapter();
    getMyLocation();
    initHomeBannerAdapter();
    newAlarmCountAPI();


  }


  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
    if (menuVisible) {
      newAlarmCountAPI();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    newAlarmCountAPI();
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 현재 내 위치 받아오기
   */
  private void getMyLocation() {
    LocationManagerProvider mProvider = new LocationManagerProvider();
    SmartLocation.with(mActivity)
        .location(mProvider)
        .config(LocationParams.BEST_EFFORT)
        .oneFix()
        .start(new OnLocationUpdatedListener() {
          @Override
          public void onLocationUpdated(Location location) {
            mLatitude = location.getLatitude(); // 위도
            mLongitude = location.getLongitude(); //경도
            Timber.i("latitude == " + mLatitude + "  /  longitude == " + mLongitude);
            mainAPI(String.valueOf(mLatitude), String.valueOf(mLongitude));

          }
        });


  }

  /**
   * 홈 메인 API
   *
   * @param lat - 위도
   * @param lng - 경도
   */
  private void mainAPI(String lat, String lng) {
    MainModel mainRequest = new MainModel();
    mainRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    mainRequest.setMember_lat(lat);
    mainRequest.setMember_lng(lng);

    CommonRouter.api().main(Tools.getInstance().getMapper(mainRequest)).enqueue(new Callback<MainModel>() {
      @Override
      public void onResponse(Call<MainModel> call, Response<MainModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMainResponse = response.body();
          mMemberNickNameTextView.setText(mMainResponse.getMember_nickname() + "님!");

          MainModel recommendObj = mMainResponse.getRecommend_obj();

          mUserNameTextView.setText(recommendObj.getMember_nickname());
          mDistantTextView.setText(recommendObj.getDistant() + "m");
          requestOptions.centerCrop();
          if (recommendObj.getMember_img() != null ){
            Glide.with(mActivity)
                .load(BaseRouter.BASE_URL + recommendObj.getMember_img())
                .apply(requestOptions)
                .placeholder(R.drawable.default_profile)
                .into(mUserImageView);
          }
          mRecordDateTextView.setText(recommendObj.getRecord_date());
          mUserAgeTextView.setText(recommendObj.getMember_age() + "대");


//         mAnimalCntTextView.setText(" 외 " + mMainResponse.getRecommend_obj().getMember_animal_idxs().length()  + "마리");
          String[] animalIdx = recommendObj.getMember_animal_idxs().split(",");
          int animalCnt = animalIdx.length;
          Timber.i("animalCnt" + animalCnt);
//          mAnimalNameTextView.setText(mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_name());
          if (animalCnt > 1) {
            mAnimalNameTextView.setText(mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_name() + " 외" + (animalCnt - 1) + "마리");
          } else {
            mAnimalNameTextView.setText(mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_name());
          }

          // 오늘의 추천 산채 반려견
          mAnimalAgeTextView.setText(recommendObj.getAnimal_obj().getAnimal_year());

          if (recommendObj.getAnimal_obj().getAnimal_character().equals("0")) {
            mPetCharcaterTextView.setText("#온순");
          } else if (recommendObj.getAnimal_obj().getAnimal_character().equals("1")) {
            mPetCharcaterTextView.setText("#입질");
          } else if (recommendObj.getAnimal_obj().getAnimal_character().equals("2")) {
            mPetCharcaterTextView.setText("#호기심많음");
          } else if (recommendObj.getAnimal_obj().getAnimal_character().equals("3")) {
            mPetCharcaterTextView.setText("#활동적");
          }

          if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_training().equals("N")) {
            mTrainTextView.setText("#훈련 안 했어요");
          } else if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_training().equals("Y")) {
            mTrainTextView.setText("#훈련 했어요");
          }

          if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_neuter().equals("N")) {
            mNeuterTextView.setText("#중성화 안 했어요");
          } else if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_neuter().equals("Y")) {
            mNeuterTextView.setText("#중성화 했어요");
          }
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_img_path())
              .apply(requestOptions)
              .placeholder(R.drawable.default_dog2)
              .into(mAnimalImageView);


          mAnimalCategoryTextView.setText(mMainResponse.getRecommend_obj().getAnimal_obj().getCategory_name());
          if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_gender().equals("0")) {
            mAnimalGenderTextView.setText("남아");
          } else if (mMainResponse.getRecommend_obj().getAnimal_obj().getAnimal_gender().equals("1")) {
            mAnimalGenderTextView.setText("여아");
          }

          // 칭찬해요
          if (mMainResponse.getCompliment_array() != null) {
            mComplimentList.addAll(mMainResponse.getCompliment_array());
          }
          mComplimentAdapter.setNewData(mComplimentList);

          // 내 반려견
          if (mMainResponse.getMy_animal_array() != null) {
            mMyPetList.addAll(mMainResponse.getMy_animal_array());
          }
          mMyPetAdapter.setNewData(mMyPetList);

          // 배너
          if (mMainResponse.getBanner_array().size() == 0) {
            mHomeBannerLayout.setVisibility(View.GONE);
          } else {
            mHomeBannerLayout.setVisibility(View.VISIBLE);
            mHomeBannerList.addAll(mMainResponse.getBanner_array());
          }
          mHomeBannerAdapter.notifyDataSetChanged();

          DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
          try {
            Date date = format.parse(mMainResponse.getRecord_king_start_date());
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            mStartDateTextView.setText(transFormat.format(date));
          } catch (ParseException e) {
            e.printStackTrace();
          }
          try {
            Date date = format.parse(mMainResponse.getRecord_king_end_date());
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            mEndDateTextView.setText(transFormat.format(date));
          } catch (ParseException e) {
            e.printStackTrace();
          }

          // 애니멀고 산책왕
          if (mMainResponse.getRank_array().size() > 0) {
            mHomeRecyclerView.setVisibility(View.VISIBLE);
            mAnimalgoWalkKingLayout.setVisibility(View.VISIBLE);
            mHomeList.addAll(mMainResponse.getRank_array());
          } else {
            mAnimalgoWalkKingLayout.setVisibility(View.GONE);
            mHomeRecyclerView.setVisibility(View.GONE);
          }
          mHomeListAdapter.setNewData(mHomeList);


          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              mSkeletonLayout.showOriginal();
            }
          }, 100);


        }
      }

      @Override
      public void onFailure(Call<MainModel> call, Throwable t) {

      }
    });
  }

  /**
   * 애니멀고 산책왕 리스트
   */

  private void initHomeAdapter() {

    mHomeListAdapter = new HomeListAdapter(R.layout.row_home_walk_king, mHomeList);
    mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mHomeRecyclerView.setAdapter(mHomeListAdapter);
  }

  /**
   * 칭찬해요 리스트
   */
  private void initComplimentAdapter() {

    mComplimentAdapter = new ComplimentAdapter(R.layout.row_compliment, mComplimentList);
    mComplimentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mComplimentRecyclerView.setAdapter(mComplimentAdapter);
  }

  /**
   * 광고 배너 목록
   */
  private void initHomeBannerAdapter() {
    mHomeBannerAdapter = new HomeBannerAdapter(mActivity, mHomeBannerList);
    mHomeBannerViewPager.setAdapter(mHomeBannerAdapter);
    mHomeBannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
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
  private void initMyPetAdapter() {

    mMyPetAdapter = new MyPetAdapter(R.layout.row_my_pet, mMyPetList);
    mMyPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent editMyPetActivity = EditMyPetActivity.getStartIntent(mActivity, mMyPetList.get(position).getAnimal_idx(), new EditMyPetActivity.MyPetListener() {
          @Override
          public void onRefresh() {
            mMyPetList.clear();
            mMainResponse.resetPage();
            mainAPI(String.valueOf(mLatitude), String.valueOf(mLongitude));
          }
        });
        startActivity(editMyPetActivity, RocateerActivity.TRANS.PRESENT);
      }
    });
    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(mMyPetList.size(), -100);
    mMyPetRecyclerView.addItemDecoration(decoration_width);
    mMyPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mMyPetRecyclerView.setAdapter(mMyPetAdapter);
  }

  /**
   * 안읽은 알림 수
   */
  private void newAlarmCountAPI() {
    AlarmModel alarmRequest = new AlarmModel();
    alarmRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().new_alarm_count(Tools.getInstance().getMapper(alarmRequest)).enqueue(new Callback<AlarmModel>() {
      @Override
      public void onResponse(Call<AlarmModel> call, Response<AlarmModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          AlarmModel mAlarmResponse = response.body();
          if (mAlarmResponse.getNew_alarm_conut().equals("0")) {
            mNewAlarmCountTextView.setVisibility(View.GONE);
          } else {
            mNewAlarmCountTextView.setVisibility(View.VISIBLE);
            mNewAlarmCountTextView.setText(mAlarmResponse.getNew_alarm_conut());
          }

          if (mAlarmResponse.getNew_memo_conut().equals("0")) {
            mNewMemoCountTextView.setVisibility(View.GONE);
          } else {
            mNewMemoCountTextView.setVisibility(View.VISIBLE);
            mNewMemoCountTextView.setText(mAlarmResponse.getNew_memo_conut());
          }


        }
      }

      @Override
      public void onFailure(Call<AlarmModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 산책 쪽지
   */
  @OnClick(R.id.message_button)
  public void messageTouched() {
    Intent walkNoteActivity = WalkNoteActivity.getStartIntent(mActivity);
    startActivity(walkNoteActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 추천 친구 보기
   */
  @OnClick(R.id.recommended_friend_layout)
  public void recommendedFriendTouched() {
    Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mMainResponse.getRecommend_obj().getRecord_idx(), mMainResponse.getMember_idx());
    startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 알림 버튼
   */
  @OnClick(R.id.alarm_button)
  public void alarmToucehd() {
    Intent alarmActivity = AlarmActivity.getStartIntent(mActivity);
    startActivity(alarmActivity, RocateerActivity.TRANS.PUSH);
  }

}
