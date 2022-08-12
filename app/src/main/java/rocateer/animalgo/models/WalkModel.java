package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class WalkModel extends BaseListModel {
  // 산책 구분(0: 나와 반려견만, 1: 산책 친구와 함께)
  private String record_type;
  // 반려견 수
  private String member_animal_cnt;
  // 반려견 이름
  private String animal_name;
  // 반려견 키
  private String animal_idx;
  // 산책 횟수
  private String record_cnt;
  // 채팅 날짜
  private String st_date;
  // 채팅 시간
  private String ins_hi;
  // 산책 시간
  private String record_hour;
  // 산책 일기
  private String memo;
  // 산책 주소
  private String record_addr;
  // 채팅 내용
  private String comment;
  // 산책 날짜
  private String record_date;
  // 반려견 성별
  private String animal_gender;
  // 산책 시작 날짜
  private String record_start_date;
  // 산책 거리
  private String record_distant;
  // 날짜 별 채팅 리스트
  private ArrayList<WalkModel> day_list_array;
  // 반려견 사진
  private String animal_img_path;
  // 반랴견 나아
  private String animal_year;
  // 새로운 채팅
  private String new_chat_yn;
  // 반려견 수
  private String animal_cnt;
  // 가이드 사진
  private String guide_img;
  // 위도
  private String record_lat;
  // 경도
  private String record_lng;
  // 나의 반려견 수
  private String my_animal_cnt;
  // 산책 지원 상태
  private String state;
  // 산책친구 반려견 수
  private String partner_animal_cnt;
  // 산책 등록자 키
  private String record_owner_idx;
  // 산책친구 조건 :: 보호자 성별 (0:남성,1:여성, 상관없음: 공백)
  private String guardian_gender;
  // 산책친구 조건 :: 보호자 나이대(20: 20대, 30: 30대, 40: 40대, 50: 50대 이상, 상관없음: 공백)
  private String guardian_age;
  // 유저 닉네임
  private String member_nickname;
  // 유저 나이
  private String member_age;
  // 유저 성병
  private String member_gender;
  // 유저 이미지
  private String member_img;
  // 산책 키
  private String record_idx;
  // 내 반려견 키
  private String member_animal_idxs;
  // 산책친구 지원 거부
  private String record_participant_idx;
  // 산책기록 키
  private String record_diary_idx;
  // 주행기록 좌표 (lat:"위도",lng:"경도")
  private String location;
  // 채팅방 키
  private String chatting_room_idx;
  // 견종 이름
  private String category_name;
  // 반려견 성격
  private String animal_character;
  // 중성화 여부
  private String animal_neuter;
  // 훈련
  private String animal_training;
  // 산책준비
  private String review_0;
  // 산책매너
  private String review_1;
  // 시간약속
  private String review_2;
  // 사교성
  private String review_3;
  // 지원여부
  private String apply_yn;
  // 회원키
  private String partner_member_idx;
  // 채팅 버튼 활성화 여부
  private String chat_active_yn;
  // 산책일기 사진
  private String record_img_paths;
  private String record_img_path;
  // 대화 갯수
  private String new_chat_cnt;
  // 산책 경로
  private String coordinates;
  // 산책 반려견 리스트
  private ArrayList<WalkModel> record_animal_array;
  // 나의 반려견 리스트
  private ArrayList<WalkModel> my_animal_array;
  // 산책친구 반려견 리스트
  private ArrayList<WalkModel> partner_animal_array;
  // 나의 반려견 산책 리스트
  private ArrayList<WalkModel> member_animal_array;
  // QNA 리스트
  private ArrayList<WalkModel> data_array;
  // 반려견 리스트
  private ArrayList<WalkModel> animal_array;
}
