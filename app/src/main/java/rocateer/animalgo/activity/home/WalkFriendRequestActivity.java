package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.walk.BottomDialogReportBlock;
import rocateer.animalgo.activity.walk.PetViewPagerAdapter;
import rocateer.animalgo.dialog.BottomDialogApplyWalkFriend;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BannerListModel;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;

public class WalkFriendRequestActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, WalkFriendRequestActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.map_view)
  MapView mMapView;
  @BindView(R.id.dot_button)
  ImageView mDotButton;
  @BindView(R.id.pet_view_pager)
  CardSliderViewPager mPetViewPager;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private ArrayList<WalkModel> mBannerList = new ArrayList<>();
  private PetViewPagerAdapter mPetViewPagerAdapter;
  private ArrayList<WalkModel> mPetModel = new ArrayList<>();
  private WalkModel mWalkResponse = new WalkModel();
  private AnimalModel mAnimalResponse = new AnimalModel();
  private RecyclerView mRecyclerView;
  private AppCompatEditText mAppCompatEditText;
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_friend_request_detail;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 찾기");
    mDotButton.setVisibility(View.GONE);
    mMapView.getMapAsync(this);

  }

  @Override
  protected void initRequest() {
    initBannerAdapter();

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
    uiSettings.setAllGesturesEnabled(false);

    Marker mMarker;
    mMarker = new Marker();
    mMarker.setIcon(OverlayImage.fromResource(R.drawable.i_location));  //set image
    mMarker.setPosition(naverMap.getCameraPosition().target);
    mMarker.setMap(mNaverMap);
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 반려견 배너
   **/
  private void initBannerAdapter() {
    mPetViewPagerAdapter = new PetViewPagerAdapter(mActivity, mBannerList, mWalkResponse, mWalkResponse.getAnimal_img_path());
    mPetViewPager.setAdapter(mPetViewPagerAdapter);
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

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 산책친구 지원
   */
  @OnClick(R.id.walk_friend_apply_button)
  public void walkFriendApplyTouched() {
    BottomDialogApplyWalkFriend bottomDialogApplyWalkFriend = new BottomDialogApplyWalkFriend(mActivity, mWalkResponse, mAnimalResponse, new BottomDialogApplyWalkFriend.ApplyRefresh() {
      @Override
      public void onRefresh() {

      }
    });
    bottomDialogApplyWalkFriend.show(getSupportFragmentManager(),"");
  }

  /**
   * 신고 차단
   */
  @OnClick(R.id.friend_bottom_popup)
  public void friendBottomPopupTouched() {
    BottomDialogReportBlock bottomDialogReportBlock = new BottomDialogReportBlock(mActivity, mWalkResponse.getPartner_member_idx());
    bottomDialogReportBlock.show(getSupportFragmentManager(), "WalkFriednRequstActivity");
  }

}

