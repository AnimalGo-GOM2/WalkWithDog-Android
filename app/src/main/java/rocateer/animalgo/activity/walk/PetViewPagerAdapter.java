package rocateer.animalgo.activity.walk;

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

import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.models.BannerModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class PetViewPagerAdapter extends CardSliderAdapter<PetViewPagerAdapter.BannerHolder> {

  private Context mContext;
  private ArrayList<WalkModel> mImageList;
  private WalkModel mWalkModel;
  private String mAnimalImage;

  public PetViewPagerAdapter(Context context, ArrayList<WalkModel> imageList, WalkModel walkModel, String animalImage) {
    this.mContext = context;
    this.mImageList = imageList;
    this.mWalkModel = walkModel;
    this.mAnimalImage = animalImage;
  }

  @Override
  public int getItemCount() {
    return mImageList.size();
  }

  @Override
  public PetViewPagerAdapter.BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pet_banner, parent, false);
    return new PetViewPagerAdapter.BannerHolder(view);
  }

  @Override
  public void bindVH(@NotNull PetViewPagerAdapter.BannerHolder bannerHolder, int j) {
    RequestOptions requestOptions = new RequestOptions();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + mImageList.get(j).getAnimal_img_path())
        .centerCrop()
        .placeholder(R.drawable.default_dog1)
        .apply(requestOptions)
        .into(bannerHolder.mPetImage);
    bannerHolder.mBannerNameTextView.setText(mImageList.get(j).getAnimal_name());
    bannerHolder.mPetAgeTextView.setText(mImageList.get(j).getAnimal_year() + "살");
    bannerHolder.mCategoryNameTextView.setText(mImageList.get(j).getCategory_name());
    if (mImageList.get(j).getAnimal_character().equals("0")) {
      bannerHolder.mPetCharTextView.setText("온순");
    } else if (mImageList.get(j).getAnimal_character().equals("1")) {
      bannerHolder.mPetCharTextView.setText("입질");
    } else if (mImageList.get(j).getAnimal_character().equals("2")) {
      bannerHolder.mPetCharTextView.setText("호기심많음");
    } else if (mImageList.get(j).getAnimal_character().equals("3")) {
      bannerHolder.mPetCharTextView.setText("활동적");
    }
    if (mImageList.get(j).getAnimal_training().equals("Y")) {
      bannerHolder.mPetTrainingTextView.setText("O");
    } else if (mImageList.get(j).getAnimal_training().equals("N")) {
      bannerHolder.mPetTrainingTextView.setText("X");
    }
    if (mImageList.get(j).getAnimal_neuter().equals("Y")) {
      bannerHolder.mPetTransTextView.setText("O");
    } else if (mImageList.get(j).getAnimal_neuter().equals("N")) {
      bannerHolder.mPetTransTextView.setText("X");
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
    AppCompatImageView mPetImage;
    AppCompatTextView mBannerNameTextView;
    AppCompatTextView mCategoryNameTextView;
    AppCompatTextView mPetAgeTextView;
    AppCompatTextView mPetCharTextView;
    AppCompatTextView mPetTrainingTextView;
    AppCompatTextView mPetTransTextView;

    public BannerHolder(View view) {
      super(view);
      mPetImage = view.findViewById(R.id.pet_image);
      mBannerNameTextView = view.findViewById(R.id.banner_pet_name_text_view);
      mCategoryNameTextView = view.findViewById(R.id.category_name_text_view);
      mPetAgeTextView = view.findViewById(R.id.pet_age_text_view);
      mPetCharTextView = view.findViewById(R.id.pet_char_text_view);
      mPetTrainingTextView = view.findViewById(R.id.training_text_view);
      mPetTransTextView = view.findViewById(R.id.trans_text_view);



    }
  }
}
