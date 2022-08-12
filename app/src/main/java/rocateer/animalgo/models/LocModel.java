package rocateer.animalgo.models;

import lombok.Builder;
import lombok.Data;

@Data
public class LocModel {
  private String lat;
  private String lng;
//  private String s;
//  private String t;

  public LocModel(String lat, String lng) {
    this.lat = lat;
    this.lng = lng;
  }

  @Builder
  public LocModel(String lat, String lng, String s, String t) {
    this.lat = lat;
    this.lng = lng;
  }
}
