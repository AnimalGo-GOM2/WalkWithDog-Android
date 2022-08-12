package rocateer.animalgo.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class AlarmModel extends BaseListModel {
  /// 알림 인덱스
  private String alarm_idx;
  /// 알림 제목
  private String msg;
  /// 알림 데이터: JSON 형식 > 파싱해서 써야함.
  private AlarmModel data;
  /// 읽음 유무: (Y: 읽음, N: 읽지 않음)
  private String read_yn;
  /// 삭제 유무: (Y: 삭제, N: 정상)
  private String del_yn;
  /// 알림 등록일
  private String ins_date;
  // 타이틀
  private String title;
  /// 알림 수정일
  private String upd_date;
  /// 모든 푸시 알림 상태: (Y: 수신, N: 수신거부)
  private String all_alarm_yn;
  /// 이벤트 푸시 알림 상태: (Y: 수신, N: 수신거부)
  private String event_alarm_yn;
  /// 공지사항 푸시 알림 상태: (Y: 수신, N: 수신거부)
  private String notice_alarm_yn;
  /// 이메일 푸시 알림 상태: (Y: 수신, N: 수신거부)
  private String email_alarm_yn;
  // 산책친구 대화 알림
  private String  chatting_alarm_yn;
  // 산책중 가이드 메세지 알림
  private String guide_alarm_yn;
  // 마케팅 동의 여부
  private String marketing_agree_yn;
  /// 새로운 알림 카운트
  private String new_alarm_conut;
  // 새로운 편지 카운트
  private String new_memo_conut;
  // 설정 타입(0: 선택친구 대화 알림, 1: 산책중 가이드 알림, 2: 마케팅 동의여부)
  private String type;
  // 설정 값(Y: 설정, N: 미설정)
  private String value;
  private String index;
  // 채팅 방 idx
  private String chatting_room_idx;
  /// 알림 리스트
  private ArrayList<AlarmModel> data_array;
}
