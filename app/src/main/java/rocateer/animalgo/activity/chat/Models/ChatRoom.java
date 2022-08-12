package rocateer.animalgo.activity.chat.Models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ChatRoom {
  // 채팅 아이디
  private String chat_id;
  // 채팅방 사용자들
  private ArrayList<String> userIdxs;

  public ChatRoom(String chat_id) {
    this.chat_id = chat_id;
  }
}
