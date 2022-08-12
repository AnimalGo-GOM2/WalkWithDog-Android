package rocateer.animalgo.commons;

import java.util.Date;

public class Constants {
  // 카메라
  public static int CAPTURE_IMAGE = 1000;

  // Preference
  public static String TUTORIAL = "tutorial";
  public static String FCM_TOKEN = "fcm_token";
  public static String MEMBER_IDX = "member_idx";
  public static String MEMBER_NAME = "member_name";
  public static String MEMBER_ID = "member_id";
  public static String MEMBER_PW = "member_pw";
  public static String MEMBER_NICKNAME = "member_nickname";
   public static String PARTNER_MEMBER_IDX = "partner_member_idx";
  public static String AUTH_WEB = "auth_web";
  public static String YOUTUBE_API_KEY = "AIzaSyC17RGfHutd5QorqZTgUqSgsA4YniEo1fQ";
  public static String MEMBER_REGION = "member_region";
  public static String MEMBER_LAT = "member_lat";
  public static String MEMBER_LNG = "member_lng";
  public static String RECORD_IDX = "record_idx";
  public static String GUIDE_ALARM_YN = "guide_alarm_yn";
  public static String DATA = "data";
  public static String START_POPUP = "start_popup";
  public static String TOTAL_LOCATION_LIST = "location_lost";
  //  public static Double TOTAL_DISTANCE = Double.valueOf("totalDistance");
  public static String REOCRD_IDX = "record_idx";
  public static String RECORD_TYPE = "record_type";
  public static String MEMBER_ANIMAL_IDXS = "member_animal_idxs";
  public static String START_DATE = "start_date";
  //  public static int RECORDIN_TIME = Integer.parseInt("recording_time");
  public static String RECORDING_ADDRESS = "recording_aFddress";


  // Boardcast
  public static String WALK_REFRESH = "walk_refresh"; // 산책하기 리프레시
  public static String HiSTORY_REFRESH = "history_refresh"; // 산책기록 리프레시
  public static String WITH_FRIEND_HISTORY_REFRESH = "with_friend_history_refresh"; // 산책친구와 함께 기록 리프래시
  public static String MEMO_REFRESH = "memo_refresh";
  public static String WITH_FRIEND_REFRESH = "with_friend_refresh";
  public static String RECORDING_STATUS = "recording_status";
  public static String CHAT_REFRESH = "chat_refresh";
  public static String AlARM_REFRESH = "alarm_refresh";
  public static String ACCEPT_REFRESH = "accept_refresh";
  public static String MAIN_REFRESH = "main_refresh";
  public static int CHAT_NOTIFICATION = 501;


  public static String RECORDING_STATUS_UI = "recording_status_ui";
  public static String RECORDING = "recording";

  // 산책 트래킹 알림 (나와 반려견만 산책)
  public static String[] TRACKING_PUSH_TEXT = {"산책 시에는 \"애니멀고 산책\"으로 추억을 계속 남겨주세요.",
      "반려견과 사진도 남겨 좋은 추억을 만들어 보세요.", "산책친구 완료 시 기록 잊지 말고 남겨주세요.",
      "배변활동시 배변봉투로 뒤처리 꼭 잊지 마세요!", "산책 후 발바닥을 깨끗이 닦아주세요!", "잠깐의 휴식 시간을 가져 충분한 휴식을 취해주세요.",
      "산책 시 주변 반려견과 인사해보는 건 어떨까요?", "다음 산책 시에는 산책친구와 같이 해보세요!", "산책 시 마실 물과 간식은 챙기셨나요?", "기본적인 산책 에티켓을 지켜주세요."};

  // 산책 트래킹 알림 (산책친구와 함께)
  public static String[] TRACKING_PUSH_TEXT_WITH_FRIEND = {"반려견들은 서로 잘 어울려서 놀고 있나요?", "서로 사진도 남겨 좋은 추억을 만들어 보세요.",
      "산책친구 완료 시 기록 잊지말고 남겨주세요.", "다음 산책 약속도 잡아보세요.", "산책 후 발바닥을 깨끗이 닦아주세요!", "잠깐의 휴식시간을 가져보는 건 어떠신가요?",
      "자주가는 산책코스도 서로 공유해보세요.", "상대 견주분과 유익한 정보들을 나눠보세요.", "우리 아이의 일상 사진첩을 서로 공유해보세요.", "산책이 끝나고 서로 일상얘기도 나눠보세요."};
}
