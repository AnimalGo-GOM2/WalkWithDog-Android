package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.HomeListAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.dialog.WalkBottomDialog;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class MoreRequestFriendActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, MoreRequestFriendActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.apply_friend_recycler_view)
  RecyclerView mApplyFriendRecyclerView;
  @BindView(R.id.dot_button)
  ImageView mDotButton;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ApplyFriendsAdapter mApplyFriendsAdpater;
  private ArrayList<WalkModel> mApplyList = new ArrayList<>();

  private WalkModel mWalkResponse = new WalkModel();
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_more_request_friend;
  }
  @Override
  protected void initLayout() {
    mDotButton.setVisibility(View.GONE);
    initToolbar("지원한 산책친구들");
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mWalkResponse.resetPage();
        mApplyList.clear();
        recordApplyListAPI();
      }
    });

  }

  @Override
  protected void initRequest() {
    initApplyFriendAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 산책친구 리스트
   */
  private void initApplyFriendAdapter() {

    mApplyFriendsAdpater = new ApplyFriendsAdapter(R.layout.row_apply_friend, mApplyList);
    mApplyFriendsAdpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Intent walkFriendDetailActivity = WalkFriendDetailActivity.getStartIntent(mActivity, mApplyList.get(position).getRecord_idx(), mApplyList.get(position).getMember_idx());
        startActivity(walkFriendDetailActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mMoreScrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
      @Override
      public void onBottomReached() {
        if (mWalkResponse.isMore()) {
          mWalkResponse.resetPage();
          mApplyList.clear();
          recordApplyListAPI();

        }
      }
    });
    mApplyFriendRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mApplyFriendRecyclerView.setAdapter(mApplyFriendsAdpater);
    recordApplyListAPI();
  }

  /**
   * 산책친구 지원 리스트 API
   */
  private void recordApplyListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().record_apply_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkModel = response.body();
          if (mWalkModel.getData_array() != null) {
            mApplyList.addAll(mWalkModel.getData_array());
          }
          mApplyFriendsAdpater.setNewData(mApplyList);
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
   * 산책취소
   */
  @OnClick(R.id.dot_button)
  public void dotTouched() {
//    WalkBottomDialog walkBottomDialog = new WalkBottomDialog(mActivity);
//    walkBottomDialog.show(getSupportFragmentManager(),"ApplyFriendActivity");
  }


}
