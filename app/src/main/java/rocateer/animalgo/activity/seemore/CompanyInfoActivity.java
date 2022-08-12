package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;

public class CompanyInfoActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, CompanyInfoActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.company_info_image_view)
  AppCompatImageView mCompanyInfoImageView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private RequestOptions requestOptions = new RequestOptions();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_company_info;
  }

  @Override
  protected void initLayout() {
    mToolbarTitle.setText("회사소개");
    companyInfoAPI();

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 회사 소개
   */
  private void companyInfoAPI() {
    MainModel mainRequest = new MainModel();
    CommonRouter.api().company_info(Tools.getInstance().getMapper(mainRequest)).enqueue(new Callback<MainModel>() {
      @Override
      public void onResponse(Call<MainModel> call, Response<MainModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MainModel mMainResponse = response.body();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMainResponse.getCompany_info())
              .into(mCompanyInfoImageView);
        }
      }

      @Override
      public void onFailure(Call<MainModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
}

