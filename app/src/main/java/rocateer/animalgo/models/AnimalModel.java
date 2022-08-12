package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class AnimalModel extends BaseListModel{
  // 반려견 이름
  private String animal_name;
  // 견분류 키(9: 대형견, 12: 중형견, 15: 소형견)
  private String first_category_idx;
  // 견종 키
  private String second_category_idx;
  // 견분류 이름
  private String first_category_name;
  // 견종 이름
  private String second_category_name;
  // 성별(0:남아,1:여아)
  private String animal_gender;
  // 중성화여부(Y:함, N:안함)
  private String animal_neuter;
  // 생년원 구분자 -
  private String animal_birth;
  // 반려견 나이
  private String animal_year;
  // 훈련(Y:훈련,N:안함)
  private String animal_training;
  // 성격(0:온순, 1:입질, 2:호기심많음,3:활동적)
  private String animal_character;
  // 건강상태(0:좋아요,1:보통이에요,2:주의가 필요해요)
  private String animal_health;
  // 견 분류
  private String first_category;
  // 견 종
  private String second_category;
  // 반려견 사진
  private String animal_img_path;
  // 반려견 키
  private String animal_idx;
  // 산책횟수
  private String record_cnt;
  // 카테고리 키
  private String category_management_idx;
  // 반려견 리스트
  private ArrayList<WalkModel> my_animal_array;
  // 산첵 키
  private String record_idx;
  // 카테고리 명
  private String category_name;
  //부모 카테고리 키( 견분류 키 :: animal_v_1_0_0/animal_list_type 에서 반환되는 category_management_idx)
  private String parent_category_management_idx;
  // 날짜
  private String ins_date;
  // 반려견 리스트
  private ArrayList<AnimalModel> data_array;
}
