package rocateer.animalgo.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.MemoModel;

public class SendNoteListAdapter extends BaseQuickAdapter<MemoModel, BaseViewHolder> {
  public SendNoteListAdapter(int layoutResId, @Nullable List<MemoModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MemoModel item) {
    AppCompatImageView sendUserImageView = helper.getView(R.id.send_user_image_view);
    helper.setText(R.id.send_user_name_text_view, item.getMember_nickname());
    helper.setText(R.id.send_date_text_view, item.getMemo_date());
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .into(sendUserImageView);
  }
}
