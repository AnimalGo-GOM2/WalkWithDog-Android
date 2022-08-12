package rocateer.animalgo.activity.commons.notice;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NoticeDetailItem extends AbstractExpandableItem<NoticeDetailItem> implements MultiItemEntity {
  String contents;
  String img_path;

  public NoticeDetailItem(String contents, String img_path) {
    this.contents = contents;
    this.img_path = img_path;
  }

  @Override
  public int getLevel() {
    return 1;
  }

  @Override
  public int getItemType() {
    return NoticeListAdapter.NOTICE_DETAIL;
  }
}
