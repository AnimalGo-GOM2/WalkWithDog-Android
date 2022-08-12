package rocateer.animalgo.activity.walk;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.RocateerApplication;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.chat.CustomIncomingImageMessageViewHolder;
import rocateer.animalgo.activity.chat.CustomIncomingTextMessageViewHolder;
import rocateer.animalgo.activity.chat.CustomOutcomingImageMessageViewHolder;
import rocateer.animalgo.activity.chat.CustomOutcomingTextMessageViewHolder;
import rocateer.animalgo.activity.chat.Models.Message;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.dialog.BottomDialogWalkChat;
import rocateer.animalgo.dialog.WalkChatApplyDenialDialog;
import rocateer.animalgo.models.MessageItem;
import rocateer.animalgo.models.ReplyItem;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WalkChatActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, String chattingRoomIdx) {
    Intent intent = new Intent(context, WalkChatActivity.class);
    mChattingRoomIdx = chattingRoomIdx;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.message_recycler_view)
  RecyclerView mMessageRecyclerView;
  @BindView(R.id.send_chat_button)
  ImageView mSendChatButton;
  @BindView(R.id.my_name_text_view)
  AppCompatTextView mMyNameTextView;
  @BindView(R.id.my_age_text_view)
  AppCompatTextView mMyAgeTextView;
  @BindView(R.id.my_gender_text_view)
  AppCompatTextView mMyGenderTextView;
  @BindView(R.id.walk_start_date_text_view)
  AppCompatTextView mWalkStartDateTextView;
  @BindView(R.id.my_image_view)
  AppCompatImageView mMyImageView;
  @BindView(R.id.chat_edit_text)
  AppCompatEditText mChatEditText;
  @BindView(R.id.walk_start_button)
  LinearLayout mWalkStartButton;
  @BindView(R.id.walk_start_text_view)
  AppCompatTextView mWalkStartTextView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MessageAdapter mMessageAdapter;
  private ReplyAdapter mReplyAdapter;
  private WalkModel mWalkResponse = new WalkModel();
  private RequestOptions requestOptions = new RequestOptions();
  private static String mChattingRoomIdx;
  private String senderId = "0";
  private boolean isFirstScroll = true;
  private ArrayList<ReplyItem> mReplyList = new ArrayList<>();
  private WalkModel mWalkAcceptResponse = new WalkModel();
  private WalkModel mChattingDetailResponse = new WalkModel();



  private BroadcastReceiver mChatRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Timber.i("채팅 ---- ");
      mWalkResponse.resetPage();
      isFirstScroll = true;
      mReplyList.clear();
      chattingListAPI();
      NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.cancel(Constants.CHAT_NOTIFICATION);
      notificationManager.cancel(Integer.parseInt(mChattingRoomIdx));
    }
  };

  private BroadcastReceiver mAcceptRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      chattingRoomDetailAPI();
    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_chat;
  }

  @Override
  protected void initLayout() {
    registerReceiver(mChatRefreshReceiver, new IntentFilter(Constants.CHAT_REFRESH));
    registerReceiver(mAcceptRefreshReceiver, new IntentFilter(Constants.ACCEPT_REFRESH));

    initToolbar("지원한 산책 친구");
    initReplyAdapter();
  }

  @Override
  protected void initRequest() {
    chattingRoomDetailAPI();
    chattingListAPI();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mChatRefreshReceiver);
    unregisterReceiver(mAcceptRefreshReceiver);
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------


  /**
   * 채팀방 상세
   */
  private void chattingRoomDetailAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setChatting_room_idx(mChattingRoomIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().chatting_room_detail(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mChattingDetailResponse = response.body();
          mMyNameTextView.setText(mChattingDetailResponse.getMember_nickname());
          mMyAgeTextView.setText(mChattingDetailResponse.getMember_age() + "대");
          if (mChattingDetailResponse.getMember_gender().equals("0")) {
            mMyGenderTextView.setText("남성");
          } else if (mChattingDetailResponse.getMember_gender().equals("1")) {
            mMyGenderTextView.setText("여성");
          }
          mWalkStartDateTextView.setText(mChattingDetailResponse.getRecord_date());
          requestOptions.centerCrop();
          Glide.with(mActivity)
              .load(BaseRouter.BASE_URL + mChattingDetailResponse.getMember_img())
              .placeholder(R.drawable.default_profile)
              .into(mMyImageView);

          if (mChattingDetailResponse.getRecord_owner_idx().equals(mChattingDetailResponse.getMember_idx())) {
            if (mChattingDetailResponse.getState() != null) {
              if (mChattingDetailResponse.getState().equals("0") || mChattingDetailResponse.getState().equals("2") || mChattingDetailResponse.getState().equals("3") || mChattingDetailResponse.getState().equals("4")) {
                mWalkStartButton.setEnabled(false);
              } else if (mChattingDetailResponse.getState().equals("1")) {
                mWalkStartButton.setEnabled(true);
              }
            }
          } else if (mChattingDetailResponse.getMember_idx() != mChattingDetailResponse.getRecord_owner_idx()) {
            if (mChattingDetailResponse.getState() != null) {
              if (mChattingDetailResponse.getState().equals("0")) {
                mWalkStartTextView.setText("수락");
                mWalkStartButton.setEnabled(true);
              } else if (mChattingDetailResponse.getState().equals("1")) {
                mWalkStartButton.setEnabled(true);
                mWalkStartTextView.setText("산책시작");
              }
            }

          }


        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 산책 등록 취소
   */

  private void recordApplyCancelAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mChattingDetailResponse.getRecord_idx());
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().record_apply_cancel(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          finishWithRemove();
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });

  }

  /**
   * 산책 거부
   */
  private void recordApplyRefuseAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_participant_idx(mChattingDetailResponse.getRecord_participant_idx());
    CommonRouter.api().record_apply_refuse(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {


        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 채팅방 채탕 리스트
   */
  private void initReplyAdapter() {
    ArrayList<ReplyItem> chatList = new ArrayList<>();
    mReplyAdapter = new ReplyAdapter(chatList);
    mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mMessageRecyclerView.setAdapter(mReplyAdapter);
  }


  /**
   * 채팅 리스트 API
   */
  private void chattingListAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setChatting_room_idx(mChattingRoomIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));

    CommonRouter.api().chatting_list(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkResponse = response.body();

          for (WalkModel dataArray : mWalkResponse.getData_array()) {
            mReplyList.add(new ReplyItem(ReplyItem.HEADER, dataArray));
            for (WalkModel dayListArray : dataArray.getDay_list_array()) {
              if (dayListArray.getMember_idx().equals(Prefs.getString(Constants.MEMBER_IDX, ""))) {
                mReplyList.add(new ReplyItem(ReplyItem.MY, dayListArray));
              } else {
                mReplyList.add(new ReplyItem(ReplyItem.USER, dayListArray));
              }
            }
          }
        }
        mReplyAdapter.setNewData(mReplyList);

        if (isFirstScroll) {
          if (mReplyList.size() > 0) {
            mMessageRecyclerView.scrollToPosition(mReplyList.size() - 1);
            isFirstScroll = false;
          }

        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

  /**
   * 산책 수락
   */
  private void recordStartAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setRecord_idx(mChattingDetailResponse.getRecord_idx());
    walkRequest.setPartner_member_idx(mChattingDetailResponse.getMember_idx());
    walkRequest.setMember_nickname(mChattingDetailResponse.getMember_nickname());

    CommonRouter.api().record_start(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mWalkAcceptResponse = response.body();
          Prefs.putString(Constants.PARTNER_MEMBER_IDX, mChattingDetailResponse.getMember_idx());
          Prefs.putString(Constants.RECORD_TYPE,"1");
          Prefs.putString(Constants.REOCRD_IDX, mChattingDetailResponse.getRecord_idx());
          chattingRoomDetailAPI();
        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });


  }

  /**
   * 채팅 입력
   */
  private void chattingRegInAPI() {
    WalkModel walkRequest = new WalkModel();
    walkRequest.setChatting_room_idx(mChattingRoomIdx);
    walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    walkRequest.setComment(mChatEditText.getText().toString());
    walkRequest.setMember_nickname(Prefs.getString(Constants.MEMBER_NICKNAME, ""));

    CommonRouter.api().chatting_reg_in(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
      @Override
      public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mReplyList.clear();
          mWalkResponse.resetPage();
          chattingListAPI();
          isFirstScroll = true;


        }
      }

      @Override
      public void onFailure(Call<WalkModel> call, Throwable t) {

      }
    });
  }

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


  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 채팅
   */
  @OnClick(R.id.send_chat_button)
  public void sendTouched() {
    chattingRegInAPI();
    mChatEditText.setText("");
  }

  /**
   * 채팅 버튼 보이기
   */
  @OnClick(R.id.chat_edit_text)
  public void chatEditTextTouched() {
    mSendChatButton.setVisibility(View.VISIBLE);
  }

  /**
   * 신고 차단
   */
  @OnClick(R.id.dot_button)
  public void friendBottomPopupTouched() {

    if (mChattingDetailResponse.getRecord_owner_idx().equals(mChattingDetailResponse.getMember_idx())) {
      BottomDialogWalkChat bottomDialogWalkChat = new BottomDialogWalkChat(mActivity, mWalkResponse, new BottomDialogWalkChat.BottomDialogWalkChatListener() {
        @Override
        public void onCancel() {
          recordApplyCancelAPI();
        }
      });
      bottomDialogWalkChat.show(getSupportFragmentManager(), "WalkFriednRequstActivity");
    } else if (mChattingDetailResponse.getMember_idx() != mChattingDetailResponse.getRecord_owner_idx()) {
      WalkChatApplyDenialDialog walkChatApplyDenialDialog = new WalkChatApplyDenialDialog(mActivity, mWalkResponse, new WalkChatApplyDenialDialog.WalkChatApplyDenialDialogListener() {
        @Override
        public void onCancel() {
          recordApplyCancelAPI();
        }

        @Override
        public void onDenial() {
          recordApplyRefuseAPI();
        }
      });
      walkChatApplyDenialDialog.show(getSupportFragmentManager(), "");
    }
  }

  /**
   * 산책 시작
   */
  @OnClick(R.id.walk_start_button)
  public void walkStartTouched() {
    if (mChattingDetailResponse.getRecord_owner_idx().equals(mChattingDetailResponse.getMember_idx())) {
      if (mChattingDetailResponse.getState() != null) {
        if (mChattingDetailResponse.getState().equals("0") || mChattingDetailResponse.getState().equals("2") || mChattingDetailResponse.getState().equals("3") || mChattingDetailResponse.getState().equals("4")) {
          mWalkStartButton.setEnabled(false);
        } else if (mChattingDetailResponse.getState().equals("1")) {
          Prefs.putString(Constants.RECORD_TYPE,"1");
          Prefs.putString(Constants.REOCRD_IDX, mChattingDetailResponse.getRecord_idx());
          Prefs.putString(Constants.PARTNER_MEMBER_IDX, mChattingDetailResponse.getMember_idx());
          Timber.i(Prefs.getString(Constants.REOCRD_IDX,""));
          Intent walkFriendGuideActivity = WalkFriendGuideActivity.getStartIntent(mActivity, mChattingDetailResponse.getRecord_idx(), mChattingDetailResponse);
          startActivity(walkFriendGuideActivity, TRANS.PUSH);
          finishMultiRemoveActivity(3);
        }
      }
    } else if (mChattingDetailResponse.getMember_idx() != mChattingDetailResponse.getRecord_owner_idx()) {
      if (mChattingDetailResponse.getState() != null) {
        if (mChattingDetailResponse.getState().equals("0")) {
          recordStartAPI();
        } else if (mChattingDetailResponse.getState().equals("1")) {
          mWalkStartButton.setEnabled(true);
          Timber.i(Prefs.getString(Constants.REOCRD_IDX,""));
          Intent walkFriendGuideActivity = WalkFriendGuideActivity.getStartIntent(mActivity, mChattingDetailResponse.getRecord_idx(), mChattingDetailResponse);
          startActivity(walkFriendGuideActivity, TRANS.PUSH);
          finishMultiRemoveActivity(3);

        }
      }

    }

  }
}

