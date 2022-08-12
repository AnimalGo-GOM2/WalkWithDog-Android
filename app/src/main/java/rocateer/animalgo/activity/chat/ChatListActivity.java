package rocateer.animalgo.activity.chat;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.chat.Models.ChatRoom;
import rocateer.animalgo.models.BaseModel;
import timber.log.Timber;

public class ChatListActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, ChatListActivity.class);
    return intent;
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.chat_list_recyclerview)
  RecyclerView mChatListRecyclerView;
  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ChatListAdapter mChatListAdapter;
  private ArrayList<BaseModel> mChatList = new ArrayList<>();
  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_chat_list;
  }

  @Override
  protected void initLayout() {
    mToolbarTitle.setText("채팅 목록");
    initChatListAdapter();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 채팅 목록
   */
  private void initChatListAdapter() {
    mChatListAdapter = new ChatListAdapter(R.layout.row_chat_list, mChatList);

    mChatListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mChatListRecyclerView.setAdapter(mChatListAdapter);
  }

  /**
   *
   */
  private void chatListAPI() {
    FirebaseDatabase.getInstance().getReference("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
          ChatRoom chatRooms = task.getResult().getValue(ChatRoom.class);

          Timber.i("읽은 값" + chatRooms.getChat_id());
        } else {
          Timber.e("[ERROR] " + task.getException());
        }
      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 채팅 방 생성
   */
  @OnClick(R.id.add_chat_button)
  public void addChatTouched() {
    DatabaseReference chatRoom = FirebaseDatabase.getInstance().getReference("chat");

    ArrayList<ChatRoom> chatRoomList = new ArrayList<>();
    chatRoomList.add(new ChatRoom("10dflsldkasldkfj"));
    chatRoom.setValue(chatRoomList, new DatabaseReference.CompletionListener() {
      @Override
      public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
        chatListAPI();
      }
    });


//    myRef.setValue(message, new DatabaseReference.CompletionListener() {
//      @Override
//      public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
//        if (error != null) {
//          Timber.e("ERROR : " + error.getMessage());
//        }
//      }
//    }
  }

}
