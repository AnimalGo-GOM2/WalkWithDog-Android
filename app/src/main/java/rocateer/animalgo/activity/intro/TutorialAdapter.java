package rocateer.animalgo.activity.intro;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import rocateer.animalgo.R;

public class TutorialAdapter extends PagerAdapter {


  public ArrayList<Drawable> mImageList;
  public Context mContext;


  public TutorialAdapter(ArrayList<Drawable> imageList, Context context) {
    mImageList = imageList;
    mContext = context;
  }

  @Override
  public int getCount() {
    return mImageList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view.equals(object);
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//    super.destroyItem(container, position, object);
    container.removeView((View)object);
  }

  public Object instantiateItem(ViewGroup container, int position) {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View view = inflater.inflate(R.layout.row_tutorial, container, false);

    AppCompatImageView tutorialImageView = view.findViewById(R.id.tutorial_image_view);

    RequestOptions requestOptions = new RequestOptions();
    requestOptions.centerCrop();
    Glide.with(mContext)
        .load(mImageList.get(position))
        .apply(requestOptions)
        .into(tutorialImageView);

    container.addView(view);
    return view;
  }
}
