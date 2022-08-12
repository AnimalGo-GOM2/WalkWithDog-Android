package rocateer.animalgo.models.naver;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class NaverRouter extends NaverAPIClient {

  public static NaverAPI api() {
    return (NaverAPI) retrofit(NaverAPI.class);
  }

  public interface NaverAPI {
    @GET("map-reversegeocode/v2/gc")
    Call<NaverModel> map_geo_code(
        @Header("X-NCP-APIGW-API-KEY-ID") String id,
        @Header("X-NCP-APIGW-API-KEY") String key,
        @Query("request") String request,
        @Query("coords") String coords,
        @Query("sourcecrs") String sourcecrs,
        @Query("output") String output,
        @Query("orders") String orders);
  }
}
