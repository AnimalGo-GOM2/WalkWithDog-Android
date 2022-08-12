package rocateer.animalgo.activity.main;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.MenuModel;

public class MenuAdapter extends BaseSectionQuickAdapter<MenuSection, BaseViewHolder> {


  /**
   * Same as QuickAdapter#QuickAdapter(Context,int) but with
   * some initialization data.
   *
   * @param layoutResId      The layout resource id of each item.
   * @param sectionHeadResId The section head layout id for each item
   * @param data             A new list is created out of this one to avoid mutable list
   */
  public MenuAdapter(int layoutResId, int sectionHeadResId, List<MenuSection> data) {
    super(layoutResId, sectionHeadResId, data);
  }

  @Override
  protected void convertHead(BaseViewHolder helper, MenuSection item) {
    helper.setText(R.id.section_title_text_view, item.header);
  }

  @Override
  protected void convert(BaseViewHolder helper, MenuSection item) {
    MenuModel menuModel = item.t;
    helper.setText(R.id.title_text_view, menuModel.getTitle());
    helper.setText(R.id.description_text_view, menuModel.getDescription());
  }
}
