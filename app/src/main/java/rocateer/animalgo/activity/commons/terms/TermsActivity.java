package rocateer.animalgo.activity.commons.terms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.RocateerWebViewClient;
import rocateer.animalgo.models.api.BaseRouter;

public class TermsActivity extends RocateerActivity {
  public enum TermsType {
    Terms1, Terms2, Terms3, Terms4
  }

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, TermsType termsType) {
    Intent intent = new Intent(context, TermsActivity.class);
    mTermsType = termsType;
    return intent;
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.web_view)
  WebView mWebView;
  private String url = "";
  private static TermsType mTermsType;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.terms_spinner)
  PowerSpinnerView mTermsSpinner;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_terms;
  }

  @Override
  protected void initLayout() {
     if (mTermsType == TermsType.Terms2) {
      mToolbarTitle.setText("이용약관");
      mTermsSpinner.setText("이용약관");
    } else if (mTermsType == TermsType.Terms3) {
      mToolbarTitle.setText("개인정보 처리방침");
       mTermsSpinner.setText("개인정보 처리방침");
     } else if (mTermsType == TermsType.Terms4) {
      mToolbarTitle.setText("위치정보 이용약관");
       mTermsSpinner.setText("위치정보 이용약관");
     }
    initWebView();

    mTermsSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        if (mTermsSpinner.getSelectedIndex() == 0) {
          url = "terms_web_view_v_1_0_0/terms_detail?type=1";
          mWebView.loadUrl(BaseRouter.BASE_URL + url);
          mToolbarTitle.setText(mTermsSpinner.getText().toString());
        } else if (mTermsSpinner.getSelectedIndex() == 1) {
          url = "terms_web_view_v_1_0_0/terms_detail?type=0";
          mToolbarTitle.setText(mTermsSpinner.getText().toString());
          mWebView.loadUrl(BaseRouter.BASE_URL + url);
        } else if (mTermsSpinner.getSelectedIndex() == 2) {
          url = "terms_web_view_v_1_0_0/terms_detail?type=4";
          mWebView.loadUrl(BaseRouter.BASE_URL + url);
          mToolbarTitle.setText(mTermsSpinner.getText().toString());

        }
      }
    });

  }



  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 웹뷰 초기
   */
  private void initWebView() {

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setDefaultTextEncodingName("utf-8");
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSettings.setUseWideViewPort(true);
    webSettings.setJavaScriptEnabled(true);

    mWebView.setWebChromeClient(new WebChromeClient());
    mWebView.setWebViewClient(new RocateerWebViewClient(this));
    mWebView.setVerticalScrollbarOverlay(true); //스크롤설정

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //혼합콘텐츠,타사쿠키사용
      webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

      CookieManager cookieManager = CookieManager.getInstance();
      cookieManager.setAcceptCookie(true);
      cookieManager.setAcceptThirdPartyCookies(mWebView, true);
    }
    String url = "";
    switch (mTermsType) {
      case Terms2:
        url = "terms_web_view_v_1_0_0/terms_detail?type=1";
        break;
      case Terms3:
        url = "terms_web_view_v_1_0_0/terms_detail?type=0";
        break;
      case Terms4:
        url = "terms_web_view_v_1_0_0/terms_detail?type=4";
        break;
    }
    mWebView.loadUrl(BaseRouter.BASE_URL + url);
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------


}