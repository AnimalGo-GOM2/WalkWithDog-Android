package rocateer.animalgo.activity.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WalkNoteTabAdapter extends FragmentStatePagerAdapter {

  private InBoxFragment inBoxFragment;
  private SentInboxFragment sentInboxFragment;

  public WalkNoteTabAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      inBoxFragment = new InBoxFragment();
      return inBoxFragment;
    } else {
      sentInboxFragment =new SentInboxFragment();
      return sentInboxFragment;
    }


  }

  @Override
  public int getCount() {
    return 2;
  }
}