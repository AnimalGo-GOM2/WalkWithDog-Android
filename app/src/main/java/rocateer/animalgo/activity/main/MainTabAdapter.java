package rocateer.animalgo.activity.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import rocateer.animalgo.activity.history.HistoryFragment;
import rocateer.animalgo.activity.home.HomeFragment;
import rocateer.animalgo.activity.mypet.MyPetFragment;
import rocateer.animalgo.activity.seemore.SeeMoreFragment;
import rocateer.animalgo.activity.walk.WalkFragment;

public class MainTabAdapter extends FragmentStatePagerAdapter {

  private HomeFragment homeFragment;
  private WalkFragment walkFragment;
  private HistoryFragment historyFragment;
  private MyPetFragment myPetFragment;
  private SeeMoreFragment seeMoreFragment;

  public MainTabAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      homeFragment = new HomeFragment();
      return homeFragment;
    } else if (position == 1) {
      walkFragment = new WalkFragment();
      return walkFragment;
    } else if (position == 2) {
      historyFragment = new HistoryFragment();
      return historyFragment;
    } else if (position == 3){
      myPetFragment = new MyPetFragment();
      return myPetFragment;
    } else {
      seeMoreFragment = new SeeMoreFragment();
      return seeMoreFragment;
    }

  }

  @Override
  public int getCount() {
    return 5;
  }
}


