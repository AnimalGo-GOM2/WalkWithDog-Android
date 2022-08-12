package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pixplicity.easyprefs.library.Prefs;
import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;
import rocateer.animalgo.models.api.MemoModel;

public class SendNoteActivity extends RocateerActivity {

  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context, MemoModel memoModel) {
    Intent intent = new Intent(context, SendNoteActivity.class);
    mMemoModel = memoModel;
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.user_list_recycler_View)
  RecyclerView mUserListRecyclerView;
  @BindView(R.id.search_edit_text)
  AppCompatEditText mSearchEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private SendNoteListAdapter mSendNoteListAdapter;
  private ArrayList<MemoModel> mUserList = new ArrayList<>();
  private static MemoModel mMemoModel;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_send_note;
  }

  @Override
  protected void initLayout() {
    initToolbar("쪽지 보내기");

    mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          if (mSearchEditText.getText().length() > 0) {
            mUserList.clear();
            mMemoModel.resetPage();
            memberListAPI();
          }

          HideUtil.hideSoftKeyboard(mActivity);
          handled = true;
        }
        return handled;
      }
    });

  }

  @Override
  protected void initRequest() {
    initUserListAdapter();


  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  private void initUserListAdapter() {

    mSendNoteListAdapter = new SendNoteListAdapter(R.layout.row_search_user, mUserList);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mUserListRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("검색결과가 없습니다.");
    mSendNoteListAdapter.setEmptyView(emptyView);
    mSendNoteListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent inputNoteActivity = InPutNoteActivity.getStartIntent(mActivity, mUserList.get(position).getPartner_member_idx());
        startActivity(inputNoteActivity, TRANS.PUSH);
      }
    });
    mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mUserListRecyclerView.setAdapter(mSendNoteListAdapter);
    memberListAPI();
  }

  /**
   * 회원 리스트 API
   */
  public void memberListAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setMember_nickname(mMemoModel.getMember_nickname());
    memoRequest.setMember_age(mMemoModel.getMember_age());
    memoRequest.setMember_gender(mMemoModel.getMember_gender());
    memoRequest.setPage_num(memoRequest.getNextPage());

    CommonRouter.api().member_list(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          mMemoModel = response.body();
          if (mMemoModel.getData_array() != null) {
            mUserList.addAll(mMemoModel.getData_array());
          }
          mSendNoteListAdapter.setNewData(mUserList);

        }
      }

      @Override
      public void onFailure(Call<MemoModel> call, Throwable t) {

      }
    });
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  @OnClick(R.id.search_button)
  public void searchTouched() {
    mMemoModel.resetPage();
    mUserList.clear();
    memberListAPI();
  }

}