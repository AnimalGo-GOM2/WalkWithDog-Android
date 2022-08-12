package rocateer.animalgo.models;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MessageItem implements MultiItemEntity {
  public static final int HEADER = 0;
  public static final int TEXT = 1;
  private int itemType;

  public MessageItem(int itemType) {
    this.itemType = itemType;
  }

  @Override
  public int getItemType() {
    return itemType;
  }
}