package rocateer.animalgo.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.intro.StartPopupDialog;
import rocateer.animalgo.activity.walk.WalkTrackingActivity;
import rocateer.animalgo.activity.walk.WithPetTrackingActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.SwipeViewPager;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.BaseRouter;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;


public class MainActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.main_view_pager)
  SwipeViewPager mMainViewPager;
  @BindView(R.id.main_tab_layout)
  TabLayout mMainTabLayout;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  private MainTabAdapter mainTabAdapter;
  private String mRecordType = Prefs.getString(Constants.RECORD_TYPE, "");

  Intent mTrackingService;

  //기록 상태 표시//
  private BroadcastReceiver mRecordingStatusReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Timber.i("RECORD STATUS--------");
      boolean recordStatus = intent.getBooleanExtra("RECORD_STATUS", false);
      if (recordStatus) {
        Intent walkTrackingActivitiy = WalkTrackingActivity.getStartIntent(mActivity, Prefs.getString(Constants.RECORD_IDX,""), null);
        startActivity(walkTrackingActivitiy, TRANS.PUSH);
      } else {

      }
    }
  };

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_main;
  }

  @Override
  protected void initLayout() {
    mActivity.registerReceiver(mRecordingStatusReceiver, new IntentFilter(Constants.RECORDING_STATUS));
    initMenu();
    setTabItem();
    if (Prefs.getString(Constants.RECORD_TYPE,"").equals("0")) {
      Intent withPetTrackingActivity = WithPetTrackingActivity.getStartIntent(mActivity, Prefs.getString(Constants.REOCRD_IDX, ""));
      startActivity(withPetTrackingActivity, TRANS.PRESENT);
    } else if (Prefs.getString(Constants.RECORD_TYPE,"").equals("1")) {
      Intent walkTrackingActivity = WalkTrackingActivity.getStartIntent(mActivity, Prefs.getString(Constants.REOCRD_IDX, ""), null);
      startActivity(walkTrackingActivity, TRANS.PRESENT);

    } else {


    }

  }

  @Override
  protected void initRequest() {
    popupDetailAPI();
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------

  /**
   * 메뉴 세팅
   */
  private void initMenu() {

    mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
    mMainViewPager.setAdapter(mainTabAdapter);
    mMainViewPager.setOffscreenPageLimit(5);
    mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
    mMainViewPager.setPagingEnabled(false);

    mMainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
          case 0:
            Intent mainRefresh = new Intent(Constants.MAIN_REFRESH);
            mActivity.sendBroadcast(mainRefresh);
            break;
          case 1:
            Intent walkRefresh = new Intent(Constants.WALK_REFRESH);
            mActivity.sendBroadcast(walkRefresh);
            break;
          case 2:
            Intent withFriendRefresh = new Intent(Constants.WITH_FRIEND_HISTORY_REFRESH);
            mActivity.sendBroadcast(withFriendRefresh);
            break;
          case 3:
            break;
          case 4:
            break;
        }
        mMainViewPager.setCurrentItem(tab.getPosition());
      }


      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });

  }

  /**
   * 커스텀 탭 아이템 설정
   */
  private void setTabItem() {
    String[] menuText = {"홈", "산책하기", "산책기록", "내 반려견", "더보기"};
    int[] menuIcon = {R.drawable.selector_menu_1, R.drawable.selector_menu_2, R.drawable.selector_menu_3, R.drawable.selector_menu_4, R.drawable.selector_menu_5};

    for (int i = 0; i < menuText.length; i++) {
      View menuItem = getLayoutInflater().inflate(R.layout.main_menu_tab, null);
      AppCompatImageView menuImageView = menuItem.findViewById(R.id.menu_icon_image_view);
      AppCompatTextView menu1TextView = menuItem.findViewById(R.id.menu_text_view);
      menuImageView.setImageResource(menuIcon[i]);
      menu1TextView.setText(menuText[i]);
      mMainTabLayout.getTabAt(i).setCustomView(menuItem);
    }
    mMainTabLayout.getTabAt(0).select();
    mMainViewPager.setCurrentItem(0);
  }

  /**
   * 앱 시작 팝업 API
   */
  private void popupDetailAPI() {
    MemberModel memberRequest = new MemberModel();
    CommonRouter.api().start_popup_detail(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          MemberModel mMemberResponse = response.body();

          DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
          Date nowDate = Tools.getInstance().getZeroTimeDate(new Date());


          String todayCheck = Prefs.getString(Constants.START_POPUP, "");

          if (mMemberResponse.getImg_path() != null) {
            if (todayCheck.equals("")) {
              StartPopupDialog startDialog = new StartPopupDialog(mActivity, mMemberResponse, nowDate);
              startDialog.show(getSupportFragmentManager(), "MainActivity");
            } else {
              try {
                Date savedDate = sdFormat.parse(todayCheck);
                int compare = savedDate.compareTo(nowDate) + 2;

                if (compare < 0) {
                  StartPopupDialog startDialog = new StartPopupDialog(mActivity, mMemberResponse, nowDate);
                  startDialog.show(getSupportFragmentManager(), "MainActivity");
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
            }
          }


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