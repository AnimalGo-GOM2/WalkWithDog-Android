package rocateer.animalgo.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

public class NoteSearchActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, NoteSearchActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.send_note_search_recycler_View)
  RecyclerView mSendNoteSearchRecyclerView;
  @BindView(R.id.search_edit_text)
  AppCompatEditText mSearchEditText;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private SearchNoteAdapter mSearchNoteAdapter;
  private ArrayList<MemoModel> mSearchList = new ArrayList<>();
  private MemoModel mMemoResponse = new MemoModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_note_search_activity;
  }

  @Override
  protected void initLayout() {
    initToolbar("보낸 쪽지 검색");

    mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          if (mSearchEditText.getText().length() > 0) {
            mSearchList.clear();
            mMemoResponse.resetPage();
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
    initSearchSendNoteAdapter();

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  private void initSearchSendNoteAdapter() {

    mSearchNoteAdapter = new SearchNoteAdapter(R.layout.row_search_send_note, mSearchList);
    View emptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mSendNoteSearchRecyclerView.getParent(), false);
    AppCompatImageView emptyImageView = (AppCompatImageView) emptyView.findViewById(R.id.empty_image_view);
    emptyImageView.setVisibility(View.GONE);
    AppCompatTextView emptyTextView = (AppCompatTextView) emptyView.findViewById(R.id.empty_text_view);
    emptyTextView.setText("검색결과가 없습니다.");
    mSearchNoteAdapter.setEmptyView(emptyView);
    mSearchNoteAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent sentInBoxDetailActivity = SentInBoxDetailActivity.getStartIntent(mActivity,mSearchList.get(position).getMemo_idx());
        startActivity(sentInBoxDetailActivity, TRANS.PUSH);
      }
    });
    mSendNoteSearchRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mSendNoteSearchRecyclerView.setAdapter(mSearchNoteAdapter);
    memoListAPI();
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    View focusView = getCurrentFocus();
    if (focusView != null) {
      Rect rect = new Rect();
      focusView.getGlobalVisibleRect(rect);
      int x = (int) ev.getX(), y = (int) ev.getY();
      if (!rect.contains(x, y)) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
          imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        focusView.clearFocus();
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  /**
   * 보낸 쪽지 검색
   */
  public void memoListAPI() {
    MemoModel memoRequest = new MemoModel();
    memoRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memoRequest.setType("1");
    memoRequest.setSearch(mSearchEditText.getText().toString());
    memoRequest.setPage_num(mMemoResponse.getNextPage());

    CommonRouter.api().memo_list(Tools.getInstance().getMapper(memoRequest)).enqueue(new Callback<MemoModel>() {
      @Override
      public void onResponse(Call<MemoModel> call, Response<MemoModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
         MemoModel mMemoResponse = response.body();
          if (mMemoResponse.getData_array() != null) {
            mSearchList.addAll(mMemoResponse.getData_array());
          }
          mSearchNoteAdapter.setNewData(mSearchList);
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
  /**
   * 검색 버튼
   */
  @OnClick(R.id.search_button)
  public void searchTouched() {
    HideUtil.hideSoftKeyboard(mActivity);
    mSearchList.clear();
    mMemoResponse.resetPage();
    memoListAPI();
  }
}