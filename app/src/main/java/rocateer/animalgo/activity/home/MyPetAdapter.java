package rocateer.animalgo.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class MyPetAdapter extends BaseQuickAdapter<MainModel, BaseViewHolder> {
  public MyPetAdapter(int layoutResId, @Nullable List<MainModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MainModel item) {
    AppCompatImageView myPetImageView = helper.getView(R.id.my_pet_image_view);

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .placeholder(R.drawable.default_profile)
        .into(myPetImageView);
  }
}

