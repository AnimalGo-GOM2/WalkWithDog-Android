package rocateer.animalgo.activity.walk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.GpsTracker;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.naver.NaverModel;
import rocateer.animalgo.models.naver.NaverRouter;
import timber.log.Timber;

public class SelectLocationActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, WalkModel walkModel) {
    Intent intent = new Intent(context, SelectLocationActivity.class);
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.back_button)
  AppCompatImageView mBackButton;
  @BindView(R.id.select_location_map_view)
  MapView mSelectLocationMapView;
  @BindView(R.id.location_text_view)
  AppCompatTextView mLocationTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private static WalkModel mWalkModel;
  String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
  private double mLatitude = 0; // ??????
  private double mLongitude = 0; //??????
  private Marker mMarker;
  private GpsTracker gpsTracker;
  private String mRegion;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_select_location;
  }

  @Override
  protected void initLayout() {
    mBackButton.setVisibility(View.GONE);
    initToolbar("???????????? ??????");
    permissionCheck();

  }

  @Override
  protected void initRequest() {
    mSelectLocationMapView.getMapAsync(this);

  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {
    mNaverMap = naverMap;
    // ????????? ?????? Ready

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
    //????????? degree
    uiSettings.setRotateGesturesEnabled(false);
    //Naver Logo Click
    uiSettings.setLogoClickEnabled(false);
    // ?????????
    uiSettings.setAllGesturesEnabled(true);

//    Marker mMarker;
//    mMarker = new Marker();
//    mMarker.setIcon(OverlayImage.fromResource(R.drawable.i_location));  //set image
//    mMarker.setPosition(naverMap.getCameraPosition().target);
//    mMarker.setMap(mNaverMap);

    String memberRegion = Prefs.getString(Constants.MEMBER_REGION, "");
    if (!memberRegion.equals("")) {
      mLatitude = Prefs.getDouble(Constants.MEMBER_LAT, 37.56638274705945); // ??????
      mLongitude = Prefs.getDouble(Constants.MEMBER_LNG, 126.97794372508841); //??????
      try {
        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(mLatitude, mLongitude), 15);
        cameraUpdate.animate(CameraAnimation.Fly, 1000);
        mNaverMap.moveCamera(cameraUpdate);
      } catch (Exception e) {
        Timber.e(e.getLocalizedMessage());
      }
    } else {
      currentLocation();
    }
    mNaverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
      @Override
      public void onCameraIdle() {
        mLatitude = mNaverMap.getCameraPosition().target.latitude;
        mLongitude = mNaverMap.getCameraPosition().target.longitude;
        reverseGeoCodeAPI((mLongitude) + "," + (mLatitude));
      }
    });


  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * ?????? ??????
   *
   * @return
   */
  @RequiresApi(api = Build.VERSION_CODES.Q)
  private boolean permissionCheck() {

    // ?????? ??????
    int cameraPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
    int readExternalPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
    int writeExternalPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    int accessFineLocationPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
    int accessCoarseLocationPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
//    int accessBackgroundLocationPermissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);

    if (cameraPermissionCheck == PackageManager.PERMISSION_DENIED
        || readExternalPermissionCheck == PackageManager.PERMISSION_DENIED
        || writeExternalPermissionCheck == PackageManager.PERMISSION_DENIED
        || accessFineLocationPermissionCheck == PackageManager.PERMISSION_DENIED
        || accessCoarseLocationPermissionCheck == PackageManager.PERMISSION_DENIED
    ) {
      //|| accessBackgroundLocationPermissionCheck == PackageManager.PERMISSION_DENIED
      // ?????? ??????
      PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
          currentLocation();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
        }
      };

      TedPermission.with(mActivity)
          .setPermissionListener(permissionlistener)
          .setRationaleMessage(R.string.common_auth)
          .setDeniedMessage(R.string.common_auth_denined)
          .setPermissions(Manifest.permission.CAMERA,
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION)
          // Manifest.permission.ACCESS_BACKGROUND_LOCATION
          .check();
    } else {
      currentLocation();
      return true;
    }


    return false;
  }

  /**
   * ?????? ?????? ??????
   */
  private void currentLocation() {
    LocationManagerProvider mProvider = new LocationManagerProvider();
    SmartLocation.with(mActivity)
        .location(mProvider)
        .config(LocationParams.BEST_EFFORT)
        .oneFix()
        .start(new OnLocationUpdatedListener() {
          @Override
          public void onLocationUpdated(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mLatitude = location.getLatitude(); // ??????
            mLongitude = location.getLongitude(); //??????
            Timber.i("latitude == " + mLatitude + "  /  longitude == " + mLongitude);

            try {
              CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(mLatitude, mLongitude), 15);
              cameraUpdate.animate(CameraAnimation.Fly, 1000);
              mNaverMap.moveCamera(cameraUpdate);
            } catch (Exception e) {
              Timber.e("Exception == " + e);
            }
            if (mMarker != null) {
              mMarker.setMap(null);
            }

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

                mRegion = area1 + " " + area2 + " " + area3;
                mLocationTextView.setText(area1 + " " + area2 + " " + area3);
              } catch (Exception e) {
                Timber.e(e.getLocalizedMessage());
                mLocationTextView.setText("");
              }
            }
          }

          @Override
          public void onFailure(Call<NaverModel> call, Throwable t) {
            Timber.e(t.getLocalizedMessage());
          }
        });

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------


  /**
   * ?????? ????????? ??????
   */
  @OnClick(R.id.move_my_location_image_view)
  public void moveLocation() {
    currentLocation();
  }
  /**
   * ?????? ??????
   */
  @OnClick(R.id.location_next_button)
  public void locationCompleteTouched() {
    if (mLatitude != 0 && mLongitude != 0 && mRegion != null) {
      Prefs.putDouble(Constants.MEMBER_LAT, mLatitude);
      Prefs.putDouble(Constants.MEMBER_LNG, mLongitude);
      Prefs.putString(Constants.MEMBER_REGION, mRegion);
      mWalkModel.setRecord_addr(mRegion);
      mWalkModel.setRecord_lat(mLatitude + "");
      mWalkModel.setRecord_lng(mLongitude + "");
      Intent selectFriendActivty = SelectFriendPetActivity.getStartIntent(mActivity, mWalkModel);
      startActivity(selectFriendActivty, TRANS.PRESENT);
      finishWithRemove();
    } else {
      showAlertDialog("?????? ????????? ?????? ??????????????????.","??????",null);
    }
    Timber.i("latitude == " + mLatitude + "  /  longitude == " + mLongitude + "/ region ==" + mRegion );
  }

  /**
   * ??????
   */
  @OnClick(R.id.walk_cancle_button)
  public void walkCancleTouched() {
    finishWithRemove();
  }

}
