package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
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
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class AddWalkFriendActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, AddWalkFriendActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.select_my_pet_recycler_view)
  RecyclerView mMyPetViewRecyclerView;
  @BindView(R.id.back_button)
  AppCompatImageView mBackButton;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  private ArrayList<AnimalModel> mMyPetList = new ArrayList<>();
  private MyPetListAdapter mMyPetListAdapter;
  private WalkModel mWalkResponse = new WalkModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_add_walk_friend;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 등록");
    mBackButton.setVisibility(View.GONE);


  }

  @Override
  protected void initRequest() {
    initMyPetListAdapter();

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
        ArrayList<String> animalIdxs = new ArrayList<>();
        for (AnimalModel value: mMyPetList) {
          if (value.isSelect()) {
            animalIdxs.add(value.getAnimal_idx());
          }
        }
        mWalkResponse.setMember_animal_idxs(TextUtils.join(",", animalIdxs));


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
        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 다음 버튼
   */
  @OnClick(R.id.add_next_button)
  public void addNextButton() {
    boolean isSelected = false;
    for (AnimalModel value : mMyPetList) {
      if (value.isSelect()) {
        isSelected = true;
      }
    }
    if (isSelected) {
      Intent selectDateActiviy = SelectDateActivity.getStartIntent(mActivity, mWalkResponse);
      startActivity(selectDateActiviy, TRANS.PRESENT);
      finishWithRemove();
    } else {
      showAlertDialog("함께 산책 할 반려견을\n선택하여 주세요.", "확인", new DialogEventListener() {
        @Override
        public void onReceivedEvent() {
        }
      });
    }


  }

  /**
   * 취소
   */
  @OnClick(R.id.walk_cancle_button)
  public void walkCancleTouched() {
    finishWithRemove();
  }

}
