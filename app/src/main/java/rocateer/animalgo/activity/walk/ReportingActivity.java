package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.DecorationHorizontal;
import rocateer.animalgo.commons.SpacingItemDecoration;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.models.ImageModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class ReportingActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String partnerMemberIdx) {
    Intent intent = new Intent(context, ReportingActivity.class);
    mPartnerMemberIdx = partnerMemberIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.reporting_recycler_view)
  RecyclerView mReportingRecyclerView;
  @BindView(R.id.reporting_spinner)
  PowerSpinnerView mReportingSpinner;
  @BindView(R.id.reporting_content)
  LinearLayout mReportingContent;
  @BindView(R.id.reporting_edit_text)
  AppCompatEditText mReportingEditText;
  @BindView(R.id.image_cnt_text_view)
  AppCompatTextView mImageCntTextView;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private WalkDialyAdapter mReportingAdapter;
  private ArrayList<ImageModel> mImageList = new ArrayList<>();
  private ArrayList<ImageModel> mTempImageFiles = new ArrayList<>();
  private ImagePickerDialog mImagePickDialog;
  private static String mPartnerMemberIdx;
  private boolean mImageAddButtonState = false;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_reporting;
  }

  @Override
  protected void initLayout() {
    initToolbar("신고하기");

    mReportingSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int i, @Nullable String s, int i1, String t1) {
        if (mReportingSpinner.getSelectedIndex() == 0) {
          mReportingContent.setVisibility(View.GONE);
        } else if (mReportingSpinner.getSelectedIndex() == 1) {
          mReportingContent.setVisibility(View.GONE);
        } else if (mReportingSpinner.getSelectedIndex() == 2) {
          mReportingContent.setVisibility(View.VISIBLE);
        }
      }
    });

    mReportingSpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
        mReportingSpinner.dismiss();
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
      List<Image> images = ImagePicker.getImages(data);
      for (int i = 0; i < images.size(); i++) {
        Uri imageUri = Uri.fromFile(new File(images.get(i).getPath()));
        File file = Tools.getInstance().compressImage(imageUri);
        mTempImageFiles.add(new ImageModel(file));
      }
      mImagePickDialog.dismiss();

      if (mTempImageFiles.size() == 5) {
        mTempImageFiles.remove(0);
      }

      fileUploads();
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void initRequest() {
    initReportingAdapter();


  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 신고하기 API
   */
  private void reportRegInAPI() {

    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    if (mReportingSpinner.getSelectedIndex() == 0) {
      memberRequest.setReport_type("0");
    } else if (mReportingSpinner.getSelectedIndex() == 1) {
      memberRequest.setReport_type("1");
    } else if (mReportingSpinner.getSelectedIndex() == 2) {
      memberRequest.setReport_type("2");
    }
    memberRequest.setPartner_member_idx(mPartnerMemberIdx);
    memberRequest.setReport_contents(mReportingEditText.getText().toString());
    ArrayList<String> imgPaths = new ArrayList<>();
    for (ImageModel value : mImageList) {
      if (value.getImg_path() != null) {
        imgPaths.add(value.getImg_path());
      }
    }
    memberRequest.setImg_path(TextUtils.join(",", imgPaths));

    CommonRouter.api().report_reg_in(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          finishWithRemove();

        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  /**
   * 이미지 목록
   */
  private void initReportingAdapter() {

    mImageList.add(new ImageModel());
    mReportingAdapter = new WalkDialyAdapter(R.layout.row_edit_image, mImageList);
    mReportingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
    mReportingRecyclerView.addItemDecoration(new DecorationHorizontal(mActivity, Tools.getInstance().dpTopx(mActivity, 10), Tools.getInstance().dpTopx(mActivity, 0)));
    mReportingRecyclerView.setAdapter(mReportingAdapter);

    mReportingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Timber.i("position ===== " + position);
        if (position == 0) {
          if (!mImageAddButtonState) {
            startGetImage();
          }
        }
      }
    });

    mReportingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Timber.i("position ==== " + position);
        switch (view.getId()) {
          case R.id.delete_image_view:
            mImageList.remove(position);
            mReportingAdapter.setNewData(mImageList);
            mImageCntTextView.setText(mImageList.size() - 1 + "/5");
            break;
        }
      }
    });

  }

  /**
   * 이미지 피커
   **/
  private void startGetImage() {
    Timber.i("mTitleImageTouched");
    mImagePickDialog = new ImagePickerDialog(mActivity);
    mImagePickDialog.mCameraButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 카메라 선택
        ImagePicker.create(mActivity)
            .imageDirectory(getString(R.string.app_name))
            .cameraOnly()
            .start(mActivity);
      }
    });

    mImagePickDialog.mAlbumButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 앨범에서 선택
        ImagePicker.create(mActivity)
            .returnMode(ReturnMode.NONE)
            .folderMode(true)
            .toolbarFolderTitle(getString(R.string.app_name))
            .toolbarImageTitle(getString(R.string.app_name))
            .includeVideo(false)
            .includeAnimation(false)
            .imageDirectory(getString(R.string.app_name))
            .theme(R.style.ImagePickerTheme)
            .multi() // multi mode (default mode)
            .limit(10 - (mImageList.size() - 1)) // m
            .showCamera(false)
            .start();
      }
    });
    mImagePickDialog.show();
  }

  /**
   * 파일 업로드 > 여러개
   */
  private void fileUploads() {
    if (mTempImageFiles.size() > 0) {
      Tools.getInstance().fileUploadAction(mTempImageFiles.get(0).getFile(), new Tools.FileUploadListener() {
        @Override
        public void onResult(boolean isSuccess, String filePath) {
          if (isSuccess) {
            mImageList.add(new ImageModel(filePath));
            mTempImageFiles.remove(0);

            if (mTempImageFiles.size() == 0) {
              mReportingAdapter.setNewData(mImageList);
            } else {
              fileUploads();
            }
            mImageCntTextView.setText(mImageList.size() - 1 + "/5");
          }
        }
      });
      return;
    }
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 확인
   */
  @OnClick(R.id.reporting_button)
  public void reportingTouched() {
    reportRegInAPI();
  }
}
