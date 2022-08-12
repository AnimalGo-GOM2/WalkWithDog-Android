package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class WalkRequestActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, WalkRequestActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.walk_request_recycler_view)
  RecyclerView mWalkRequestRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private AddFriendsListAdapter mAddFriendsListAdapter;
  private ArrayList<WalkModel> mWalkList = new ArrayList<>();
  private WalkModel mWalkResponse = new WalkModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_request;
  }

  @Override
  protected void initLayout() {
    initToolbar("등록한 산책친구 요청");
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mWalkResponse.resetPage();
        mWalkList.clear();
        registeredRecordListAPI();
      }
    });


  }

  @Override
  protected void initRequest() {
    initAddFriendsListAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  private void initAddFriendsListAdapter() {

    mAddFriendsListAdapter = new AddFriendsListAdapter(R.layout.row_walk_list, mWalkList);
    mAddFriendsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mWalkList.get(position).getRecord_idx(), Prefs.getString(Constants.MEMBER_IDX,""));
        startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mMoreScrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
      @Override
      public void onBottomReached() {
        if (mWalkResponse.isMore()) {
          registeredRecordListAPI();
        }
      }
    });
    mWalkRequestRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mWalkRequestRecyclerView.setAdapter(mAddFriendsListAdapter);
    registeredRecordListAPI();
  }

  /**
   * 산책친구 등록 리스트 API
   */
  private void registeredRecordListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().registered_record_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkModel = response.body();
          if (mWalkModel.getData_array() != null) {
            mWalkList.addAll(mWalkModel.getData_array());
          }
          mAddFriendsListAdapter.setNewData(mWalkList);

        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

//  /**
//   * 산책친구 등록 리스트 API
//   */
//  private void registeredRecordListAPI() {
//    WalkModel walkRequest = new WalkModel();
//    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
//    walkRequest.setPage_num(mWalkResponse.getNextPage());
//
//    CommonRouter.api().registered_record_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
//      @Override
//      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
//        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
//          WalkModel mWalkModel = response.body();
//          mWalkList.addAll(mWalkModel.getData_array());
//          mAddFriendsListAdapter.setNewData(mWalkList);
//
//        }
//      }
//
//      @Override
//      public void onFailure(Call<WalkModel> call, Throwable t) {
//
//      }
//    });
//  }



  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
}
