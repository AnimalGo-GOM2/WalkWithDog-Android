package rocateer.animalgo.activity.walk;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.thedeanda.lorem.LoremIpsum;

import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class ApplyFriendsAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public ApplyFriendsAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {


    AppCompatImageView applyUserImageView = helper.getView(R.id.apply_user_image_view);
    AppCompatImageView applyUserPetImageView = helper.getView(R.id.apply_user_pet_image_view);
    helper.setText(R.id.apply_friend_nickname_text_view, item.getMember_nickname());
    if (item.getMember_gender().equals("0")) {
      helper.setText(R.id.apply_user_gender_text_view, "남성");
    } else if (item.getMember_gender().equals("1")) {
      helper.setText(R.id.apply_user_gender_text_view, "여성");
    }
    helper.setText(R.id.apply_user_age_text_view, item.getMember_age() + "대");
    helper.setText(R.id.walk_start_date_text_view, item.getRecord_date());
    helper.setText(R.id.apply_user_pet_name_text_view, item.getAnimal_array().get(0).getAnimal_name());
    helper.setText(R.id.apply_user_pet_category_text_view, item.getAnimal_array().get(0).getCategory_name());
    if (item.getAnimal_array().get(0).getAnimal_gender().equals("0")) {
      helper.setText(R.id.apply_user_pet_gender_text_view, "남아");
    } else if (item.getAnimal_array().get(0).getAnimal_gender().equals("1")) {
      helper.setText(R.id.apply_user_pet_gender_text_view, "여아");
    }
    helper.setText(R.id.apply_user_pet_year_text_view, item.getAnimal_array().get(0).getAnimal_year() +"대");

    RequestOptions requestOptions = new RequestOptions();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .apply(requestOptions)
        .centerCrop()
        .into(applyUserImageView);

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_array().get(0).getAnimal_img_path())
        .placeholder(R.drawable.default_dog1)
        .apply(requestOptions)
        .centerCrop()
        .into(applyUserPetImageView);
    AppCompatImageView mNewImageView = helper.getView(R.id.new_image_view);
    if (item.getNew_chat_yn().equals("N")) {
      mNewImageView.setVisibility(View.GONE);
    } else if (item.getNew_chat_yn().equals("Y")) {
      mNewImageView.setVisibility(View.VISIBLE);
    }
  }
}