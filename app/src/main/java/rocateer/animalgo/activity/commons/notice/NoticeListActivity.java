package rocateer.animalgo.activity.commons.notice;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.NoticeModel;
import rocateer.animalgo.models.api.CommonRouter;

public class NoticeListActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, NoticeListActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.notice_list_recycler_view)
  public RecyclerView mNoticeListRecyclerView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private NoticeListAdapter mNoticeListAdapter;
  private ArrayList<MultiItemEntity> mNoticeList = new ArrayList<>();
  private NoticeModel mNoticeResponse = new NoticeModel();


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_notice_list;
  }

  @Override
  protected void initLayout() {
//    for (int i = 0; i <= 10; i++) {
//      mNoticeList.add(new Mu());
//    }
    initToolbar("공지사항");
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mNoticeResponse.resetPage();
        mNoticeList.clear();
        noticeListAPI();
      }
    });
  }

  @Override
  protected void initRequest() {
    initNoticeListAdapter();
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 공지사항 리스트
   */
  private void initNoticeListAdapter() {
    mNoticeListAdapter = new NoticeListAdapter(mNoticeList);
    mNoticeListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mNoticeListRecyclerView.setAdapter(mNoticeListAdapter);

    noticeListAPI();
  }

  /**
   * API - 공지사항 리스트
   */
  private void noticeListAPI() {
    NoticeModel noticeRequest = new NoticeModel();
    noticeRequest.setPage_num(mNoticeResponse.getNextPage());

    CommonRouter.api().notice_list(Tools.getInstance(mActivity).getMapper(noticeRequest)).enqueue(new Callback<NoticeModel>() {
      @Override
      public void onResponse(Call<NoticeModel> call, Response<NoticeModel> response) {
        NoticeModel noticeResponse = response.body();
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mNoticeList.clear();
          for (NoticeModel value : noticeResponse.getData_array()) {
            NoticeListItem noticeListItem = new NoticeListItem(value.getTitle(), value.getIns_date());
            NoticeDetailItem noticeDetailItem = new NoticeDetailItem(value.getContents(), value.getImg_path());
            noticeListItem.addSubItem(noticeDetailItem);
            mNoticeList.add(noticeListItem);
          }
          mNoticeListAdapter.setNewData(mNoticeList);
        }
      }

      @Override
      public void onFailure(Call<NoticeModel> call, Throwable t) {

      }
    });
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

}