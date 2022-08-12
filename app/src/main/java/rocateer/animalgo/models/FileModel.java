package rocateer.animalgo.models;

import lombok.Data;

@Data
public class FileModel extends BaseModel {
  // 파일 경로
  private String file_path;
  // 파일 원본 이름
  private String orig_name;
  // 파일 가로 사이즈
  private String img_width;
  // 파일 세로 사이즈
  private String img_height;
}
