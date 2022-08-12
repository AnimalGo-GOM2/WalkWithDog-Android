package rocateer.animalgo.activity.walk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Member;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import rocateer.animalgo.activity.intro.StartPopupDialog;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TrackingService;
import rocateer.animalgo.dialog.BottomDialogPetList;
import rocateer.animalgo.dialog.WalkBottomDialog;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.LocModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.naver.NaverModel;
import rocateer.animalgo.models.naver.NaverRouter;
import timber.log.Timber;

public class WalkTrackingActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordIdx, WalkModel walkModel) {
    Intent intent = new Intent(context, WalkTrackingActivity.class);
    mRecordIdx = recordIdx;
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------'
  @BindView(R.id.walk_tracking_map_view)
  MapView mWalkTrackingMapView;
  @BindView(R.id.walk_time_text_view)
  AppCompatTextView mWalkTimeTextView;
  @BindView(R.id.walk_start_text_view)
  AppCompatTextView mWalkStartTextView;
  @BindView(R.id.walk_distant_text_view)
  AppCompatTextView mWalkDistantTextView;
  @BindView(R.id.walk_location_text_view)
  AppCompatTextView mWalkLocationTextView;
  @BindView(R.id.user_nickname_text_view)
  AppCompatTextView mUserNickNameTextView;
  @BindView(R.id.user_gender_text_view)
  AppCompatTextView mUserGenderTextView;
  @BindView(R.id.user_age_text_view)
  AppCompatTextView mUserAgeTextView;
  @BindView(R.id.user_image_view)
  AppCompatImageView mUserImageView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private BottomDialogPetList mBottomDialogPetList;
  private ArrayList<AnimalModel> mPetList = new ArrayList<>();
  private Marker mMarker;
  private PathOverlay mFilteredPath;
  private static WalkModel mWalkModel;
  private String mRecordType = Prefs.getString(Constants.RECORD_TYPE, "");
  private RequestOptions requestOptions = new RequestOptions();
  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도
  long mNow;
  Date mDate;
  SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat mTime = new SimpleDateFormat("HH:mm");
  private long mDiffTime;


  private final Handler mHandler = new Handler();
  private Timer mTimer;

  private static String mRecordIdx;
  private Timer mNotificationTimer;
  private TimerTask mNotificationTimerTask;

