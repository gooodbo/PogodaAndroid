package com.example.ivasik.asinctask;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends AppCompatActivity {
    private String parsingData = "";
    private TextView tvCity, tvWeathere, tvTemp, tvHumidity, tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCity = findViewById(R.id.textView1);
        tvWeathere = findViewById(R.id.textView2);
        tvTemp = findViewById(R.id.textView3);
        tvHumidity = findViewById(R.id.textView4);
        tvWind = findViewById(R.id.textView5);
        Background background = new Background();
        background.execute();

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

    private class Background extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main.this, "Вызов PreExecute ", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String query = "http://api.openweathermap.org/data/2.5/weather?q=Minsk,by&units=metric&APPID=b1e607de7b2c594b9d4a4d6d8fe3916b\n";
            HttpURLConnection connection = null;
            StringBuilder stringBuilder = new StringBuilder();
            System.out.println("Я в соединении");
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
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Parsing parsing = new Parsing();

            Toast.makeText(Main.this, "Вызов onPostExecute", Toast.LENGTH_LONG).show();
            // tvCity.setText(parsingCity(parsingData));
            tvCity.setText(parsing.parseString("name", parsingData));
            // tvWeathere.setText("Облачность: " + parsingPogoda(parsingData));
            tvWeathere.setText("Облачность: " + parsing.parseString("weather", "main", parsingData));
            //tvTemp.setText("Температура: " + parsingTemp(parsingData).intValue() + " °С");
            tvTemp.setText("Температура: " + parsing.parseLong("main", "temp", parsingData) + " °С");
            // tvHumidity.setText("Влажность: " + parsingHumidity(parsingData).intValue() + " %");
            tvHumidity.setText("Влажность: " + parsing.parseLong("main", "humidity", parsingData) + " %");
            //   tvWind.setText("Скорость ветра: " + parsingWind(parsingData).intValue() + " км/ч");
            tvWind.setText("Скорость ветра: " + parsing.parseLong("wind", "speed", parsingData) + " км/ч");

        }


    }
}



