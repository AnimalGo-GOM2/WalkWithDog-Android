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

public class AddFriendsListAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  public AddFriendsListAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }


  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    AppCompatImageView petImageView = helper.getView(R.id.pet_image_view);
    AppCompatImageView newChatYnView = helper.getView(R.id.new_chat_yn_view);
    RequestOptions requestOptions = new RequestOptions();
    int memberAnimalCnt = Integer.parseInt(String.valueOf(item.getMember_animal_cnt()));
    if (memberAnimalCnt > 1) {
      helper.setText(R.id.pet_name_text_view, item.getAnimal_name() +" 외 " + (memberAnimalCnt -1) + "마리");
    } else {
      helper.setText(R.id.pet_name_text_view, item.getAnimal_name());
    }
    helper.setText(R.id.location_text_view, item.getRecord_addr());

    helper.setText(R.id.walk_start_date_text_view, item.getRecord_date());
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getAnimal_img_path())
        .placeholder(R.drawable.default_dog2)
        .into(petImageView);
    if (item.getNew_chat_yn().equals("Y")) {
      newChatYnView.setVisibility(View.VISIBLE);
    } else if (item.getNew_chat_yn().equals("N")) {
      newChatYnView.setVisibility(View.GONE);
    }
  }
}
