package rocateer.animalgo.models;

import lombok.Data;

@Data
public class BannerModel extends BaseListModel {
  /// 배너 인덱스
  private String banner_idx;
  /// 배너 제목
  private String title;
  /// 배너 이미지 URL
  private String img_url;
  /// 배너 링크
  private String link_url;

}
