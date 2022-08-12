package rocateer.animalgo.activity.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import rocateer.animalgo.activity.chat.Models.Message;

public class CustomIncomingImageMessageViewHolder
    extends MessageHolders.IncomingImageMessageViewHolder<Message> {


  public CustomIncomingImageMessageViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  public void onBind(Message message) {
    super.onBind(message);
  }
}
