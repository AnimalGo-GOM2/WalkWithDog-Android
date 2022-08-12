package rocateer.animalgo.activity.commons;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import timber.log.Timber;

public class WebViewActivity extends RocateerActivity {
  public interface WebViewListener {
    void onPayResult(String order_number, String payment_type, String pg_date, String pg_price, String pg_result);
    void onAuthResult(String member_name, String member_phone, String member_gender, String member_birth, String auth_code);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String url, WebViewListener webViewListener) {
    Intent intent = new Intent(context, WebViewActivity.class);
    mUrl = url;
    mWebViewListener = webViewListener;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.web_view)
  WebView mWebView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private static WebViewListener mWebViewListener;
  private static String mUrl;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_web_view;
  }

  @Override
  protected void initLayout() {
    initWebView();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 웹뷰 초기화
   */
  private void initWebView() {

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setDefaultTextEncodingName("utf-8");
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSettings.setUseWideViewPort(true);
    webSettings.setJavaScriptEnabled(true);

    mWebView.addJavascriptInterface(new AndroidPayAuthBridge(), "rocateer");

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

    mWebView.loadUrl(mUrl);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 뒤로가기
   */
  @Optional
  @OnClick(R.id.back_button)
  public void backTouched() {
    finishWithRemove();
  }


  //--------------------------------------------------------------------------------------------
  // MARK : More Class
  //--------------------------------------------------------------------------------------------
  class RocateerWebViewClient extends WebViewClient {
    private Activity activity;

    public RocateerWebViewClient(Activity activity) {
      this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      String url = request.getUrl().toString();
      if (url.startsWith("intent")) {
        try {
          Intent tempIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
          String strParams = tempIntent.getDataString();
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(strParams));
          startActivity(intent);
          return true;
        } catch (Exception e) {
          Intent intent = null;
          try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
            startActivity(marketIntent);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
          return true;
        }
      } else if (url.startsWith("market://")) {
        try {
          Intent tempIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
          String strParams = tempIntent.getDataString();
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(strParams));
          startActivity(intent);
          return true;
        } catch (Exception e) {
          Intent intent = null;
          try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
            startActivity(marketIntent);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
          return true;
        }
      } else {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
//          view.loadUrl(url);
//          startActivity(intent);
//          mWebView.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
          if (url.startsWith("ispmobile://")) {
            startActivity(intent);
            return true;
          }
        } catch (ActivityNotFoundException e) {
          if (url.startsWith("ispmobile://")) {
//            showAlertDialog("모바일 ISP 어플리케이션이 설치되어 있지 않습니다.\n설치를 눌러 진행 해 주십시요.", "설치", new DialogEventListener() {
//              @Override
//              public void onReceivedEvent() {
//
////                finish();
//              }
//            });
            mWebView.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
            return true;
          }
        }
      }
      return false;
    }
  }

  private class AndroidPayAuthBridge {
    // 결제
    @JavascriptInterface
    public void payResult(String order_number, String payment_type, String pg_date, String pg_price, String pg_result) {
      Timber.i("=============PayResult=============");
      Timber.i("order_number=" + order_number
          + ", payment_type=" + payment_type
          + ", pg_date=" + pg_date
          + ", pg_price=" + pg_price
          + ", pg_result=" + pg_result);
      mWebViewListener.onPayResult(order_number, payment_type, pg_date, pg_price, pg_result);
      finishWithRemove();
    }

    // 본인인증
    @JavascriptInterface
    public void auth(String member_name, String member_phone, String member_gender, String member_birth, String auth_code) {
      Timber.i("=============AuthResult=============");
      Timber.i("member_name=" + member_name
          + ", member_phone=" + member_phone
          + ", member_gender=" + member_gender
          + ", member_birth=" + member_birth
          + ", auth_code=" + auth_code);
      mWebViewListener.onAuthResult(member_name, member_phone, member_gender, member_birth, auth_code);
      finishWithRemove();
    }

  }
}
