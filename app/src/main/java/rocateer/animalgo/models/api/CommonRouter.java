package rocateer.animalgo.models.api;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rocateer.animalgo.models.AlarmModel;
import rocateer.animalgo.models.AnimalModel;
import rocateer.animalgo.models.FaqModel;
import rocateer.animalgo.models.FileModel;
import rocateer.animalgo.models.MainModel;
import rocateer.animalgo.models.MemberModel;
import rocateer.animalgo.models.NoticeModel;
import rocateer.animalgo.models.QnaModel;
import rocateer.animalgo.models.WalkModel;

public class CommonRouter extends BaseRouter {

  public static CommonAPI api() {
    return (CommonAPI) retrofit(CommonAPI.class);
  }

  public interface CommonAPI {
    /**
     * 회원 가입
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("join_v_1_0_0/member_reg_in")
    Call<MemberModel> member_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 본인인증
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("nice_web_view/member_auth")
    Call<MemberModel> member_auth(@FieldMap Map<String, Object> map);


    /**
     * 앱 시작 팝업
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("start_popup_v_1_0_0/start_popup_detail")
    Call<MemberModel> start_popup_detail(@FieldMap Map<String, Object> map);

    /**
     * 회원 로그인
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("login_v_1_0_0/member_login")
    Call<MemberModel> member_login(@FieldMap Map<String, Object> map);

    /**
     * 산책수락
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_start")
    Call<WalkModel> record_start(@FieldMap Map<String, Object> map);

    /**
     * 내 정보 상세 보기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_state_v_1_0_0/member_state_detail")
    Call<MemberModel> member_state_detail(@FieldMap Map<String, Object> map);

    /**
     * 회원 상태
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_info_v_1_0_0/member_info_detail")
    Call<MemberModel> member_info_detail(@FieldMap Map<String, Object> map);

    /**
     * 내 정보 수정
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_info_v_1_0_0/member_info_mod_up")
    Call<MemberModel> member_info_mod_up(@FieldMap Map<String, Object> map);

    /**
     * 비밃번호 변경
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_pw_change_v_1_0_0/member_pw_mod_view")
    Call<MemberModel> member_pw_mod_view(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 지원
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_apply_reg_in")
    Call<WalkModel> record_apply_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 지원 거부
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_apply_refuse")
    Call<WalkModel> record_apply_refuse(@FieldMap Map<String, Object> map);

    /**
     * 쪽지 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/memo_detail")
    Call<MemoModel> memo_detail(@FieldMap Map<String, Object> map);

    /**
     * 쪽지 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/memo_list")
    Call<MemoModel> memo_list(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 지원 취소
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_apply_cancel")
    Call<WalkModel> record_apply_cancel(@FieldMap Map<String, Object> map);


    /**
     * 쪽지 모두 읽기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/read_all")
    Call<MemoModel> read_all(@FieldMap Map<String, Object> map);

    /**
     * 회원리스트(검색)
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/member_list")
    Call<MemoModel> member_list(@FieldMap Map<String, Object> map);

    /**
     * 쪽지 보내기 폼
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/memo_reg_in")
    Call<MemoModel> memo_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 쪽지 보내기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("memo_v_1_0_0/memo_reg_view")
    Call<MemoModel> memo_reg_view(@FieldMap Map<String, Object> map);

    /**
     * 비밃번호 변경 폼
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_pw_change_v_1_0_0/member_pw_mod_up")
    Call<MemberModel> member_pw_mod_up(@FieldMap Map<String, Object> map);

    /**
     * 로그아웃
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("logout_v_1_0_0/member_logout")
    Call<MemberModel> member_logout(@FieldMap Map<String, Object> map);

    /**
     * 아이디 찾기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("find_id_v_1_0_0/member_id_find")
    Call<MemberModel> member_id_find(@FieldMap Map<String, Object> map);

    /**
     * 비밀번호 찾기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("find_pw_to_email_v_1_0_0/member_pw_reset_send_email")
    Call<MemberModel> member_pw_reset_send_email(@FieldMap Map<String, Object> map);


    /**
     * 홈
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("main_v_1_0_0/main")
    Call<MainModel> main(@FieldMap Map<String, Object> map);

    /**
     * 반려견 등록
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/animal_reg_in")
    Call<AnimalModel> animal_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 반려견 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/my_animal_list")
    Call<AnimalModel> my_animal_list(@FieldMap Map<String, Object> map);

    /**
     * 상대방 반려견 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/partner_animal_list")
    Call<AnimalModel> partner_animal_list(@FieldMap Map<String, Object> map);

    /**
     * 트래킹정보 저장
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/tracking_reg_in")
    Call<WalkModel> tracking_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 산책일기 저장
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/diary_reg_in")
    Call<WalkModel> diary_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 산책 가이드
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_guide")
    Call<WalkModel> record_guide(@FieldMap Map<String, Object> map);

    /**
     * 산책 등록
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_reg_in")
    Call<WalkModel> record_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 산책일기 등록 폼
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/diary_reg_view")
    Call<WalkModel> diary_reg_view(@FieldMap Map<String, Object> map);

    /**
     * 신고하기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_report_v_1_0_0/report_reg_in")
    Call<MemberModel> report_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 차단하기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_block_v_1_0_0/block_reg_in")
    Call<MemberModel> block_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 차단 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_block_v_1_0_0/block_list")
    Call<MemberModel> block_list(@FieldMap Map<String, Object> map);

    /**
     * 견 분류 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/animal_list_type")
    Call<AnimalModel> animal_list_type(@FieldMap Map<String, Object> map);

    /**
     * 견종 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/animal_list_kind")
    Call<AnimalModel> animal_list_kind(@FieldMap Map<String, Object> map);

    /**
     * 지원한 산책친구들
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_apply_member_list")
    Call<WalkModel> record_apply_member_list(@FieldMap Map<String, Object> map);

    /**
     * 산책친구와 함께
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_together_list")
    Call<WalkModel> record_together_list(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 등록 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/registered_record_detail")
    Call<WalkModel> registered_record_detail(@FieldMap Map<String, Object> map);

    /**
     * 채팅 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("chatting_v_1_0_0/chatting_list")
    Call<WalkModel> chatting_list(@FieldMap Map<String, Object> map);

    /**
     * 채팅 입력
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("chatting_v_1_0_0/chatting_reg_in")
    Call<WalkModel> chatting_reg_in(@FieldMap Map<String, Object> map);

    /**
     * 반려견 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/registered_record_list")
    Call<WalkModel> registered_record_list(@FieldMap Map<String, Object> map);

    /**
     * 반려견 수정
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/animal_mod_up")
    Call<AnimalModel> animal_mod_up(@FieldMap Map<String, Object> map);


    /**
     * 산책친구 등록 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("animal_v_1_0_0/animal_detail")
    Call<AnimalModel> animal_detail(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 지원 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_apply_list")
    Call<WalkModel> record_apply_list(@FieldMap Map<String, Object> map);

    /**
     * 안읽은 알림 수
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("alarm_v_1_0_0/new_alarm_count")
    Call<AlarmModel> new_alarm_count(@FieldMap Map<String, Object> map);

    /**
     * 산책친구 등록 취소
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/registered_record_cancel")
    Call<WalkModel> registered_record_cancel(@FieldMap Map<String, Object> map);

    /**
     * 채팅방 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("chatting_v_1_0_0/chatting_room_detail")
    Call<WalkModel> chatting_room_detail(@FieldMap Map<String, Object> map);

    /**
     * 공지사항 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("notice_v_1_0_0/notice_list")
    Call<NoticeModel> notice_list(@FieldMap Map<String, Object> map);

    /**
     * FAQ 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("faq_v_1_0_0/faq_list")
    Call<FaqModel> faq_list(@FieldMap Map<String, Object> map);

    /**
     * QNA 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("qa_v_1_0_0/qa_list")
    Call<QnaModel> qa_list(@FieldMap Map<String, Object> map);

    /**
     * QNA 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("qa_v_1_0_0/qa_detail")
    Call<QnaModel> qa_detail(@FieldMap Map<String, Object> map);



    /**
     * 회원탈퇴
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("member_out_v_1_0_0/member_out_up")
    Call<MemberModel> member_out_up(@FieldMap Map<String, Object> map);

    /**
     * 산책 기록
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_diary_list")
    Call<WalkModel> record_diary_list(@FieldMap Map<String, Object> map);

    /**
     * 산책 기록 상세
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("record_v_1_0_0/record_diary_detail")
    Call<WalkModel> record_diary_detail(@FieldMap Map<String, Object> map);

    /**
     * 더보기 화면
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("mypage_v_1_0_0/mypage_detail")
    Call<MemberModel> mypage_detail(@FieldMap Map<String, Object> map);

    /**
     * 앱 링크 바로가기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("mypage_v_1_0_0/link_list")
    Call<MemberModel> link_list(@FieldMap Map<String, Object> map);

    /**
     * 알림 리스트
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("alarm_v_1_0_0/alarm_list")
    Call<AlarmModel> alarm_list(@FieldMap Map<String, Object> map);

    /**
     * 알림 설정
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("alarm_v_1_0_0/alarm_toggle")
    Call<AlarmModel> alarm_toggle(@FieldMap Map<String, Object> map);

    /**
     * 알림 설정 보기
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("alarm_v_1_0_0/alarm_toggle_view")
    Call<AlarmModel> alarm_toggle_view(@FieldMap Map<String, Object> map);

    /**
     * QNA 등록
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("qa_v_1_0_0/qa_reg_in")
    Call<QnaModel> qa_reg_in(@FieldMap Map<String, Object> map);

    /**
     * QNA 삭제
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("qa_v_1_0_0/qa_del")
    Call<QnaModel> qa_del(@FieldMap Map<String, Object> map);

    /**
     * QNA 삭제
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("company_info_v_1_0_0/company_info")
    Call<MainModel> company_info(@FieldMap Map<String, Object> map);


    /**
     * 파일 업로드
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("common/fileUpload_action")
    Call<FileModel> fileUpload_action(@Part MultipartBody.Part file);

  }


}