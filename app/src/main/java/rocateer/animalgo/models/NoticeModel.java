package rocateer.animalgo.models;


import java.util.ArrayList;

import lombok.Data;

@Data
public class NoticeModel extends BaseListModel {
  /// 공지사항 인덱스
  private String notice_idx;
  /// 공지사항 제목
  private String title;
  /// 공지사항 이미지
  private String img_path;
  /// 이미지 가로 길이 (px)
  private String img_width;
  /// 이미지 세로 길이 (px)
  private String img_height;
  /// 공지사항 내용
  private String contents;
  /// 공지사항 등록일
  private String ins_date;
  /// 공지사항 리스트
  private ArrayList<NoticeModel> data_array;

}
