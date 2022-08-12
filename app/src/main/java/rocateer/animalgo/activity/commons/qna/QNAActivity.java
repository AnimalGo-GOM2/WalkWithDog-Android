package rocateer.animalgo.activity.commons.qna;

import android.content.Context;
import android.content.Intent;
import android.view.View;

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
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.commons.TouchDetectableScrollView;
import rocateer.animalgo.models.QnaModel;
import rocateer.animalgo.models.api.CommonRouter;

//import rocateer.kit.models.NoticeModel;

public class QNAActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, QNAActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.qna_list_recycler_view)
  RecyclerView mQnaListRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private QNAAdapter mQnaAdapter;
  private ArrayList<QnaModel> mQnaList = new ArrayList<>();
  private QnaModel mQnaResponse = new QnaModel();


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_qna_list;
  }

  @Override
  protected void initLayout() {
    initToolbar("1:1 문의");
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mQnaResponse.resetPage();
        mQnaList.clear();
        qaListAPI();
      }
    });



  }

  @Override
  protected void initRequest() {
    initQnaListAdapter();
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * QNA 리스트
   */
  private void initQnaListAdapter() {
    mQnaAdapter = new QNAAdapter(R.layout.row_qna_list, mQnaList);
    mQnaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent qnaDetailActivity = QNADetailActivity.getStartIntent(mActivity, mQnaList.get(position), position, new QNADetailActivity.QNADetailLitener() {
          @Override
          public void onDelete(int position) {
            Tools.getInstance(mActivity).showToast("1:1 문의가 삭제되었습니다.");
            mQnaList.remove(position);
            mQnaAdapter.notifyItemRemoved(position);
          }
        });
        startActivity(qnaDetailActivity, TRANS.PUSH);
      }
    });
    mMoreScrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
      @Override
      public void onBottomReached() {
        if (mQnaResponse.isMore()) {
          qaListAPI();
        }
      }
    });

    mQnaListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mQnaListRecyclerView.setAdapter(mQnaAdapter);
    qaListAPI();
  }

  /**
   * QNA 리스트 API
   */
  public void qaListAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    qnaRequest.setPage_num(mQnaResponse.getNextPage());

    CommonRouter.api().qa_list(Tools.getInstance().getMapper(qnaRequest)).enqueue(new Callback<QnaModel>() {
        @Override
        public void onResponse(Call<QnaModel> call, Response<QnaModel> response) {
            if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
              mQnaResponse = response.body();
                if (mQnaResponse.getData_array() != null) {
                  mQnaList.addAll(mQnaResponse.getData_array());
                }
                mQnaAdapter.setNewData(mQnaList);
            }
        }

        @Override
        public void onFailure(Call<QnaModel> call, Throwable t) {

        }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * QNA 문의
   */
  @OnClick(R.id.add_qna)
  public void addQnaTouched() {
    Intent addQnaActivity = AddQnaActivity.getStartIntent(mActivity, new AddQnaActivity.QNAAddLitener(){
      @Override
      public void onAdd() {
        mQnaResponse.resetPage();
        mQnaList.clear();
        qaListAPI();
      }
    });
    startActivity(addQnaActivity, TRANS.PUSH);
  }
}
