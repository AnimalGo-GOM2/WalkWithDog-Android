package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.SpacingItemDecoration;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class SearchPetActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, SearchPetActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.small_pet_list_recyclerview)
  RecyclerView mSamllPetListRecyclerview;
  @BindView(R.id.pet_size_recycler_view)
  RecyclerView mPetSizeRecyclerView;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private SmallPetAdapter mSmallPetAdapter;
  private ArrayList<AnimalModel> mPetSizeList = new ArrayList<>();
  private PetSizeAdapter mPetSizeAdapter;
  private AnimalModel mAnimalResponse = new AnimalModel();
  private AnimalModel mAnimalKindResponse = new AnimalModel();
  private String mFirstCategoryIdx = "";
  private String mSecondCategoryIdx;
  private ArrayList<AnimalModel> mPetCategoryList = new ArrayList<>();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_search_pet;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책친구 지원");

  }

  @Override
  protected void initRequest() {
    initSmallPetAdpater();
    initPetSizeAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 견 분류 리스트
   */
  private void initPetSizeAdapter() {

    mPetSizeAdapter = new PetSizeAdapter(R.layout.row_pet_size, mPetSizeList);
    mPetSizeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAnimalKindResponse.resetPage();
        mPetCategoryList.clear();
        animalListKindAPI(mPetSizeList.get(position).getCategory_management_idx());
        mFirstCategoryIdx = mPetSizeList.get(position).getCategory_management_idx();
        if (mAnimalKindResponse.getData_array() != null) {
          mPetCategoryList.addAll(mAnimalKindResponse.getData_array());
        }
        mSmallPetAdapter.setNewData(mPetCategoryList);
        Timber.i(mFirstCategoryIdx);
      }
    });
    mPetSizeRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mPetSizeRecyclerView.addItemDecoration(new SpacingItemDecoration(1, Tools.getInstance().dpTopx(mActivity,20), true));
    mPetSizeRecyclerView.setAdapter(mPetSizeAdapter);
    animalListTypeAPI();
  }

  /**
   * 견 분류 리스트 API
   */
  private void animalListTypeAPI() {
    AnimalModel animalRequest = new AnimalModel();
    CommonRouter.api().animal_list_type(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalResponse = response.body();
          if (mAnimalResponse.getData_array() != null) {
            mPetSizeList.addAll(mAnimalResponse.getData_array());
          }
          mPetSizeAdapter.setNewData(mPetSizeList);

        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }

  /**
   * 견종 리스트
   */
  private void initSmallPetAdpater() {
    mSmallPetAdapter = new SmallPetAdapter(R.layout.row_search_pet_list, mPetCategoryList);
    mSmallPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mSecondCategoryIdx = mAnimalKindResponse.getData_array().get(position).getCategory_management_idx();


      }
    });
    mSamllPetListRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mSamllPetListRecyclerview.setAdapter(mSmallPetAdapter);
  }

  /**
   * 견종 리스트 API
   */
  private void animalListKindAPI(String parentcategoryManageMentIdx) {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setParent_category_management_idx(parentcategoryManageMentIdx);

    CommonRouter.api().animal_list_kind(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalKindResponse = response.body();
          List<String> categoryKindList = new ArrayList<>();
          for (AnimalModel value : mAnimalKindResponse.getData_array()) {
            categoryKindList.add(value.getCategory_name());
          }
//          mPetSizeList.addAll(categoryKindList);
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

//  /**
//   * 소형견 리스트
//   */
//  @OnClick(R.id.small_pet_button)
//  public void smallPetTouched() {
//    mSamllPetListRecyclerview.setVisibility(View.VISIBLE);
//    mMiddlePetRecyclerView.setVisibility(View.GONE);
//    mBigPetRecyclerView.setVisibility(View.GONE);
//    Timber.i("small");
//  }
//
//  /**
//   * 중형견 리스트
//   */
//  @OnClick(R.id.middle_pet_button)
//  public void middlePetTouched() {
//    mSamllPetListRecyclerview.setVisibility(View.GONE);
//    mMiddlePetRecyclerView.setVisibility(View.VISIBLE);
//    mBigPetRecyclerView.setVisibility(View.GONE);
//    Timber.i("middle");
//  }
//
//  /**
//   * 대형견 리스트
//   */
//  @OnClick(R.id.big_pet_button)
//  public void bigPetTouched() {
//    mSamllPetListRecyclerview.setVisibility(View.GONE);
//    mMiddlePetRecyclerView.setVisibility(View.GONE);
//    mBigPetRecyclerView.setVisibility(View.VISIBLE);
//    Timber.i("big");
//  }
}

