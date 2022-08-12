package rocateer.animalgo.activity.commons.faq;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class FAQDetailItem extends AbstractExpandableItem<FAQDetailItem> implements MultiItemEntity {
  String content;
  String img;

  public FAQDetailItem(String content, String img) {
    this.content = content;
    this.img = img;
  }

  @Override
  public int getLevel() {
    return 1;
  }

  @Override
  public int getItemType() {
    return FAQListAdapter.FAQ_DETAIL;
  }
}
