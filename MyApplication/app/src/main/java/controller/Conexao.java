package controller;

import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Controlador;



/**
 * Created by eli on 06/05/17.
 */

public class Conexao extends AsyncTask<Void, Void, Void> {

    private static Conexao conexao;
    private static Controlador arduino;
    private int portaConexao;
    private String resposta = "";
    private char comando;
    private boolean statusRede = false;

    public Controlador getArduino() {
        return arduino;
    }

    public void setArduino(Controlador arduino) {
        this.arduino = arduino;
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

    public Conexao(Controlador arduino, int portaConexao, char comando){
        this.arduino = arduino;
        this.portaConexao = portaConexao;
        this.comando = comando;
    }

//SINGLETON
    public static Conexao getInstance(char comandoRecebido, Controlador arduino){
        if (conexao == null){
            return new Conexao(arduino, 3333, comandoRecebido);
        }
        else {
            return conexao;
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        Socket socket = null;
        Void respostaLocal;


        try{
            socket = new Socket(arduino.getIpControlador(), portaConexao);

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


}
