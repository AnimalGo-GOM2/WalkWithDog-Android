package rocateer.animalgo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.home.WalkFriendRequestActivity;
import rocateer.animalgo.activity.walk.BlockingActivity;
import rocateer.animalgo.activity.walk.DialogPetListAdapter;
import rocateer.animalgo.activity.walk.ReportingActivity;
import rocateer.animalgo.activity.walk.WalkTrackingActivity;
import rocateer.animalgo.activity.walk.WithFriendAdapter;
import rocateer.animalgo.activity.walk.WithFriendDialyActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;

public class BottomDialogApplyWalkFriend extends BottomSheetDialogFragment {
    public interface ApplyRefresh {
        void onRefresh();
    }

    private RocateerActivity mActivity;
    private MemberModel memberModel;
    private Dialog mDialog;
    ArrayList<AnimalModel> mPetList = new ArrayList<>();
    WalkModel mWalkModel = new WalkModel();
    AnimalModel mAnimalModel = new AnimalModel();
    DialogPetListAdapter mDialogPetListAdapter;
    RecyclerView mWithMyPetRecyclerView;
    AppCompatEditText mApplyCommentEditText;
    private static ApplyRefresh mApplyRefresh;


    public BottomDialogApplyWalkFriend(RocateerActivity activity, WalkModel walkModel, AnimalModel animalModel, ApplyRefresh applyRefresh) {
        this.mActivity = activity;
        this.mWalkModel = walkModel;
        this.mAnimalModel = animalModel;
        mApplyRefresh = applyRefresh;
        mDialog = new Dialog(mActivity);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v;

        v = inflater.inflate(R.layout.dialog_apply_walk_friend, container, false);


        AppCompatButton mHideInFeedTextView = v.findViewById(R.id.apply_button);
        mHideInFeedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("HIDE IN FEED BUTTON -----------");
                boolean isSelected = false;
                for (AnimalModel value : mPetList) {
                    if (value.isSelect()) {
                        isSelected = true;
                    }
                }

                if (!isSelected) {
                    mActivity.showAlertDialog("함께 산책 할 반려견을\n산책하여 주세요.", "확인", new RocateerActivity.DialogEventListener() {
                        @Override
                        public void onReceivedEvent() {

                        }
                    });
                    return;
                }

                if (mApplyCommentEditText.getText().toString().equals("")) {
                    mActivity.showAlertDialog("첫 대화 메시지는 필수 입니다.", "확인", new RocateerActivity.DialogEventListener() {
                        @Override
                        public void onReceivedEvent() {

                        }
                    });
                    return;
                }

                if (isSelected || !(mApplyCommentEditText.getText().toString().equals(""))) {
                    recordApplyRegInAPI();
                    dismiss();
                }
                //
            }
        });

        AppCompatButton mDeclarationTextView = v.findViewById(R.id.apply_cancle_button);
        mDeclarationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("DECLARATION BUTTON -----------");
                dismiss();
            }
        });

        mApplyCommentEditText = (AppCompatEditText) v.findViewById(R.id.apply_comment_edit_text);
        mWithMyPetRecyclerView = (RecyclerView) v.findViewById(R.id.my_pet_recycler_view);

        mDialogPetListAdapter = new DialogPetListAdapter(R.layout.row_with_my_pet, mPetList);
        mDialogPetListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.pet_checkbox) {
                    mPetList.get(position).setSelect(!mPetList.get(position).isSelect());
                    mDialogPetListAdapter.notifyItemChanged(position, mPetList.get(position));

                }
            }
        });
        mWithMyPetRecyclerView.setLayoutManager(new

                LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mWithMyPetRecyclerView.setAdapter(mDialogPetListAdapter);

        myAnimalListAPI();

        return v;
    }


    /**
     * 지원하기
     */
    private void recordApplyRegInAPI() {
        WalkModel walkRequest = new WalkModel();
        walkRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
        walkRequest.setRecord_idx(mWalkModel.getRecord_idx());
        ArrayList<String> animalIdxs = new ArrayList<>();
        for (AnimalModel value : mPetList) {
            if (value.isSelect()) {
                animalIdxs.add(value.getAnimal_idx());
            }
        }
        walkRequest.setAnimal_idx(TextUtils.join(",", animalIdxs));
        walkRequest.setComment(mApplyCommentEditText.getText().toString());
        CommonRouter.api().record_apply_reg_in(Tools.getInstance().getMapper(walkRequest)).enqueue(new Callback<WalkModel>() {
            @Override
            public void onResponse(Call<WalkModel> call, Response<WalkModel> response) {
                if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
                    mApplyRefresh.onRefresh();


                }
            }

            @Override
            public void onFailure(Call<WalkModel> call, Throwable t) {

            }
        });
    }

    /**
     * 반려견 리스트
     */
    private void myAnimalListAPI() {
        AnimalModel animalModel = new AnimalModel();
        animalModel.setMember_idx(Prefs.getString(Constants.MEMBER_IDX, ""));
        CommonRouter.api().my_animal_list(Tools.getInstance().getMapper(animalModel)).enqueue(new Callback<AnimalModel>() {
            @Override
            public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
                if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
                    AnimalModel mAnimalResponse = response.body();
                    mPetList.addAll(mAnimalResponse.getData_array());
                    mDialogPetListAdapter.setNewData(mPetList);


                }
            }

            @Override
            public void onFailure(Call<AnimalModel> call, Throwable t) {

            }
        });

    }
}



