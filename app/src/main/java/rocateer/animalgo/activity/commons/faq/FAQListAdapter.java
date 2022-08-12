package rocateer.animalgo.activity.commons.faq;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.api.BaseRouter;

public class FAQListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  public static final int FAQ_LIST = 0;
  public static final int FAQ_DETAIL = 1;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  public FAQListAdapter(List<MultiItemEntity> data) {
    super(data);
    addItemType(FAQ_LIST, R.layout.row_faq_list);
    addItemType(FAQ_DETAIL, R.layout.row_faq_detail);
  }

  @Override
  protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
    switch (helper.getItemViewType()) {
      case FAQ_LIST:
        final FAQListItem faqListItem = (FAQListItem) item;
        helper.setText(R.id.faq_title_text, faqListItem.title);
        AppCompatTextView mFaqTypeText = helper.getView(R.id.faq_type_text);
        if (faqListItem.faqType.equals("0")) {
          mFaqTypeText.setText("[회원서비스]");
        } else if (faqListItem.faqType.equals("1")) {
          mFaqTypeText.setText("[산책하기]");
        } else if (faqListItem.faqType.equals("2")) {
          mFaqTypeText.setText("[산책기록]");
        } else if (faqListItem.faqType.equals("3")) {
          mFaqTypeText.setText("[앱사용]");
        } else if (faqListItem.faqType.equals("4")) {
          mFaqTypeText.setText("[기타]");
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int pos = helper.getAdapterPosition();
            if (faqListItem.isExpanded()) {
              collapse(pos, true);
            } else {
              expand(pos, true);
            }
          }
        });
        break;
      case FAQ_DETAIL:
        final FAQDetailItem faqDetailItem = (FAQDetailItem) item;
        helper.setText(R.id.faq_detail_text, faqDetailItem.content);
        RequestOptions requestOptions = new RequestOptions();
        AppCompatImageView mFaqImageView = helper.getView(R.id.faq_image_view);
        if (faqDetailItem.img != null) {
          mFaqImageView.setVisibility(View.VISIBLE);
          Glide.with(mContext)
              .load(BaseRouter.BASE_URL + faqDetailItem.img)
              .into(mFaqImageView);
        } else {
          mFaqImageView.setVisibility(View.GONE);
        }
        break;
    }
  }
}
