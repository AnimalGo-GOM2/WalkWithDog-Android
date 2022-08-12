package rocateer.animalgo.activity.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import rocateer.animalgo.activity.chat.Models.Message;

public class CustomOutcomingTextMessageViewHolder
    extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

  public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
    super(itemView, payload);
  }

  @Override
  public void onBind(Message message) {
    super.onBind(message);

//    time.setText(time.getText());
  }
}
