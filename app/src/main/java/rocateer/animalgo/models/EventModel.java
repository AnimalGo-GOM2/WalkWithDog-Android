package rocateer.animalgo.models;

import lombok.Data;

@Data
public class EventModel extends BaseListModel {
  /// 이벤트 인덱스
  private String event_idx;
  /// 이벤트 제목
  private String title;
  /// 이벤트 내용
  private String contents;
  /// 이벤트 이미지 URL
  private String img_url;
  /// 이벤트 이미지 가로 길이
  private String img_width;
  /// 이벤트 이미지 세로 길이
  private String img_height;
  /// 이벤트 링크
  private String link_url;

}
