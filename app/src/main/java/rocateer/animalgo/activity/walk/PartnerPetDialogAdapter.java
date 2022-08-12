package rocateer.animalgo.activity.walk;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.api.BaseRouter;

public class PartnerPetDialogAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public PartnerPetDialogAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {


    AppCompatImageView petImageView = helper.getView(R.id.pet_image_view);
    AppCompatTextView petNameTextView = helper.getView(R.id.pet_name_text_view);
    AppCompatTextView petCharTextView = helper.getView(R.id.pet_char_text_view);
    AppCompatTextView petNeuterTextView = helper.getView(R.id.pet_neuter_text_view);
    AppCompatTextView petTrainTextView = helper.getView(R.id.pet_train_text_view);
    AppCompatTextView petGenderTextView = helper.getView(R.id.pet_gender_text_view);

    helper.setText(R.id.pet_name_text_view, item.getAnimal_name());
    helper.setText(R.id.pet_category_text_view, item.getCategory_name());
    helper.setText(R.id.pet_age_text_view, item.getAnimal_year() + "살");
    if (item.getAnimal_character().equals("0")) {
      petCharTextView.setText("온순");
    } else if (item.getAnimal_character().equals("1")) {
      petCharTextView.setText("입질");
    } else if (item.getAnimal_character().equals("2")) {
      petCharTextView.setText("호기심 많음");
    } else if (item.getAnimal_character().equals("3")) {
      petCharTextView.setText("활동적");
    }
    if (item.getAnimal_neuter().equals("Y")) {
      petNeuterTextView.setText("O");
    } else if (item.getAnimal_neuter().equals("N")) {
      petNeuterTextView.setText("X");
    }
    if (item.getAnimal_training().equals("Y")) {
      petTrainTextView.setText("O");
    } else if (item.getAnimal_training().equals("N")) {
      petTrainTextView.setText("X");
    }
    if (item.getAnimal_gender().equals("0")) {
      petGenderTextView.setText("남아");
    } else if (item.getAnimal_gender().equals("1")) {
      petGenderTextView.setText("여아");
    }


    RequestOptions requestOptions = new RequestOptions();
    requestOptions.centerCrop();

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .apply(requestOptions)
        .centerCrop()
        .into(petImageView);





  }
}