  Intent mTrackingService;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_tracking;
  }

  @Override
  protected void initLayout() {

    mTrackingService = new Intent(this, TrackingService.class);
    startService(mTrackingService);
    mWalkTrackingMapView.getMapAsync(this);
    RocateerApplication.rcState = RocateerApplication.RC_STATE.RECORDING;
    Date date = new Date();
    mDate = date;

  }


  @Override
  protected void initRequest() {
    memberInfoDetail();

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

    setCurrentLocation();

    startTick();

    mNaverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
      @Override
      public void onCameraIdle() {
        mLatitude = mNaverMap.getCameraPosition().target.latitude;
        mLongitude = mNaverMap.getCameraPosition().target.longitude;
      }
    });
  }

  @Override
  protected void onDestroy() {

    if (mTrackingService != null) {
      stopService(mTrackingService);
    }
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
  }

  /**
   * 시간 계산
   *
   * @return
   */
  private void setNotificationTimer() {
    String start = Prefs.getString(Constants.START_DATE, "");
    Date startDate = new Date();
    try {
      startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date nowDate = new Date();
    mDiffTime = (nowDate.getTime() - startDate.getTime()) / 1000;
    int minutes = (int) (mDiffTime / 60);
    if (minutes >= 90) {
      mTimer.cancel();
      stopService(mTrackingService);


      showAlertDialog("최대 산책기록 시간을 초과하여 산책일기 쓰기 화면으로 이동합니다.", "확인", new DialogEventListener() {
        @Override
        public void onReceivedEvent() {
          if (RocateerApplication.recordingList.size() > 1) {

            showProgressBar();
            String mResultLocations = "";
            ObjectMapper mapper = new ObjectMapper();
            try {
              String distance = String.format("%.1f", calDistance(RocateerApplication.recordingList));
              ArrayList<LocModel> locationModel = new ArrayList<>();
              for (Location value : RocateerApplication.recordingList) {
                locationModel.add(new LocModel(String.valueOf(value.getLatitude()), String.valueOf(value.getLongitude())));
              }
              mResultLocations = mapper.writeValueAsString(locationModel);

              String minute = String.valueOf((mDiffTime / 60));

              trackingRegInAPI(distance, minute, mResultLocations);


            } catch (JsonProcessingException e) {
              Timber.e("JsonProcessingException == " + e);
            }
          }
        }
      });
    }
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 현재 위치로 지도 이동
   */
  public void setCurrentLocation() {
    SmartLocation smartLocation = SmartLocation.with(this);
    LocationManagerProvider mProvider = new LocationManagerProvider();
    smartLocation
        .location(mProvider)
        .config(LocationParams.NAVIGATION)
        .oneFix()
        .start(new OnLocationUpdatedListener() {
          @Override
          public void onLocationUpdated(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 15);
            cameraUpdate.animate(CameraAnimation.Fly, 1000);
            mNaverMap.moveCamera(cameraUpdate);


            reverseGeoCodeAPI((latLng.longitude) + "," + (latLng.latitude));

            if (mMarker != null) {
              mMarker.setMap(null);
            }

            mMarker = new Marker();
            mMarker.setIcon(OverlayImage.fromResource(R.drawable.i_location));  //set image
            mMarker.setPosition(latLng);
            mMarker.setMap(mNaverMap);

          }
        });
  }


  /**
   * Naver Geo Code API
   */
  private void reverseGeoCodeAPI(String latLng) {
    NaverRouter.api().map_geo_code("ljauqq6zev", "oQk3XLH3KG5Ld37ZPYiy1QISb8cOf4MfPZEJTVQs", "coordsToaddr", latLng, "epsg:4326", "json", "addr")
        .enqueue(new Callback<NaverModel>() {
          @Override
          public void onResponse(Call<NaverModel> call, Response<NaverModel> response) {
            if (response.code() == 200) {
              NaverModel naverResponse = response.body();
              try {
                Timber.i(naverResponse.getResults().get(0).getRegion().getArea1().getName());

                String area1 = naverResponse.getResults().get(0).getRegion().getArea1().getName();
                String area2 = naverResponse.getResults().get(0).getRegion().getArea2().getName();
                String area3 = naverResponse.getResults().get(0).getRegion().getArea3().getName();

                mWalkLocationTextView.setText(area1 + " " + area2 + " " + area3);
                Prefs.putString(Constants.RECORDING_ADDRESS, area1 + " " + area2 + " " + area3);
              } catch (Exception e) {
                Timber.e(e.getLocalizedMessage());
                mWalkLocationTextView.setText("");
              }
            }
          }

          @Override
          public void onFailure(Call<NaverModel> call, Throwable t) {
            Timber.e(t.getLocalizedMessage());
          }
        });

  }


  /**
   * 시작
   */
  private void startTick() {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = format.parse(Prefs.getString(Constants.START_DATE,""));
      SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      mWalkStartTextView.setText(transFormat.format(date));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    mTimer = new Timer(false);
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            setNotificationTimer();
            ArrayList<Location> recordingList = RocateerApplication.recordingList;
            if (recordingList.size() > 1) {
              Location lastLocation = recordingList.get(recordingList.size() - 1);
              // 경로 세팅
              if (recordingList.size() > 2) {
                if (mFilteredPath != null) {
                  mFilteredPath.setMap(null);
                }

                ArrayList<LatLng> locationList = new ArrayList<>();
                for (Location value : recordingList) {
                  locationList.add(new LatLng(value.getLatitude(), value.getLongitude()));
                }
                mFilteredPath = new PathOverlay();
                mFilteredPath.setWidth(20);
                mFilteredPath.setColor(mActivity.getColor(R.color.colorAccent));
                mFilteredPath.setCoords(locationList);
                mFilteredPath.setMap(mNaverMap);
              }


              // 마커 세팅
              if (mMarker != null) {
                mMarker.setMap(null);
              }


              mMarker = new Marker();
              LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
              mMarker.setIcon(OverlayImage.fromResource(R.drawable.i_location));  //set image
              mMarker.setPosition(latLng);
              mMarker.setMap(mNaverMap);

              CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 15);
              cameraUpdate.animate(CameraAnimation.Fly, 1000);
              if (mNaverMap != null) {
                mNaverMap.moveCamera(cameraUpdate);
              }

            }
            mWalkTimeTextView.setText(secTotime());
            mWalkDistantTextView.setText(String.format("%.1f", calDistance(RocateerApplication.recordingList)) + "km");
          }
        });
      }
    };
    mTimer.schedule(timerTask, 0, 1000);
  }

  private String secTotime() {
    String start = Prefs.getString(Constants.START_DATE, "");
    // 시작 시간
    Date startDate = new Date();
    try {
      startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date nowDate = new Date();

    mDiffTime = (nowDate.getTime() - startDate.getTime()) / 1000;
    int hour = (int) (mDiffTime / 3600);
    int minutes = (int) ((mDiffTime % 3600) / 60);
    int seconds = (int) ((mDiffTime % 3600) % 60);

    return String.format("%02d:%02d:%02d", hour, minutes, seconds);
  }

  /**
   * 전체 거리 측정
   */
  private double calDistance(ArrayList<Location> locationList) {
    float distance = 0;
    for (int i = 1; i < locationList.size(); i++) {
      distance += locationList.get(i).distanceTo(locationList.get(i - 1));
    }
    return distance / 1000;
  }

  /**
   * 트래킹정보 저장
   */
  private void trackingRegInAPI(String distant, String recordHour, String location) {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mRecordIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setRecord_distant(distant);
    walkRequest.setRecord_hour(recordHour);
    walkRequest.setLocation(location);
    walkRequest.setRecord_start_date(Prefs.getString(Constants.START_DATE,""));

    CommonRouter.api().tracking_reg_in(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          RocateerApplication.recordingList = new ArrayList<>();
          RocateerApplication.recordSeconds = 0;
          RocateerApplication.recordDistance = 0.0;
          Prefs.remove(Constants.RECORD_TYPE);
          Prefs.remove(Constants.REOCRD_IDX);
          Prefs.remove(Constants.START_DATE);
          Prefs.remove(Constants.PARTNER_MEMBER_IDX);
          Intent withFriendDialyActivity = WithFriendDialyActivity.getStartIntent(mActivity, mRecordIdx, mWalkResponse.getRecord_diary_idx(), distant, recordHour);
          startActivity(withFriendDialyActivity, TRANS.PUSH);
          finishWithRemove();

        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 회원 정보 상세 보기
   */
  private void memberInfoDetail() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.PARTNER_MEMBER_IDX, ""));
    CommonRouter.api().member_info_detail(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemberModel mMemberResponse = response.body();
          if (mMemberResponse != null) {
            mUserAgeTextView.setText(mMemberResponse.getMember_age() + "대");
            if (mMemberResponse.getMember_gender().equals("0")) {
              mUserGenderTextView.setText("남성");
            } else if (mMemberResponse.getMember_gender().equals("1")) {
              mUserGenderTextView.setText("여성");
            }
            mUserNickNameTextView.setText(mMemberResponse.getMember_nickname());
            Glide.with(mActivity)
                .load(BaseRouter.BASE_URL + mMemberResponse.getMember_img())
                .placeholder(R.drawable.default_profile)
                .centerCrop()
                .apply(requestOptions)
                .into(mUserImageView);

          }
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

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
          RocateerApplication.recordingList = new ArrayList<>();
          RocateerApplication.recordSeconds = 0;
          RocateerApplication.recordDistance = 0.0;
          Prefs.remove(Constants.RECORD_TYPE);
          Prefs.remove(Constants.REOCRD_IDX);
          Prefs.remove(Constants.START_DATE);
          Prefs.remove(Constants.PARTNER_MEMBER_IDX);
          removeAllActivity();
          Intent mainActivity = MainActivity.getStartIntent(mActivity);
          startActivity(mainActivity, TRANS.PRESENT);
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
   * 보호자의 반려견
   */
  @OnClick(R.id.pet_list_layout)
  public void petListTouched() {
    BottomDialogPetList bottomDialogPetList = new BottomDialogPetList(mActivity, mPetList, mRecordIdx);
    bottomDialogPetList.show(getSupportFragmentManager(), "WalkTrackingActivity");

  }

  /**
   * 산책일기
   */
  @OnClick(R.id.walk_dialy_button)
  public void walkDialyTouched() {
    showConfirmDialog("산책을 종료하고 일기를 작성하시겠습니까?", "아니요", "예", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        if (RocateerApplication.recordingList.size() > 1) {

          showProgressBar();
          String mResultLocations = "";
          ObjectMapper mapper = new ObjectMapper();
          try {
            String distance = String.format("%.1f", calDistance(RocateerApplication.recordingList));
            ArrayList<LocModel> locationModel = new ArrayList<>();
            for (Location value : RocateerApplication.recordingList) {
              locationModel.add(new LocModel(String.valueOf(value.getLatitude()), String.valueOf(value.getLongitude())));
            }
            mResultLocations = mapper.writeValueAsString(locationModel);

            String minute = String.valueOf((mDiffTime % 3600) / 60);

            trackingRegInAPI(distance, minute, mResultLocations);
          } catch (JsonProcessingException e) {
            Timber.e("JsonProcessingException == " + e);
          }

        }

      }
    });
  }

  /**
   * 산책취소
   */
  @OnClick(R.id.tracking_dot_button)
  public void trackingDotTouched() {
    WalkBottomDialog walkBottomDialog = new WalkBottomDialog(mActivity, new WalkBottomDialog.WalkBottomDialogListener() {
      @Override
      public void onCancel() {
        registeredRecordCancelAPI();
      }
    });
    walkBottomDialog.show(getSupportFragmentManager(), "ApplyFriendActivity");
  }
}
