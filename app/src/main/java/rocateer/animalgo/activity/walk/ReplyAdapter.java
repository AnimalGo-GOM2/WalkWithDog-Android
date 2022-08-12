package rocateer.animalgo.activity.walk;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.ReplyItem;

public class ReplyAdapter extends BaseMultiItemQuickAdapter<ReplyItem, BaseViewHolder> {

  public ReplyAdapter(List<ReplyItem> data) {
    super(data);
    addItemType(ReplyItem.HEADER, R.layout.row_message_header);
    addItemType(ReplyItem.USER, R.layout.row_reply_message_text);
    addItemType(ReplyItem.MY, R.layout.row_reply_my);
  }

  @Override
  protected void convert(BaseViewHolder helper, ReplyItem item) {
//    helper.setText(R.id.chat_date_text_view, item.getWalkModel().getSt_date());

    switch (helper.getItemViewType()) {
      case ReplyItem.HEADER:
        helper.setText(R.id.chat_date_text_view, item.getWalkModel().getSt_date());
        break;
      case ReplyItem.USER:
        helper.setText(R.id.user_text_view, item.getWalkModel().getComment());
        helper.setText(R.id.time_text_view, item.getWalkModel().getIns_hi());
        break;
      case ReplyItem.MY:
        helper.setText(R.id.my_contents_text_view, item.getWalkModel().getComment());
        helper.setText(R.id.message_time_text_view, item.getWalkModel().getIns_hi());
        break;
    }
  }
}
