package rocateer.animalgo.activity.walk;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.MessageItem;

public class MessageAdapter extends BaseMultiItemQuickAdapter<MessageItem, BaseViewHolder> {

  /**
   * Same as QuickAdapter#QuickAdapter(Context,int) but with
   * some initialization data.
   *
   * @param data A new list is created out of this one to avoid mutable list
   */
  public MessageAdapter(List<MessageItem> data) {
    super(data);
    addItemType(MessageItem.HEADER, R.layout.row_message_header);
    addItemType(MessageItem.TEXT, R.layout.row_reply_message_text);
  }

  @Override
  protected void convert(BaseViewHolder helper, MessageItem item) {
    switch (helper.getItemViewType()) {
      case MessageItem.HEADER:
        break;
      case MessageItem.TEXT:
        break;
    }
    helper.addOnClickListener(R.id.send_button);
  }
}