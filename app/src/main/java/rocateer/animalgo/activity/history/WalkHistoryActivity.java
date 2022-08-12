package rocateer.animalgo.activity.history;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationManagerProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WalkHistoryActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordDiaryIdx) {
    Intent intent = new Intent(context, WalkHistoryActivity.class);
    mRecordDiaryIdx = recordDiaryIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.dialy_image_recycler_view)
  RecyclerView mDialyImageRecyclerView;
  @BindView(R.id.with_pet_list_recycler_view)
  RecyclerView mWithMyPetRecyclerView;
  @BindView(R.id.history_map_view)
  MapView mHistoryMapView;
  @BindView(R.id.with_pet_walk_date_text_view)
  AppCompatTextView mWithPetWalkDateTextView;
  @BindView(R.id.walk_start_addr_text_view)
  AppCompatTextView mWalkStartAddrTextView;
  @BindView(R.id.total_time_text_view)
  AppCompatTextView mTotalTimeTextView;
  @BindView(R.id.total_distance_text_view)
  AppCompatTextView mTotalDistanceTextView;
  @BindView(R.id.memo_text_view)
  AppCompatTextView mMemoTextView;
  @BindView(R.id.empty_photo)
  LinearLayout mEmptyPhoto;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ArrayList<WalkModel> mDialyImageList = new ArrayList<>();
  private DialyImageAdapter mDialyImageAdapter;
  private HistoryMyPetAdapter mMyPetAdapter;
  private NaverMap mNaverMap;
  private static String mRecordDiaryIdx;
  private ArrayList<PolylineOverlay> mPolyLineList = new ArrayList<>();
  private List<LatLng> mLatLngList = new ArrayList<>();
  private ArrayList<WalkModel> mMyPetList = new ArrayList<>();
  private WalkModel mWalkResponse = new WalkModel();
  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도
  private DateFormat mDateFormat;
  private ArrayList<Marker> mMarkers = new ArrayList<>();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_history;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책기록");

    mHistoryMapView.getMapAsync(this);


  }

  @Override
  protected void initRequest() {
    initDialyImageAdapter();
    initMyPetAdapter();

  }



  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  private void initDialyImageAdapter() {

    mDialyImageAdapter = new DialyImageAdapter(R.layout.row_dialy_image_list, mDialyImageList);
    mDialyImageRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mDialyImageRecyclerView.setAdapter(mDialyImageAdapter);
  }

  private void initMyPetAdapter() {

    mMyPetAdapter = new HistoryMyPetAdapter(R.layout.row_my_pet, mMyPetList);
    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(mMyPetList.size(), -100);
    mWithMyPetRecyclerView.addItemDecoration(decoration_width);
    mWithMyPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mWithMyPetRecyclerView.setAdapter(mMyPetAdapter);
  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {
    mNaverMap = naverMap;
    // 네이버 지도 Ready

//    //Camera Move
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

    recordDialyDetailAPI();
  }


  /**
   * 산책기록 상세 API
   */
  private void recordDialyDetailAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_diary_idx(mRecordDiaryIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setRecord_type("0");

    CommonRouter.api().record_diary_detail(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          mTotalTimeTextView.setText("총 시간 " +mWalkResponse.getRecord_hour() + "분");
          mTotalDistanceTextView.setText("총 거리" + mWalkResponse.getRecord_distant() + "km");
          DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
          try {
            Date date = format.parse(mWalkResponse.getRecord_start_date());
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            mWithPetWalkDateTextView.setText(transFormat.format(date));
          } catch (ParseException e) {
            e.printStackTrace();
          }
//          mWithPetWalkDateTextView.setText(getTime());
          mWalkStartAddrTextView.setText(mWalkResponse.getRecord_addr());
          mMemoTextView.setText(mWalkResponse.getMemo());
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
          mMyPetAdapter.setNewData(mMyPetList);

          String[] coordinatesArr = mWalkResponse.getCoordinates().split("\\|");

          for (String coordinates: coordinatesArr) {
            String[] coordinateData = coordinates.split(",");
            mLatLngList.add(new LatLng(Double.parseDouble(coordinateData[0]), Double.parseDouble(coordinateData[1])));
          }

          setPolyLine(mNaverMap, mLatLngList);


          /**
           * //move camera//
           */
          if (mLatLngList.size() > 0 ) {
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
            mNaverMap.moveCamera(CameraUpdate.fitBounds(mMapPosition, 100, 200, 100, 200));
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

