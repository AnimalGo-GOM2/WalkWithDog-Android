package rocateer.animalgo.activity.history;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.walk.WalkDiaryPetListAdapter;
import rocateer.animalgo.activity.walk.WalkHistoryMyPetAdapter;
import rocateer.animalgo.commons.MyPetRecyclerView_Width;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.BaseRouter;

public class HistoryWithPetAdapter extends BaseQuickAdapter<WalkModel, BaseViewHolder> {
  WalkHistoryMyPetAdapter mWalkHistoryMyPetAdapter;
  public HistoryWithPetAdapter(int layoutResId, @Nullable List<WalkModel> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, WalkModel item) {
    helper.setText(R.id.history_distant_text_view, item.getRecord_distant() +"km");
    helper.setText(R.id.history_time_text_view, item.getRecord_hour()+"분");
    AppCompatTextView mRecordDateTextView = helper.getView(R.id.history_walk_date_text_view);
    helper.setText(R.id.history_walk_date_text_view, item.getRecord_start_date());


    initAnimalAdapter(helper, item);

  }
  private void initAnimalAdapter(BaseViewHolder helper, WalkModel item) {
    RecyclerView mMyPetListRecyclerView = helper.getView(R.id.my_pet_list_recycler_view);
//    RecyclerView mImageRecyclerView = helper.getView(R.id.history_my_pet_iamge_view);
    mWalkHistoryMyPetAdapter = new WalkHistoryMyPetAdapter(R.layout.row_history_my_pet, item.getMy_animal_array());

    MyPetRecyclerView_Width decoration_width = new MyPetRecyclerView_Width(item.getMy_animal_array().size(), -70);
    mMyPetListRecyclerView.addItemDecoration(decoration_width);
    mMyPetListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

    mMyPetListRecyclerView.setAdapter(mWalkHistoryMyPetAdapter);
  }
}
