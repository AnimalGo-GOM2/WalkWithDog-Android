package rocateer.animalgo.activity.walk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
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
import rocateer.animalgo.activity.home.WalkFriendRequestActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.naver.NaverModel;
import rocateer.animalgo.models.naver.NaverRouter;
import timber.log.Timber;

public class WithFriendsActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String region, WalkModel walkModel) {
    Intent intent = new Intent(context, WithFriendsActivity.class);
    mRegion = region;
    mWalkModel = walkModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.with_friend_recycler_view)
  RecyclerView mWithFriendRecyclerView;
  @BindView(R.id.location_text_view)
  AppCompatTextView mLocationTextView;
  @BindView(R.id.setting_location_button)
  LinearLayout mSettingLocationButton;
  @BindView(R.id.default_layout)
  LinearLayout mDefaultLayout;
  @BindView(R.id.list_layout)
  RelativeLayout mListLayout;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private WithFriendAdapter mWithFriendAdapter;
  private WalkModel mWalkResponse = new WalkModel();
  private static String mRegion;
  private double mLatitude = 0; // 위도
  private static WalkModel mWalkModel;
  private double mLongitude = 0; //경도
  private ArrayList<WalkModel> mWithFriendList = new ArrayList<>();

  private BroadcastReceiver mWithFriendReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mWalkModel.resetPage();
      mWithFriendList.clear();
      recordTogetherListAPI();

    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_with_friends;
  }

  @Override
  protected void initLayout() {

    mMoreScrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
      @Override
      public void onBottomReached() {
        if (mWalkModel.isMore()) {
          recordTogetherListAPI();
        }
      }
    });
    mActivity.registerReceiver(mWithFriendReceiver, new IntentFilter(Constants.WITH_FRIEND_REFRESH));
    initToolbar("산책친구와 함께");
    if (Prefs.getString(Constants.MEMBER_REGION, "").equals("")) {
      mSettingLocationButton.setVisibility(View.GONE);
      mListLayout.setVisibility(View.GONE);
      mDefaultLayout.setVisibility(View.VISIBLE);
    } else {
      mSettingLocationButton.setVisibility(View.VISIBLE);
      mListLayout.setVisibility(View.VISIBLE);
      mDefaultLayout.setVisibility(View.GONE);
      mLocationTextView.setText(Prefs.getString(Constants.MEMBER_REGION, ""));

    }

  }

  @Override
  protected void initRequest() {
    initWithFriendListAdapter();

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
                Prefs.putString(Constants.MEMBER_REGION, mRegion);
                mLocationTextView.setText(area1 + " " + area2 + " " + area3);
              } catch (Exception e) {
                Timber.e(e.getLocalizedMessage());
              }
            }
          }

          @Override
          public void onFailure(Call<NaverModel> call, Throwable t) {
            Timber.e(t.getLocalizedMessage());
          }
        });

  }

  private void initWithFriendListAdapter() {

    mWithFriendAdapter = new WithFriendAdapter(R.layout.row_with_friend, mWithFriendList);
    mWithFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mWithFriendList.get(position).getRecord_idx(), mWithFriendList.get(position).getMember_idx());
        startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
      }
    });

    mWithFriendRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mWithFriendRecyclerView.setAdapter(mWithFriendAdapter);
    recordTogetherListAPI();
  }

  /**
   * 산책친구와 함께
   */
  private void recordTogetherListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setMember_age(mWalkModel.getMember_age());
    walkRequest.setMember_gender(mWalkModel.getMember_gender());
    walkRequest.setAnimal_neuter(mWalkModel.getAnimal_neuter());
    walkRequest.setAnimal_character(mWalkModel.getAnimal_character());
    walkRequest.setAnimal_gender(mWalkModel.getAnimal_gender());
    walkRequest.setPage_num(mWalkModel.getNextPage());
    CommonRouter.api().record_together_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkModel = response.body();
          if (mWalkModel.getData_array() != null) {
            mWithFriendList.addAll(mWalkModel.getData_array());
          }
          mWithFriendAdapter.setNewData(mWithFriendList);
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
   * 필터 버튼
   */
  @OnClick(R.id.filter_button)
  public void withFriendFilter() {
    Intent walkFilterActivity = WalkFilterActivity.getStartIntent(mActivity);
    startActivity(walkFilterActivity, TRANS.PUSH);
    finishWithRemove();
  }

  /**
   * 내 위치 설정
   */
  @OnClick(R.id.setting_location_button)
  public void settingLocationTouched() {
    Intent settingMyLocationActivity = SettingMyLocationActivity.getStartIntent(mActivity, new SettingMyLocationActivity.SelectLocationListener() {
      @Override
      public void onResult(String location) {
        mLocationTextView.setText(location);
//       mLocationTextView.setText(Prefs.getString(Constants.MEMBER_REGION,location));
      }
    });
    startActivity(settingMyLocationActivity, TRANS.PUSH);
  }

  /**
   * 산책친구 등록
   */
  @OnClick(R.id.enroll_walk_friend_button)
  public void enrollWalkFriendTouched() {
    finishWithRemove();
    Intent addWalkFriendActivity = AddWalkFriendActivity.getStartIntent(mActivity);
    startActivity(addWalkFriendActivity, TRANS.PUSH);
  }

  /**
   * 위치설정
   */
  @OnClick(R.id.location_button)
  public void settingTouched() {
    Intent settingMyLocationActivity = SettingMyLocationActivity.getStartIntent(mActivity, new SettingMyLocationActivity.SelectLocationListener() {
      @Override
      public void onResult(String location) {
        mSettingLocationButton.setVisibility(View.VISIBLE);
        mListLayout.setVisibility(View.VISIBLE);
        mDefaultLayout.setVisibility(View.GONE);
      }
    });
    startActivity(settingMyLocationActivity, TRANS.PUSH);
  }
}