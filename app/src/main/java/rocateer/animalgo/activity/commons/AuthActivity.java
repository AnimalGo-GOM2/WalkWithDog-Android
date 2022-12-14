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

import java.net.URISyntaxException;

import butterknife.BindView;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.RocateerWebViewClient;
import rocateer.animalgo.models.api.BaseRouter;
import timber.log.Timber;

public class AuthActivity extends RocateerActivity {
  public interface AuthListener {
    void onAuthResult(String name, String tel, String gender, String birth);
  }

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, AuthListener authListener) {
    Intent intent = new Intent(context, AuthActivity.class);
    mAuthListener = authListener;
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
  private static AuthListener mAuthListener;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_auth;
  }

  @Override
  protected void initLayout() {
    mToolbarTitle.setText("????????????");
    initWebView();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * ?????? ??????
   */
  private void initWebView() {

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setDefaultTextEncodingName("utf-8");

    webSettings.setJavaScriptEnabled(true);	//????????????(true)
    webSettings.setDomStorageEnabled(true);		//????????????(true)
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //????????????(true)

    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSettings.setLoadsImagesAutomatically(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setSupportZoom(true);
    webSettings.setSupportMultipleWindows(true);
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);

    mWebView.addJavascriptInterface(new AndroidBridge(), "rocateer");
    mWebView.setWebChromeClient(new WebChromeClient());
    mWebView.setWebViewClient(new RocateerWebViewClient(this));


//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      //???????????????,??????????????????
//      webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//      CookieManager cookieManager = CookieManager.getInstance();
//      cookieManager.setAcceptCookie(true);
//      cookieManager.setAcceptThirdPartyCookies(mWebView, true);
//    }


    mWebView.loadUrl(BaseRouter.BASE_URL + "nice_web_view/member_auth");
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  private class AndroidBridge {
    /**
     * ?????? ??????
     * @param member_name
     * @param member_phone
     * @param member_gender
     * @param member_birth
     * @param unique_key
     * @param auth_code
     */
    @JavascriptInterface
    public void request_auth(String member_name, String member_phone, String member_gender, String member_birth, String unique_key, String auth_code) {
      Timber.i(member_name + " : " + member_phone + " : " + member_gender + " : " + member_birth + " : " + auth_code);
      mAuthListener.onAuthResult(member_name, member_phone, member_gender, member_birth);
      finishWithRemove();
    }

  }

  class RocateerWebViewClient extends WebViewClient {
    private Activity activity;

    public RocateerWebViewClient(Activity activity) {
      this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      String url = request.getUrl().toString();
      Timber.i(">>>>>>>>>>>>>>>>>>>>>>>>");
      Timber.i(url);
      Timber.i("<<<<<<<<<<<<<<<<<<<<<<<<");
      if (url.startsWith("intent://")) {
        Intent intent = null;
        try {
          intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
          if (intent != null) {
            //?????????
            startActivity(intent);
          }
        } catch (URISyntaxException e) {
          //URI ?????? ?????? ??? ?????? ??????

        } catch (ActivityNotFoundException e) {
          String packageName = intent.getPackage();
          if (!packageName.equals("")) {
            // ?????? ???????????? ?????? ?????? ?????? ???????????? ??????
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
          }
        }
        //return  ?????? ????????? true??? ?????? ?????????.
        return true;

      } else if (url.startsWith("https://play.google.com/store/apps/details?id=") || url.startsWith("market://details?id=")) {
        //????????? ??? ??????????????? ?????? ?????? ??? PlayStore ????????? ???????????? ?????? ??????
        Uri uri = Uri.parse(url);
        String packageName = uri.getQueryParameter("id");
        if (packageName != null && !packageName.equals("")) {
          // ???????????? ??????
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        }
        //return  ?????? ????????? true??? ?????? ?????????.
        return true;
      }

      //return  ?????? ????????? false??? ?????? ?????????.
      return false;
    }
  }
}
