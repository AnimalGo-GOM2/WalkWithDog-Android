package rocateer.animalgo.activity.history;

import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerFragment;
import rocateer.animalgo.activity.home.HomeFragment;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.activity.main.MainTabAdapter;
import rocateer.animalgo.activity.walk.WalkFragment;
import rocateer.animalgo.commons.SwipeViewPager;

public class HistoryFragment extends RocateerFragment {

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.history_tab_layout)
  TabLayout mHistoryTabLayout;
  @BindView(R.id.history_view_pager)
  SwipeViewPager mHistoryViewPager;
  @BindView(R.id.history_list_layout)
  LinearLayout mHistoryListLayout;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private HistoryTabAdapter mHistoryTabAdapter;
  private WalkFragment walkFragment;


  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.fragment_history;
  }

  @Override
  protected void initLayout() {


  }

  @Override
  protected void initRequest() {
    initMenu();

  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);

  }


  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 메뉴 세팅
   */
  private void initMenu() {
    mHistoryTabAdapter = new HistoryTabAdapter(getChildFragmentManager());
    mHistoryViewPager.setAdapter(mHistoryTabAdapter);
    mHistoryViewPager.setOffscreenPageLimit(2);
    mHistoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mHistoryTabLayout));
    mHistoryViewPager.setPagingEnabled(false);

    mHistoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
          case 0:
            break;
          case 1:
            break;
        }
        mHistoryViewPager.setCurrentItem(tab.getPosition());
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




}
