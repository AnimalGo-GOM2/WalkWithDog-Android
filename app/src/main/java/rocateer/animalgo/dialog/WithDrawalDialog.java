package rocateer.animalgo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Member;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.InBoxFragment;
import rocateer.animalgo.activity.intro.SigninActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.api.CommonRouter;

public class WithDrawalDialog extends BottomSheetDialogFragment {
    private Context mContext;
    private RocateerActivity mActivity;
    private Dialog mDialog;
    private MemberModel mMemberResposne = new MemberModel();

    public AppCompatButton mWithDrawalButton;
    public AppCompatButton mWithDrawalCancleButton;
    public AppCompatEditText mLeaveReasonEditText;


  /**
   * 회원탈퇴 API
   */
  public void memberOutUpAPI() {
    MemberModel memberRequest = new MemberModel();
    mLeaveReasonEditText = (AppCompatEditText) mDialog.findViewById(R.id.leave_reason_edit_text);
    memberRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));
    memberRequest.setMember_leave_reason(mLeaveReasonEditText.getText().toString());

    CommonRouter.api().member_out_up(Tools.getInstance().getMapper(memberRequest)).enqueue(new Callback<MemberModel>() {
      @Override
      public void onResponse(Call<MemberModel> call, Response<MemberModel> response) {
        if (Tools.getInstance().isSuccessResponse(response)) {
          Prefs.remove(Constants.MEMBER_IDX);
          Prefs.remove(Constants.MEMBER_ID);
          Prefs.remove(Constants.MEMBER_PW);
          Prefs.remove(Constants.MEMBER_NAME);
          Prefs.remove(Constants.MEMBER_REGION);
          Intent signInActivity = SigninActivity.getStartIntent(mActivity);
          startActivity(signInActivity);

        }
      }

      @Override
      public void onFailure(Call<MemberModel> call, Throwable t) {

      }
    });
  }

  public WithDrawalDialog(Context context) {
      this.mContext = context;
      mDialog = new Dialog(context);
      mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      mDialog.setContentView(R.layout.dialog_withdrawal);
      mDialog.setCancelable(true);

      mWithDrawalButton = (AppCompatButton) mDialog.findViewById(R.id.withdrawal_button);
      mWithDrawalButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          memberOutUpAPI();
          dismiss();

        }
      });
      mWithDrawalCancleButton = (AppCompatButton) mDialog.findViewById(R.id.withdrawal_cancel_button);
      mWithDrawalCancleButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          dismiss();
        }
      });


    }

    public void show() {
      mDialog.show();
    }

    public void dismiss() {
      mDialog.dismiss();
    }
}