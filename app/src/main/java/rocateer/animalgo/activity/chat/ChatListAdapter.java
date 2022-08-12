package rocateer.animalgo.activity.chat;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rocateer.animalgo.models.BaseModel;

public class ChatListAdapter extends BaseQuickAdapter<BaseModel, BaseViewHolder> {
  public ChatListAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<BaseModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, BaseModel item) {

  }
}
