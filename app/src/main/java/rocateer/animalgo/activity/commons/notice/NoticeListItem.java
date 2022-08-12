package rocateer.animalgo.activity.commons.notice;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;



public class NoticeListItem extends AbstractExpandableItem<NoticeDetailItem> implements MultiItemEntity {
  public String title;
  public String date;


  public NoticeListItem(String title, String date) {
    this.title = title;
    this.date = date;
  }


  @Override
  public int getLevel() {
    return 0;
  }

  @Override
  public int getItemType() {
    return NoticeListAdapter.NOTICE_LIST;
  }
}