package rocateer.animalgo.activity.chat;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.stfalcon.chatkit.messages.MessageHolders;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.chat.Models.Message;

public class CustomIncomingTextMessageViewHolder
    extends MessageHolders.IncomingTextMessageViewHolder<Message> {

  private View onlineIndicator;

  private AppCompatTextView mUserNameTextview;
  private AppCompatTextView mTypeTextView;

  public CustomIncomingTextMessageViewHolder(View itemView, Object payload) {
    super(itemView, payload);
    mUserNameTextview = itemView.findViewById(R.id.user_name_text_view);
    mTypeTextView = itemView.findViewById(R.id.type_text_view);
  }

  @Override
  public void onBind(Message message) {
    super.onBind(message);

    mUserNameTextview.setText(message.getUser().getName());
    mTypeTextView.setText(message.getUser().getType());

    final Payload payload = (Payload) this.payload;
    userAvatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (payload != null && payload.avatarClickListener != null) {
          payload.avatarClickListener.onAvatarClick();
        }
      }
    });
  }

  public static class Payload {
    public OnAvatarClickListener avatarClickListener;
  }

  public interface OnAvatarClickListener {
    void onAvatarClick();
  }
}
