package rocateer.animalgo.activity.history;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.MyPetAdapter;
import rocateer.animalgo.activity.walk.PetViewPagerAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BannerListModel;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class HistoryWithFriendDetailActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordDiaryIDX) {
    Intent intent = new Intent(context, HistoryWithFriendDetailActivity.class);
    mRecordDiaryIdx = recordDiaryIDX;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.with_friend_dialy_image_recycler_view)
  RecyclerView mDialyImageRecyclerView;
  @BindView(R.id.history_with_freind_list_recycler_view)
  RecyclerView mWithMyPetRecyclerView;
  @BindView(R.id.history_with_friend_map_view)
  MapView mHistoryWithMapMapView;
  @BindView(R.id.friend_nickname_text_view)
  AppCompatTextView mFriendNickNameTextView;
  @BindView(R.id.friend_age_text_view)
  AppCompatTextView mFriendAgeTextView;
  @BindView(R.id.friend_gender_text_view)
  AppCompatTextView mFriendGenderTextView;
  @BindView(R.id.friend_image_view)
  AppCompatImageView mFriendImageView;
  @BindView(R.id.review_star)
  AppCompatRatingBar mReviewStar;
  @BindView(R.id.review_star1)
  AppCompatRatingBar mReviewStar1;
  @BindView(R.id.review_star2)
  AppCompatRatingBar mReviewStar2;
  @BindView(R.id.review_star3)
  AppCompatRatingBar mReviewStar3;
  @BindView(R.id.total_distance_text_view)
  AppCompatTextView mTotlaDistanceTextView;
  @BindView(R.id.total_time_text_view)
  AppCompatTextView mTotalTimeTextView;
  @BindView(R.id.walk_date_text_view)
  AppCompatTextView mDateTextView;
  @BindView(R.id.with_walk_date_text_view)
  AppCompatTextView mWithWalkDateTextView;
  @BindView(R.id.walk_dialy_text_view)
  AppCompatTextView mDialyTextView;
  @BindView(R.id.walk_start_addr_text_view)
  AppCompatTextView mStartAddrTextView;
  @BindView(R.id.pet_view_pager)
  CardSliderViewPager mPetViewPager;
  @BindView(R.id.empty_photo)
  LinearLayout mEmptyPhoto;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private static String mRecordDiaryIdx;
  private WalkModel mWalkResponse = new WalkModel();
  private RequestOptions requestOptions = new RequestOptions();

  private ArrayList<WalkModel> mBannerList = new ArrayList<>();
  private HistoryPetBannerAdapter mHistoryPetBannerAdapter;

  private ArrayList<WalkModel> mMyPetList = new ArrayList<>();
  private HistoryMyPetAdapter mHistoryMyPetAdapter;

  private ArrayList<WalkModel> mDialyImageList = new ArrayList<>();
  private DialyImageAdapter mDialyImageAdapter;

  private List<LatLng> mLatLngList = new ArrayList<>();
  private ArrayList<Marker> mMarkers = new ArrayList<>();
  private ArrayList<PolylineOverlay> mPolyLineList = new ArrayList<>();


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_history_with_friend_detail;
  }

  @Override
  protected void initLayout() {

  }

  @Override
  protected void initRequest() {
    initDialyImageAdapter();
    initMyPetAdapter();
    initBannerAdapter();

    mHistoryWithMapMapView.getMapAsync(this);

  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {
    mNaverMap = naverMap;
    // 네이버 지도 Ready

    //Camera Move
    mNaverMap.moveCamera(CameraUpdate.zoomTo(14));
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

    recordDialyDetailAPI();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 이미지 리스트
   */

  private void initDialyImageAdapter() {

    mDialyImageAdapter = new DialyImageAdapter(R.layout.row_dialy_image_list, mDialyImageList);
    mDialyImageRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mDialyImageRecyclerView.setAdapter(mDialyImageAdapter);
  }

  /**
   * 반려견 리스트
   */

  private void initMyPetAdapter() {

    mHistoryMyPetAdapter = new HistoryMyPetAdapter(R.layout.row_my_pet, mMyPetList);
    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(mMyPetList.size(), -100);
    mWithMyPetRecyclerView.addItemDecoration(decoration_width);
    mWithMyPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mWithMyPetRecyclerView.setAdapter(mHistoryMyPetAdapter);
  }

  /**
   * 반려견 배너
   **/
  private void initBannerAdapter() {
    mHistoryPetBannerAdapter = new HistoryPetBannerAdapter(mActivity, mBannerList, mWalkResponse, mWalkResponse.getAnimal_img_path());
    mPetViewPager.setAdapter(mHistoryPetBannerAdapter);
    mPetViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
   * 산책기록 상세 API
   */
  private void recordDialyDetailAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_diary_idx(mRecordDiaryIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setRecord_type("1");

    CommonRouter.api().record_diary_detail(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          mFriendNickNameTextView.setText(mWalkResponse.getMember_nickname());
          if (mWalkResponse.getMember_age().equals("20")) {
            mFriendAgeTextView.setText("20대");
          } else if (mWalkResponse.getMember_age().equals("30")) {
            mFriendAgeTextView.setText("30대");
          } else if (mWalkResponse.getMember_age().equals("40")) {
            mFriendAgeTextView.setText("40대");
          } else if (mWalkResponse.getMember_age().equals("50")) {
            mFriendAgeTextView.setText("50대이상");
          }
          if (mWalkResponse.getMember_gender().equals("0")) {
            mFriendGenderTextView.setText("남성");
          } else if (mWalkResponse.getMember_gender().equals("1")) {
            mFriendGenderTextView.setText("여성");
          }
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mWalkResponse.getMember_img())
              .centerCrop()
              .placeholder(R.drawable.default_profile)
              .apply(requestOptions)
              .into(mFriendImageView);
          mReviewStar.setRating(Float.parseFloat(mWalkResponse.getReview_0()));
          mReviewStar1.setRating(Float.parseFloat(mWalkResponse.getReview_1()));
          mReviewStar2.setRating(Float.parseFloat(mWalkResponse.getReview_2()));
          mReviewStar3.setRating(Float.parseFloat(mWalkResponse.getReview_3()));
          mWithWalkDateTextView.setText(mWalkResponse.getRecord_date());
          mDateTextView.setText(mWalkResponse.getRecord_date());
          mStartAddrTextView.setText(mWalkResponse.getRecord_addr());
          mTotalTimeTextView.setText("총 시간 " + mWalkResponse.getRecord_hour() + "분");
          mTotlaDistanceTextView.setText("총 거리 " + mWalkResponse.getRecord_distant() + "km");
          mDialyTextView.setText(mWalkResponse.getMemo());
          mBannerList.addAll(mWalkResponse.getPartner_animal_array());
          mHistoryPetBannerAdapter.notifyDataSetChanged();
          if (mWalkResponse.getRecord_img_paths() != null) {
            mDialyImageRecyclerView.setVisibility(View.VISIBLE);
            mEmptyPhoto.setVisibility(View.GONE);
            String[] imageList = mWalkResponse.getRecord_img_paths().split(",");
            for (String imageUrl : imageList) {
              WalkModel walkModel = new WalkModel();
              walkModel.setRecord_img_path(imageUrl);
              mDialyImageList.add(walkModel);
            }
            mDialyImageAdapter.setNewData(mDialyImageList);
          } else {
            mDialyImageRecyclerView.setVisibility(View.GONE);
            mEmptyPhoto.setVisibility(View.VISIBLE);
          }
          if (mWalkResponse.getMember_animal_array() != null) {
            mMyPetList.addAll(mWalkResponse.getMember_animal_array());
          }
          mHistoryMyPetAdapter.setNewData(mMyPetList);
          String[] coordinatesArr = mWalkResponse.getCoordinates().split("\\|");
          for (String coordinates : coordinatesArr) {
            String[] coordinateData = coordinates.split(",");
            mLatLngList.add(new LatLng(Double.parseDouble(coordinateData[0]), Double.parseDouble(coordinateData[1])));
          }

          setPolyLine(mNaverMap, mLatLngList);

          /**
           * //move camera//
           */
          if (mLatLngList.size() > 0) {
            setMarker1(
                mNaverMap,
                mLatLngList.get(0).latitude,
                mLatLngList.get(0).longitude,
                R.drawable.i_location_s2,
                0,
                "0"
            );
            setMarker1(
                mNaverMap,
                mLatLngList.get(mLatLngList.size() - 1).latitude,
                mLatLngList.get(mLatLngList.size() - 1).longitude,
                R.drawable.i_location_s1,
                0,
                "0"
            );


            LatLngBounds mMapPosition = LatLngBounds.from(mLatLngList);
            mNaverMap.moveCamera(CameraUpdate.fitBounds(mMapPosition, 100, 150, 100, 150));
          }

        }
      }


      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * setPolyLine
   * Reference - https://navermaps.github.io/android-map-sdk/guide-ko/5-2.html
   *
   * @author khh
   * @since 6/25/21
   **/
  private void setPolyLine(@NonNull NaverMap naverMap, List<LatLng> mLatLngList) {
    Timber.i("setPolyLine ===- %s", mLatLngList.toArray());
    try {
      PolylineOverlay polyline = new PolylineOverlay();
      polyline.setCoords(mLatLngList);
      polyline.setWidth(20);
      polyline.setColor(getColor(R.color.colorAccent));
      polyline.setMap(naverMap);
      polyline.setCapType(PolylineOverlay.LineCap.Round);
      mPolyLineList.add(polyline);
    } catch (Exception e) {
      Timber.e("Exception === " + e);
    }
  }

  /**
   * set marker
   *
   * @author khh
   * @since 6/25/21
   **/
  private void setMarker1(@NonNull NaverMap naverMap,
                          Double latitude,
                          Double longitude,
                          int image,
                          int position,
                          String type) {
    Timber.i("=======================setMarker=======================");
    Marker marker = new Marker();
    marker.setPosition(new LatLng(latitude, longitude));
    //원금감 표시
    marker.setIconPerspectiveEnabled(true);
    //아이콘 지정
    marker.setIcon(OverlayImage.fromResource(image));
    //마커 표시 위치 오프 (왼쪽 위가 (0, 0), 오른쪽 아래가 (1, 1)인 비율로 표현)
//    marker.setAnchor(new PointF(0.5f, 0.5f));
    marker.setMap(naverMap);

    mMarkers.add(marker);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

}