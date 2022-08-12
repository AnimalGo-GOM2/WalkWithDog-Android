package rocateer.animalgo.activity.walk;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.thedeanda.lorem.LoremIpsum;

import java.util.List;

import butterknife.BindView;
import co.lujun.androidtagview.TagContainerLayout;
import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class WithFriendAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public WithFriendAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {

    AppCompatImageView userImageView = helper.getView(R.id.user_image_view);
    AppCompatImageView petImageView = helper.getView(R.id.pet_image_view);
    AppCompatTextView neuterTextView = helper.getView(R.id.neuter_text_view);
    AppCompatTextView charactorTextView = helper.getView(R.id.character_text_view);
    AppCompatTextView trainTextView = helper.getView(R.id.train_text_view);

    helper.setText(R.id.user_nickname_text_view, item.getMember_nickname());
    helper.setText(R.id.user_age_text_view, item.getMember_age() + "대");
    if (item.getMember_gender().equals("0")) {
      helper.setText(R.id.user_gender_text_view, "남성");
    } else if (item.getMember_gender().equals("1")) {
      helper.setText(R.id.user_gender_text_view, "여성");
    }
    helper.setText(R.id.walk_date_text_view,"산책일시 : " + item.getRecord_date());

    RequestOptions requestOptions = new RequestOptions();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .centerCrop()
        .apply(requestOptions)
        .into(userImageView);
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_array().get(0).getAnimal_img_path())
        .apply(requestOptions)
        .placeholder(R.drawable.default_dog1)
        .centerCrop()
        .into(petImageView);
    helper.setText(R.id.pet_name_text_view, item.getAnimal_array().get(0).getAnimal_name());
    helper.setText(R.id.pet_category_name_text_view, item.getAnimal_array().get(0).getCategory_name());
    if (item.getAnimal_array().get(0).getAnimal_gender().equals("0")) {
      helper.setText(R.id.pet_gender_text_view, "남아");
    } else if (item.getAnimal_array().get(0).getAnimal_gender().equals("1")) {
      helper.setText(R.id.pet_gender_text_view, "여아");
    }
    helper.setText(R.id.pet_age_text_view, item.getAnimal_array().get(0).getAnimal_year());

    if (item.getAnimal_array().get(0).getAnimal_character().equals("0")) {
      charactorTextView.setText("#온순");
    } else if (item.getAnimal_array().get(0).getAnimal_character().equals("1")) {
      charactorTextView.setText("#입질");
    } else if (item.getAnimal_array().get(0).getAnimal_character().equals("2")) {
      charactorTextView.setText("#호기심많음");
    } else if (item.getAnimal_array().get(0).getAnimal_character().equals("3")) {
      charactorTextView.setText("#활동적");
    }

    if (item.getAnimal_array().get(0).getAnimal_training().equals("N")) {
      trainTextView.setText("#훈련 안 했아요");
    } else if (item.getAnimal_array().get(0).getAnimal_training().equals("Y")) {
      trainTextView.setText("#훈련 했어요");
    }

    if (item.getAnimal_array().get(0).getAnimal_neuter().equals("N")) {
      neuterTextView.setText("#중성화 안 했어요");
    } else if (item.getAnimal_array().get(0).getAnimal_neuter().equals("Y")) {
      neuterTextView.setText("#중성화 했어요");
    }
  }
}