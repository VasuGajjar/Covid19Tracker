package com.vermaxdevs.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    TextView todayCase, todayRecovered, todayDeath;
    TextView totalCase, totalRecovered, totalDeath, active;
    TextView addCase, addRecovered, addDeath;
    TextView updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayCase = findViewById(R.id.newCases);
        todayRecovered = findViewById(R.id.newRecovered);
        todayDeath = findViewById(R.id.newDeath);
        totalCase = findViewById(R.id.totalCases);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        active = findViewById(R.id.active);
        addCase = findViewById(R.id.addCases);
        addRecovered = findViewById(R.id.addRecovered);
        addDeath = findViewById(R.id.addDeath);
        updateTime = findViewById(R.id.lastUpdate);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    new India().execute();
                }
                else if (position == 1) {
                    new Gujarat().execute();
                }
                else if (position == 2) {
                    new Ahmedabad().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new India().execute();
            }
        });
    }

    class India extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);progressDialog.dismiss();
            progressDialog.dismiss();

            try {

                todayCase.setText(jsonObject.getJSONObject("total_values").getString("deltaconfirmed"));
                todayRecovered.setText(jsonObject.getJSONObject("total_values").getString("deltarecovered"));
                todayDeath.setText(jsonObject.getJSONObject("total_values").getString("deltadeaths"));
                totalCase.setText(jsonObject.getJSONObject("total_values").getString("confirmed"));
                totalRecovered.setText(jsonObject.getJSONObject("total_values").getString("recovered"));
                totalDeath.setText(jsonObject.getJSONObject("total_values").getString("deaths"));
                active.setText(jsonObject.getJSONObject("total_values").getString("active"));
                addCase.setText("+"+jsonObject.getJSONObject("total_values").getString("deltaconfirmed"));
                addDeath.setText("+"+jsonObject.getJSONObject("total_values").getString("deltadeaths"));
                addRecovered.setText("+"+jsonObject.getJSONObject("total_values").getString("deltarecovered"));
                updateTime.setText("Last Updated "+jsonObject.getJSONObject("total_values").getString("lastupdatedtime"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject object = null;

            try {
                HttpResponse<String> response = Unirest.get("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
                        .header("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                        .header("x-rapidapi-key", "f8f40d658cmsh0ec9c8db1e09aacp168ea7jsn1d3fea5ba18e")
                        .asString();

                object = new JSONObject(String.valueOf(response.getBody()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return object;
        }
    }

    class Gujarat extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);progressDialog.dismiss();
            progressDialog.dismiss();

            try {

                todayCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltaconfirmed"));
                todayRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltarecovered"));
                todayDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltadeaths"));
                totalCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("confirmed"));
                totalRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("recovered"));
                totalDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deaths"));
                active.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("active"));
                addCase.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltaconfirmed"));
                addDeath.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltadeaths"));
                addRecovered.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltarecovered"));
                updateTime.setText("Last Updated "+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("lastupdatedtime"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject object = null;

            try {
                HttpResponse<String> response = Unirest.get("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
                        .header("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                        .header("x-rapidapi-key", "f8f40d658cmsh0ec9c8db1e09aacp168ea7jsn1d3fea5ba18e")
                        .asString();

                object = new JSONObject(String.valueOf(response.getBody()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return object;
        }
    }

    class Ahmedabad extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);progressDialog.dismiss();
            progressDialog.dismiss();

            try {

                todayCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltaconfirmed"));
                todayRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltarecovered"));
                todayDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltadeaths"));
                totalCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("confirmed"));
                totalRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("recovered"));
                totalDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deaths"));
                active.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("active"));
                addCase.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltaconfirmed"));
                addDeath.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltadeaths"));
                addRecovered.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("deltarecovered"));
                updateTime.setText("Last Updated "+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("lastupdatedtime"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject object = null;

            try {
                HttpResponse<String> response = Unirest.get("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
                        .header("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                        .header("x-rapidapi-key", "f8f40d658cmsh0ec9c8db1e09aacp168ea7jsn1d3fea5ba18e")
                        .asString();

                object = new JSONObject(String.valueOf(response.getBody()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return object;
        }
    }

}
