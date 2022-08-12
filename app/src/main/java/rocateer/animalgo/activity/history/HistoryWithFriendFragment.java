package rocateer.animalgo.activity.history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.activity.home.SearchNoteAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class HistoryWithFriendFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.history_with_friend_recycler_view)
  RecyclerView mHistoryWithFriendRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private HistoryWithFriendAdapter mHistoryWithFriendAdapter;
  private WalkModel mWalkResponse = new WalkModel();
  private ArrayList<WalkModel> mWithFriendList = new ArrayList<>();

  private BroadcastReceiver mWithFriendHisotyReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mWalkResponse.resetPage();
      mWithFriendList.clear();
      recordDiaryListAPI();

    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_history_with_friend;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mWithFriendHisotyReceiver, new IntentFilter(Constants.WITH_FRIEND_HISTORY_REFRESH));

  }

  @Override
  protected void initRequest() {
    initHistoryWithFriendAdapter();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 산책친구와 함께
   */
  private void initHistoryWithFriendAdapter() {
    mHistoryWithFriendAdapter = new HistoryWithFriendAdapter(R.layout.row_history_with_friend, mWithFriendList);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mHistoryWithFriendRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("산책기록이 없습니다.");
    mHistoryWithFriendAdapter.setEmptyView(emptyView);
    mHistoryWithFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent historyWithFriendDetailActivity = HistoryWithFriendDetailActivity.getStartIntent(mActivity, mWithFriendList.get(position).getRecord_diary_idx());
        startActivity(historyWithFriendDetailActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mHistoryWithFriendRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mHistoryWithFriendRecyclerView.setAdapter(mHistoryWithFriendAdapter);
    recordDiaryListAPI();
  }

  /**
   * 산책기록 산책 친구와 함께
   */
  public void recordDiaryListAPI() {
    WalkModel walkRequest= new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    walkRequest.setRecord_type("1");
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().record_diary_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          if (mWalkResponse.getData_array() != null) {
            mWithFriendList.addAll(mWalkResponse.getData_array());
          }
          mHistoryWithFriendAdapter.setNewData(mWithFriendList);

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


}
