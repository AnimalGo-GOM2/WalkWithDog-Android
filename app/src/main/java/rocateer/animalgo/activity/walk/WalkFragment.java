package rocateer.animalgo.activity.walk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

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
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.activity.mypet.AddMyPetActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WalkFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.add_friends_request_recycler_view)
  RecyclerView mAddFriendsRecyclerView;
  @BindView(R.id.apply_friends_recycler_view)
  RecyclerView mApplyFriendsRecyclerView;
  @BindView(R.id.add_walk_friend_layout)
  LinearLayout mAddWalkFriendLayout;
  @BindView(R.id.apply_layout)
  LinearLayout mApplyLayout;
  @BindView(R.id.empty_walk_list)
  LinearLayout mEmptyWalkListLayout;
  @BindView(R.id.empty_my_pet)
  LinearLayout mEmptyMyPetLayout;
  @BindView(R.id.scroll_view)
  NestedScrollView mScrollView;
  @BindView(R.id.walk_request_list_button)
  LinearLayout mWalkRequetListButton;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private AnimalModel mAnimalResponse = new AnimalModel();
  private WalkModel mWalkResponse = new WalkModel();
  private WalkModel mWalkModel = new WalkModel();

  private ArrayList<WalkModel> mWalkList = new ArrayList<>();
  private ArrayList<WalkModel> mSupportList = new ArrayList<>();

  private AddFriendsListAdapter mAddFriendsListAdapter;
  private ApplyFriendListAdapter mApplyFriendListAdapter;
  private double mLatitude = 0; // 위도
  private double mLongitude = 0; //경도
  private String mRegion; // 위치

  private BroadcastReceiver mWalkFragmentReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mWalkList.clear();
      mWalkResponse.resetPage();
      registeredRecordListAPI();

      mSupportList.clear();
      mWalkModel.resetPage();
      recordApplyListAPI();
    }
  };


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_walk;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mWalkFragmentReceiver, new IntentFilter(Constants.WALK_REFRESH));

    if (mAnimalResponse.getAnimal_idx() == null) {
      mEmptyMyPetLayout.setVisibility(View.VISIBLE);

    } else if (mAnimalResponse.getAnimal_idx() != null) {
      mEmptyMyPetLayout.setVisibility(View.GONE);

    }

  }

  @Override
  protected void initRequest() {
    initAddFriendsListAdapter();
    initSupportFriendsListAdapter();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 현재 위치 찾기
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
            mLatitude = location.getLatitude(); // 위도
            mLongitude = location.getLongitude(); //경도
            Timber.i("latitude == " + mLatitude + "  /  longitude == " + mLongitude);

          }
        });
  }

  /**
   * 산책친구 등록 리스트 API
   */
  private void registeredRecordListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().registered_record_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkModel = response.body();
          if (mWalkModel.getData_array().size() == 0) {
            mAddFriendsRecyclerView.setVisibility(View.GONE);
            mAddWalkFriendLayout.setVisibility(View.VISIBLE);
            mWalkRequetListButton.setSelected(false);
          } else if (mWalkModel.getData_array().size() == 1) {
            mAddWalkFriendLayout.setVisibility(View.GONE);
            mAddFriendsRecyclerView.setVisibility(View.VISIBLE);
            mWalkRequetListButton.setSelected(true);
            mWalkList.add(mWalkModel.getData_array().get(0));
          } else {
            mAddWalkFriendLayout.setVisibility(View.GONE);
            mAddFriendsRecyclerView.setVisibility(View.VISIBLE);
            mWalkRequetListButton.setSelected(true);

            for (int i = 0; i < 2; i++) {
              mWalkList.add(mWalkModel.getData_array().get(i));
            }
          }
          mAddFriendsListAdapter.setNewData(mWalkList);
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 등록한 산책친구 요청 리스트
   */
  private void initAddFriendsListAdapter() {

    mAddFriendsListAdapter = new AddFriendsListAdapter(R.layout.row_walk_list, mWalkList);
    mAddFriendsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mWalkList.size() != 0) {
          Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mWalkList.get(position).getRecord_idx(), Prefs.getString(Constants.MEMBER_IDX, ""));
          startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
        }
      }
    });
    mAddFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mAddFriendsRecyclerView.setAdapter(mAddFriendsListAdapter);
    registeredRecordListAPI();
  }

  /**
   * 산책친구 지원 리스트 API
   */
  private void recordApplyListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().record_apply_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkModel = response.body();
          if (mWalkModel.getData_array().size() == 0) {
            mApplyLayout.setVisibility(View.VISIBLE);
            mApplyFriendsRecyclerView.setVisibility(View.GONE);
          } else if (mWalkModel.getData_array().size() == 1 ) {
            mApplyLayout.setVisibility(View.GONE);
            mApplyFriendsRecyclerView.setVisibility(View.VISIBLE);
            mSupportList.add(mWalkModel.getData_array().get(0));
          }else {
            mApplyLayout.setVisibility(View.GONE);
            mApplyFriendsRecyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < 2; i++) {
              mSupportList.add(mWalkModel.getData_array().get(i));
            }

          }
          mApplyFriendListAdapter.setNewData(mSupportList);
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 지원한 산책친구 리스트
   */
  private void initSupportFriendsListAdapter() {

    mApplyFriendListAdapter = new ApplyFriendListAdapter(R.layout.row_apply_friends, mSupportList);
    mApplyFriendListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mSupportList.size() != 0) {
          Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mSupportList.get(position).getRecord_idx(), mSupportList.get(position).getMember_idx());
          startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
        }
      }
    });
    mApplyFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mApplyFriendsRecyclerView.setAdapter(mApplyFriendListAdapter);
    recordApplyListAPI();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 나와 반려견만 산책
   */
  @OnClick(R.id.with_pet_button)
  public void withPetTouched() {
    Intent withPetActivity = WithPetActivity.getStartIntent(mActivity);
    startActivity(withPetActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 산책친구와 함께
   */
  @OnClick(R.id.with_friend_button)
  public void withFriendButton() {
    if (Prefs.getString(Constants.MEMBER_REGION,"").equals("")) {
      Intent firstWalkFriendActivity = FirstWalkFriendActivity.getStartIntent(mActivity);
      startActivity(firstWalkFriendActivity, RocateerActivity.TRANS.PUSH);
    } else {
      Intent withFriendsActivity = WithFriendsActivity.getStartIntent(mActivity, mRegion, mWalkResponse);
      startActivity(withFriendsActivity, RocateerActivity.TRANS.PUSH);
    }

  }

  /**
   * 등록한 산책친구 요청
   */
  @OnClick(R.id.walk_request_list_button)
  public void walkRequestListTouched() {
    Intent walkRequestActivity = WalkRequestActivity.getStartIntent(mActivity);
    startActivity(walkRequestActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 지원한 산책 친구들
   */
  @OnClick(R.id.apply_friend_list_button)
  public void applyFriendTouched() {
    Intent moreRequestFriendActivity = MoreRequestFriendActivity.getStartIntent(mActivity);
    startActivity(moreRequestFriendActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 산책친구 등록
   */
  @OnClick(R.id.add_walk_friend_button)
  public void addWalkFriendTouched() {
    Intent addWalkFriendActivity = AddWalkFriendActivity.getStartIntent(mActivity);
    startActivity(addWalkFriendActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 산책친구 지원
   */
  @OnClick(R.id.request_walk_friend_button)
  public void requestWalkFriendTouched() {
    Intent withFriendActivity = WithFriendsActivity.getStartIntent(mActivity, mRegion, mWalkResponse);
    startActivity(withFriendActivity, RocateerActivity.TRANS.PUSH);
  }


  /**
   * 반려견 등록
   */
  @OnClick(R.id.add_pet_button)
  public void addPetTouched() {
    Intent addMyPetActivity = AddMyPetActivity.getStartIntent(mActivity, new AddMyPetActivity.AddMyPetListener() {
      @Override
      public void onAdd() {
      }
    });
    startActivity(addMyPetActivity, RocateerActivity.TRANS.PUSH);
  }

}
