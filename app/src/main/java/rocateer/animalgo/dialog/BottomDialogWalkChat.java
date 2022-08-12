package rocateer.animalgo.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.walk.BlockingActivity;
import rocateer.animalgo.activity.walk.ReportingActivity;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.WalkModel;
import timber.log.Timber;

public class BottomDialogWalkChat  extends BottomSheetDialogFragment {
  public interface BottomDialogWalkChatListener {
    void onCancel();
  }


  private RocateerActivity mActivity;
  private WalkModel walkModel;
  private BottomDialogWalkChatListener mBottomDialogWalkChatListener;



  public BottomDialogWalkChat(RocateerActivity activity, WalkModel mWalkModel, BottomDialogWalkChatListener bottomDialogWalkChatListener) {
    mActivity = activity;
    this.walkModel = mWalkModel;
    mBottomDialogWalkChatListener = bottomDialogWalkChatListener;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View v;

    v = inflater.inflate(R.layout.bottom_dialog_walk_chat, container, false);

    LinearLayout mApplyCancelButton = v.findViewById(R.id.apply_cancel_button);
    mApplyCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBottomDialogWalkChatListener.onCancel();
        Timber.i("HIDE IN FEED BUTTON -----------");
        dismiss();
      }
    });


    AppCompatTextView mReportingButton = v.findViewById(R.id.reporting_button);
    mReportingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("HIDE IN FEED BUTTON -----------");
        dismiss();
        Intent reportingActivity = ReportingActivity.getStartIntent(mActivity, walkModel.getMember_idx());
        startActivity(reportingActivity);
      }
    });

    AppCompatTextView mDeclarationTextView = v.findViewById(R.id.blocking_button);
    mDeclarationTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("DECLARATION BUTTON -----------");
        dismiss();
        Intent blockingActivity = BlockingActivity.getStartIntent(mActivity, walkModel.getMember_idx());
        startActivity(blockingActivity);
      }
    });

    return v;
  }



}