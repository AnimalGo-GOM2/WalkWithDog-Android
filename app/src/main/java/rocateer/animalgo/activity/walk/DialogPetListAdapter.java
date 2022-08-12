package rocateer.animalgo.activity.walk;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class DialogPetListAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public DialogPetListAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {
    RequestOptions requestOptions = new RequestOptions();
    AppCompatImageView mMyPetImageView =  helper.getView(R.id.my_pet_image_view);
    AppCompatTextView mMyPetNameTextView = helper.getView(R.id.my_pet_name_text_view);
    helper.addOnClickListener(R.id.pet_checkbox);


    helper.setText(R.id.my_pet_name_text_view, item.getAnimal_name());
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .apply(requestOptions)
        .centerCrop()
        .placeholder(R.drawable.default_dog1)
        .into(mMyPetImageView);

  }
}