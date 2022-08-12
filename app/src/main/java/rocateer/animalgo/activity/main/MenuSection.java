package rocateer.animalgo.activity.main;

import com.chad.library.adapter.base.entity.SectionEntity;

import rocateer.animalgo.models.MenuModel;

public class MenuSection extends SectionEntity<MenuModel> {
  public MenuSection(boolean isHeader, String header) {
    super(isHeader, header);
  }

  public MenuSection(MenuModel menuModel) {
    super(menuModel);
  }
}
