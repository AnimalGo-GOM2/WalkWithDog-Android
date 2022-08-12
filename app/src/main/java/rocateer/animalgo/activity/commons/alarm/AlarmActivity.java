package rocateer.animalgo.activity.commons.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
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
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.api.CommonRouter;

public class AlarmActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, AlarmActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.alarm_recycler_view)
  RecyclerView mAlarmRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private AlarmAdapter mAlarmAdapter;
  private AlarmModel mAlarmResponse = new AlarmModel();
  private ArrayList<MultiItemEntity> mAlarmList = new ArrayList<>();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_alarm;
  }

  @Override
  protected void initLayout() {
    initToolbar("알림");

  }

  @Override
  protected void initRequest() {
    initAlarmAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 알림 리스트
   */

  private void initAlarmAdapter() {
    mAlarmAdapter = new AlarmAdapter(mAlarmList);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mAlarmRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("받은 알림이 없습니다.");
    mAlarmAdapter.setEmptyView(emptyView);
    mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mAlarmRecyclerView.setAdapter(mAlarmAdapter);
    alarmListAPI();
  }

  // 알림 리스트 API
  public void alarmListAPI() {
    AlarmModel alarmRequest = new AlarmModel();
    alarmRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    alarmRequest.setPage_num(mAlarmResponse.getNextPage());

    CommonRouter.api().alarm_list(Tools.getInstance().getMapper(alarmRequest)).enqueue(new Callback<AlarmModel>() {
      @Override
      public void onResponse(Call<AlarmModel> call, Response<AlarmModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAlarmResponse = response.body();
          for (AlarmModel value : mAlarmResponse.getData_array()) {
            AlarmListItem alarmListItem = new AlarmListItem(value.getData().getTitle(), value.getIns_date());
            AlarmDetailItem alarmDetailItem = new AlarmDetailItem(value.getMsg());
            alarmListItem.addSubItem(alarmDetailItem);
            mAlarmList.add(alarmListItem);
          }
          mAlarmAdapter.setNewData(mAlarmList);
        }
      }

      @Override
      public void onFailure(Call<AlarmModel> call, Throwable t) {

      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
}

