package rocateer.animalgo.models.naver;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import lombok.Data;

@Data
public class NaverModel {
  //상태
  private NaverModel status;
  //전체 JSON
  private ArrayList<NaverModel> results;
  // 시/구/군 까지 Json
  private NaverModel region;
  // 시
  private NaverModel area1;
  // 구/군
  private NaverModel area2;
  // 동
  private NaverModel area3;

  //상세주소
  private NaverModel land;
  //번지
  private String number1;
  //이름
  private String name;
}
