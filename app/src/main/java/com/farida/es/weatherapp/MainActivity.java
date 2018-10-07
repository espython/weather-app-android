package com.farida.es.weatherapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jsonResponseData.ApiKeys;
import jsonResponseData.ApiService;
import jsonResponseData.CurrentWeather;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    /* Variables Declarations */
    RelativeLayout mainViewLayout;
    CurrentWeather currentWeather;
    private TextView apiResponseCodeView;
    ApiKeys apiKeys;
    String weatherApiKey = "";
    String currentWeatherURL = "https://api.openweathermap.org/data/2.5/";

    int currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*  If the Android version is lower than Jellybean, use this call to hide
            the status bar. */
        /* Make Status Bar transparent */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);
        /* Starting Set the background View */
        mainViewLayout = findViewById(R.id.main_id);
        Drawable backGround = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.clear_sky_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainViewLayout.setBackground(backGround);
        }
        /* Ending of set the background view */

        /* GET Data from the weather API */
        apiKeys = new ApiKeys();
        String weatherApiKey = apiKeys.getWeatherApiKey();
        System.out.println("My key is : " + weatherApiKey);
        currentWeather = new CurrentWeather();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(currentWeatherURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // create an instance of the ApiService
        ApiService apiService = retrofit.create(ApiService.class);
        // make a request by calling the corresponding method
        Observable<CurrentWeather> currentWeatherRes = apiService.getCurrentWeatherData(weatherApiKey, 35.0, 139.0);
        currentWeatherRes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CurrentWeather>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CurrentWeather currentWeather) {
                        System.out.println("API Response Code: " + currentWeather.getCod());
                        String cityName = currentWeather.getName();
                        showCityName(cityName, currentTime);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Mission completed successfully");
                    }
                });

    }

    /*
     * Show code response to the screen
     * */
    private void showCityName(String city , int currentTime) {
        /*
        * customize the city name position */
        TextView cityNameView = findViewById(R.id.city_name);
        cityNameView.setText(city);
        cityNameView.setTextSize(36);
        cityNameView.setTextColor(Color.parseColor("#fefefe"));

        /*
         * customize the current date  position */
        int unixSeconds = currentTime;
// convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
// the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);



    }
}
