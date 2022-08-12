package rocateer.animalgo.activity.walk;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.AnimalModel;

public class PetSizeAdapter extends BaseQuickAdapter<AnimalModel, BaseViewHolder> {
  public PetSizeAdapter(int layoutResId, @Nullable List<AnimalModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, AnimalModel item) {
    helper.setText(R.id.pet_size_text_view, item.getCategory_name());
    RoundRectView petView = helper.getView(R.id.pet_view);
    LinearLayout petLayout = helper.getView(R.id.pet_layout);
    AppCompatTextView petSizeTextView = helper.getView(R.id.pet_size_text_view);
    if (item.isSelect()) {
      petView.setBorderColor(mContext.getColor(R.color.colorAccent));
      petLayout.setBackgroundColor(mContext.getColor(R.color.color_28E0AB0A));
      petSizeTextView.setTextColor(mContext.getColor(R.color.colorAccent));
    } else {
      petView.setBorderColor(mContext.getColor(R.color.color_ffffff));
      petLayout.setBackgroundColor(mContext.getColor(R.color.color_f9f9f9));
      petSizeTextView.setTextColor(mContext.getColor(R.color.color_999999));
    }
  }
}

