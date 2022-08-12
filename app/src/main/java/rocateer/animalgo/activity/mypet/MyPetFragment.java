package rocateer.animalgo.activity.mypet;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
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
import rocateer.animalgo.activity.home.WalkFriendRequestActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.api.CommonRouter;

public class MyPetFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.my_pet_list_recycler_view)
  RecyclerView mMyPetListRecyclerView;
  @BindView(R.id.empty_my_pet)
  LinearLayout mEmptyMyPet;
  @BindView(R.id.add_my_pet_button)
  AppCompatButton mAddMyPetButton;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MyPetListAdapter mMyPetListAdapter;
  private  ArrayList<AnimalModel> mMyPetList = new ArrayList<>();
  private AnimalModel mAnimalResponse = new AnimalModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_my_pet;
  }

  @Override
  protected void initLayout() {

  }

  @Override
  protected void initRequest() {
    initMyPetListAdapter();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  private void initMyPetListAdapter() {

    mMyPetListAdapter = new MyPetListAdapter(R.layout.row_my_pet_list, mMyPetList);
    mMyPetListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent editMyPetActivity = EditMyPetActivity.getStartIntent(mActivity, mMyPetList.get(position).getAnimal_idx(), new EditMyPetActivity.MyPetListener() {
          @Override
          public void onRefresh() {
            mMyPetList.clear();
            mAnimalResponse.resetPage();
            myAnimalListAPI();
          }
        });
        startActivity(editMyPetActivity, RocateerActivity.TRANS.PUSH);
      }
    });
    mMyPetListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mMyPetListRecyclerView.setAdapter(mMyPetListAdapter);
    myAnimalListAPI();
  }

  /**
   * 내 반려견 리스트 API
   */
  public void myAnimalListAPI() {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));

    CommonRouter.api().my_animal_list(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalResponse = response.body();
          if (mAnimalResponse.getData_array().size() != 0) {
            mMyPetListRecyclerView.setVisibility(View.VISIBLE);
            mAddMyPetButton.setVisibility(View.VISIBLE);
            mEmptyMyPet.setVisibility(View.GONE);
            mMyPetList.addAll(mAnimalResponse.getData_array());
          } else if (mAnimalResponse.getData_array().size() == 0){
            mEmptyMyPet.setVisibility(View.VISIBLE);
            mAddMyPetButton.setVisibility(View.GONE);
            mMyPetListRecyclerView.setVisibility(View.GONE);
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
   * 반려견 등록
   */

  @OnClick(R.id.add_my_pet_button)
  public void addMyPetTouched() {
    Intent addMyPetActivity = AddMyPetActivity.getStartIntent(mActivity, new AddMyPetActivity.AddMyPetListener() {
      @Override
      public void onAdd() {
        mAnimalResponse.resetPage();
        mMyPetList.clear();
        myAnimalListAPI();
      }
    });
    startActivity(addMyPetActivity, RocateerActivity.TRANS.PUSH);
  }

  /**
   * 반려견이 없을때 반려견 등록
   */
  @OnClick(R.id.add_my_pet_layout)
  public void addMyPetLayoutTouched() {
    Intent addMyPetActivity = AddMyPetActivity.getStartIntent(mActivity, new AddMyPetActivity.AddMyPetListener() {
      @Override
      public void onAdd() {
        mAnimalResponse.resetPage();
        mMyPetList.clear();
        myAnimalListAPI();
      }
    });
    startActivity(addMyPetActivity, RocateerActivity.TRANS.PUSH);
  }


}