package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.pixplicity.easyprefs.library.Prefs;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.MyPetAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;

public class WithPetActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, WithPetActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.my_pet_view_recycler_view)
  RecyclerView mMyPetViewRecyclerView;
  @BindView(R.id.loading_indicator_view)
  AVLoadingIndicatorView mLoadingIndicatorView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MyPetListAdapter mMyPetListAdapter;
  private ArrayList<AnimalModel> mMyPetList = new ArrayList<>();
  private WalkModel mWalkResponse = new WalkModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_with_pet;
  }

  @Override
  protected void initLayout() {
    initToolbar("나와 반려견만 산책");

  }

  @Override
  protected void initRequest() {
    initMyPetListAdapter();

  }

  private String getTime() {
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    String getTime = dateFormat.format(date);
    return getTime;
  }

  



  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 반려견 리스트
   */
  private void initMyPetListAdapter() {


    mMyPetListAdapter = new MyPetListAdapter(R.layout.row_my_pet_view, mMyPetList);
    mMyPetListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        mMyPetList.get(position).setSelect(!mMyPetList.get(position).isSelect());
        mMyPetListAdapter.notifyItemChanged(position, mMyPetList.get(position));

      }
    });
    mMyPetViewRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mMyPetViewRecyclerView.setAdapter(mMyPetListAdapter);
    myAnimalListAPI();
  }

  /**
   * 내 반려견 리스트
   */
  private void myAnimalListAPI() {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    CommonRouter.api().my_animal_list(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          AnimalModel mAnimalResponse = response.body();
          if (mAnimalResponse.getData_array() != null) {
            mMyPetList.addAll(mAnimalResponse.getData_array());
          }
          mMyPetListAdapter.setNewData(mMyPetList);

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              mLoadingIndicatorView.smoothToHide();
            }
          }, 1000);
        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }

  /**
   * 산책등록 API
   */
  public void recordRegInAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_type("0");
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));


    ArrayList<String> animalIdxs = new ArrayList<>();
    for (AnimalModel value : mMyPetList) {
      if (value.isSelect()) {
        animalIdxs.add(value.getAnimal_idx());
      }
    }
    walkRequest.setMember_animal_idxs(TextUtils.join(",", animalIdxs));

    CommonRouter.api().record_reg_in(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkResponse = response.body();
          Intent walkGiudeAcitivty = WalkGuideActivity.getStartIntent(mActivity, mWalkResponse.getRecord_idx());
          startActivity(walkGiudeAcitivty, TRANS.PUSH);

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
   * 취소 버튼
   */
  @OnClick(R.id.walk_cancel_button)
  public void walkCancelTouched() {
    finishWithRemove();
  }

  /**
   * 산책시작
   */
  @OnClick(R.id.start_walk_button)
  public void startWalkTouched() {
    boolean isSelected = false;
    for (AnimalModel value : mMyPetList) {
      if (value.isSelect()) {
        isSelected = true;
      }
    }
    if (isSelected) {
      recordRegInAPI();
    } else {
      showAlertDialog("함께 산책 할 반려견을\n선택하여 주세요.", "확인", new DialogEventListener() {
        @Override
        public void onReceivedEvent() {
        }
      });
    }
  }

}

