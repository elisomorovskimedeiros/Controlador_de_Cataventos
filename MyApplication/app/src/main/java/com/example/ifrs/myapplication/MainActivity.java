package com.example.ifrs.myapplication;

import android.app.TabActivity;
import android.graphics.drawable.AnimationDrawable;
import android.net.sip.SipSession;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import controller.Conexao;
import controller.Controle;
import model.Controlador;

import static controller.Constantes.NA_ESCUTA;

public class MainActivity extends AppCompatActivity {
    //ImageView imagem_catavento;
    AnimationDrawable animacao_catavento;
    Switch chaveCatavento;
    public EditText[] campoIp;
    TextView statusRede;
    Controle controle;
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



        statusRede = (TextView)findViewById(R.id.status_rede);

        campoIp[0] = (EditText)findViewById(R.id.byte1);
        campoIp[1] = (EditText)findViewById(R.id.byte2);
        campoIp[2] = (EditText)findViewById(R.id.byte3);
        campoIp[3] = (EditText)findViewById(R.id.byte4);

        campoIp[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    controle = comunicarControlador();


                    //A animação foi colocada dentro do listerner do preenchimento do campo ip, pois apenas pode haver moviemnto no
                    // catavento após haver uma conexão online
                    //animação

                    ImageView imagem_catavento = (ImageView)findViewById(R.id.animacao);
                    imagem_catavento.setImageResource(R.drawable.animacao);

                    animacao_catavento = (AnimationDrawable)imagem_catavento.getDrawable();


                    chaveCatavento = (Switch)findViewById(R.id.chave_catavento);
                    chaveCatavento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked && controle.conexao.getStatusRede()){
                                animacao_catavento.start();
                            }
                            else {
                                animacao_catavento.stop();
                            }

                        }

                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    public Controle comunicarControlador() throws InterruptedException {
        Controlador controlador = new Controlador();
        Conexao conexao = Conexao.getInstance(NA_ESCUTA,controlador);
        Controle controle = Controle.getInstance();
        controle.executarConexao(campoIp, NA_ESCUTA);
        if (conexao.getStatusRede()){
            statusRede.setText("Controlador ON LINE");
        }
        return controle;
    }

}

//FICOU FALTANDO FAZER OS LISTENERS PARA INSERIR O RETORNO DA REDE NA TELA E OS METODOS DE CAPTURA DOS COMANDOS NA TELA E
//ENVIO PARA O OBJETO CONTROLE











