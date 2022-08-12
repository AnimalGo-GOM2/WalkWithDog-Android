package rocateer.animalgo.activity.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;
import timber.log.Timber;

public class SentInboxFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.sent_inbox_list_recycler_view)
  RecyclerView mSentInBoxListRecyclerView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private SentInBoxAdapter mSentInBoxAdapter;
  private ArrayList<MemoModel> mSendInBoxLIst = new ArrayList<>();
  private MemoModel mMemoResponse = new MemoModel();
  private BroadcastReceiver mMemoReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mMemoResponse.resetPage();
      mSendInBoxLIst.clear();
      memoListAPI();

    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_sent_inbox;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mMemoReceiver, new IntentFilter(Constants.MEMO_REFRESH));
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mMemoResponse.resetPage();
        mSendInBoxLIst.clear();
        memoListAPI();
      }
    });

  }

  @Override
  protected void initRequest() {
    initSentInBoxAdapter();
  }


  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 보낸 쪽지함 리스트
   */

  private void initSentInBoxAdapter() {

    mSentInBoxAdapter = new SentInBoxAdapter(R.layout.row_search_send_note, mSendInBoxLIst);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mSentInBoxListRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("보낸쪽지가 없습니다.");
    mSentInBoxAdapter.setEmptyView(emptyView);
    mSentInBoxAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent sentInBoxDetailActivity = SentInBoxDetailActivity.getStartIntent(mActivity, mSendInBoxLIst.get(position).getMemo_idx());
        startActivity(sentInBoxDetailActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mMoreScrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
      @Override
      public void onBottomReached() {
        Timber.i("다음 페이지 --->");
        if (mMemoResponse.isMore()) {
          memoListAPI();
        }
      }
    });
    mSentInBoxListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mSentInBoxListRecyclerView.setAdapter(mSentInBoxAdapter);
    memoListAPI();
  }

  /**
   * 보낸 쪽지 목록
   */
  public void memoListAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setType("1");
    memoRequest.setPage_num(mMemoResponse.getNextPage());

    CommonRouter.api().memo_list(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemoModel mMemoResponse = response.body();
          if (mMemoResponse.getData_array() != null) {
            mSendInBoxLIst.addAll(mMemoResponse.getData_array());
          }
          mSentInBoxAdapter.setNewData(mSendInBoxLIst);
        }
      }

      @Override
      public void onFailure(Call<MemoModel> call, Throwable t) {

      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

}