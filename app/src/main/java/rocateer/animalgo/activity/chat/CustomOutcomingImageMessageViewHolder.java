package rocateer.animalgo.activity.chat;

import android.util.Pair;
import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import rocateer.animalgo.activity.chat.Models.Message;

public class CustomOutcomingImageMessageViewHolder
    extends MessageHolders.OutcomingImageMessageViewHolder<Message> {

  public CustomOutcomingImageMessageViewHolder(View itemView, Object payload) {
    super(itemView, payload);
  }

  @Override
  public void onBind(Message message) {
    super.onBind(message);
  }

  //Override this method to have ability to pass custom data in ImageLoader for loading image(not avatar).
  @Override
  protected Object getPayloadForImageLoader(Message message) {
    //For example you can pass size of placeholder before loading
    return new Pair<>(100, 100);
  }
}
