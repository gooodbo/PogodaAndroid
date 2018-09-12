package com.example.ivasik.asinctask;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Main extends AppCompatActivity {
    private String data = "";
    private TextView tvCity, tvWeathere, tvTemp, tvHumidity, tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvCity = findViewById(R.id.textView1);
        tvWeathere = findViewById(R.id.textView2);
        tvTemp = findViewById(R.id.textView3);
        tvHumidity = findViewById(R.id.textView4);
        tvWind = findViewById(R.id.textView5);

        setContentView(R.layout.activity_main);
        Background background = new Background();
        background.execute();

        try {
            data = background.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            tvCity.setText(parsingCity(data));
            tvWeathere.setText(parsingCity(data));
            tvTemp.setText(parsingCity(data));
            tvHumidity.setText(parsingCity(data));
            tvWind.setText(parsingCity(data));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String parsingCity(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        return (String) weatherJsonObject.get("name");
    }

    public String parsingPogoda(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONArray weatherArray = (JSONArray) weatherJsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        return (String) weatherData.get("main");
    }

    public Long parsingTemp(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject temp = (JSONObject) weatherJsonObject.get("main");
        JSONArray arrayTemp = new JSONArray();
        arrayTemp.add(temp);
        JSONObject tempData = (JSONObject) arrayTemp.get(0);
        return (Long) tempData.get("temp");
    }

    public Long parsingHumidity(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject humidity = (JSONObject) weatherJsonObject.get("main");
        JSONArray arrayHumidity = new JSONArray();
        arrayHumidity.add(humidity);
        JSONObject HumidityData = (JSONObject) arrayHumidity.get(0);
        return (Long) HumidityData.get("humidity");
    }

    public Long parsingWind(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject wind = (JSONObject) weatherJsonObject.get("wind");
        JSONArray arrayWind = new JSONArray();
        arrayWind.add(wind);
        JSONObject windData = (JSONObject) arrayWind.get(0);
        return (Long) windData.get("speed");
    }


    @SuppressLint("StaticFieldLeak")
    private class Background extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main.this, "Вызов PreExecute ", Toast.LENGTH_LONG).show();
        }

        String parsingData = "";


        @Override
        protected String doInBackground(Void... voids) {

            String query = "http://api.openweathermap.org/data/2.5/weather?q=Minsk,by&units=metric&APPID=b1e607de7b2c594b9d4a4d6d8fe3916b\n";
            HttpURLConnection connection = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                connection = (HttpURLConnection) new URL(query).openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setConnectTimeout(25000);
                connection.setReadTimeout(25000);
                connection.connect();

                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    parsingData = stringBuilder.toString();
                }


            } catch (Throwable cause) {
                cause.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

            return parsingData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Main.this, "Вызов onPostExecute", Toast.LENGTH_LONG).show();

        }

    }
}



