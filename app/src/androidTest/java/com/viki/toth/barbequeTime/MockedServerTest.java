package com.viki.toth.barbequeTime;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MockedServerTest {
    private final MockWebServer mockWebServer = new MockWebServer();
    private IdlingResource mIdlingResource;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws IOException {
        mockWebServer.start(8080);
        registerIdlingResource();


/*
        IdlingRegistry.getInstance().register(
                OkHttp3IdlingResource.create(
                        "okhttp",
                        new OkHttpClient()
                        //OkHttpProvider.getOkHttpClient()
                ));*/
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
        unregisterIdlingResource();
    }

    @Test
    public void salala() {
        mockWebServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                return new MockResponse().setResponseCode(200).setBody("[]");
            }
        });
        onView(withId(R.id.barbequeTimeTextView))
                .check(matches(withText("Loading...")));
    }

    private void registerIdlingResource() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            mIdlingResource = activity.getIdlingResource();
            // To prove that the test fails, omit this call:
            IdlingRegistry.getInstance().register(mIdlingResource);
        });
    }

    private void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private void setMockLocation() {
        LocationManager locationManager = (LocationManager) InstrumentationRegistry.getInstrumentation().getContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
                false, false, true, true, true, 0, 5);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
        mockLocation.setLatitude(-33.852);  // Sydney
        mockLocation.setLongitude(151.211);
        mockLocation.setAltitude(10);
        mockLocation.setAccuracy(5);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setElapsedRealtimeNanos(System.nanoTime());
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);
    }
}