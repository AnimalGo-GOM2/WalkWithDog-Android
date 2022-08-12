package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class FaqModel extends BaseListModel {

  /// FAQ 카테고리 키
  private String faq_category_idx;
  /// FAQ 카테고리 제목
  private String faq_category_name;
  /// FAQ 인덱스
  private String faq_idx;
  // FAQ 타입
  private String faq_type;
  /// FAQ 제목
  private String title;
  /// FAQ 이미지
  private String img;
  /// FAQ 내용
  private String contents;
  /// FAQ 리스트 또는 FAQ 카테고리 리스트
  ArrayList<FaqModel> data_array;

}
