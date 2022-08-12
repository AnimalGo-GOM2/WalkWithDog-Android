package rocateer.animalgo.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import rocateer.animalgo.R;
import timber.log.Timber;

//https://codinginflow.com/tutorials/android/modal-bottom-sheet
public class BottomSheetDialog extends BottomSheetDialogFragment {
  private BottomSheetListener mListener;
  private Activity mActivity;
  private String mViewTYpe;
  private String mViewType;


  public BottomSheetDialog(Activity activity, String viewType) {
    mActivity = activity;
    mViewTYpe = viewType;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View v;

    v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

    return v;

    /**
     * https://stackoverflow.com/questions/42694355/how-to-set-recyclerview-max-height/48728490
     */
  }

  public interface BottomSheetListener {
    void onButtonClicked(String text);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      mListener = (BottomSheetListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement BottomSheetListener");
    }
  }
}
