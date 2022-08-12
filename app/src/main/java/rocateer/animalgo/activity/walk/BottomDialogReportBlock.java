package rocateer.animalgo.activity.walk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Member;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.models.MemberModel;
import timber.log.Timber;

public class BottomDialogReportBlock extends BottomSheetDialogFragment {
  private RocateerActivity mActivity;
  private MemberModel memberModel;
  private String mPartnerMemberIdx;



  public BottomDialogReportBlock(RocateerActivity activity, String partnerMemberIdx) {
    this.mActivity = activity;
    mPartnerMemberIdx = partnerMemberIdx;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View v;

    v = inflater.inflate(R.layout.dialog_bottom_report_blocking, container, false);

    AppCompatTextView mHideInFeedTextView = v.findViewById(R.id.reporting_button);
    mHideInFeedTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("HIDE IN FEED BUTTON -----------");
        dismiss();
        Intent reportingActivity = ReportingActivity.getStartIntent(mActivity, mPartnerMemberIdx);
        startActivity(reportingActivity);
      }
    });

    AppCompatTextView mDeclarationTextView = v.findViewById(R.id.blocking_button);
    mDeclarationTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.i("DECLARATION BUTTON -----------");
        dismiss();
        Intent blockingActivity = BlockingActivity.getStartIntent(mActivity, mPartnerMemberIdx);
        startActivity(blockingActivity);
      }
    });

    return v;
  }



}