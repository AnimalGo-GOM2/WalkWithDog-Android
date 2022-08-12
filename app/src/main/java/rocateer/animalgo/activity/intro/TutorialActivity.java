package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.pixplicity.easyprefs.library.Prefs;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Constants;


public class TutorialActivity extends RocateerActivity {
  //--------------------------------------------------------------------------------------------
  // MARK : GET START INTENT
  //--------------------------------------------------------------------------------------------
  public static Intent getStartIntent(Context context) {
    Intent intent = new Intent(context, TutorialActivity.class);
    return intent;
  }

  //--------------------------------------------------------------------------------------------
  // MARK : Bind Area
  //--------------------------------------------------------------------------------------------
  @BindView(R.id.tutorial_view_pager)
  ViewPager mTutorialViewPager;
  @BindView(R.id.dots_indicator)
  WormDotsIndicator mDotsIndicator;
  @BindView(R.id.start_round_rect_view)
  RoundRectView mStartRoundRectView;
  @BindView(R.id.shadow_image_view)
  AppCompatImageView mShadowImageView;

  //--------------------------------------------------------------------------------------------
  // MARK : Local variables
  //--------------------------------------------------------------------------------------------
  TutorialAdapter mTutorialAdapter;
  int intentData = 0;

  //--------------------------------------------------------------------------------------------
  // MARK : Override
  //--------------------------------------------------------------------------------------------
  @Override
  protected int inflateLayout() {
    return R.layout.activity_tutorial;
  }

  @Override
  protected void initLayout() {
    intentData = getIntent().getIntExtra("value", 0);
    if (intentData == 1) {
      mStartRoundRectView.setVisibility(View.VISIBLE);
    } else {
      mStartRoundRectView.setVisibility(View.GONE);
    }
    initAdapter();
  }

  @Override
  protected void initRequest() {

  }

  //--------------------------------------------------------------------------------------------
  // MARK : Local functions
  //--------------------------------------------------------------------------------------------
  private void initAdapter() {
    ArrayList<Drawable> imageList = new ArrayList<>();
    imageList.add(getDrawable(R.drawable.aos_play_01));
    imageList.add(getDrawable(R.drawable.aos_play_02));
    imageList.add(getDrawable(R.drawable.aos_play_03));
    imageList.add(getDrawable(R.drawable.aos_play_04));
    imageList.add(getDrawable(R.drawable.aos_play_05));
    mTutorialAdapter = new TutorialAdapter(imageList, mActivity);
    mTutorialViewPager.setAdapter(mTutorialAdapter);
    mDotsIndicator.setViewPager(mTutorialViewPager);
    mTutorialViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOgreffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (position == imageList.size() - 1 || intentData == 1) {
          mStartRoundRectView.setVisibility(View.VISIBLE);
        } else {
          mStartRoundRectView.setVisibility(View.GONE);
        }
        if (position == 0 || position == 4) {
          mShadowImageView.setVisibility(View.GONE);
        } else {
          mShadowImageView.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }
  //--------------------------------------------------------------------------------------------
  // MARK : Bind Actions
  //--------------------------------------------------------------------------------------------

  /**
   * 닫기
   */
  @OnClick(R.id.start_round_rect_view)
  public void closeTouched() {
    Prefs.putBoolean(Constants.TUTORIAL, true);
    Intent signInActivity = SigninActivity.getStartIntent(mActivity);
    startActivity(signInActivity,TRANS.ZOOM);
    finishWithRemove();

  }
}