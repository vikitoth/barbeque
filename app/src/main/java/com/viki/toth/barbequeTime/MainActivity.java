package com.viki.toth.barbequeTime;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;
import com.google.android.gms.location.LocationServices;
import com.viki.toth.barbequeTime.client.RetrofitClient;
import com.viki.toth.barbequeTime.idlingResource.SimpleIdlingResource;
import com.viki.toth.barbequeTime.location.BarbequeLocation;
import com.viki.toth.barbequeTime.model.Daily;
import com.viki.toth.barbequeTime.model.DailyWeatherResponse;
import com.viki.toth.barbequeTime.model.Temp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String CALL = "Call";
    private final String WEATHER = "Weather";
    private TextView barbequeTimeTextView;
    private BarbequeLocation barbequeLocation;
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barbequeTimeTextView = findViewById(R.id.barbequeTimeTextView);
        barbequeLocation = new BarbequeLocation(LocationServices.getFusedLocationProviderClient(this), (LocationManager) getSystemService(Context.LOCATION_SERVICE));

        barbequeLocation.getLastLocation(this, this::getWeather);
    }

    private void getWeather(double latitude, double longitude) {
        Log.d(CALL, "Call weather API");
        new RetrofitClient()
                .getWeatherApi()
                .getDailyWeather(latitude, longitude)
                .enqueue(new Callback<DailyWeatherResponse>() {
                    @Override
                    public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                        processWeather(response.body());
                    }

                    @Override
                    public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                        Log.e(CALL, "An error has occurred! " + t.getMessage(), t);
                        Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                    }
                });
    }

    void processWeather(DailyWeatherResponse response) {
        // if temp is equal or above 20 Celsius and rain is less than 2 mm -> barbecue time.
        // if temp is below than 20 Celsius or rain is equal or above than 2 mm -> no barbecue party
        String status;
        if (isBarbequeTime(response)) {
            status = "barbecue time";
        } else {
            status = "no barbecue party";
        }
        Log.i(WEATHER, "ProcessWeather: " + status);
        barbequeTimeTextView.setText(status);
    }

    private boolean isBarbequeTime(DailyWeatherResponse dw) {
        List<Daily> dailies = dw.getDaily();

        if (dailies != null) {
            for (Daily daily : dailies) {
                Log.d(WEATHER, "daily: " + daily);
                if (isRainy(daily.getRain()) || !isWarmEnough(daily.getTemp())) {
                    Log.d(WEATHER, "Bad weather! Rain: " + daily.getRain() + " Temp: " + daily.getTemp());
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isRainy(Double rain) {
        return rain != null && rain >= 2.0;
    }

    private boolean isWarmEnough(Temp temp) {
        return temp.getDay() > 20.0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == barbequeLocation.PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            barbequeLocation.getLastLocation(this, this::getWeather);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        barbequeLocation.onResume(this, this::getWeather);
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
