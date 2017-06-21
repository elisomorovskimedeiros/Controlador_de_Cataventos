package controller;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;

import com.example.ifrs.myapplication.MainActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;

import model.Controlador;

import com.example.ifrs.myapplication.MainActivity.*;

import static controller.Constantes.NA_ESCUTA;


/**
 * Created by ifrs on 08/06/2017.
 */

public class Controle {

    public static Controle controle;
    public Controlador controlador;
    public Conexao conexao;
    public char comando;
    public String giroCatavento;
    public String nivelTensaoBaterias;
    public String stringStatusInversor;
    public boolean statusInversor;

//SINGLETON
    public static Controle getInstance(){
        if (controle == null){
            return new Controle();
        }
        else {
            return controle;
        }
    }

//ESTAVA FAZENDO O CONTROLE, ESSA CLASSE É A QUE MONTA OS OBJECTOS ABAIXO E REALIZA REALMENTE A CONEXAO;
    /*public Controle(){
        this.controlador = new Controlador();
        InetAddress ip = null;
       // String ipSeparado[4];
        int i = 0;
        //for (String ipFor : ipSeparado){

        //controlador.setIpControlador();
        controlador.setStatusControlador(false);
        this.conexao = new Conexao(controlador, 3333, TO_ESCUTANDO);
            //ipFor = MainActivity.ipString[i];
            i++;
        //}
    }*/

    public void executarConexao(EditText[] arrayIp, char naEscuta) throws InterruptedException {

       // this.comando = comando;

        String enderecoIp = "";
        for(int i = 0; i < 4; i++){
            enderecoIp += arrayIp[i];
            if (i < 3){
                enderecoIp += ".";
            }
        }
        InetAddress ip = null;
        try {
            ip.getByName(enderecoIp);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        controlador = new Controlador();
        controlador.setIpControlador(ip);

        while(true) {
            conexao = Conexao.getInstance(this.comando, controlador);
            if (conexao.getStatusRede()){
                preencherDadosResposta();
            }
            this.wait(500);
        }



        //AbrirThread thread = new AbrirThread();
        //thread.execute(new String[] { enderecoIp });
    }

    private class AbrirThread extends AsyncTask<String, Void, Uri> {

        @Override
        protected Uri doInBackground(String... params) {
            return abrirConexao(params[0]);
        }
    }

    public Uri abrirConexao(String stringIp){
        InetAddress ip = null;
        Uri uri = null;
        try {
            ip.getByName(String.valueOf(stringIp));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void preencherDadosResposta(){
        String resposta[] = new String[3];

        resposta = conexao.getResposta().split(":");

        stringStatusInversor = resposta[0];

        nivelTensaoBaterias = resposta[1];

        giroCatavento = resposta[2];

        if (stringStatusInversor.equals("false")){
            statusInversor = false;

        }else {
            statusInversor = true;

        }
    }


}
