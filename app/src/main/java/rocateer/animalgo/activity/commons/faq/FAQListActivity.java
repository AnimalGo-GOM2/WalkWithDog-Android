package rocateer.animalgo.activity.commons.faq;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.FaqModel;
import rocateer.animalgo.models.api.CommonRouter;

public class FAQListActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, FAQListActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.faq_recycler_view)
  public RecyclerView mFaqRecyclerView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  FAQListAdapter mFaqListAdapter;
  ArrayList<MultiItemEntity> mFaqList = new ArrayList<>();
  private FaqModel mFaqResponse = new FaqModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_faq_list;
  }

  @Override
  protected void initLayout() {
    initToolbar("자주묻는 질문");
    initFAQAdapter();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * FAQ 리스트 API
   */
  private void faqListAPI() {
    FaqModel faqModel = new FaqModel();
    faqModel.setPage_num(mFaqResponse.getNextPage());
    CommonRouter.api().faq_list(Tools.getInstance(mActivity).getMapper(faqModel)).enqueue(new Callback<FaqModel>() {
      @Override
      public void onResponse(Call<FaqModel> call, Response<FaqModel> response) {
        FaqModel faqResponse = response.body();
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mFaqList.clear();
          for (FaqModel value : faqResponse.getData_array()) {
            FAQListItem faqListItem = new FAQListItem(value.getTitle(), value.getFaq_type());
            FAQDetailItem faqDetailItem = new FAQDetailItem(value.getContents(), value.getImg());
            faqListItem.addSubItem(faqDetailItem);
            mFaqList.add(faqListItem);
          }
          mFaqListAdapter.setNewData(mFaqList);
        }
      }

      @Override
      public void onFailure(Call<FaqModel> call, Throwable t) {

      }
    });
  }

  /**
   * FAQ 어뎁터
   */
  private void initFAQAdapter() {

    mFaqListAdapter = new FAQListAdapter(mFaqList);
    mFaqRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mFaqRecyclerView.setAdapter(mFaqListAdapter);
    faqListAPI();
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

}