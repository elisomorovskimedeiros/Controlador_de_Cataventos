package controller;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ifrs.myapplication.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import helpers.extensions.ObservableArrayList;
import helpers.extensions.listeners.EChange;
import helpers.extensions.listeners.IArrayChangeListener;
import model.Controlador;

import static controller.Constantes.NA_ESCUTA;


/**
 * Created by eli on 06/05/17.
 */

public class Conexao{

    private static Conexao conexao;
    private static Controlador controlador;
    private int portaConexao;
    private String resposta = "";
    private char comando;
    private boolean statusRede = false;
    public ObservableArrayList<String> lista;

    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public int getPortaConexao() {
        return portaConexao;
    }

    public void setPortaConexao(int portaConexao) {
        this.portaConexao = portaConexao;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public char getComando() {
        return comando;
    }

    public void setComando(char comando) {
        this.comando = comando;
    }

    public boolean isStatusRede() { return statusRede;  }

    public void setStatusRede(boolean statusRede) { this.statusRede = statusRede; }

    public boolean getStatusRede(){ return this.statusRede; }

    @Override
    public String toString() {
        return "Conexao{" +
                "portaConexao=" + portaConexao +
                ", resposta='" + resposta + '\'' +
                ", comando=" + comando +
                ", statusRede=" + statusRede +
                ", lista=" + lista +
                '}';
    }
/*
    public Conexao(Controlador controlador, int portaConexao, char comando){
        this.controlador = controlador;
        this.portaConexao = portaConexao;
        this.comando = comando;
    }*/

//SINGLETON
    public static Conexao getInstance(){
        if (conexao == null){
            conexao = new Conexao();
            conexao.setControlador(Controlador.getInstance());
            conexao.setPortaConexao(3333);
            conexao.setComando(NA_ESCUTA);
            conexao.lista = new ObservableArrayList<>();
            conexao.lista.add("false:0:0");
            return conexao;
            //return new Conexao(arduino, 3333, comandoRecebido);
        }
        else {
            return conexao;
        }
    }


    public void executar(){
      /*  lista = new ObservableArrayList<>();
        lista.addChangeListener(new IArrayChangeListener<String>() {

            @Override
            public void onChange(EChange action, String changed) {
                if(action == EChange.added){
                    //...
                }
            }
        });*/
        new AsyncTask<Conexao, Conexao, Conexao> () {
            @Override
            protected void onPreExecute() {}

            @Override
            protected Conexao doInBackground(Conexao... paramss)
            {
               // ObservableArrayList<String> lista = paramss[0];
                Conexao conexao = paramss[0];
                Socket socket = null;

                try{
                    Log.i("Controlador", conexao.controlador.toString());
                    socket = new Socket(conexao.controlador.getIp(), conexao.getPortaConexao());

                    //Log.i("Conexao", conexao.toString());
                    if (socket.isConnected()){
                        conexao.setStatusRede(true);
                    }


                    /*OutputStreamWriter envioW = new OutputStreamWriter(envio);
                    BufferedWriter bfw = new BufferedWriter(envioW);
                    bfw.write(conexao.getComando());

                    InputStream recebimento = socket .getInputStream();
                    InputStreamReader inr = new InputStreamReader(recebimento);
                    BufferedReader bfr = new BufferedReader(inr);*/

                    //DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    OutputStream ou = socket.getOutputStream();
                    OutputStreamWriter ouw = new OutputStreamWriter(ou);
                    BufferedWriter bfw = new BufferedWriter(ouw);


                    InputStream in = socket.getInputStream();
                    InputStreamReader inr = new InputStreamReader(in);
                    BufferedReader bfr = new BufferedReader(inr);
                    String msg = "";




                    while(true){

                        bfw.write(NA_ESCUTA + "\r\n");




                            if(bfr.ready()) {
                                msg = bfr.readLine();
                                if (msg.equals("Sair")) {
                                    Log.i("Mensagem", "Servidor Caiu");
                                }
                                else {
                                    Log.i("Mensagem", msg);
                                    conexao.setResposta(msg);
                                    if (lista.size() > 100) {
                                        lista.clear();
                                    }
                                    conexao.lista.add(msg);

                                }


                            }


                        bfw.flush();

/*
                        if(bfr.ready()) {
                            msg = bfr.readLine();

                        }
                        Log.i("Mensagem", msg);

                        /*
                        if(bfr.ready()){
                            conexao.setResposta(bfr.readLine());
                        }
                        bfw.write(conexao.getComando());
                        dos.writeChar(NA_ESCUTA);
                        Log.i("Conexao", conexao.toString());*/

                        publishProgress(conexao);
                        Thread.sleep(2000);
                    }

/*
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
                    byte[] enviaComando = new byte[1];
                    enviaComando[0] = (byte) conexao.getComando();
                    byte[] recebeParametro = new byte[15];
                    String resposta = "";
                    int leituraDados;
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();


                    while(true){
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                        leituraDados = inputStream.read(recebeParametro);
                        //leituraDados = inputStream.read(buffer);
                        byteArrayOutputStream.write(enviaComando);
                        //byteArrayOutputStream.write(enviaComando, 0 , leituraDados);
                        Log.i("Conexao", conexao.toString());
                        //resposta += byteArrayOutputStream.toString();
                        //conexao.setStatusRede(true);
                       // lista.add(resposta);
                        publishProgress(conexao);
                    }*/


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    //resposta = "Host não encontrado! Excessão encontrada: " + e.toString();
                    //conexao.statusRede = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    //resposta = "Host não encontrado! Excessão encontrada: " + e.toString();
                    //conexao.statusRede = false;
                }



                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

                //@Override
            protected void onProgressUpdate(Conexao... progress) {
                Log.i("Conexao", conexao.toString());


                //conexao = progress[0];
                Conexao.conexao = conexao;
            }

            //@Override
            protected void onPostExecute(Void result) {}
        }.execute(this.conexao, this.conexao, this.conexao);
    }
}






/*
    @Override
    protected Void doInBackground(Void... arg0) {
        Socket socket = null;
        Void respostaLocal;


        try{
            //socket = new Socket(controlador.getIpControlador(), portaConexao);
            socket = new Socket("192.168.1.5", portaConexao);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int leituraDados;
            InputStream inputStream = socket.getInputStream();
            while ((leituraDados = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, leituraDados);
                resposta +=  byteArrayOutputStream.toString("UTF-8");
            }
            statusRede = true;

        }catch (UnknownHostException e){
            e.printStackTrace();
            resposta = "Host não encontrado! Excessão encontrada: " + e.toString();
            statusRede = false;
        }
        catch (IOException a){
            a.printStackTrace();
            resposta = "Falha de IO! Excessão encontrada: " + a.toString();
            statusRede = false;
        }
        finally {
            if (socket != null){
                try {
                    socket.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }
        }

        return null;
    }

    protected void onPostExecute(Void resultado){
        //textoResposta.setText(resposta);
        super.onPostExecute(resultado);
    }

    public void sendData(String data, Socket socket) throws IOException {
        if (socket != null && socket.isConnected()) {
            OutputStream os = null;
            os.write(data.getBytes());
        }
    }


}*/
