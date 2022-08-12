package rocateer.animalgo.activity.mypet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.api.BaseRouter;

public class MyPetListAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public MyPetListAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {
    AppCompatImageView myPetImageView = helper.getView(R.id.my_pet_image_view);
    AppCompatTextView animalGenderTextview = helper.getView(R.id.animal_gender_text_view);
    AppCompatTextView animalneuterTextView = helper.getView(R.id.my_pet_neuter_yn_text_view);
    helper.setText(R.id.my_animal_name_text_view, item.getAnimal_name());
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .placeholder(R.drawable.default_profile)
        .into(myPetImageView);
    if (item.getAnimal_gender().equals("1")) {
      animalGenderTextview.setText("여아");
    } else if (item.getAnimal_gender().equals("0")) {
      animalGenderTextview.setText("남아");
    }
    helper.setText(R.id.my_pet_walk_cnt_text_view, item.getRecord_cnt() + "회");
    helper.setText(R.id.animal_year_text_view, item.getAnimal_birth());
    helper.setText(R.id.my_pet_age_text_view, item.getAnimal_year()+"살");
    if (item.getAnimal_neuter().equals("Y")) {
      animalneuterTextView.setText("O");
    } else if (item.getAnimal_neuter().equals("N")) {
      animalneuterTextView.setText("X");
    }
    helper.setText(R.id.my_pet_category_text_view, "(" +item.getSecond_category()+")");
    helper.setText(R.id.my_pet_second_category_text_view,  item.getFirst_category());
  }
}
