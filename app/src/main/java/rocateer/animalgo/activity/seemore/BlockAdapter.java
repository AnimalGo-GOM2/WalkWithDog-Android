package rocateer.animalgo.activity.seemore;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;

public class BlockAdapter extends BaseQuickAdapter<MemberModel, BaseViewHolder> {
  public BlockAdapter(int layoutResId, @Nullable List<MemberModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MemberModel item) {
    AppCompatTextView mUserNickNameTextView = helper.getView(R.id.user_nickname_text_view);
    AppCompatImageView mUserImageView= helper.getView(R.id.user_image_view);
    helper.addOnClickListener(R.id.block_button);

    RequestOptions requestOptions = new RequestOptions();
    mUserNickNameTextView.setText(item.getMember_nickname());
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .apply(requestOptions)
        .centerCrop()
        .into(mUserImageView);

  }
}
