package rocateer.animalgo.activity.commons.address;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.BindView;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.RocateerWebViewClient;
import rocateer.animalgo.models.api.BaseRouter;
import timber.log.Timber;

public class AddressActivity extends RocateerActivity {
  public interface AddressListener {
    void onResult(String member_addr_postcode, String member_addr, String member_region_code, String member_region_name, String member_lat, String member_lng);
  }


  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, AddressListener addressListener) {
    Intent intent = new Intent(context, AddressActivity.class);
    mAddressListener = addressListener;
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
  private static AddressListener mAddressListener;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_address;
  }

  @Override
  protected void initLayout() {
    mToolbarTitle.setText("주소 검색");
    initWebView();
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
    mWebView.addJavascriptInterface(new AddressActivity.AndroidBridge(), "rocateer");

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
    mWebView.loadUrl(BaseRouter.BASE_URL + "daum_address_web_view_v_1_0_0/daum_address_api_view");
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  private class AndroidBridge {
    @JavascriptInterface
    public void addr(String member_addr_postcode, String member_addr, String member_region_code, String member_region_name, String member_lat, String member_lng) {
//      member_addr_postcode, member_addr, member_region_code, member_region_name,member_lat,member_lng
      Timber.i("post:" + member_addr_postcode);
      Timber.i("addr:" + member_addr);


      mAddressListener.onResult(member_addr_postcode, member_addr, member_region_code, member_region_name, member_lat, member_lng);
      finishWithRemove();
    }
  }
}
