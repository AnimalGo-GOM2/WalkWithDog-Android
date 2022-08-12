package rocateer.animalgo.activity.walk;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;

public class SmallPetAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public SmallPetAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {
    helper.setText(R.id.pet_category_name_text_view, item.getCategory_name());
  }
}