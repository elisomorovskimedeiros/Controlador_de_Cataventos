package com.example.ifrs.myapplication;

import android.app.TabActivity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.eli.controlador_de_cataventos.R;

public class MainActivity extends AppCompatActivity {
    //ImageView imagem_catavento;
    AnimationDrawable animacao_catavento;
    Switch chaveCatavento;
    EditText byte1, byte2, byte3, byte4;
 //   TextView response;
 //   EditText editTextAddress, editTextPort;
 //   Button buttonConnect, buttonClear;
 TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Controles");
        spec.setContent(R.id.controles);
        spec.setIndicator("Controles");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Medidas");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Medidas");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Configs");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Configurações");
        host.addTab(spec);

        //animação
        ImageView imagem_catavento = (ImageView)findViewById(R.id.animacao);
        imagem_catavento.setImageResource(R.drawable.animacao);

        animacao_catavento = (AnimationDrawable)imagem_catavento.getDrawable();


        chaveCatavento = (Switch)findViewById(R.id.chave_catavento);
        chaveCatavento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    animacao_catavento.start();
                }
                else {
                    animacao_catavento.stop();
                }

            }

        });

        byte1 = (EditText)findViewById(R.id.byte1);
        byte2 = (EditText)findViewById(R.id.byte2);
        byte3 = (EditText)findViewById(R.id.byte3);
        byte4 = (EditText)findViewById(R.id.byte4);

    }





    }












