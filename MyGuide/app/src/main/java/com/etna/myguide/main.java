package com.etna.myguide;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main extends AppCompatActivity {

    private static final String API_URL = "https://myguideapi.herokuapp.com/";
    public static int splash_screen_time = 3000;

    public static String getAPI_URL()
    {
        return API_URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(main.this, ConnexionActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },splash_screen_time);

    }

}
