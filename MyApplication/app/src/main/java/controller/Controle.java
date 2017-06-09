package controller;

import com.example.ifrs.myapplication.MainActivity;

import java.net.InetAddress;

import model.Controlador;

import static controller.Constantes.TO_ESCUTANDO;

/**
 * Created by ifrs on 08/06/2017.
 */

public class Controle {

    public Controlador arduino;
    public Conexao conexao;

//SINGLETON
    public Controle getInstance(){
        if (this == null){
            return new Controle();
        }
        else {
            return this;
        }
    }

//ESTAVA FAZENDO O CONTROLE, ESSA CLASSE É A QUE MONTA OS OBJECTOS ABAIXO E REALIZA REALMENTE A CONEXAO;
    public Controle(){
        this.arduino = new Controlador();
        InetAddress ip = null;
        String ipSeparado[4];
        for (String ipFor : ipSeparado){
            ipFor = MainActivity.byte;
        }
        arduino.setIpControlador();
        arduino.setStatusControlador(false);
        this.conexao = new Conexao(arduino, 3333, TO_ESCUTANDO);
    }
}
