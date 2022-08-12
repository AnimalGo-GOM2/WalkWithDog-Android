package rocateer.animalgo.activity.commons.qna;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.QnaModel;
import rocateer.animalgo.models.api.CommonRouter;

public class QNADetailActivity extends RocateerActivity {
  public interface QNADetailLitener {
    void onDelete(int postion);
  }
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, QnaModel qnaModel , int position, QNADetailLitener qNADeleteListener) {
    Intent intent = new Intent(context, QNADetailActivity.class);
    mPosition = position;
    mQnaModel = qnaModel;
    mQnaDeleteListener = qNADeleteListener;
    return intent;
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.add_qna)
  TextView mDelQna;
  @BindView(R.id.title_text_view)
  AppCompatTextView mTitleTextView;
  @BindView(R.id.content_text_view)
  AppCompatTextView mContentTextView;
  @BindView(R.id.date_text_view)
  AppCompatTextView mDateTextView;
  @BindView(R.id.reply_layout)
  LinearLayout mReplyLayout;
  @BindView(R.id.apply_date_text_view)
  AppCompatTextView mApplyDateTextView;
  @BindView(R.id.apply_content)
  AppCompatTextView mApplyContent;
  @BindView(R.id.reply_title_text_view)
  AppCompatTextView mReplyTitleTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private QnaModel mQnaResponse = new QnaModel();
  private static QNADetailLitener mQnaDeleteListener;
  private static QnaModel mQnaModel;
  private static int mPosition;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_qna_detail;
  }

  @Override
  protected void initLayout() {
    initToolbar("1:1 문의");
    mDelQna.setText("삭제");
    qaDetailAPI();


  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  // QNA 문의 삭제
  public void qaDelAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setQa_idx(mQnaModel.getQa_idx());

    CommonRouter.api().qa_del(Tools.getInstance().getMapper(qnaRequest)).enqueue(new Callback<QnaModel>() {
      @Override
      public void onResponse(Call<QnaModel> call, Response<QnaModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mQnaDeleteListener.onDelete(mPosition);
          finishWithRemove();

        }
      }

      @Override
      public void onFailure(Call<QnaModel> call, Throwable t) {

      }
    });
  }

  /**
   * QNA 상세 API
   */
  public void qaDetailAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setQa_idx(mQnaModel.getQa_idx());

    CommonRouter.api().qa_detail(Tools.getInstance().getMapper(qnaRequest)).enqueue(new Callback<QnaModel>() {
      @Override
      public void onResponse(Call<QnaModel> call, Response<QnaModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mQnaResponse = response.body();
          if (mQnaResponse.getQa_category().equals("0")) {
            mTitleTextView.setText("[회원서비스] " + mQnaResponse.getQa_title());
          } else if (mQnaResponse.getQa_category().equals("1")) {
            mTitleTextView.setText("[산책하기] " + mQnaResponse.getQa_title());
          }else if (mQnaResponse.getQa_category().equals("2")) {
            mTitleTextView.setText("[산책기록] " + mQnaResponse.getQa_title());
          }else if (mQnaResponse.getQa_category().equals("3")) {
            mTitleTextView.setText("[앱사용] " + mQnaResponse.getQa_title());
          }else if (mQnaResponse.getQa_category().equals("4")) {
            mTitleTextView.setText("[가타] " + mQnaResponse.getQa_title());
          }
          mContentTextView.setText(mQnaResponse.getQa_contents());
          mDateTextView.setText(mQnaResponse.getIns_date());

          if (mQnaResponse.getReply_yn().equals("Y")) {
            mReplyLayout.setVisibility(View.VISIBLE);
            mApplyContent.setText(mQnaResponse.getReply_contents());
            mApplyDateTextView.setText(mQnaResponse.getReply_date());
          } else if (mQnaResponse.getReply_yn().equals("N")) {
            mReplyLayout.setVisibility(View.GONE);
          }
        }
      }

      @Override
      public void onFailure(Call<QnaModel> call, Throwable t) {

      }
    });
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  /**
   * Qna 삭제
   */
  @OnClick(R.id.add_qna)
  public void delQnaTouched() {
    showConfirmDialog("삭제 하시겠습니까?", "취소", "확인", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        qaDelAPI();
      }
    });
  }

}
