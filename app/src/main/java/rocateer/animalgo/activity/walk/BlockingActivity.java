package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import rocateer.animalgo.activity.home.MyPetAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.DecorationHorizontal;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.ImageModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class BlockingActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String partnerMemberIdx) {
    Intent intent = new Intent(context, BlockingActivity.class);
    mPartnerMemberIdx = partnerMemberIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.blocking_recycler_view)
  RecyclerView mBlockingRecyclerView;
  @BindView(R.id.blocking_spinner)
  PowerSpinnerView mBlockingSpinner;
  @BindView(R.id.blocking_content_edit_text)
  AppCompatEditText mblockingcontentEditText;
  @BindView(R.id.blocking_content)
  LinearLayout mBlockingContent;
  @BindView(R.id.image_cnt_text_view)
  AppCompatTextView mImageCntTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private WalkDialyAdapter mBlockingAdapter;
  private ArrayList<ImageModel> mImageList = new ArrayList<>();
  private ArrayList<ImageModel> mTempImageFiles = new ArrayList<>();
  private ImagePickerDialog mImagePickDialog;
  private boolean mImageAddButtonState = false;
  private static String mPartnerMemberIdx;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_blocking;
  }

  @Override
  protected void initLayout() {
    initToolbar("차단하기");

    mBlockingSpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        mBlockingSpinner.dismiss();
      }
    });

    mBlockingSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int i, @Nullable String s, int i1, String t1) {
        if (mBlockingSpinner.getSelectedIndex() == 0) {
          mBlockingContent.setVisibility(View.GONE);
        } else if (mBlockingSpinner.getSelectedIndex() == 1) {
          mBlockingContent.setVisibility(View.GONE);
        } else if (mBlockingSpinner.getSelectedIndex() == 2) {
          mBlockingContent.setVisibility(View.VISIBLE);
        }
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
    initBlockingAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 이미지 목록
   */
  private void initBlockingAdapter() {

    mImageList.add(new ImageModel());

    mBlockingAdapter = new WalkDialyAdapter(R.layout.row_edit_image, mImageList);
    mBlockingRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
    mBlockingRecyclerView.addItemDecoration(new DecorationHorizontal(mActivity, Tools.getInstance().dpTopx(mActivity, 10), Tools.getInstance().dpTopx(mActivity, 0)));
    mBlockingRecyclerView.setAdapter(mBlockingAdapter);

    mBlockingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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

    mBlockingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Timber.i("position ==== " + position);
        switch (view.getId()) {
          case R.id.delete_image_view:
            mImageList.remove(position);
            mBlockingAdapter.setNewData(mImageList);
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
              mBlockingAdapter.setNewData(mImageList);
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

  /**
   * 차단하기
   */
  private void blockRegInAPI() {

    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    memberRequest.setPartner_member_idx(mPartnerMemberIdx);
    if (mBlockingSpinner.getSelectedIndex() == 0) {
      memberRequest.setBlock_type("0");
    } else if (mBlockingSpinner.getSelectedIndex() == 1) {
      memberRequest.setBlock_type("1");
    } else if (mBlockingSpinner.getSelectedIndex() == 2) {
      memberRequest.setBlock_type("2");
    }
    memberRequest.setBlock_contents(mblockingcontentEditText.getText().toString());
    ArrayList<String> imgPaths = new ArrayList<>();
    for (ImageModel value : mImageList) {
      if (value.getImg_path() != null) {
        imgPaths.add(value.getImg_path());
      }
    }
    memberRequest.setImg_path(TextUtils.join(",", imgPaths));

    CommonRouter.api().block_reg_in(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          Intent withFriendRefresh = new Intent(Constants.WITH_FRIEND_REFRESH);
          mActivity.sendBroadcast(withFriendRefresh);
          finishMultiRemoveActivity(2);

        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 차단하기 버튼
   */
  @OnClick(R.id.blocking_button)
  public void blockingTouched() {
    blockRegInAPI();
  }
}
