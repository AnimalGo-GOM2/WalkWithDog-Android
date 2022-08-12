package rocateer.animalgo.activity.commons.alarm;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;



public class AlarmDetailItem extends AbstractExpandableItem<AlarmDetailItem> implements MultiItemEntity {
  String msg;

  public AlarmDetailItem(String msg) {
    this.msg = msg;
  }

  @Override
  public int getLevel() {
    return 1;
  }

  @Override
  public int getItemType() {
    return AlarmAdapter.ALARM_DETAIL;
  }
}

