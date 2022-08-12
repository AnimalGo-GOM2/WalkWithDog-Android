package rocateer.animalgo.activity.walk;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class ApplyFriendListAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public ApplyFriendListAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    RequestOptions requestOptions = new RequestOptions();
    AppCompatImageView newChatYnImageView = helper.getView(R.id.new_chat_yn_image_view);
    AppCompatImageView supportPetImageView = helper.getView(R.id.support_pet_image_view);
    helper.setText(R.id.support_pet_name_text_view, item.getAnimal_array().get(0).getAnimal_name());
    helper.setText(R.id.walk_start_time_text_view, item.getRecord_date());

    helper.setText(R.id.addr_text_view, item.getRecord_addr());
    if (item.getNew_chat_yn().equals("Y")) {
      newChatYnImageView.setVisibility(View.VISIBLE);
    } else if (item.getNew_chat_yn().equals("N")) {
      newChatYnImageView.setVisibility(View.GONE);
    }
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_array().get(0).getAnimal_img_path())
        .placeholder(R.drawable.default_dog2)
        .into(supportPetImageView);

  }
}
