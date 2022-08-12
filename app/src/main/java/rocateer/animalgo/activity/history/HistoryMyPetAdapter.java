package rocateer.animalgo.activity.history;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class HistoryMyPetAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public HistoryMyPetAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    AppCompatImageView myPetImageView = helper.getView(R.id.my_pet_image_view);

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .placeholder(R.drawable.default_profile)
        .into(myPetImageView);
  }
}


