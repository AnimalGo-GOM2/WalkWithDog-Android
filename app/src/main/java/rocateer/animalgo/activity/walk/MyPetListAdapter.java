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
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.api.BaseRouter;

public class MyPetListAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public MyPetListAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {

    RoundRectView petView = helper.getView(R.id.pet_view);
    LinearLayout petLayout = helper.getView(R.id.pet_layout);
    AppCompatImageView petImageView = helper.getView(R.id.pet_image_view);
    AppCompatTextView petNameTextView = helper.getView(R.id.pet_name_text_view);


    if (item.isSelect()) {
      petView.setBorderColor(mContext.getColor(R.color.colorAccent));
      petLayout.setBackgroundColor(mContext.getColor(R.color.color_28E0AB0A));
    } else {
      petView.setBorderColor(mContext.getColor(R.color.color_EAE8E5));
      petLayout.setBackgroundColor(mContext.getColor(R.color.color_ffffff));
    }
    RequestOptions requestOptions = new RequestOptions();

    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .apply(requestOptions)
        .centerCrop()
        .into(petImageView);
    petNameTextView.setText(item.getAnimal_name());





  }
}
