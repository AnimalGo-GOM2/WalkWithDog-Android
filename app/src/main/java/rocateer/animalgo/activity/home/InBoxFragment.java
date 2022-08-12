package rocateer.animalgo.activity.home;

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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;

public class InBoxFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.inbox_recycler_view)
  RecyclerView mInBoxRecyclerView;
  @BindView(R.id.no_read_note_text_view)
  AppCompatTextView mNoReadNoteTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private InBoxListAdpater mInBoxListAdapter;
  private MemoModel mMemoResponse = new MemoModel();
  private ArrayList<MemoModel> mInBoxLIst = new ArrayList<>();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_in_box;
  }

  @Override
  protected void initLayout() {
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mMemoResponse.resetPage();
        mInBoxLIst.clear();
        memoListAPI();
      }
    });


  }

  @Override
  protected void initRequest() {
    initInBoxAdapter();


  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }

  @Override
  public void onResume(){
    super.onResume();
    mMemoResponse.resetPage();
    mInBoxLIst.clear();
    memoListAPI();

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  private void initInBoxAdapter() {

    mInBoxListAdapter = new InBoxListAdpater(R.layout.row_inbox, mInBoxLIst);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mInBoxRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("받은쪽지가 없습니다.");
    mInBoxListAdapter.setEmptyView(emptyView);
    mInBoxListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent receivedNoteActivity = ReceivedNoteActivity.getStartIntent(mActivity, mInBoxLIst.get(position).getMemo_idx());
        startActivity(receivedNoteActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mInBoxRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mInBoxRecyclerView.setAdapter(mInBoxListAdapter);
    memoListAPI();
  }

  /**
   * 쪽지 리스트
   */
  public void memoListAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setType("0");
    memoRequest.setPage_num(mMemoResponse.getNextPage());

    CommonRouter.api().memo_list(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemoModel mMemoResponse = response.body();
          mNoReadNoteTextView.setText(mMemoResponse.getUnread_cnt());
          if (mMemoResponse.getData_array() != null) {
            mInBoxLIst.addAll(mMemoResponse.getData_array());
          }
          mInBoxListAdapter.setNewData(mInBoxLIst);
        }
      }

      @Override
      public void onFailure(Call<MemoModel> call, Throwable t) {

      }
    });
  }

  /**
   * 쪽지 모두 읽기 API
   */
  public void readAllAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    CommonRouter.api().read_all(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {

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
  @OnClick(R.id.all_read_button)
  public void allReadTouched() {
    readAllAPI();
  }



}