package rocateer.animalgo.activity.commons.qna;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import butterknife.BindView;
import butterknife.OnClick;
import jrizani.jrspinner.JRSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.QnaModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class AddQnaActivity extends RocateerActivity {
  public interface QNAAddLitener {
    void onAdd();
  }
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, QNAAddLitener qnaAddLitener) {
    Intent intent = new Intent(context, AddQnaActivity.class);
    mQNAAddLitener = qnaAddLitener;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.qna_category_spinner)
  PowerSpinnerView mQnaCategorySpinner;
  @BindView(R.id.qna_title_edit_text)
  AppCompatEditText mQnaTitleEditText;
  @BindView(R.id.qna_contents_edit_text)
  AppCompatEditText mQnaContentsEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private QnaModel mQnaResponse = new QnaModel();
  private static QNAAddLitener mQNAAddLitener;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_add_qna;
  }

  @Override
  protected void initLayout() {
    initToolbar("1:1문의");

  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    View focusView = getCurrentFocus();
    if (focusView != null) {
      Rect rect = new Rect();
      focusView.getGlobalVisibleRect(rect);
      int x = (int) ev.getX(), y = (int) ev.getY();
      if (!rect.contains(x, y)) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
          imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        focusView.clearFocus();
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  /**
   * qna 등록
   */
  public void qaRegInAPI() {
    QnaModel qnaRequest = new QnaModel();
    qnaRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    qnaRequest.setQa_title(mQnaTitleEditText.getText().toString());
    qnaRequest.setQa_contents(mQnaContentsEditText.getText().toString());
    if (mQnaCategorySpinner.getSelectedIndex() == 0) {
      qnaRequest.setQa_category("0");
    } else if (mQnaCategorySpinner.getSelectedIndex() == 1) {
      qnaRequest.setQa_category("1");
    } else if (mQnaCategorySpinner.getSelectedIndex() == 2) {
      qnaRequest.setQa_category("2");
    } else if (mQnaCategorySpinner.getSelectedIndex() == 3) {
      qnaRequest.setQa_category("3");
    } else if (mQnaCategorySpinner.getSelectedIndex() == 4) {
      qnaRequest.setQa_category("4");
    }
    qnaRequest.setQa_type("0");

    CommonRouter.api().qa_reg_in(Tools.getInstance().getMapper(qnaRequest)).enqueue(new Callback<QnaModel>() {
      @Override
      public void onResponse(Call<QnaModel> call, Response<QnaModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mQNAAddLitener.onAdd();
          finishWithRemove();
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
   * qna 등록
   */
  @OnClick(R.id.add_qna_button)
  public void addQnaTouched() {
    showAlertDialog("문의가 등록되었습니다.", "확인", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        qaRegInAPI();
      }
    });
  }

}
