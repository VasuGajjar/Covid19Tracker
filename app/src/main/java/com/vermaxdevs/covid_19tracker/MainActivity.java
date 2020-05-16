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
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


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
                    new Global().execute();
                }
                else if (position == 1) {
                    new India().execute();
                }
                else if (position == 2) {
                    new Gujarat().execute();
                }
                else if (position == 3) {
                    new Ahmedabad().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new India().execute();
            }
        });
    }

    private String timeAgo(String dataDate) {
        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second+" Seconds "+suffix;
            } else if (minute < 60) {
                convTime = minute+" Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour+" Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 30) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 360) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Covid Tracker", e.getMessage());
        }

        return convTime;
    }

    private String timeAgo2(String dataDate) {
        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second+" Seconds "+suffix;
            } else if (minute < 60) {
                convTime = minute+" Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour+" Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 30) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 360) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Covid Tracker", e.getMessage());
        }

        return convTime;
    }

    class Global extends AsyncTask<Void, Void, JSONObject> {
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
                updateTime.setText(jsonObject.getJSONObject("world_total").toString());
                todayCase.setText(jsonObject.getJSONObject("world_total").getString("new_cases"));
                todayRecovered.setText("NA");
                todayDeath.setText(jsonObject.getJSONObject("world_total").getString("new_deaths"));
                totalCase.setText(jsonObject.getJSONObject("world_total").getString("total_cases"));
                totalRecovered.setText(jsonObject.getJSONObject("world_total").getString("total_recovered"));
                totalDeath.setText(jsonObject.getJSONObject("world_total").getString("total_deaths"));
                active.setText(jsonObject.getJSONObject("world_total").getString("active_cases"));
                addCase.setText("+"+jsonObject.getJSONObject("world_total").getString("new_cases"));
                addDeath.setText("+"+jsonObject.getJSONObject("world_total").getString("new_deaths"));
                addRecovered.setText("+NA");
                updateTime.setText("Last Updated "+timeAgo2(jsonObject.getJSONObject("world_total").getString("statistic_taken_at")));
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject object = null;

            try {
                HttpResponse<String> response = Unirest.get("https://corona-virus-world-and-india-data.p.rapidapi.com/api")
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
                updateTime.setText("Last Updated "+timeAgo(jsonObject.getJSONObject("total_values").getString("lastupdatedtime")));
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                updateTime.setText("Last Updated "+timeAgo(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("lastupdatedtime")));
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                todayCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("confirmed"));
                todayRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("recovered"));
                todayDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("deceased"));
                totalCase.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getString("confirmed"));
                totalRecovered.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getString("recovered"));
                totalDeath.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getString("deceased"));
                active.setText(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getString("active"));
                addCase.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("confirmed"));
                addDeath.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("deceased"));
                addRecovered.setText("+"+jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getJSONObject("district").getJSONObject("Ahmedabad").getJSONObject("delta").getString("recovered"));
                updateTime.setText("Last Updated "+timeAgo(jsonObject.getJSONObject("state_wise").getJSONObject("Gujarat").getString("lastupdatedtime")));
            }
            catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
