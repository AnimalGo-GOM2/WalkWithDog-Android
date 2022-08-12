package rocateer.animalgo.commons;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MyPetRecyclerView_Width extends RecyclerView.ItemDecoration {

  private final int divWidth;
  private final int listSize;

  public MyPetRecyclerView_Width(int listSize, int divWidth) {
    this.listSize = listSize;
    this.divWidth = divWidth;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    int position = parent.getChildAdapterPosition(view);

    if ((listSize - 1) == position) {
      outRect.right = 0;
    } else {
      outRect.right = divWidth;
    }


  }
}
