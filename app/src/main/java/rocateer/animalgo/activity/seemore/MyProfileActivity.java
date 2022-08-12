package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.canhub.cropper.CropImageView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;

public class MyProfileActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, MyProfileActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.member_nickname_edit_text)
  AppCompatEditText mMemberNickNameEditText;
  @BindView(R.id.user_image_view)
  AppCompatImageView mUserImageView;
  @BindView(R.id.gender_text_view)
  AppCompatTextView mGenderTextView;
  @BindView(R.id.age_text_view)
  AppCompatTextView mAgeTextView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private String mImagePath;
  private MemberModel mMemberResponse = new MemberModel();
  private ImagePickerDialog mImagePickerDialog;
  private RequestOptions requestOptions = new RequestOptions();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_my_profile;
  }

  @Override
  protected void initLayout() {
    initToolbar("내 프로필");

    memberInfoDetailAPI();
  }

  @Override
  protected void initRequest() {

  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // 이미지 선택 결과
    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
      Image image = ImagePicker.getFirstImageOrNull(data);
      Uri imageUri = FileProvider.getUriForFile(mActivity, "rocateer.animalgo.provider", new File(image.getPath()));


      Tools.getInstance(mActivity).cropImage(imageUri, CropImageView.CropShape.OVAL, new ActivityResultCallback<CropImageView.CropResult>() {
        @Override
        public void onActivityResult(CropImageView.CropResult result) {
          if (result.isSuccessful()) {
            String uriFilePath = result.getUriFilePath(mActivity, false);
            File file = new File(uriFilePath);

            Tools.getInstance().fileUploadAction(file, new Tools.FileUploadListener() {
              @Override
              public void onResult(boolean isSuccess, String filePath) {
                if (isSuccess) {
                  // 이미지 업로드 성공
                  requestOptions.centerCrop();
                  Glide.with(mActivity)
                      .load(BaseRouter.BASE_URL + filePath)
                      .apply(requestOptions)
                      .into(mUserImageView);
                  mImagePath = filePath;
                }

                if (mImagePickerDialog != null) {
                  mImagePickerDialog.dismiss();
                }
              }
            });
          }
        }
      });
    }


    super.onActivityResult(requestCode, resultCode, data);

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 회원정보 상세 보기
   */
  public void memberInfoDetailAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));

    CommonRouter.api().member_info_detail(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();
          requestOptions.placeholder(R.drawable.default_profile);
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mMemberResponse.getMember_img())
              .apply(requestOptions)
              .into(mUserImageView);
          mMemberNickNameEditText.setText(mMemberResponse.getMember_nickname());
          if (mMemberResponse.getMember_gender().equals("0")) {
            mGenderTextView.setText("남");
          } else if (mMemberResponse.getMember_gender().equals("1")) {
            mGenderTextView.setText("여");
          }
          mAgeTextView.setText(mMemberResponse.getMember_age() + "대");
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  /**
   * 내 정보 수정
   */
  public void memberInfoModUpAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memberRequest.setMember_img(mImagePath);
    memberRequest.setMember_nickname(mMemberNickNameEditText.getText().toString());

    CommonRouter.api().member_info_mod_up(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();
          Intent mainActivity = MainActivity.getStartIntent(mActivity);
          startActivity(mainActivity,TRANS.ZOOM);
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
   * 완료
   */
  @OnClick(R.id.complete_button)
  public void completeTouched() {
    memberInfoModUpAPI();
  }

  /**
   * 이미지 수정 버튼
   */
  @OnClick(R.id.camera_button)
  public void editImageTouched() {

    mImagePickerDialog = new ImagePickerDialog(mActivity);
    mImagePickerDialog.mCameraButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ImagePicker.create(mActivity)
            .imageDirectory(getString(R.string.app_name))
            .cameraOnly()
            .start(mActivity);
      }
    });
    mImagePickerDialog.mAlbumButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ImagePicker.create(mActivity)
            .returnMode(ReturnMode.ALL)
            .folderMode(true)
            .toolbarFolderTitle(getString(R.string.app_name))
            .toolbarImageTitle(getString(R.string.app_name))
            .includeVideo(false)
            .includeAnimation(false)
            .imageDirectory(getString(R.string.app_name))
            .single()
            .theme(R.style.ImagePickerTheme)
            .showCamera(false)
            .start();
      }
    });
    mImagePickerDialog.show();
  }


}

