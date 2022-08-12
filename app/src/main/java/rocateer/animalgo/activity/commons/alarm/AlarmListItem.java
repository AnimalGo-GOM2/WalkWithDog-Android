package rocateer.animalgo.activity.commons.alarm;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class AlarmListItem extends AbstractExpandableItem<AlarmDetailItem> implements MultiItemEntity {
  public String title;
  public String date;


  public AlarmListItem(String title, String date) {
    this.title = title;
    this.date = date;
  }


  @Override
  public int getLevel() {
    return 0;
  }

  @Override
  public int getItemType() {
    return AlarmAdapter.ALARM_LIST;
  }
}
