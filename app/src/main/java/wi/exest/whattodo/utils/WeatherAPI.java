package wi.exest.whattodo.utils;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherAPI {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "ВАШ_API_КЛЮЧ";
    private FusedLocationProviderClient fusedLocationClient;

    public interface WeatherCallback {
        void onWeatherReceived(String weatherCondition);
        void onError(String message);
    }

    public void getCurrentWeather(Context context, WeatherCallback callback) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            fetchWeatherData(location, callback);
                        } else {
                            callback.onError("Не удалось получить местоположение");
                        }
                    });
        } catch (SecurityException e) {
            callback.onError("Необходимо разрешение на доступ к местоположению");
        }
    }

    private void fetchWeatherData(Location location, WeatherCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather(
                location.getLatitude(),
                location.getLongitude(),
                API_KEY,
                "ru",
                "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onWeatherReceived(response.body().weather.get(0).main);
                } else {
                    callback.onError("Ошибка получения данных о погоде");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }

    // Интерфейс для Retrofit
    public interface WeatherService {
        @GET("data/2.5/weather")
        Call<WeatherResponse> getCurrentWeather(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("appid") String apiKey,
                @Query("lang") String lang,
                @Query("units") String units);
    }

    // Модели для парсинга ответа
    public static class WeatherResponse {
        List<Weather> weather;
    }

    public static class Weather {
        String main;
    }
}
