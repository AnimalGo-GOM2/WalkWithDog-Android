package rocateer.animalgo.models;

import lombok.Data;

@Data
public class BaseModel {

  private String code;
  private String code_msg;
  private String member_idx;
  private String device_os;
  private String gcm_key;

  // TODO: 임시
  private boolean isSelect = false;

  public BaseModel() {
  }

  public BaseModel(String code, String code_msg, String member_idx, String device_os, String gcm_key) {
    this.code = code;
    this.code_msg = code_msg;
    this.member_idx = member_idx;
    this.device_os = device_os;
    this.gcm_key = gcm_key;
  }
}
