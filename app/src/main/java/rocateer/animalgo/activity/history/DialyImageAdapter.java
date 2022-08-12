package rocateer.animalgo.activity.history;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import timber.log.Timber;

public class DialyImageAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public DialyImageAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    Timber.i("Record_img_path" + item.getRecord_img_path());
    RequestOptions requestOptions = new RequestOptions();
    AppCompatImageView mDialyImageView = helper.getView(R.id.dialy_image_view);
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getRecord_img_path())
        .centerCrop()
        .apply(requestOptions)
        .into(mDialyImageView);

  }
}
