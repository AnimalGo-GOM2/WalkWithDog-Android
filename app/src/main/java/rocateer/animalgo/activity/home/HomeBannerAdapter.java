package rocateer.animalgo.activity.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.islamkhsh.CardSliderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import rocateer.animalgo.R;
import rocateer.animalgo.activity.RocateerActivity;
import rocateer.animalgo.commons.Tools;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.api.BaseRouter;

public class HomeBannerAdapter extends CardSliderAdapter<HomeBannerAdapter.BannerHolder> {

  private RocateerActivity mActivity;
  private ArrayList<MainModel> mImageList;

  public HomeBannerAdapter(RocateerActivity context, ArrayList<MainModel> imageList) {
    this.mActivity = context;
    this.mImageList = imageList;
  }

  @Override
  public int getItemCount() {
    return mImageList.size();
  }

  @Override
  public HomeBannerAdapter.BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_banner, parent, false);

    return new HomeBannerAdapter.BannerHolder(view);
  }

  @Override
  public void bindVH(@NotNull HomeBannerAdapter.BannerHolder bannerHolder, int i) {

    RequestOptions requestOptions = new RequestOptions();
    requestOptions.centerCrop();

    Glide.with(mActivity)
        .load(BaseRouter.BASE_URL + mImageList.get(i).getImg_path())
        .into(bannerHolder.bannerImageView);

    bannerHolder.bannerImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mImageList.get(i).getLink_url() != null) {
          Tools.getInstance(mActivity).openBrowser(mImageList.get(i).getLink_url());
        }
      }
    });
  }

  /**
   * 리스트 갱신
   */
  public void setNewData(ArrayList<MainModel> imageList) {
    this.mImageList = imageList;
    this.notifyDataSetChanged();
  }

  class BannerHolder extends RecyclerView.ViewHolder {
    AppCompatImageView bannerImageView;
    AppCompatButton imageButton;

    public BannerHolder(View view) {
      super(view);
      bannerImageView = view.findViewById(R.id.banner_image_view);

    }
  }
}
