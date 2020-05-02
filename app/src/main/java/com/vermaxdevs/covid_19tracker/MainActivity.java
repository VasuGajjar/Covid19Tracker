package com.vermaxdevs.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {

    TextView newCase, totalCase, died, totalDeath, recovered, active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newCase = findViewById(R.id.new_cases);
        totalCase = findViewById(R.id.total_cases);
        died = findViewById(R.id.died_today);
        totalDeath = findViewById(R.id.total_death);
        recovered = findViewById(R.id.recovered);
        active = findViewById(R.id.active);

        fetchData();
    }

    private void fetchData() {
        AsyncHttpClient client =new AsyncHttpClient();
        client.get("https://corona.lmao.ninja/v2/countries/india?yesterday&strict&query%20", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                updateUI(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(),"Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(JSONObject object) {
        try {
            newCase.setText(object.getString("todayCases"));
            totalCase.setText(object.getString("cases"));
            died.setText(object.getString("todayDeaths"));
            totalDeath.setText(object.getString("deaths"));
            recovered.setText(object.getString("recovered"));
            active.setText(object.getString("active"));
        }
        catch (Exception e) {
            log.e("covid tracker",e.toString());
        }
    }
}
