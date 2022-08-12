package rocateer.animalgo.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.MemoModel;

public class SentInBoxAdapter extends BaseQuickAdapter<MemoModel, BaseViewHolder> {
  public SentInBoxAdapter(int layoutResId, @Nullable List<MemoModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MemoModel item) {
    RequestOptions requestOptions = new RequestOptions();
    AppCompatTextView searchUserAgeTextView = helper.getView(R.id.search_user_age_text_view);
    AppCompatTextView searchUserGenderTextView = helper.getView(R.id.search_user_gender_text_view);
    AppCompatImageView searchImageView = helper.getView(R.id.search_user_image_view);
    helper.setText(R.id.search_user_name_text_view, item.getMember_nickname());
    helper.setText(R.id.search_contents_text_view, item.getContents());
    helper.setText(R.id.search_date_text_view, item.getIns_date());
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .placeholder(R.drawable.default_profile)
        .into(searchImageView);
    searchUserAgeTextView.setText(item.getMember_age() + "대");
    if (item.getMember_gender().equals("0")) {
      searchUserGenderTextView.setText("남");
    } else if (item.getMember_gender().equals("1")) {
      searchUserGenderTextView.setText("여");
    }
  }
}