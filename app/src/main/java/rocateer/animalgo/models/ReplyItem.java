package rocateer.animalgo.models;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ReplyItem  implements MultiItemEntity {
  public static final int HEADER = 0;
  public static final int USER = 1;
  public static final int MY = 2;
  private int itemType;
  private WalkModel walkModel;

  public ReplyItem(int itemType, WalkModel walkModel) {
    this.itemType = itemType;
    this.walkModel = walkModel;
  }

  @Override
  public int getItemType() {
    return itemType;
  }
  public WalkModel getWalkModel() {
    return walkModel;
  }
}
