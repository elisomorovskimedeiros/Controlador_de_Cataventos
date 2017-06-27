package model;

import android.net.IpPrefix;

import java.net.InetAddress;

/**
 * Created by ifrs on 08/06/2017.
 */

public class Controlador {

    public static Controlador controlador;
    private boolean statusControlador;
    //private InetAddress ipControlador;
    private String ip;

    public String getIp() {  return ip; }

    public void setIp(String ip) {  this.ip = ip; }

    public boolean isStatusControlador() {
        return statusControlador;
    }

    public void setStatusControlador(boolean statusControlador) {
        this.statusControlador = statusControlador;
    }
/*
    public InetAddress getIpControlador() {
        return ipControlador;
    }

    public void setIpControlador(InetAddress ipControlador) {
        this.ipControlador = ipControlador;
    }
*/
    @Override
    public String toString() {
        return "Controlador{" +
                "statusControlador=" + statusControlador +
               "ip: " + ip +
                '}';
    }

    public static Controlador getInstance(){
        if (controlador == null){
            controlador = new Controlador();
            controlador.setIp("127.0.0.1");
            controlador.setStatusControlador(false);
            return controlador;
        }
        else{
            return controlador;
        }
    }
}
