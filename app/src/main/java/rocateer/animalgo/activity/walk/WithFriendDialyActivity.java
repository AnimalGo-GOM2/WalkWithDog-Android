package rocateer.animalgo.activity.walk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import per.wsj.library.AndRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.MyPetAdapter;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.DecorationHorizontal;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.commons.SpacingItemDecoration;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.ImageModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WithFriendDialyActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String recordIdx, String recordDiaryIdx,  String distant, String walkTime) {
    Intent intent = new Intent(context, WithFriendDialyActivity.class);
    mRecordIdx = recordIdx;
    mRecordDiaryIdx = recordDiaryIdx;
    mDistant = distant;
    mWalkTime = walkTime;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.back_button)
  ImageView mBackButton;
  @BindView(R.id.with_pet_recycler_view)
  RecyclerView mWithPetRecyclerView;
  @BindView(R.id.walk_dialy_recycler_view)
  RecyclerView mWalkDailyRecyclerView;
  @BindView(R.id.walk_memo_edit_text)
  AppCompatEditText mWalkMemoEditText;
  @BindView(R.id.image_list_cnt_text_view)
  AppCompatTextView mImageListCntTextView;
  @BindView(R.id.review_star)
  AndRatingBar mReviewStar;
  @BindView(R.id.review_star1)
  AndRatingBar mReviewStar1;
  @BindView(R.id.review_star2)
  AndRatingBar mReviewStar2;
  @BindView(R.id.review_star3)
  AndRatingBar mReviewStar3;
  @BindView(R.id.walk_distant_text_view)
  AppCompatTextView mWalkDistantTextView;
  @BindView(R.id.walk_time_text_view)
  AppCompatTextView mWalkTimeTextView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ImagePickerDialog mImagePickDialog;
  private ArrayList<ImageModel> mTempImageFiles = new ArrayList<>();
  private ArrayList<ImageModel> mImageList = new ArrayList<>();
  private static String mRecordIdx;
  private static String mWalkTime;
  private static String mDistant;
  private static String mRecordDiaryIdx;
  private WalkDialyAdapter mWalkDialyAdapter;
  private WalkDiaryPetListAdapter mWalkDiaryPetListAdapter;
  private WalkModel mWalkDiaryResponse = new WalkModel();
  private WalkModel mWalkResponse = new WalkModel();
  private boolean mImageAddButtonState = false;
  private ArrayList<WalkModel> mMyPetList = new ArrayList<>();


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_with_friend_dialy;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책일기");
    mWalkDistantTextView.setText(mDistant + "km");
    mWalkTimeTextView.setText("총" + mWalkTime + "분");

    diaryRegViewAPI();

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

      if (mTempImageFiles.size() == 11) {
        mTempImageFiles.remove(0);
      }

      fileUploads();
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void initRequest() {

    initWithPetAdapter();
    initWalkDialyImageList();


  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 함께한 반려견 리스트
   */
  private void initWithPetAdapter() {

    mWalkDiaryPetListAdapter = new WalkDiaryPetListAdapter(R.layout.row_my_pet, mMyPetList);
    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(mMyPetList.size(), -100);
    mWithPetRecyclerView.addItemDecoration(decoration_width);
    mWithPetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mWithPetRecyclerView.setAdapter(mWalkDiaryPetListAdapter);
  }

  /**
   * 이미지 목록
   */
  private void initWalkDialyImageList() {

    mImageList.add(new ImageModel());

    mWalkDialyAdapter = new WalkDialyAdapter(R.layout.row_edit_image, mImageList);
    mWalkDailyRecyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.getInstance().dpTopx(mActivity, 10), true));
    mWalkDailyRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
    mWalkDailyRecyclerView.setAdapter(mWalkDialyAdapter);

    mWalkDialyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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

    mWalkDialyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Timber.i("position ==== " + position);
        switch (view.getId()) {
          case R.id.delete_image_view:
            mImageList.remove(position);
            mWalkDialyAdapter.setNewData(mImageList);
            mImageListCntTextView.setText(mImageList.size() - 1 + "/5");
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
              mWalkDialyAdapter.setNewData(mImageList);
            } else {
              fileUploads();
            }
            mImageListCntTextView.setText(mImageList.size() - 1 + "/5");
          }
        }
      });
      return;
    }
  }

  /**
   * 산책일기 등록 폼 API
   */
  private void diaryRegViewAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mRecordIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().diary_reg_view(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkResponse = response.body();
          mMyPetList.addAll(mWalkResponse.getData_array());
          mWalkDiaryPetListAdapter.setNewData(mMyPetList);

        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }


  /**
   * 산책일기 저장 API
   */
  private void diaryRegInAPI() {
    try {
      ArrayList<String> imgPaths = new ArrayList<>();
      for (ImageModel value : mImageList) {
        if (value.getImg_path() != null) {
          imgPaths.add(value.getImg_path());
        }
      }
      WalkModel walkRequest = new WalkModel();
      walkRequest.setRecord_diary_idx(mRecordDiaryIdx);
      walkRequest.setRecord_idx(mRecordIdx);
      walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
      walkRequest.setRecord_img_paths(TextUtils.join(",", imgPaths));
      walkRequest.setMemo(mWalkMemoEditText.getText().toString());
      walkRequest.setRecord_type("1");
      walkRequest.setReview_0(String.valueOf(mReviewStar.getRating()));
      walkRequest.setReview_1(String.valueOf(mReviewStar1.getRating()));
      walkRequest.setReview_2(String.valueOf(mReviewStar2.getRating()));
      walkRequest.setReview_3(String.valueOf(mReviewStar3.getRating()));

      CommonRouter.api().diary_reg_in(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
        @Override
        public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
          if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
            mWalkDiaryResponse = response.body();
            finishWithRemove();
            Intent walkRefresh = new Intent(Constants.WALK_REFRESH);
            mActivity.sendBroadcast(walkRefresh);
          }
        }

        @Override
        public void onFailure(Call<WalkModel> call, Throwable t) {

        }
      });
    } catch (Exception e) {
    }
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 저장하기
   */
  @OnClick(R.id.save_dialy_button)
  public void saveDialyTouched() {
    showConfirmDialog("산책 일기를 저장하시겠습니까?", "취소", "확인", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        diaryRegInAPI();
      }
    });
    Timber.i(String.valueOf(mReviewStar.getRating()));
    Timber.i(String.valueOf(mReviewStar1.getRating()));
    Timber.i(String.valueOf(mReviewStar2.getRating()));
    Timber.i(String.valueOf(mReviewStar3.getRating()));

    Timber.i("touched");
  }

  /**
   * 취소
   */
  @OnClick(R.id.cancel_button)
  public void cancelTouched() {
    showConfirmDialog("산책일기를 저장하지 않습니다.", "취소", "확인", new DialogEventListener() {
      @Override
      public void onReceivedEvent() {
        finishWithRemove();
      }
    });
  }
}
