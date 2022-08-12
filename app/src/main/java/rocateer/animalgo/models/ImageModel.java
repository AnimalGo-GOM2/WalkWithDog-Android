package rocateer.animalgo.models;

import android.graphics.drawable.Drawable;

import java.io.File;

import lombok.Data;

@Data
public class ImageModel extends BaseModel {

  private Drawable image;
  private String img_path;
  private File file;
  private String link_url;

  private boolean isFirst;

  public ImageModel(Drawable image) {
    this.image = image;
  }

  public ImageModel() {
    super();
  }

  public ImageModel(File file) {
    this.file = file;
  }

  public ImageModel(String imagePath) {
    this.img_path = imagePath;
  }

  public ImageModel(String img_path, String link_url) {
    this.img_path = img_path;
    this.link_url = link_url;
  }
}
