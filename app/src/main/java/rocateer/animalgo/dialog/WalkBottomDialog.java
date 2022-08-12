package rocateer.animalgo.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.main.MainActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class WalkBottomDialog extends BottomSheetDialogFragment {

  public interface WalkBottomDialogListener {
    void onCancel();
  }


  private RocateerActivity mActivity;
  private String  mRecordIdx;
  private WalkBottomDialogListener mWalkBottomDialogListener;




  public WalkBottomDialog(RocateerActivity activity, WalkBottomDialogListener walkBottomDialogListener) {
    mActivity = activity;
    mWalkBottomDialogListener = walkBottomDialogListener;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View v;

    v = inflater.inflate(R.layout.dialog_bottom_walk, container, false);

    LinearLayout mWalkCancleButton = v.findViewById(R.id.walk_cancle);
    mWalkCancleButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mWalkBottomDialogListener.onCancel();
        dismiss();
      }
    });

    LinearLayout mCancleButton = v.findViewById(R.id.cancle);
    mCancleButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("DECLARATION BUTTON -----------");
        dismiss();
      }
    });

    return v;
  }



}