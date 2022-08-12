package rocateer.animalgo.activity.commons.notice;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.api.BaseRouter;

public class NoticeListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  public static final int NOTICE_LIST = 0;
  public static final int NOTICE_DETAIL = 1;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  public NoticeListAdapter(List<MultiItemEntity> data) {
    super(data);
    addItemType(NOTICE_LIST, R.layout.row_notice_list);
    addItemType(NOTICE_DETAIL, R.layout.row_notice_detail);
  }

  @Override
  protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
    switch (helper.getItemViewType()) {
      case NOTICE_LIST:
        final NoticeListItem noticeListItem = (NoticeListItem) item;
        helper.setText(R.id.title_text_view, noticeListItem.title);
        helper.setText(R.id.date_text_view, noticeListItem.date);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int pos = helper.getAdapterPosition();
            if (noticeListItem.isExpanded()) {
              collapse(pos, true);
            } else {
              expand(pos, true);
            }
          }
        });
        break;
      case NOTICE_DETAIL:
        final NoticeDetailItem noticeDetailItem = (NoticeDetailItem) item;
        RequestOptions requestOptions = new RequestOptions();
        AppCompatImageView mNoticeImage = helper.getView(R.id.notice_image);
        helper.setText(R.id.notice_detail_text, noticeDetailItem.contents);
        if (noticeDetailItem.img_path != null) {
          Glide.with(mContext)
              .load(BaseRouter.BASE_URL + noticeDetailItem.img_path)
              .apply(requestOptions)
              .into(mNoticeImage);
          mNoticeImage.setVisibility(View.VISIBLE);
        } else {
          mNoticeImage.setVisibility(View.GONE);
        }
        break;
    }
  }
}
