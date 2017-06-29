package com.example.ifrs.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class Splash_Activity extends AppCompatActivity implements Runnable{

    private static final long WAIT_TIME = 1500;
    private static final Handler handler = new Handler();
    Intent intent;
    TextView titulo;
    Typeface fonte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //as próximas duas linhas travam a tela em portrait
        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        // or = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setRequestedOrientation(orientation);


        setContentView(R.layout.activity_splash);
        titulo = (TextView)findViewById(R.id.titulo);
        intent = new Intent(this, MainActivity.class);
        fonte = Typeface.createFromAsset(getAssets(),"Lobster 1.4.otf");
        fonte.isBold();
        titulo.setTypeface(fonte);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(Splash_Activity.this, WAIT_TIME);

    }

    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        startActivity(intent);
        finish();
    }

}
