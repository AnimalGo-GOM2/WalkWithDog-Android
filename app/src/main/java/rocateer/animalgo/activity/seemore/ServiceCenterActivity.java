package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;

import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.commons.faq.FAQListActivity;
import rocateer.animalgo.activity.commons.notice.NoticeListActivity;
import rocateer.animalgo.activity.commons.qna.QNAActivity;

public class ServiceCenterActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, ServiceCenterActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_service_center;
  }

  @Override
  protected void initLayout() {
    initToolbar("고객센터");

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  /**
   * 공지사항
   */
  @OnClick(R.id.notice_button)
  public void noticeTouched() {
    Intent noticeListActivity = NoticeListActivity.getStartIntent(mActivity);
    startActivity(noticeListActivity,TRANS.PUSH);
  }

  /**
   * FAQ
   */
  @OnClick(R.id.faq_button)
  public void faqTouched() {
    Intent faqListActivity = FAQListActivity.getStartIntent(mActivity);
    startActivity(faqListActivity,TRANS.PUSH);
  }

  /**
   * 1:1문의
   */
  @OnClick(R.id.qna_button)
  public void qnaTouched() {
    Intent qnaListActivity = QNAActivity.getStartIntent(mActivity);
    startActivity(qnaListActivity,TRANS.PUSH);
  }
}

