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
import rocateer.animalgo.models.api.BaseRouter;

public class ComplimentAdapter extends BaseQuickAdapter<MainModel, BaseViewHolder> {
  public ComplimentAdapter(int layoutResId, @Nullable List<MainModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MainModel item) {

    helper.setText(R.id.compliment_star_text_view, item.getReview_star());
    helper.setText(R.id.compliment_nickname_text_view, item.getMember_nickname());
    AppCompatImageView complimentImageView = helper.getView(R.id.compliment_image_view);
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .into(complimentImageView);
  }
}