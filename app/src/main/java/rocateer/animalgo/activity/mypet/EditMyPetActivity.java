package rocateer.animalgo.activity.mypet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;

public class EditMyPetActivity extends RocateerActivity {
  public interface MyPetListener {
    void onRefresh();
  }
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String animalIdx, MyPetListener myPetListener) {
    Intent intent = new Intent(context, EditMyPetActivity.class);
    mAnimalIdx = animalIdx;
    mMyPetListener = myPetListener;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.male_button)
  RoundRectView mMaleButton;
  @BindView(R.id.female_button)
  RoundRectView mFemaleButton;
  @BindView(R.id.trans_button)
  RoundRectView mTransButton;
  @BindView(R.id.trans_no_button)
  RoundRectView mTransNoButton;
  @BindView(R.id.train_button)
  RoundRectView mTrainButton;
  @BindView(R.id.train_no_button)
  RoundRectView mTrainNoButton;
  @BindView(R.id.char1_button)
  RoundRectView mChar1Button;
  @BindView(R.id.char2_button)
  RoundRectView mChar2Button;
  @BindView(R.id.char3_button)
  RoundRectView mChar3Button;
  @BindView(R.id.char4_button)
  RoundRectView mChar4Button;
  @BindView(R.id.health1_button)
  RoundRectView mHealth1Button;
  @BindView(R.id.health2_button)
  RoundRectView mHealth2Button;
  @BindView(R.id.health3_button)
  RoundRectView mHealth3Button;
  @BindView(R.id.male_select_button)
  AppCompatTextView mMaleSelectButton;
  @BindView(R.id.female_select_button)
  AppCompatTextView mFemaleSelectButton;
  @BindView(R.id.year_power_spinner)
  PowerSpinnerView mYearPowerSpinner;
  @BindView(R.id.day_power_spinner)
  PowerSpinnerView mMonthSpinner;
  @BindView(R.id.pet_size_spinner)
  PowerSpinnerView mPetSizeSpinner;
  @BindView(R.id.pet_categ_spinner)
  PowerSpinnerView mPetCategorySpinner;
  @BindView(R.id.add_animal_image_view)
  AppCompatImageView mAddAnimalImageView;
  @BindView(R.id.add_animal_name_edit_text)
  AppCompatEditText mAddAnimalNameEditText;


  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ArrayList<RoundRectView> mGenderButtonList = new ArrayList<>();
  private ArrayList<RoundRectView> mTransButtonList = new ArrayList<>();
  private ArrayList<RoundRectView> mTrainButtonList = new ArrayList<>();
  private ArrayList<RoundRectView> mCharButtonList = new ArrayList<>();
  private ArrayList<RoundRectView> mHealthButtonList = new ArrayList<>();
  private RequestOptions requestOptions = new RequestOptions();
  private AnimalModel mAnimalResponse = new AnimalModel();
  private AnimalModel mAnimalTypeResponse = new AnimalModel();
  private AnimalModel mAnimalKindResponse = new AnimalModel();
  private ImagePickerDialog mImagePickerDialog;
  private static MyPetListener mMyPetListener;
  private String mFirstCategoryIdx;
  private String mSecondCategoryIdx;
  private String mImagePath;
  private static String mAnimalIdx;
  private String mCategoryManagementIdx = "";


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_edit_my_pet;
  }

  @Override
  protected void initLayout() {
    initToolbar("반려견 등록");

    int nowYear = Calendar.getInstance().get(Calendar.YEAR);
    ArrayList<String> mYearList = new ArrayList<>();
    for (int i = 2000; i <= nowYear; i++) {
      mYearList.add(String.valueOf(i));
    }
    mYearPowerSpinner.setItems(mYearList);


    mYearPowerSpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        mYearPowerSpinner.dismiss();
      }
    });

    mMonthSpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        mMonthSpinner.dismiss();
      }
    });
    mPetCategorySpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        mPetCategorySpinner.dismiss();
      }
    });
    mPetSizeSpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
      @Override
      public void onSpinnerOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        mPetSizeSpinner.dismiss();
      }
    });


    mGenderButtonList = new ArrayList<>(Arrays.asList(mMaleButton, mFemaleButton));
    mTransButtonList = new ArrayList<>(Arrays.asList(mTransButton, mTransNoButton));
    mTrainButtonList = new ArrayList<>(Arrays.asList(mTrainButton, mTrainNoButton));
    mCharButtonList = new ArrayList<>(Arrays.asList(mChar1Button, mChar2Button, mChar3Button, mChar4Button));
    mHealthButtonList = new ArrayList<>(Arrays.asList(mHealth1Button, mHealth2Button, mHealth3Button));



    mPetSizeSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        mPetCategorySpinner.selectItemByIndex(-1);
        mPetCategorySpinner.setText("");
        animalListKindAPI(mAnimalTypeResponse.getData_array().get(newIndex).getCategory_management_idx());
        mFirstCategoryIdx = mAnimalTypeResponse.getData_array().get(newIndex).getCategory_management_idx();

      }
    });
    mPetCategorySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
      @Override
      public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        mSecondCategoryIdx = mAnimalKindResponse.getData_array().get(newIndex).getCategory_management_idx();

      }
    });




  }




  @Override
  protected void initRequest() {
    animalDetailAPI();
    animalListTypeAPI();
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
                      .into(mAddAnimalImageView);
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
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    View focusView = getCurrentFocus();
    if (focusView != null) {
      Rect rect = new Rect();
      focusView.getGlobalVisibleRect(rect);
      int x = (int) ev.getX(), y = (int) ev.getY();
      if (!rect.contains(x, y)) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
          imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        focusView.clearFocus();
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  /**
   * 반려견 상세 API
   */
  private void animalDetailAPI() {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setAnimal_idx(mAnimalIdx);

    CommonRouter.api().animal_detail(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalResponse = response.body();
          mAddAnimalNameEditText.setText(mAnimalResponse.getAnimal_name());
          requestOptions.centerCrop();
          mImagePath = mAnimalResponse.getAnimal_img_path();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mAnimalResponse.getAnimal_img_path())
              .placeholder(R.drawable.default_profile)
              .into(mAddAnimalImageView);
          if (mAnimalResponse.getAnimal_gender().equals("0")) {
            mMaleButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mMaleButton.setBorderColor(getColor(R.color.colorAccent));
            mMaleButton.setSelected(true);
          } else if (mAnimalResponse.getAnimal_gender().equals("1")) {
            mFemaleButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mFemaleButton.setBorderColor(getColor(R.color.colorAccent));
            mFemaleButton.setSelected(true);
          }
          mPetSizeSpinner.setText(mAnimalResponse.getFirst_category_name());
          mPetCategorySpinner.setText(mAnimalResponse.getSecond_category_name());
          mFirstCategoryIdx = mAnimalResponse.getFirst_category_idx();
          mSecondCategoryIdx = mAnimalResponse.getSecond_category_idx();

          if (mAnimalResponse.getAnimal_neuter().equals("Y")) {
            mTransButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mTransButton.setBorderColor(getColor(R.color.colorAccent));
            mTransButton.setSelected(true);
          } else if (mAnimalResponse.getAnimal_neuter().equals("N")) {
            mTransNoButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mTransNoButton.setBorderColor(getColor(R.color.colorAccent));
            mTransNoButton.setSelected(true);
          }

          String[] animalBirth = mAnimalResponse.getAnimal_birth().split("-");
          mYearPowerSpinner.setText(animalBirth[0]);
          mMonthSpinner.setText(animalBirth[1]);

          if (mAnimalResponse.getAnimal_training().equals("Y")) {
            mTrainButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mTrainButton.setBorderColor(getColor(R.color.colorAccent));
            mTrainButton.setSelected(true);
          } else if (mAnimalResponse.getAnimal_training().equals("N")) {
            mTrainNoButton.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mTrainNoButton.setBorderColor(getColor(R.color.colorAccent));
            mTrainNoButton.setSelected(true);
          }

          if (mAnimalResponse.getAnimal_character().equals("0")) {
            mChar1Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mChar1Button.setBorderColor(getColor(R.color.colorAccent));
            mChar1Button.setSelected(true);
          } else if (mAnimalResponse.getAnimal_character().equals("1")) {
            mChar2Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mChar2Button.setBorderColor(getColor(R.color.colorAccent));
            mChar2Button.setSelected(true);
          } else if (mAnimalResponse.getAnimal_character().equals("2")) {
            mChar3Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mChar3Button.setBorderColor(getColor(R.color.colorAccent));
            mChar3Button.setSelected(true);
          } else if (mAnimalResponse.getAnimal_character().equals("3")) {
            mChar4Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mChar4Button.setBorderColor(getColor(R.color.colorAccent));
            mChar4Button.setSelected(true);
          }

          if (mAnimalResponse.getAnimal_health().equals("0")) {
            mHealth1Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mHealth1Button.setBorderColor(getColor(R.color.colorAccent));
            mHealth1Button.setSelected(true);
          } else if (mAnimalResponse.getAnimal_health().equals("1")) {
            mHealth2Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mHealth2Button.setBorderColor(getColor(R.color.colorAccent));
            mHealth2Button.setSelected(true);
          } else if (mAnimalResponse.getAnimal_health().equals("2")) {
            mHealth3Button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
            mHealth3Button.setBorderColor(getColor(R.color.colorAccent));
            mHealth3Button.setSelected(true);
          }
        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }

  /**
   * 견 분류 리스트
   */
  private void animalListTypeAPI() {
    AnimalModel animalRequest = new AnimalModel();
    CommonRouter.api().animal_list_type(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalTypeResponse = response.body();
          List<String> categoryList = new ArrayList<>();
          for (AnimalModel value : mAnimalTypeResponse.getData_array()) {
            categoryList.add(value.getCategory_name());

          }
          mPetSizeSpinner.setItems(categoryList);

        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }

  /**
   * 견종 리스트 API
   */
  private void animalListKindAPI(String parentcategoryManageMentIdx) {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setParent_category_management_idx(parentcategoryManageMentIdx);
    CommonRouter.api().animal_list_kind(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mAnimalKindResponse = response.body();
          List<String> categoryKindList = new ArrayList<>();
          for (AnimalModel value : mAnimalKindResponse.getData_array()) {
            categoryKindList.add(value.getCategory_name());
          }
          mPetCategorySpinner.setItems(categoryKindList);
        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }

  /**
   * 반려견 수정 API
   */
  private void animalModUpAPI() {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setAnimal_img_path(mImagePath);
    animalRequest.setAnimal_idx(mAnimalIdx);
    animalRequest.setAnimal_name(mAddAnimalNameEditText.getText().toString());
    animalRequest.setFirst_category_idx(mFirstCategoryIdx);
    animalRequest.setSecond_category_idx(mSecondCategoryIdx);
    if (mMaleButton.isSelected()) {
      animalRequest.setAnimal_gender("0");
    } else if (mFemaleButton.isSelected()) {
      animalRequest.setAnimal_gender("1");
    }
    if (mTransButton.isSelected()) {
      animalRequest.setAnimal_neuter("Y");
    } else if (mTransNoButton.isSelected()) {
      animalRequest.setAnimal_neuter("N");
    }
    if (mHealth1Button.isSelected()) {
      animalRequest.setAnimal_health("0");
    } else if (mHealth2Button.isSelected()) {
      animalRequest.setAnimal_health("1");
    } else if (mHealth3Button.isSelected()) {
      animalRequest.setAnimal_health("2");
    }
    if (mTrainButton.isSelected()) {
      animalRequest.setAnimal_training("Y");
    } else if (mTrainNoButton.isSelected()) {
      animalRequest.setAnimal_training("N");
    }
    animalRequest.setAnimal_birth(mYearPowerSpinner.getText().toString() + "-" + mMonthSpinner.getText().toString());
    if (mChar1Button.isSelected()) {
      animalRequest.setAnimal_character("0");
    } else if (mChar2Button.isSelected()) {
      animalRequest.setAnimal_character("1");
    } else if (mChar3Button.isSelected()) {
      animalRequest.setAnimal_character("2");
    } else if (mChar4Button.isSelected()) {
      animalRequest.setAnimal_character("3");
    }
    CommonRouter.api().animal_mod_up(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          finishWithRemove();
          mMyPetListener.onRefresh();

        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });

  }



  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------


  /**
   * 이미지 수정 버튼
   */
  @OnClick(R.id.add_animal_image_view)
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

  /**
   * 성별
   */
  @OnClick({R.id.male_button, R.id.female_button})
  public void genderTouched(RoundRectView button) {
    for (RoundRectView value : mGenderButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 중성화
   */
  @OnClick({R.id.trans_button, R.id.trans_no_button})
  public void transTouched(RoundRectView button) {
    for (RoundRectView value : mTransButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);
  }

  /**
   * 훈련
   */
  @OnClick({R.id.train_button, R.id.train_no_button})
  public void trainTouched(RoundRectView button) {
    for (RoundRectView value : mTrainButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 성격
   */
  @OnClick({R.id.char1_button, R.id.char2_button, R.id.char3_button, R.id.char4_button})
  public void charTouched(RoundRectView button) {
    for (RoundRectView value : mCharButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 건강상태
   */
  @OnClick({R.id.health1_button, R.id.health2_button, R.id.health3_button})
  public void healthTouched(RoundRectView button) {
    for (RoundRectView value : mHealthButtonList) {
      value.getChildAt(0).setBackgroundColor(getColor(R.color.color_ffffff));
      value.setBorderColor(getColor(R.color.color_EAE8E5));
      value.setSelected(false);
    }
    button.getChildAt(0).setBackgroundColor(getColor(R.color.color_28E0AB0A));
    button.setBorderColor(getColor(R.color.colorAccent));
    button.setSelected(true);

  }

  /**
   * 저장하기
   */
  @OnClick(R.id.save_button)
  public void saveTouched() {
    animalModUpAPI();
  }

  /**
   * 취소
   */
  @OnClick(R.id.cancle_button)
  public void cancleTouched() {
    finishWithRemove();
  }
}
