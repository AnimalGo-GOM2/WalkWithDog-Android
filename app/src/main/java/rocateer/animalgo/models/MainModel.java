package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MainModel extends BaseListModel{

  // 레코드 IDX
  private String record_idx;
  // 닉네임
  private String member_nickname;
  // 유저 이미지
  private String member_img;
  // 유저 나이
  private String member_age;
  // 유저 성병
  private String member_gender;
  // 유저 위도
  private String member_lat;
  // 유저 경도
  private String member_lng;
  // 산책 일시
  private String record_date;
  // 산책 장소
  private String record_addr;
  // 산책장소 위도
  private String record_lat;
  // 산책장소 경도
  private String record_lng;
  // 산책장소까지의 거리
  private String distant;
  // 반려견 키(마릿수)
  private String member_animal_idxs;
  // 반려견 키
  private String animal_idx;
  // 반려견 이름
  private String animal_name;
  // 반려견 이미지
  private String animal_img_path;
  // 반려견 성별
  private String animal_gender;
  // 반려견 나이
  private String animal_year;
  // 반려견 성격
  private String animal_character;
  // 반려견 중성화
  private String animal_neuter;
  // 반려견 훈련
  private String animal_training;
  // 리뷰 평점
  private String review_star;
  // 배너 갯수
  private String banner_cnt;
  // 배너 키
  private String banner_idx;
  // 배너 이미지
  private String img_path;
  // 이동경로
  private String target;
  // 산책왕 시작 날짜
  private String record_king_start_date;
  // 신첵왕 종료 날짜
  private String record_king_end_date;
  // 총 산책 횟수
  private String record_cnt;
  // 오늘의 추천 산책
  private MainModel recommend_obj;
  // 오늘의 추천 산책 반려견
  private MainModel animal_obj;
  // 반려견 카테고리 이름
  private String category_name;
  // 회사 정보
  private String company_info;
  // 배너 링크
  private String link_url;
  // 애니멀고 산책왕 리스트
  private ArrayList<MainModel> rank_array;
  // 칭찬해요 리스트
  private ArrayList<MainModel> compliment_array;
  // 배너 리스트
  private ArrayList<MainModel> banner_array;
  // 내 반려견 리스트
  private ArrayList<MainModel> my_animal_array;

}
