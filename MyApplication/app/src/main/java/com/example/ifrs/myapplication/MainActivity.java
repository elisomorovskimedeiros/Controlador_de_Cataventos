package com.example.ifrs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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

    public AnimationDrawable animacao_catavento;
    public Switch chaveCatavento;
    public Switch chaveInversor;
    public EditText campoIp;
    public TextView statusRede;
    public TextView campoTensao;
    public TextView campoGiro;
    public Button conectar;
    public Conexao conexao;
    //public Controlador controlador;
    //public boolean statusCatavento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_landscape);
        }


        //MONTAGEM DAS ABAS DA TELA PRINCIPAL
        //Cada aba é formada por um objeto tipo TabSpec

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

        // INSTANCIAMENTO DA CLASSE CONEXAO
        conexao = Conexao.getInstance();

        statusRede = (TextView) findViewById(R.id.status_rede);

        campoIp = (EditText) findViewById(R.id.campo_ip);


        conectar = (Button) findViewById(R.id.btnConectar);
        conectar.setOnClickListener(this);


        //CÓDIGO DA ANIMAÇÃO DO CATAVENTO DA PRIMEIRA ABA

        ImageView imagem_catavento = (ImageView) findViewById(R.id.animacao);
        imagem_catavento.setImageResource(R.drawable.animacao);

        animacao_catavento = (AnimationDrawable) imagem_catavento.getDrawable();


        chaveCatavento = (Switch) findViewById(R.id.chave_catavento);

        //Listener de controle do freio do catavento
        chaveCatavento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && conexao.getStatusRede()) {
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

        //INSTANCIAMENTO DOS CAMPO DA SEGUNDA ABA
        campoTensao = (TextView) findViewById(R.id.campo_tensao);
        campoGiro = (TextView) findViewById(R.id.campo_giro);


        //THREAD UTILIZADA PARA O PREENCHIMENTO AUTIMOMÁTICO DOS NÍVEIS DE TENSÃO E GIRO NA SEGUNDA ABA
        Thread t = new Thread(){
            @Override
            public void run() {

                        //Listener que avisa quando chega a string de comunicação com o servidor
                        conexao.lista.addChangeListener(new IArrayChangeListener<String>() {


                            //evento de aviso do preenchimento do array list com as Strings recebidas do servidor
                            @Override
                            public void onChange(EChange action, String changed) {

                                if (conexao.lista.size() > 0) {
                                    Log.i("Listener", conexao.lista.get(conexao.lista.size() - 1));
                                }
                                if (action == EChange.added) {
                                    String respToda = "";
                                //linhas que quebra a String recebida e a divinas três medidas esperadas:
                                    //  status do inversor, tensão das baterias e giro do catavento
                                    if (conexao.lista.size() > 0) {
                                        respToda = conexao.lista.get(conexao.lista.size() - 1);
                                    }

                                    final String arrayResp[] = respToda.split(":");

                                //Fila de execução da thread principal, que atualizará os elementos da tela
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {

                                            campoTensao.setText(arrayResp[1] + "V");
                                            campoGiro.setText(arrayResp[2] + "rpm");

                                            if (conexao.getStatusRede()){

                                                statusRede.setText("Controlador ON LINE");
                                                conectar.setEnabled(false); //DESABILITANDO O BOTÃO DE CONEXÃO

                                                if(!(conexao.getComando() == FREAR_CATAVENTO)// VERIFICANDO SE O CATAVENTO PODE SER LIBERADO
                                                        && (Integer.parseInt(arrayResp[2]) > 20)){
                                                    chaveCatavento.setChecked(true);
                                                }
                                            }else {
                                                statusRede.setText("Perda de comunicação com o controlador");
                                                conectar.setEnabled(true); //HABILITANDO O BOTÃO DE CONEXÃO
                                            }


                                        }
                                    });
                                }
                            }

                        });


            }
        };
        t.start();



    }






//  MÉTODO QUE CAPTURA O PRECIONAR DO BOTÃO DE CONEXÃO
    @Override
    public void onClick(View v) {
        String ip = String.valueOf(campoIp.getText());
        conexao.getControlador().setIp(ip);
// MÉTODO DE CONEXÃO DO SOCKET PROPRIAMENTE DITA
        conexao.executar();

    }

    @Override
    protected void onSaveInstanceState(Bundle estadoFinal) {
        super.onSaveInstanceState(estadoFinal);
        Log.i("salvando instancia", "onSaveInstanceState");

        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        int tab = host.getCurrentTab();
        estadoFinal.putInt("tab_ativa", tab);
    }

    @Override
    public void onRestoreInstanceState(Bundle estadoSalvo){
        super.onRestoreInstanceState(estadoSalvo);
        Log.i("restaurando instancia", "onRestoreInstanceState");

        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        int tab = estadoSalvo.getInt("tab_ativa");
        host.setCurrentTab(tab);
    }

    @Override
    public void onConfigurationChanged(Configuration estadoSalvo){
        super.onConfigurationChanged(estadoSalvo);
    }


}












