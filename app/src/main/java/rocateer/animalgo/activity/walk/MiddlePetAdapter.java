package rocateer.animalgo.activity.walk;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.models.BaseModel;

public class MiddlePetAdapter extends BaseQuickAdapter<BaseModel, BaseViewHolder> {
  public MiddlePetAdapter(int layoutResId, @Nullable List<BaseModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, BaseModel item) {
  }
}
