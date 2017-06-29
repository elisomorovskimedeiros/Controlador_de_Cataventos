package controller;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import helpers.extensions.ObservableArrayList;
import model.Controlador;
import static controller.Constantes.NA_ESCUTA;


/**
 * CLASSE CONEXÃO NA QUAL ESTÃO OS MÉTODOS DE CONEXÃO DO SOCKET COM O SERVIDOR, A PORTA UTILIZADA É A 3333
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
        }
        else {
            return conexao;
        }
    }

//CLASSE QUE EXECUTA A CONEXÃO
    public void executar(){
    // A conexão é feita em background, através do instanciamento de um AsyncTask
        new AsyncTask<Conexao, Conexao, Conexao> () {
            @Override
            protected void onPreExecute() {}

            @Override
            protected Conexao doInBackground(Conexao... paramss)
            {
                Conexao conexao = paramss[0];
                Socket socket = null;

                try{
                    Log.i("Controlador", conexao.controlador.toString());
                    socket = new Socket(conexao.controlador.getIp(), conexao.getPortaConexao());


                    if (socket.isConnected()){
                        conexao.setStatusRede(true);
                    }

                    //processo de envio de dados pela rede
                    OutputStream ou = socket.getOutputStream();
                    OutputStreamWriter ouw = new OutputStreamWriter(ou);
                    BufferedWriter bfw = new BufferedWriter(ouw);

                    //processo de recebimento dos dados pela rede
                    InputStream in = socket.getInputStream();
                    InputStreamReader inr = new InputStreamReader(in);
                    BufferedReader bfr = new BufferedReader(inr);
                    String msg = "";



                    //com o while (true) o sistema fica sempre escutando se está chegando nova mensagem
                    while(true){

                        bfw.write(conexao.getComando() + "\r\n");

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


                    //nessa linha o publishProgress atualiza sempre o objeto conexão para que a classe principal tenha acesso as leituras
                        //recebidas do servidor
                        publishProgress(conexao);
                        Thread.sleep(2000);
                    }


                } catch (UnknownHostException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }



                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }


            protected void onProgressUpdate(Conexao... progress) {
                Log.i("Conexao", conexao.toString());



            }


            protected void onPostExecute(Void result) {}
        }.execute(this.conexao, this.conexao, this.conexao);
    }
}