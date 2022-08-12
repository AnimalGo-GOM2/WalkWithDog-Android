package rocateer.animalgo.activity.chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.imageviewer.StfalconImageViewer;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.chat.Models.Message;
import rocateer.animalgo.dialog.ImagePickerDialog;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.chat.Models.User;
import timber.log.Timber;

public class ChatActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, ChatActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.messages_list)
  MessagesList mMessagesList;
  @BindView(R.id.message_input)
  MessageInput mMessageInput;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ImageLoader mImageLoader;
  private MessagesListAdapter<Message> mMessagesAdapter;
  private String senderId = "0";

  private ImagePickerDialog mImagePickDialog;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_chat;
  }

  @Override
  protected void initLayout() {
    initToolbar("채팅");
    mImageLoader = new ImageLoader() {
      @Override
      public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
        Glide.with(mActivity)
            .load(url)
            .into(imageView);
      }
    };

    mMessageInput.setInputListener(new MessageInput.InputListener() {
      @Override
      public boolean onSubmit(CharSequence input) {
        User user = new User("0", "김티유", "http://i.imgur.com/pv1tBmT.png", "비품담당");

        // 텍스트 메세지
        Message message = new Message("0", user, input.toString());
        // 이미지 메세지
//        Message message = new Message("0", user, new Message.Image("http://i.imgur.com/pv1tBmT.png"), new Date());

        mMessagesAdapter.addToStart(message, true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chat_room");
        myRef.setValue(message, new DatabaseReference.CompletionListener() {
          @Override
          public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
            if (error != null) {
              Timber.e("ERROR : " + error.getMessage());
            }
          }
        });


        return true;
      }
    });
    // 이미지 업로드
    mMessageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
      @Override
      public void onAddAttachments() {
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
                .returnMode(ReturnMode.ALL)
                .folderMode(false)
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
        mImagePickDialog.show();
      }
    });

//    initAdapter();
  }

  @Override
  protected void initRequest() {

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // 이미지 선택 결과
    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
      Image image = ImagePicker.getFirstImageOrNull(data);
      Uri imageUri = Uri.fromFile(new File(image.getPath()));

      File cropDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/." + getString(R.string.app_name));
      if (!cropDir.exists()) {
        cropDir.mkdir();
      }
      File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/." + getString(R.string.app_name) + "/" + "images_" + new Date().toString() + ".jpeg");

//      CropImage.activity(imageUri)
//          .setGuidelines(CropImageView.Guidelines.ON)
//          .setCropShape(CropImageView.CropShape.RECTANGLE)
//          .setAllowFlipping(false)
//          .setAllowRotation(false)
//          .setOutputUri(Uri.fromFile(dir))
//          .setOutputCompressQuality(80)
//          .setCropMenuCropButtonTitle("확인")
//          .start(this);

    }

    // 크롭 결과
//    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//      CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//      if (resultCode == RESULT_OK) {
//        Uri resultUri = result.getUri();
//        File imageFile = new File(resultUri.getPath());
//        fileUploadAction(imageFile);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.centerCrop();
//        Glide.with(this)
//            .load(resultUri)
//            .apply(requestOptions)
//            .into(mProfileImageView);

//        User user = new User("0", "김티유", "http://i.imgur.com/pv1tBmT.png", "비품담당");
//
//        // 텍스트 메세지
//        Message message = new Message("0", user, input.toString());
//        // 이미지 메세지
//        Message message = new Message("0", user, new Message.Image(resultUri.getPath().toString()), new Date());
//
//        mMessagesAdapter.addToStart(message, true);
//
//        mImagePickDialog.dismiss();
//
//      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//        Exception error = result.getError();
//        Timber.e(error.getLocalizedMessage());
//      }
//    }
//
//    super.onActivityResult(requestCode, resultCode, data);
//  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 채팅 초기화
   */
//  private void initAdapter() {
//
////    //We can pass any data to ViewHolder with payload
////    CustomIncomingTextMessageViewHolder.Payload payload = new CustomIncomingTextMessageViewHolder.Payload();
////    //For example click listener
////    payload.avatarClickListener = new CustomIncomingTextMessageViewHolder.OnAvatarClickListener() {
////      @Override
////      public void onAvatarClick() {
////        Toast.makeText(CustomHolderMessagesActivity.this,
////            "Text message avatar clicked", Toast.LENGTH_SHORT).show();
////      }
////    };
//
//    MessageHolders holdersConfig = new MessageHolders()
//        .setIncomingTextConfig(
//            CustomIncomingTextMessageViewHolder.class,
//            R.layout.chat_incoming_text_message)
//        .setOutcomingTextConfig(
//            CustomOutcomingTextMessageViewHolder.class,
//            R.layout.chat_outcoming_text_messgae)
//        .setIncomingImageConfig(
//            CustomIncomingImageMessageViewHolder.class,
//            R.layout.chat_incoming_image_message)
//        .setOutcomingImageConfig(
//            CustomOutcomingImageMessageViewHolder.class,
//            R.layout.chat_outcoming_image_message);
//
//    mMessagesAdapter = new MessagesListAdapter<>(senderId, holdersConfig, mImageLoader);
//    mMessagesAdapter.setOnMessageClickListener(new MessagesListAdapter.OnMessageClickListener<Message>() {
//      @Override
//      public void onMessageClick(Message message) {
//        if (message.getImageUrl() != null) {
//          ArrayList<String> imageList = new ArrayList<>();
//          imageList.add(message.getImageUrl());
//          new StfalconImageViewer.Builder(mActivity, imageList, new com.stfalcon.imageviewer.loader.ImageLoader<String>() {
//            @Override
//            public void loadImage(ImageView imageView, String image) {
//              Glide.with(mActivity)
//                  .load(image)
//                  .into(imageView);
//            }
//          })
//
//              .withHiddenStatusBar(true)
//              .withBackgroundColor(getColor(R.color.color_000000))
////              .withBackgroundColorResource(R.color.color_000000)
//              .allowZooming(true)
//              .allowSwipeToDismiss(true)
//              .show(true);
//        }
//      }
//    });

    // 메세지 롱클릭
    mMessagesAdapter.setOnMessageLongClickListener(new MessagesListAdapter.OnMessageLongClickListener<Message>() {
      @Override
      public void onMessageLongClick(Message message) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TU", message.getText());
        clipboard.setPrimaryClip(clip);
      }
    });

    // 메세지 더부르기
    mMessagesAdapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
//        List<Message> arrayList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//          User user = new User("1", "김티유", "http://i.imgur.com/pv1tBmT.png", "비품담당");
//          Message message = new Message("0", user, LoremIpsum.getInstance().getWords(2));
//          arrayList.add(message);
//        }
//        mMessagesAdapter.addToEnd(arrayList, false);
      }
    });

    // 메세지 헤더 세팅
    mMessagesAdapter.setDateHeadersFormatter(new DateFormatter.Formatter() {
      @Override
      public String format(Date date) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String to = transFormat.format(date);
        return to;
      }
    });
    mMessagesList.setAdapter(mMessagesAdapter);

    // TODO : 처음데이터
//    List<Message> arrayList = new ArrayList<>();
//    User user = new User("0", "김티유", "http://i.imgur.com/pv1tBmT.png", "비품담당");
//    Message message = new Message("0", user, "데이터");
//    arrayList.add(message);
//    mMessagesAdapter.addToEnd(arrayList, false);
//    mMessagesAdapter.onLoadMore(2, 10);
//    mMessagesAdapter.addToStart(message, true);
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

}