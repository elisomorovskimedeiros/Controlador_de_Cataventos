package com.example.ifrs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;



import java.net.InetAddress;

import controller.Conexao;
import helpers.extensions.ObservableArrayList;
import helpers.extensions.listeners.EChange;
import helpers.extensions.listeners.IArrayChangeListener;
import model.Controlador;

import static controller.Constantes.DESLIGAR_INVERSOR;
import static controller.Constantes.FREAR_CATAVENTO;
import static controller.Constantes.LIBERAR_CATAVENTO;
import static controller.Constantes.LIGAR_INVESOR;
import static controller.Constantes.NA_ESCUTA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //ImageView imagem_catavento;
    AnimationDrawable animacao_catavento;
    Switch chaveCatavento;
    Switch chaveInversor;
    public EditText campoIp;
    TextView statusRede;
    public TextView campoTensao;
    public TextView campoGiro;
    Button conectar;
    public Conexao conexao;
    public Controlador controlador;
    public boolean statusCatavento;
 //   EditText editTextAddress, editTextPort;
 //   Button buttonConnect, buttonClear;
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
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
        spec.setIndicator("Configs");
        host.addTab(spec);


        conexao = Conexao.getInstance();
        //Log.i("Controlador", conexao.getControlador().toString());


        statusRede = (TextView) findViewById(R.id.status_rede);
        //campoIp = new EditText;
        campoIp = (EditText) findViewById(R.id.campo_ip);


        conectar = (Button) findViewById(R.id.btnConectar);
        conectar.setOnClickListener(this);


        //animação

        ImageView imagem_catavento = (ImageView) findViewById(R.id.animacao);
        imagem_catavento.setImageResource(R.drawable.animacao);

        animacao_catavento = (AnimationDrawable) imagem_catavento.getDrawable();


        chaveCatavento = (Switch) findViewById(R.id.chave_catavento);
        //Listener de controle do freio do catavento
        chaveCatavento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked/* && conexao.getStatusRede()*/) {
                    conexao.setComando(LIBERAR_CATAVENTO);
                    animacao_catavento.start();
                } else {
                    conexao.setComando(FREAR_CATAVENTO);
                    animacao_catavento.stop();
                }

            }

        });

        //listener do controle do inversor
        chaveInversor = (Switch) findViewById(R.id.chave_inversor);
        chaveInversor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && conexao.getStatusRede()) {
                    conexao.setComando(LIGAR_INVESOR);
                } else {
                    conexao.setComando(DESLIGAR_INVERSOR);
                }
            }

        });

        campoTensao = (TextView) findViewById(R.id.campo_tensao);
        campoGiro = (TextView) findViewById(R.id.campo_giro);

        Thread t = new Thread(){
            @Override
            public void run() {


                        conexao.lista.addChangeListener(new IArrayChangeListener<String>() {

                            @Override
                            public void onChange(EChange action, String changed) {

                                Log.i("Listener", "chamou o listener");
                                if (conexao.lista.size() > 0) {
                                    Log.i("Listener", conexao.lista.get(conexao.lista.size() - 1));
                                }
                                if (action == EChange.added) {
                                    String respToda = "";

                                    if (conexao.lista.size() > 0) {
                                        respToda = conexao.lista.get(conexao.lista.size() - 1);
                                    }

                                    final String arrayResp[] = respToda.split(":");
                                    if( Integer.parseInt(arrayResp[2]) > 20){
                                        statusCatavento = true;
                                    }

                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {

                                            campoTensao.setText(arrayResp[1]);
                                            campoGiro.setText(arrayResp[2]);
                                            chaveCatavento.setChecked(true);
                                            if (conexao.getStatusRede()){
                                                statusRede.setText("Controlador ON LINE");
                                                conectar.setEnabled(false);
                                            }
                                            else {
                                                statusRede.setText("Perda de comunicação com o controlador");
                                                conectar.setEnabled(true);
                                            }


                                        }
                                    });
                                }
                            }

                        });

                //Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(mainActivity);
            }
        };
        t.start();



    }


       /*
        campoIp[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    controle = comunicarControlador();
*/





    @Override
    public void onClick(View v) {
        String ip = String.valueOf(campoIp.getText());
        conexao.getControlador().setIp(ip);
        // Log.i("ip_extraído", ip);
        // Log.i("Controlador", conexao.getControlador().toString());
        conexao.executar();

    }



/*
    Activity.runOnUiThread(new Runnable(){
        conexao.lista.addChangeListener(new IArrayChangeListener<String>() {

            @Override
            public void onChange(EChange action, String changed) {

                Log.i("Listener", "chamou o listener");
                if (conexao.lista.size() > 0) {
                    Log.i("Listener", conexao.lista.get(conexao.lista.size()-1));
                }
                if(action == EChange.added){
                    String respToda = "";

                    if (conexao.lista.size() > 0) {
                        respToda = conexao.lista.get(conexao.lista.size()-1);
                    }

                    String arrayResp[] = respToda.split(":");

                    MainActivity.campoTensao.setText(arrayResp[1]);
                    MainActivity.campoGiro.setText(arrayResp[2]);
                }
            }
        });
    });*/
}












