package rocateer.animalgo.dialog;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.activity.walk.DialogPetListAdapter;

import rocateer.animalgo.activity.walk.PartnerPetDialogAdapter;
import rocateer.animalgo.activity.walk.WithFriendDialyActivity;
import rocateer.animalgo.commons.Constants;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.BaseModel;
import rocateer.animalgo.models.WalkModel;
import rocateer.animalgo.models.api.CommonRouter;
import timber.log.Timber;


public class BottomDialogPetList extends BottomSheetDialogFragment {
  private RocateerActivity mActivity;
  private PartnerPetDialogAdapter mPartnerPetDialogAdapter;
  private String mRecordIdx;
  private String mMemberIdx;
  private AnimalModel mAnimalResponse = new AnimalModel();
  ArrayList<AnimalModel> mPetList;







  public BottomDialogPetList(RocateerActivity activity, ArrayList<AnimalModel> aniamlList, String recordIdx) {
    this.mActivity = activity;
    mPetList = aniamlList;
    this.mRecordIdx = recordIdx;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View v;
    v = inflater.inflate(R.layout.dialog_pet_list, container, false);

    AppCompatImageView mCloseButton = v.findViewById(R.id.close_button);
    mCloseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    RecyclerView mBottomPetListRecyclerView = v.findViewById(R.id.bottom_pet_list_recycler_view);
    mPartnerPetDialogAdapter = new PartnerPetDialogAdapter(R.layout.row_partner_pet_list, mPetList);
    mBottomPetListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    mBottomPetListRecyclerView.setAdapter(mPartnerPetDialogAdapter);
    mPetList.clear();
    mAnimalResponse.resetPage();
    partnerAnimalListAPI();
    return v;
  }

  /**
   *  상대 반려견 리스트 API
   */
  private void partnerAnimalListAPI() {
    AnimalModel animalRequest = new AnimalModel();
    animalRequest.setRecord_idx(mRecordIdx);
    animalRequest.setMember_idx(Prefs.getString(Constants.MEMBER_IDX,""));

    CommonRouter.api().partner_animal_list(Tools.getInstance().getMapper(animalRequest)).enqueue(new Callback<AnimalModel>() {
      @Override
      public void onResponse(Call<AnimalModel> call, Response<AnimalModel> response) {
        if (Tools.getInstance(mActivity).isSuccessResponse(response)) {
          AnimalModel mAnimalResponse = response.body();
          mPetList.addAll(mAnimalResponse.getData_array());
          mPartnerPetDialogAdapter.setNewData(mPetList);

        }
      }

      @Override
      public void onFailure(Call<AnimalModel> call, Throwable t) {

      }
    });
  }


}