package rocateer.animalgo.activity.walk;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class WalkHistoryMyPetAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public WalkHistoryMyPetAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    AppCompatImageView myPetImageView = helper.getView(R.id.history_my_pet_iamge_view);

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .placeholder(R.drawable.default_dog1)
        .into(myPetImageView);
  }
}
