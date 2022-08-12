package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class QnaModel extends BaseListModel {
  /// QNA 인덱스
  private String qa_idx;
  /// QNA 질문 제목
  private String qa_title;
  /// QNA 질문 내용
  private String qa_contents;
  /// QNA 답변 유무: (Y: 답변 있음, N: 답변 없음)
  private String reply_yn;
  /// QNA 답변 내용
  private String reply_contents;
  /// QNA 답변일
  private String reply_date;
  /// QNA 등록일
  private String ins_date;
  // 분류(0:문의, 1:의견)
  private String qa_type;
  // 구분(문의 :: 0:회원가입, 1:앱 사용, 2:산책등록, 3:산책기록, 4:기타)(의견 :: 10:지금도좋아요, 11:새로운기능이필요해요, 12:개선이필요해요)
  private String qa_category;
  /// QNA 리스트
  private ArrayList<QnaModel> data_array;

}
