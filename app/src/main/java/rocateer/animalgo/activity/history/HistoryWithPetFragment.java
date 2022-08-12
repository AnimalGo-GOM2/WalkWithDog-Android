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
import rocateer.animalgo.activity.walk.ApplyFriendsAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class HistoryWithPetFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.history_with_pet_recycler_view)
  RecyclerView mHistoryWithPetRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private HistoryWithPetAdapter mHistoryWithPetAdapter;
  private WalkModel mWalkResponse = new WalkModel();
  private ArrayList<WalkModel> mWithPetLsit = new ArrayList<>();


  private BroadcastReceiver mWithPetHistoryReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      mWalkResponse.resetPage();
      mWithPetLsit.clear();
      recordDiaryListAPI();

    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_history_with_pet;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mWithPetHistoryReceiver, new IntentFilter(Constants.HiSTORY_REFRESH));

  }

  @Override
  protected void initRequest() {
    initHistoryWithPetAdapter();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  private void initHistoryWithPetAdapter() {

    mHistoryWithPetAdapter = new HistoryWithPetAdapter(R.layout.row_history_with_pet, mWithPetLsit);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mHistoryWithPetRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("산책기록이 없습니다.");
    mHistoryWithPetAdapter.setEmptyView(emptyView);
    mHistoryWithPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent walkHistoryActivity = WalkHistoryActivity.getStartIntent(mActivity, mWithPetLsit.get(position).getRecord_diary_idx());
        startActivity(walkHistoryActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mHistoryWithPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mHistoryWithPetRecyclerView.setAdapter(mHistoryWithPetAdapter);
    recordDiaryListAPI();
  }

  /**
   * 산책기록 나와 반려견만
   */
  public void recordDiaryListAPI() {
    WalkModel walkRequest= new WalkModel();
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    walkRequest.setRecord_type("0");
    walkRequest.setPage_num(mWalkResponse.getNextPage());

    CommonRouter.api().record_diary_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          WalkModel mWalkResponse = response.body();
          if (mWalkResponse.getData_array() != null) {
            mWithPetLsit.addAll(mWalkResponse.getData_array());
          }
          mHistoryWithPetAdapter.setNewData(mWithPetLsit);

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
