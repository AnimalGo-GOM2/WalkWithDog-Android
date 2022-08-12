package rocateer.animalgo.activity.history;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.islamkhsh.CardSliderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.BackPressCloseHandler;
import rocateer.animalgo.models.ActivityModel;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import timber.log.Timber;

public class HistoryPetBannerAdapter extends CardSliderAdapter<HistoryPetBannerAdapter.BannerHolder> {

  private Context mContext;
  private ArrayList<WalkModel> mImageList;
  private WalkModel mWalkModel;
  private String mAnimalImage;

  public HistoryPetBannerAdapter(RocateerActivity context, ArrayList<WalkModel> imageList, WalkModel walkModel,String  animalImage) {
    this.mContext = context;
    this.mImageList = imageList;
    this.mWalkModel = walkModel;
    this.mAnimalImage =  animalImage;
  }

  @Override
  public int getItemCount() {
    return mImageList.size();
  }

  @Override
  public HistoryPetBannerAdapter.BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pet_banner, parent, false);
    return new HistoryPetBannerAdapter.BannerHolder(view);
  }

  @Override
  public void bindVH(@NotNull HistoryPetBannerAdapter.BannerHolder bannerHolder, int j) {

    bannerHolder.bannerPetNameTextView.setText(mImageList.get(j).getAnimal_name());
    bannerHolder.petAgeTextView.setText(mImageList.get(j).getAnimal_year());
    RequestOptions requestOptions = new RequestOptions();

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + mImageList.get(j).getAnimal_img_path())
        .centerCrop()
        .apply(requestOptions)
        .placeholder(R.drawable.default_dog1)
        .into(bannerHolder.petImage);
    bannerHolder.categoryNameTextView.setText(mImageList.get(j).getCategory_name());
    if (mImageList.get(j).getAnimal_character().equals("0")) {
      bannerHolder.petCharTextView.setText("온순");
    } else if (mImageList.get(j).getAnimal_character().equals("1")) {
      bannerHolder.petCharTextView.setText("입질");
    } else if (mImageList.get(j).getAnimal_character().equals("2")) {
      bannerHolder.petCharTextView.setText("호기심많음");
    } else if (mImageList.get(j).getAnimal_character().equals("3")) {
      bannerHolder.petCharTextView.setText("활동적");
    }

    if (mImageList.get(j).getAnimal_neuter().equals("Y")) {
      bannerHolder.transTextView.setText("O");
    } else if (mImageList.get(j).getAnimal_neuter().equals("N")) {
      bannerHolder.transTextView.setText("X");
    }
    if (mImageList.get(j).getAnimal_training().equals("Y")) {
      bannerHolder.trainingTextView.setText("O");
    } else if (mImageList.get(j).getAnimal_training().equals("N")) {
      bannerHolder.trainingTextView.setText("X");
    }

  }

  /**
   * 리스트 갱신
   */
  public void setNewData(ArrayList<WalkModel> imageList) {
    this.mImageList = imageList;
    this.notifyDataSetChanged();
  }

  class BannerHolder extends RecyclerView.ViewHolder {
    AppCompatImageView petImage;
    AppCompatTextView bannerPetNameTextView;
    AppCompatTextView categoryNameTextView;
    AppCompatTextView petAgeTextView;
    AppCompatTextView petCharTextView;
    AppCompatTextView transTextView;
    AppCompatTextView trainingTextView;

    public BannerHolder(View view) {
      super(view);
      petImage = view.findViewById(R.id.pet_image);
      bannerPetNameTextView = view.findViewById(R.id.banner_pet_name_text_view);
      categoryNameTextView = view.findViewById(R.id.category_name_text_view);
      petAgeTextView = view.findViewById(R.id.pet_age_text_view);
      petCharTextView = view.findViewById(R.id.pet_char_text_view);
      transTextView = view.findViewById(R.id.trans_text_view);
      trainingTextView = view.findViewById(R.id.training_text_view);


    }
  }
}