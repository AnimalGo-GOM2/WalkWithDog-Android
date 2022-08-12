package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MemberModel extends BaseListModel {
  /// 회원 아이디
  private String member_id;
  /// 회원 비밀번호
  private String member_pw;
  /// 회원 비밀번호 확인
  private String member_pw_confirm;
  /// 회원 새로운 비밀번호 - 비멀번호 변경 시 사용
  private String new_member_pw;
  /// 회원 새로운 비밀번호 확인 - 비밀번호 변경 시 사용
  private String new_member_pw_check;
  /// SNS 회원가입시 타입 (C: 일반, K: 카카오톡, F: 페이스북, N: 네이버)
  private String member_join_type;
  /// 회원 이름
  private String member_name;
  /// 회원 닉네임
  private String member_nickname;
  /// 회원 생년월일
  private String member_birth;
  /// 회원 휴대폰 번호
  private String member_phone;
  /// 회원 성별: (0: 남성, 1: 여성, 2: 무관-사용 안함)
  private String member_gender;
  /// 회원 이미지
  private String member_img;
  /// 회원 알림 설정: (Y: 사용함, N: 사용안함)
  private String alarm_yn;
  // 회원 상태
  private String del_yn;
  /// 회원 탈퇴 사유: (0: 사용하지 않음, 1: 컨텐츠 부족, 2: 부적절한 컨텐츠, 3: 기타)
  private String member_leave_type;
  /// 회원탈퇴 사유 내용
  private String member_leave_reason;
  // 나이
  private String member_age;
  // 앱 바로가기 타이틀
  private String title;
  // url
  private String android_url;
  // 날짜
  private String ins_date;
  // 산책거리
  private String total_record_distant;
  // 가이드 알림 유무
  private String guide_alarm_yn;
  // 산책횟수
  private String total_record_cnt;
  // 산책준비
  private String review_0;
  // 디바이스 OS (I): IOS (A): 안드로이드
  private String divice_os;
  // 산책매너
  private String review_1;
  // 시간약속
  private String review_2;
  // 사교성
  private String review_3;
  // 타입 (0: 내가 받은 쪽지, 1: 내가 보낸 쪽지)
  private String type;
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
  // 차단 유형
  private String block_type;
  // 차단내용
  private String block_contents;
  // 신고 사진
  private String img_path;
  // 리스트
  private ArrayList<MemberModel> data_array;

}
