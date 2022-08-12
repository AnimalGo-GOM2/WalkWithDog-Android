package rocateer.animalgo.activity.walk;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.BaseModel;

public class SelectDateAdapter extends BaseQuickAdapter<BaseModel, BaseViewHolder> {
  public SelectDateAdapter(int layoutResId, @Nullable List<BaseModel> data) {
    super(layoutResId, data);
  }


    @Override
    protected void convert(BaseViewHolder helper, BaseModel item) {

    RoundRectView dateView = helper.getView(R.id.date_view);
    AppCompatTextView dateTextView = helper.getView(R.id.date_text_view);

    if (item.isSelect()) {
      dateView.setBorderColor(mContext.getColor(R.color.colorAccent));
      dateTextView.setBackgroundColor(mContext.getColor(R.color.color_28E0AB0A));
    } else {
      dateView.setBorderColor(mContext.getColor(R.color.color_ffffff));
      dateTextView.setBackgroundColor(mContext.getColor(R.color.color_ffffff));
    }


  }
}
