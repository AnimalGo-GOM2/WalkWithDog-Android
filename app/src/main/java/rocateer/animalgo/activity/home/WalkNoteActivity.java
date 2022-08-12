package rocateer.animalgo.activity.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;

import rocateer.animalgo.commons.SwipeViewPager;
import rocateer.animalgo.models.MemberModel;

public class WalkNoteActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, WalkNoteActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.walk_note_tab_layout)
  TabLayout mWalkNoteTabLayout;
  @BindView(R.id.walk_note_view_pager)
  SwipeViewPager mWalkNoteViewPager;
//  Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.walk_note_view_pager + ":" + 2);

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private WalkNoteTabAdapter mWalkNoteTabAdapter;
  private MemberModel mMemberResponse = new MemberModel();

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_walk_note;
  }

  @Override
  protected void initLayout() {
    initToolbar("산책 쪽지");
    initMenu();

  }

  @Override
  protected void initRequest() {

  }
  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  /**
   * 메뉴 세팅
   */
  private void initMenu() {
    mWalkNoteTabAdapter = new WalkNoteTabAdapter(getSupportFragmentManager());
    mWalkNoteViewPager.setAdapter(mWalkNoteTabAdapter);
    mWalkNoteViewPager.setOffscreenPageLimit(1);
    mWalkNoteViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mWalkNoteTabLayout));
    mWalkNoteViewPager.setPagingEnabled(false);

    mWalkNoteTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
          case 0:
            break;
          case 1:
            break;
        }
        mWalkNoteViewPager.setCurrentItem(tab.getPosition());
      }
      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }



  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------
  /**
   * 보낸 쪽지 검색
   */
  @OnClick(R.id.note_search_button)
  public void noteSearchTouched() {
    Intent noteSearchActivity = NoteSearchActivity.getStartIntent(mActivity);
    startActivity(noteSearchActivity,TRANS.PUSH);
  }

  /**
   * 쪽지 보내기
   */

  @OnClick(R.id.send_note_button)
  public void sendNoteTouched() {
    Intent sendNoteSearchAcitivty = SendNoteSearchActivity.getStartIntent(mActivity);
    startActivity(sendNoteSearchAcitivty, RocateerActivity.TRANS.PUSH);
  }
}
