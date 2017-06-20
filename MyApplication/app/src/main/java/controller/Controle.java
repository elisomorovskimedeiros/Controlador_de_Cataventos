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

    public Controlador controlador;
    public Conexao conexao;

//SINGLETON
   /* public Controle getInstance(){
        if (this == null){
            return new Controle();
        }
        else {
            return this;
        }
    }*/

//ESTAVA FAZENDO O CONTROLE, ESSA CLASSE É A QUE MONTA OS OBJECTOS ABAIXO E REALIZA REALMENTE A CONEXAO;
    /*public Controle(){
        this.controlador = new Controlador();
        InetAddress ip = null;
       // String ipSeparado[4];
        int i = 0;
        //for (String ipFor : ipSeparado){

            //ipFor = MainActivity.ipString[i];
            i++;
        //}
        //controlador.setIpControlador();
        controlador.setStatusControlador(false);
        this.conexao = new Conexao(controlador, 3333, TO_ESCUTANDO);
    }*/

    public void executarConexao (EditText[] string){

        String enderecoIp = "";
        for(int i = 0; i < 4; i++){
            enderecoIp += string[i];
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
        conexao = Conexao.getInstance(NA_ESCUTA, controlador);



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
            ip.getByName(stringIp);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
        }
}
