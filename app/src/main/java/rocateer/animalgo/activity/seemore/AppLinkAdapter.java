package rocateer.animalgo.activity.seemore;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.MemberModel;

public class AppLinkAdapter extends BaseQuickAdapter<MemberModel, BaseViewHolder> {
  public AppLinkAdapter(int layoutResId, @Nullable List<MemberModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, MemberModel item) {
    helper.setText(R.id.title_text_view, item.getTitle());
    helper.setText(R.id.contents_text_view, item.getContents());
  }
}
