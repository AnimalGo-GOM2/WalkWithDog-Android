package rocateer.animalgo.activity.home;

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
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.api.BaseRouter;
import timber.log.Timber;

public class HomeListAdapter extends BaseQuickAdapter<MainModel, BaseViewHolder> {
  public HomeListAdapter(int layoutResId, @Nullable List<MainModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MainModel item) {
    AppCompatTextView mRankAnimalGenderTextView= helper.getView(R.id.rank_animal_gender_text_view);
    AppCompatTextView mRankUserGenderTextView = helper.getView(R.id.rank_user_gender_text_view);
    AppCompatTextView mRankUserAgeTextView = helper.getView(R.id.rank_user_age_text_view);
    AppCompatImageView mRankUserImageView = helper.getView(R.id.rank_user_image_view);
    AppCompatImageView mRankAnimalImageView = helper.getView(R.id.rank_animal_image_view);
    AppCompatTextView mNeuterTextView = helper.getView(R.id.neuter_text_view);
    AppCompatTextView mCharatorTextView = helper.getView(R.id.character_text_view);
    AppCompatTextView mTrainTextView = helper.getView(R.id.train_text_view);
    RequestOptions requestOptions = new RequestOptions();

    helper.setText(R.id.rank_user_name_text_view, item.getMember_nickname());
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .into(mRankUserImageView);
    mRankUserAgeTextView.setText(item.getMember_age() + "???");
    if (item.getMember_gender().equals("0")) {
      mRankUserGenderTextView.setText("??????");
    } else if (item.getMember_gender().equals("1")) {
      mRankUserGenderTextView.setText("??????");
    }
    String[] animalIdx = item.getMember_animal_idxs().split(",");
    int animalCnt = animalIdx.length;
    Timber.i("animalCnt" + animalCnt);
    if (animalCnt > 1) {
      helper.setText(R.id.rank_animal_name_text_view, item.getAnimal_obj().getAnimal_name() + "" + "???" + "" + (animalCnt -1) + "??????");
    } else {
      helper.setText(R.id.rank_animal_name_text_view, item.getAnimal_obj().getAnimal_name());
    }
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_obj().getAnimal_img_path())
        .placeholder(R.drawable.default_dog2)
        .into(mRankAnimalImageView);
    helper.setText(R.id.rank_animal_category_name_text_view, item.getAnimal_obj().getCategory_name());
    if (item.getAnimal_obj().getAnimal_gender().equals("0")) {
      mRankAnimalGenderTextView.setText("??????");
    } else if (item.getAnimal_obj().getAnimal_gender().equals("1")) {
      mRankAnimalGenderTextView.setText("??????");
    }
    helper.setText(R.id.rank_user_total_walk_text_view, "??? " + item.getRecord_cnt() + "???");
    helper.setText(R.id.rank_animal_age_text_view, item.getAnimal_obj().getAnimal_year());

    if (item.getAnimal_obj().getAnimal_character().equals("0")) {
      mCharatorTextView.setText("#??????");
    } else if (item.getAnimal_obj().getAnimal_character().equals("1")) {
      mCharatorTextView.setText("#??????");
    } else if (item.getAnimal_obj().getAnimal_character().equals("2")) {
      mCharatorTextView.setText("#???????????????");
    } else if (item.getAnimal_obj().getAnimal_character().equals("3")) {
      mCharatorTextView.setText("#?????????");
    }

    if (item.getAnimal_obj().getAnimal_training().equals("N")) {
      mTrainTextView.setText("#?????? ??? ?????????");
    } else if (item.getAnimal_obj().getAnimal_training().equals("Y")) {
      mTrainTextView.setText("#?????? ?????????");
    }

    if (item.getAnimal_obj().getAnimal_neuter().equals("N")) {
      mNeuterTextView.setText("#????????? ??? ?????????");
    } else if (item.getAnimal_obj().getAnimal_neuter().equals("Y")) {
      mNeuterTextView.setText("#????????? ?????????");
    }
  }
}
