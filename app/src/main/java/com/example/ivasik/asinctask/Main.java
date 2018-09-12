package com.example.ivasik.asinctask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    private Button buttonStart;
    private TextView tvPrecent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.button1);
        tvPrecent = findViewById(R.id.textView1);
        progressBar = findViewById(R.id.progressBar);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(false);
                new ShowDialogAsynkTask().execute();

            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class ShowDialogAsynkTask extends AsyncTask<Void, Integer, Void> {

        int progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(Main.this, "Вызов PreExecute ", Toast.LENGTH_LONG).show();
            progress = 0;
            tvPrecent.setText("Загрузка 0%");

        }

        @Override
        protected Void doInBackground(Void... params) {

            while (progress < 100) {
                progress = +2;
                publishProgress(progress);
                SystemClock.sleep(300);
            }

            return null;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0]);
            tvPrecent.setText("Загрузка " + values[0] + "%");
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Toast.makeText(Main.this, "Вызов onPostExecute", Toast.LENGTH_LONG).show();
            tvPrecent.setText("Загрузка завершена!");
            buttonStart.setEnabled(true);

        }
    }

}

