package jsonResponseData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("weather")
    Observable<CurrentWeather> getCurrentWeatherData(
            @Query("appid") String apiKey,
            @Query("lat") Double lat,
            @Query("lon")Double lon);
}
