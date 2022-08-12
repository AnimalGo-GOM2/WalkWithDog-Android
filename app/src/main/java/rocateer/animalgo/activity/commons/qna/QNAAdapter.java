package rocateer.animalgo.activity.commons.qna;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.models.QnaModel;

public class QNAAdapter extends BaseQuickAdapter<QnaModel, BaseViewHolder> {
    public QNAAdapter(int layoutResId, @Nullable List<QnaModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QnaModel item) {
        helper.setText(R.id.title_text_view, item.getQa_title());
        helper.setText(R.id.date_text_view, item.getIns_date());

        AppCompatTextView mQnaTypeTextView = helper.getView(R.id.qna_type_text_view);
        AppCompatTextView mReplyStateTextView = helper.getView(R.id.reply_state_text_view);

        if (item.getQa_category().equals("0")) {
            mQnaTypeTextView.setText("회원서비스");
        } else if (item.getQa_category().equals("1")) {
            mQnaTypeTextView.setText("산책하기");
        } else if (item.getQa_category().equals("2")) {
            mQnaTypeTextView.setText("산책기록");
        } else if (item.getQa_category().equals("3")) {
            mQnaTypeTextView.setText("앱사용");
        } else if (item.getQa_category().equals("4")) {
            mQnaTypeTextView.setText("기타");
        }

        if (item.getReply_yn().equals("Y")) {
            mReplyStateTextView.setText("답변");
            mReplyStateTextView.setBackgroundColor(mContext.getColor(R.color.colorAccent));
            mReplyStateTextView.setTextColor(mContext.getColor(R.color.color_ffffff));
        } else if (item.getReply_yn().equals("N")) {
            mReplyStateTextView.setText("미답변");
            mReplyStateTextView.setBackgroundColor(mContext.getColor(R.color.color_f6f6f6));
            mReplyStateTextView.setTextColor(mContext.getColor(R.color.color_666666));
        }
    }
}
