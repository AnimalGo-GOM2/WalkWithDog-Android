package rocateer.animalgo.commons;

import static android.graphics.Color.WHITE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.FileModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class Tools {

  private static final Tools ourInstance = new Tools();
  private static RocateerActivity mActivity;

  public static Tools getInstance() {
    return ourInstance;
  }

  public static Tools getInstance(RocateerActivity activity) {
    mActivity = activity;
    return ourInstance;
  }

  public interface FileUploadListener {
    void onResult(boolean isSuccess, String filePath);
  }

  private Tools() {
  }

  /**
   * API ?????? ????????? ??????
   *
   * @return
   */
  public boolean isSuccessResponse(Response<?> response) {
    ArrayList<String> successCode = new ArrayList<String>() {{
      add("1000");
      add("2000");
    }};
    ArrayList<String> messageCode = new ArrayList<String>() {{
      add("2001");
    }};

    String code = "";
    String code_msg = "";

    if (response != null && response.isSuccessful()) {
      code = ((BaseModel) response.body()).getCode();
      code_msg = ((BaseModel) response.body()).getCode_msg();
    } else {
      showToast("??? ??? ?????? ????????? ?????????????????????.");
      return false;
    }

    if (successCode.contains(code)) { // ??????
      return true;
    } else if (messageCode.contains(code)) { // ?????? ??? ????????? ??????
      showToast(code_msg);
      return true;
    } else { // ??????
      showToast(code_msg);
      return false;
    }
  }

  /**
   * ????????? ??????
   *
   * @param object
   * @return
   */
  public Map<String, Object> getMapper(Object object) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    Map<String, Object> map = mapper.convertValue(object, Map.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Timber.d("PARAMETER :" + gson.toJson(map));
    return map;
  }

  /**
   * ????????? ??????
   *
   * @param message - ?????????
   */
  public void showToast(String message) {
    Snackbar mSnackbar;
    mSnackbar = Snackbar.make(mActivity.getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
    mSnackbar.show();
  }


  /**
   * ?????? String ??? ",(??????)" ?????? ??????
   *
   * @param value ?????? String
   * @return ?????????
   */
  public String numberPlaceValue(String value) {
    String ret = "0";
    if (value == null) {
      return ret;
    } else {
      long val = Long.parseLong(value);
      try {
        DecimalFormat format = new DecimalFormat("#,###");
        ret = format.format(val);
      } catch (NumberFormatException nfe) {
        ret = "0";
      } finally {
        return ret;
      }
    }
  }

  /**
   * ????????? ??? ?????? ??????
   *
   * @param act
   * @param color
   */
  public void setSystemBarColor(Activity act, @ColorRes int color) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = act.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.setStatusBarColor(act.getResources().getColor(color));
    }
  }

  /**
   * ????????? ??? ?????? Light ??????
   *
   * @param act
   */
  public void setSystemBarLight(Activity act) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      View view = act.findViewById(android.R.id.content);
      int flags = view.getSystemUiVisibility();
      flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
      view.setSystemUiVisibility(flags);
    }
  }

  /**
   * Notification ??? ????????????
   */
  public void setSystemBarTransparent(Activity act) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = act.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
    }
  }

  /**
   * DP to PX
   *
   * @param c
   * @param dp
   * @return
   */
  public int dpTopx(Context c, int dp) {
    Resources r = c.getResources();
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }

  /**
   * PX to DP
   *
   * @param context
   * @param pxValue
   * @return
   */
  public int px2dp(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * ?????????
   *
   * @param nested
   * @param targetView
   */
  public void nestedScrollTo(final NestedScrollView nested, final View targetView) {
    nested.post(new Runnable() {
      @Override
      public void run() {
        nested.scrollTo(500, targetView.getBottom());
      }
    });
  }

  /**
   * ?????? ??????
   *
   * @param tel
   */
  public void openPhone(String tel) {
    try {
      Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
      mActivity.startActivity(tt);
    } catch (Exception e) {
      showToast("????????? ????????? ??? ????????????.");
    }

  }

  /**
   * ?????? ?????? ??????
   *
   * @param url
   */
  public void openBrowser(String url) {
    try {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      mActivity.startActivity(intent);
    } catch (Exception e) {
      showToast("????????? ????????? ??? ????????????.");
    }
  }

  /**
   * ?????? ????????? ??????
   *
   * @param context
   * @param text
   */
  public void copyToClipboard(Context context, String text) {
    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("clipboard", text);
    clipboard.setPrimaryClip(clip);
    showToast("Text copied to clipboard");
  }

  /**
   * ????????? ?????? ??????
   *
   * @param fecha
   * @return
   */
  public static Date getZeroTimeDate(Date fecha) {
    Date res = fecha;
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(fecha);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    res = calendar.getTime();

    return res;
  }

  /**
   * ?????? ?????????
   * @param file
   */
  public void fileUploadAction(File file, FileUploadListener fileUploadListener) {
    RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);
    CommonRouter.api().fileUpload_action(body).enqueue(new Callback<FileModel>() {
      @Override
      public void onResponse(Call<FileModel> call, Response<FileModel> response) {
        FileModel fileUploadResponse = response.body();

        if (Tools.getInstance().isSuccessResponse(response)) {
          fileUploadListener.onResult(true, fileUploadResponse.getFile_path());
        } else {
//          showToast(mActivity.getString(R.string.api_error));
          fileUploadListener.onResult(false, "");
        }
      }
      @Override
      public void onFailure(Call<FileModel> call, Throwable t) {
//        showToast(mActivity.getString(R.string.api_error));
        fileUploadListener.onResult(false, "");
      }
    });
  }


  /**
   * ????????? ??????
   * @param imageUri
   * @return
   */
  public File compressImage(Uri imageUri) {
    Bitmap loadedBitmap = loadBitmap(String.valueOf(imageUri));
    Bitmap mResizeImageBitmap = bitmapResizePrc(loadedBitmap).getBitmap();

    ExifInterface exif = null;
    try {
      exif = new ExifInterface(imageUri.getPath());

    } catch (IOException e) {
      Timber.e(e.getLocalizedMessage());
    }
    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
    Bitmap mRotateBitmap = rotateBitmap(mResizeImageBitmap, orientation);
    String mGetBitmapFile = saveBitmapToJpeg(mActivity, mRotateBitmap, UUID.randomUUID().toString());
    File imageFile = new File(mGetBitmapFile);
    return imageFile;
  }


  /**
   * Image -> Bitmap ??????
   *
   * @author khh
   * @since 4/16/21
   **/
  public Bitmap loadBitmap(String url) {
    Bitmap bm = null;
    InputStream is = null;
    BufferedInputStream bis = null;
    try {
      URLConnection conn = new URL(url).openConnection();
      conn.connect();
      is = conn.getInputStream();
      bis = new BufferedInputStream(is, 8192);
      bm = BitmapFactory.decodeStream(bis);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bis != null) {
        try {
          bis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bm;
  }

  /**
   * ????????? ????????? ??????
   *
   * @author khh
   * @since 4/16/21
   **/
  public BitmapDrawable bitmapResizePrc(Bitmap mSrc) {
    BitmapDrawable bitmapResult = null;
    int newHeight, newWidth;

    Timber.i("ORIGINAL IMAGE WIDTH SIZE === " + mSrc.getWidth());
    Timber.i("ORIGINAL IMAGE HEIGHT SIZE === " + mSrc.getHeight());

    int ratio = 1;
    if (mSrc.getWidth() > 1500) {  //????????? ????????? 1000 ?????? ??????
      ratio = mSrc.getWidth() / 1500;
    } else { //????????? ????????? 1000 ?????? ????????? ????????? ?????? ?????? ????????? Default??? 1??? ????????????.
      ratio = 1;
    }

    Timber.i("RATION === " + ratio);
    newHeight = mSrc.getHeight() / ratio;
    newWidth = mSrc.getWidth() / ratio;

    int width = mSrc.getWidth();
    int height = mSrc.getHeight();

    // calculate the scale - in this case = 0.4f
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;

    // createa matrix for the manipulation
    Matrix matrix = new Matrix();
    // resize the bit map

    matrix.postScale(scaleWidth, scaleHeight);

    // rotate the Bitmap  ??????
//    matrix.postRotate(90);

    // recreate the new Bitmap
    Bitmap resizedBitmap = Bitmap.createBitmap(mSrc, 0, 0, width, height, matrix, true);

    // check
    width = resizedBitmap.getWidth();
    height = resizedBitmap.getHeight();

    Timber.i("AFTER IMAGE WIDTH SIZE === " + width);
    Timber.i("AFTER IMAGE HEIGHT SIZE === " + height);

//    Log.i("ImageResize", "Image Resize Result : " + Boolean.toString((newHeight==height)&&(newWidth==width)) );
    Timber.i("ImageResize" + "Image Resize Result : " + Boolean.toString((newHeight == height) && (newWidth == width)));

    // make a Drawable from Bitmap to allow to set the BitMap
    // to the ImageView, ImageButton or what ever
    bitmapResult = new BitmapDrawable(resizedBitmap);

    return bitmapResult;
  }

  /**
   * Bitmap -> File ??????
   *
   * @author khh
   * @since 4/15/21
   **/
  public String saveBitmapToJpeg(Context context, Bitmap bitmap, String name) {
    File storage = context.getCacheDir();
    String fileName = name + ".jpg";
    File tempFile = new File(storage, fileName);
    try {
      tempFile.createNewFile();
      FileOutputStream out = new FileOutputStream(tempFile);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempFile.getAbsolutePath();
  }

  /**
   * ????????? ??????
   *
   * @author khh
   * @since 4/29/21
   **/
  public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
    Matrix matrix = new Matrix();
    switch (orientation) {
      case ExifInterface.ORIENTATION_NORMAL:
        return bitmap;
      case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
        matrix.setScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_ROTATE_180:
        matrix.setRotate(180);
        break;
      case ExifInterface.ORIENTATION_FLIP_VERTICAL:
        matrix.setRotate(180);
        matrix.postScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_TRANSPOSE:
        matrix.setRotate(90);
        matrix.postScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_ROTATE_90:
        matrix.setRotate(90);
        break;

      case ExifInterface.ORIENTATION_TRANSVERSE:
        matrix.setRotate(-90);
        matrix.postScale(-1, 1);
        break;

      case ExifInterface.ORIENTATION_ROTATE_270:
        matrix.setRotate(-90);
        break;
      default:
        return bitmap;
    }

    try {
      Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
      bitmap.recycle();
      return bmRotated;
    } catch (OutOfMemoryError e) {
      Timber.e(e.getLocalizedMessage());
      return null;
    }
  }
  /**
   * 1???1?????? ??????
   */
  public String[] initQnaList() {
    String[] qnaBreedArray = {"?????? ?????????","?????? ??????"," ??? ??????", "??????"};
    return qnaBreedArray;

  }

  /**
   * ????????? ??????
   */
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static void cropImage(Uri imageUri, CropImageView.CropShape cropShape , ActivityResultCallback<CropImageView.CropResult> resultListener) {

    int initRotate = 0;
    try {
      InputStream in = mActivity.getContentResolver().openInputStream(imageUri);
      ExifInterface exif = new ExifInterface(in);
      in.close();

      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
        initRotate = 90;
      } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
        initRotate = 180;
      } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
        initRotate = 270;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    CropImageContractOptions options = new CropImageContractOptions(imageUri, new CropImageOptions())
        .setScaleType(CropImageView.ScaleType.FIT_CENTER)
        .setCropShape(cropShape)
        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
        .setAspectRatio(1, 1)
        .setMaxZoom(4)
        .setAutoZoomEnabled(true)
        .setMultiTouchEnabled(true)
        .setCenterMoveEnabled(true)
        .setShowCropOverlay(true)
        .setAllowFlipping(true)
        .setSnapRadius(3f)
        .setTouchRadius(48f)
        .setInitialCropWindowPaddingRatio(0.1f)
        .setBorderLineThickness(3f)
        .setBorderLineColor(Color.argb(170, 255, 255, 255))
        .setBorderCornerThickness(2f)
        .setBorderCornerOffset(5f)
        .setBorderCornerLength(14f)
        .setBorderCornerColor(WHITE)
        .setGuidelinesThickness(1f)
        .setGuidelinesColor(R.color.white)
        .setBackgroundColor(Color.argb(119, 0, 0, 0))
        .setMinCropWindowSize(24, 24)
        .setMinCropResultSize(20, 20)
        .setMaxCropResultSize(99999, 99999)
        .setActivityTitle("")
        .setActivityMenuIconColor(0)
        .setOutputUri(null)
        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
        .setOutputCompressQuality(100)
        .setRequestedSize(0, 0)
        .setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
        .setInitialCropWindowRectangle(null)
        .setInitialRotation(initRotate)
        .setAllowCounterRotation(false)
        .setFlipHorizontally(false)
        .setFlipVertically(false)
        .setCropMenuCropButtonTitle(null)
        .setCropMenuCropButtonIcon(0)
        .setAllowRotation(true)
        .setNoOutputImage(false)
        .setFixAspectRatio(true);

//    ComponentActivity
    ActivityResultLauncher<CropImageContractOptions> cropImage = mActivity.registerForActivityResult(new CropImageContract(), resultListener);
    cropImage.launch(options);
  }


  /**
   * ????????? ??????
   *
   * @param imgPath
   */
  public String getImagePath(String imgPath) {
    return BaseRouter.BASE_URL + imgPath;
  }

}
