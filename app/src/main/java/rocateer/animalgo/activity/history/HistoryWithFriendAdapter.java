package rocateer.animalgo.activity.history;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class HistoryWithFriendAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public HistoryWithFriendAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    RequestOptions requestOptions = new RequestOptions();
    AppCompatImageView historyPetImageView = helper.getView(R.id.history_pet_image_view);
    AppCompatImageView historyAnimalImageView = helper.getView(R.id.history_animal_image_view);
    AppCompatImageView memberImageView = helper.getView(R.id.member_image_view);
    AppCompatTextView historyGenderTextView = helper.getView(R.id.history_gender_text_view);
    AppCompatTextView petGenderTextView = helper.getView(R.id.pet_gender_text_view);
    AppCompatTextView petCntTextView = helper.getView(R.id.pet_cnt_text_view);
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMy_animal_array().get(0).getAnimal_img_path())
        .placeholder(R.drawable.default_dog2)
        .into(historyPetImageView);

    helper.setText(R.id.history_record_distant_text_view, item.getRecord_distant() + "km");
    helper.setText(R.id.history_record_hout_text_view, "총 " + item.getRecord_hour() +"분");
    helper.setText(R.id.record_start_date_text_view, item.getRecord_start_date());

    helper.setText(R.id.history_name_text_view, item.getMember_nickname());
    helper.setText(R.id.history_age_text_view, item.getMember_age() + "대");
    if (item.getMember_gender().equals("0")) {
      historyGenderTextView.setText("남성");
    } else if (item.getMember_gender().equals("1")) {
      historyGenderTextView.setText("여성");
    }
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getPartner_animal_array().get(0).getAnimal_img_path())
        .placeholder(R.drawable.default_dog2)
        .into(historyAnimalImageView);
    helper.setText(R.id.pet_name_text_view, item.getAnimal_name());

    if (item.getPartner_animal_cnt().equals("1")) {
      petCntTextView.setVisibility(View.GONE);
    } else {
      petCntTextView.setText(item.getPartner_animal_cnt());
    }
    helper.setText(R.id.pet_name_text_view, item.getPartner_animal_array().get(0).getAnimal_name());
    if (item.getPartner_animal_array().get(0).getAnimal_gender().equals("0")) {
      petGenderTextView.setText("남아");
    } else if (item.getPartner_animal_array().get(0).getAnimal_gender().equals("1")) {
      petGenderTextView.setText("여아");
    }
    helper.setText(R.id.pet_age_text_view, item.getPartner_animal_array().get(0).getAnimal_year());
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .into(memberImageView);
  }
}