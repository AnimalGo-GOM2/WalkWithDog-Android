package rocateer.animalgo.activity.seemore;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.ImageModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;

public class BlockActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, BlockActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.block_recycler_view)
  RecyclerView mBlockRecyclerView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private ArrayList<MemberModel> mBlockList = new ArrayList<>();
  private BlockAdapter mBlockAdapter;
  private MemberModel mMemberResponse = new MemberModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_block;
  }

  @Override
  protected void initLayout() {
    mToolbarTitle.setText("차단 현황");


  }

  @Override
  protected void initRequest() {
    initBlockAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 차단 리스트
   */
  private void initBlockAdapter() {
    mBlockAdapter = new BlockAdapter(R.layout.row_block, mBlockList);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mBlockRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("차단한 친구가 없습니다. ");
    mBlockAdapter.setEmptyView(emptyView);
    mBlockAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.block_button) {
          showConfirmDialog("차단을 해제하시겠습니까?", "취소", "확인", new DialogEventListener() {
            @Override
            public void onReceivedEvent() {
              blockRegInAPI(mBlockList.get(position).getMember_idx());
              mBlockList.remove(position);
              mBlockAdapter.setNewData(mBlockList);


            }
          });
        }
      }
    });
    mBlockRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mBlockRecyclerView.setAdapter(mBlockAdapter);
    blockListAPI();
  }

  /**
   * 차단 리스트 API
   */
  private void blockListAPI() {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    CommonRouter.api().block_list(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        mMemberResponse = response.body();
        if (mMemberResponse.getCode().equals("1000")) {
          if (mMemberResponse.getData_array() != null) {
            mBlockList.addAll(mMemberResponse.getData_array());
          }
          mBlockAdapter.setNewData(mBlockList);
          Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).cancel();
        } else if (mMemberResponse.getCode().equals("-2")) {
        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  /**
   * 차단하기
   */
  private void blockRegInAPI(String partnerMemberIdx) {
    MemberModel memberRequest = new MemberModel();
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
    memberRequest.setPartner_member_idx(partnerMemberIdx);
    memberRequest.setBlock_contents("1");
    memberRequest.setBlock_type("1");

    CommonRouter.api().block_reg_in(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemberResponse = response.body();

        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
}

