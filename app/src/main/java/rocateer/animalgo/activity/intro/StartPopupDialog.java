package rocateer.animalgo.activity.intro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Member;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class StartPopupDialog extends BottomSheetDialogFragment {
  private RocateerActivity mActivity;
  private static MemberModel mMemberResponse;
  private static Date mDate;

  public StartPopupDialog(RocateerActivity activity, MemberModel memberResponse, Date date) {
    mActivity = activity;
    mMemberResponse = memberResponse;
    mDate = date;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return super.onCreateDialog(savedInstanceState);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogFragment);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    View v;

    v = inflater.inflate(R.layout.dialog_popup, container, false);
    setCancelable(false);

    AppCompatImageView mPopupImageView = v.findViewById(R.id.popup_dialog_image_view);
    RequestOptions requestOptions = new RequestOptions();
    Glide.with(mActivity)
        .load(BaseRouter.BASE_URL + mMemberResponse.getImg_path())
        .centerCrop()
        .apply(requestOptions)
        .into(mPopupImageView);

    AppCompatTextView mThreedaySeeAgainTextView = v.findViewById(R.id.three_day_see_again_text_view);
    mThreedaySeeAgainTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("HIDE IN FEED BUTTON -----------");
        DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        String nowString = sdFormat.format(mDate);
        Prefs.putString(Constants.START_POPUP, nowString);
        dismiss();
      }
    });

    AppCompatTextView mClosePopupTextView = v.findViewById(R.id.close_popup_text_view);
    mClosePopupTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("DECLARATION BUTTON -----------");
        dismiss();
      }
    });

    return v;
  }

}

