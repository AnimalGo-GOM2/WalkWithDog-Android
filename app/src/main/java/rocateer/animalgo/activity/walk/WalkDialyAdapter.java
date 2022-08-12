package rocateer.animalgo.activity.walk;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.ImageModel;
import rocateer.animalgo.models.api.BaseRouter;

public class WalkDialyAdapter extends BaseQuickAdapter<ImageModel, BaseViewHolder> {

  public WalkDialyAdapter(int layoutResId, @Nullable List<ImageModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, ImageModel item) {
    RoundRectView mAddImageRect = helper.getView(R.id.add_image_rect_view);
    AppCompatImageView mWalkDialyImageView = helper.getView(R.id.walk_dialy_image_view);
    AppCompatImageView mDeleteImageView = helper.getView(R.id.delete_image_view);


    if (helper.getAdapterPosition() == 0) {
      mAddImageRect.setVisibility(View.VISIBLE);
      mWalkDialyImageView.setVisibility(View.GONE);
      mDeleteImageView.setVisibility(View.GONE);
    } else {
      mAddImageRect.setVisibility(View.GONE);
      mWalkDialyImageView.setVisibility(View.VISIBLE);
      mDeleteImageView.setVisibility(View.VISIBLE);

      RequestOptions requestOptions = new RequestOptions();
      requestOptions.centerCrop();
      Glide.with(mContext)
          .load(BaseRouter.BASE_URL + item.getImg_path())
          .apply(requestOptions).dontAnimate()
          .into(mWalkDialyImageView);
    }
    helper.addOnClickListener(R.id.delete_image_view); //delete image
  }
}