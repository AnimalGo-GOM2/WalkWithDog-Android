package rocateer.animalgo.activity.home;

import android.view.View;

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

public class InBoxListAdpater extends BaseQuickAdapter<MemoModel, BaseViewHolder> {
  public InBoxListAdpater(int layoutResId, @Nullable List<MemoModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MemoModel item) {
    RequestOptions requestOptions = new RequestOptions();
    AppCompatTextView noteGenderTextView = helper.getView(R.id.note_member_gender_text_view);
    AppCompatImageView noteUserImageView = helper.getView(R.id.note_user_image_view);
    helper.setText(R.id.user_name_text_view, item.getMember_nickname());
    helper.setText(R.id.note_contents_text_view, item.getContents());
    helper.setText(R.id.note_user_age_text_view, item.getMember_age() + "대");
    helper.setText(R.id.note_date_text_view, item.getIns_date());
    if (item.getMember_gender().equals("0")) {
      noteGenderTextView.setText("남자");
    } else if (item.getMember_gender().equals("1")) {
      noteGenderTextView.setText("여자");
    }
    Glide.with(mContext)
        .load(BaseRouter.BASE_URL + item.getMember_img())
        .apply(requestOptions)
        .placeholder(R.drawable.default_profile)
        .into(noteUserImageView);

    AppCompatImageView mNewImageView = helper.getView(R.id.new_image_view);
    if (item.getRead_yn().equals("Y")) {
      mNewImageView.setVisibility(View.GONE);
    } else {
      mNewImageView.setVisibility(View.VISIBLE);
    }
  }
}