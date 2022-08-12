package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

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
import rocateer.animalgo.dialog.WalkBottomDialog;
import rocateer.animalgo.models.BannerListModel;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;

public class ApplyWalkFriendDetailActivity extends RocateerActivity implements OnMapReadyCallback {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, BaseModel baseModel) {
    Intent intent = new Intent(context, ApplyWalkFriendDetailActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.apply_map_view)
  MapView mMapView;
  @BindView(R.id.pet_view_pager)
  ViewPager mPetViewPager;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NaverMap mNaverMap;
  private List<BannerListModel> mBannerList = new ArrayList<>();
  private PetViewPagerAdapter mPetViewPagerAdapter;
  private WalkModel mWalkResponse = new WalkModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_apply_walk_friend;
  }

  @Override
  protected void initLayout() {
    initToolbar("지원한 산책친구");
    mMapView.getMapAsync(this);
    mPetViewPager.setOffscreenPageLimit(3);
  }

  @Override
  protected void initRequest() {
//    initBannerAdapter();

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

//  /**
//   * 반려견 배너
//   **/
//  private void initBannerAdapter() {
//    mBannerList.add(new BannerListModel());
//    mBannerList.add(new BannerListModel());
//    mBannerList.add(new BannerListModel());
//    mBannerList.add(new BannerListModel());
//
//    ArrayList<BannerModel> imageList = new ArrayList<>();
//    imageList.add(new BannerModel());
//    imageList.add(new BannerModel());
//    imageList.add(new BannerModel());
//    imageList.add(new BannerModel());
//    mPetViewPagerAdapter = new PetViewPagerAdapter(imageList, mActivity);
//    mPetViewPager.setAdapter(mPetViewPagerAdapter);
//    mPetViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//      @Override
//      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//      }
//
//      @Override
//      public void onPageSelected(int position) {
//
//      }
//
//      @Override
//      public void onPageScrollStateChanged(int state) {
//
//      }
//    });
//  }

    //--------------------------------------------------------------------------------------------
    // MARK : Bind Actions
    //--------------------------------------------------------------------------------------------

    /**
     * 신고 차단
     */
    @OnClick(R.id.friend_bottom_popup)
    public void friendBottomPopupTouched () {
      BottomDialogReportBlock bottomDialogReportBlock = new BottomDialogReportBlock(mActivity, mWalkResponse.getPartner_member_idx());
      bottomDialogReportBlock.show(getSupportFragmentManager(), "WalkFriednRequstActivity");
    }

    /**
     * 대화
     */
    @OnClick(R.id.apply_friend_chat_button)
    public void applyFriendChatTouched () {
//      Intent walkChatActivity = WalkChatActivity.getStartIntent(mActivity, mWalkResponse.getChatting_room_idx(), );
//      startActivity(walkChatActivity, TRANS.PUSH);
    }

    /**
     * 산책취소
     */
    @OnClick(R.id.dot_button)
    public void dotTouched () {
//      WalkBottomDialog walkBottomDialog = new WalkBottomDialog(mActivity, );
//      walkBottomDialog.show(getSupportFragmentManager(), "WalkFriednRequstActivity");
    }
  }

