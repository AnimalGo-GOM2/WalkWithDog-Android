package rocateer.animalgo.activity.commons.alarm;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.commons.notice.NoticeDetailItem;
import rocateer.animalgo.activity.commons.notice.NoticeListItem;
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.api.BaseRouter;

public class AlarmAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  public static final int ALARM_LIST = 0;
  public static final int ALARM_DETAIL = 1;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  public AlarmAdapter(List<MultiItemEntity> data) {
    super(data);
    addItemType(ALARM_LIST, R.layout.row_alarm_list);
    addItemType(ALARM_DETAIL, R.layout.row_alarm_detail);
  }

  @Override
  protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
    switch (helper.getItemViewType()) {
      case ALARM_LIST:
        final AlarmListItem alarmListItem = (AlarmListItem) item;
        helper.setText(R.id.title_text_view, alarmListItem.title);
        helper.setText(R.id.date_text_view, alarmListItem.date);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int pos = helper.getAdapterPosition();
            if (alarmListItem.isExpanded()) {
              collapse(pos, true);
            } else {
              expand(pos, true);
            }
          }
        });
        break;
      case ALARM_DETAIL:
        final AlarmDetailItem alarmDetailItem = (AlarmDetailItem) item;
        helper.setText(R.id.alarm_detail_text, alarmDetailItem.msg);

        break;
    }
  }
}

