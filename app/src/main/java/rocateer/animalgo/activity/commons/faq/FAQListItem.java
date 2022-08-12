package rocateer.animalgo.activity.commons.faq;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class FAQListItem extends AbstractExpandableItem<FAQDetailItem> implements MultiItemEntity {
  public String title;
  public String faqType;


  public FAQListItem(String title, String faqType) {
    this.title = title;
    this.faqType = faqType;
  }

  @Override
  public int getLevel() {
    return 0;
  }

  @Override
  public int getItemType() {
    return FAQListAdapter.FAQ_LIST;
  }
}