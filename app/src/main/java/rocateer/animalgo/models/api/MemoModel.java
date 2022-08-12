package rocateer.animalgo.models.api;

import java.util.ArrayList;

import lombok.Data;
import rocateer.animalgo.models.BaseListModel;
@Data
public class MemoModel extends BaseListModel {
  /// 회원 이름
  private String member_name;
  /// 회원 닉네임
  private String member_nickname;
  /// 회원 성별: (0: 남성, 1: 여성, 2: 무관-사용 안함)
  private String member_gender;
  /// 회원 이미지
  private String member_img;
  // 나이
  private String member_age;
  // 날짜
  private String ins_date;
  // 쪽지 내용
  private String contents;
  // 읽지 않은 쪽지
  private String unread_cnt;
  // 검색
  private String search;
  // 쪽지를 주고받은 날짜
  private String memo_date;
  // 회원키(받는 사람)
  private String partner_member_idx;
  // 메모 키
  private String memo_idx;
  // 신고유형(0:부적절한대화,1:불친절한매너,2:기타)
  private String report_type;
  //신고내용 :: * report_type 2번일 때 필수입력
  private String report_contents;
  // 신고 사진
  private String img_path;
  // 리스트
  private ArrayList<MemoModel> data_array;
  // 읽음
  private String read_yn;
  // 타입 (0: 내가 받은 쪽지, 1: 내가 보낸 쪽지)
  private String type;
}
