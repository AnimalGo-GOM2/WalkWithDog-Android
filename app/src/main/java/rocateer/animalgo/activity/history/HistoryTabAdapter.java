package rocateer.animalgo.activity.history;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class HistoryTabAdapter extends FragmentStatePagerAdapter {

  private HistoryWithPetFragment historyWithPetFragment;
  private HistoryWithFriendFragment historyWithFriendFragment;

  public HistoryTabAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      historyWithPetFragment = new HistoryWithPetFragment();
      return historyWithPetFragment;
    } else {
      historyWithFriendFragment =new HistoryWithFriendFragment();
      return historyWithFriendFragment;
    }

  }

  @Override
  public int getCount() {
    return 2;
  }
}
