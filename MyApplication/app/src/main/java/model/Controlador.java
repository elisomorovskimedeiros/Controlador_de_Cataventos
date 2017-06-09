package model;

import android.net.IpPrefix;

import java.net.InetAddress;

/**
 * Created by ifrs on 08/06/2017.
 */

public class Controlador {

    private boolean statusControlador;
    private InetAddress ipControlador;

    public boolean isStatusControlador() {
        return statusControlador;
    }

    public void setStatusControlador(boolean statusControlador) {
        this.statusControlador = statusControlador;
    }

    public InetAddress getIpControlador() {
        return ipControlador;
    }

    public void setIpControlador(InetAddress ipControlador) {
        this.ipControlador = ipControlador;
    }

    @Override
    public String toString() {
        return "Controlador{" +
                "statusControlador=" + statusControlador +
                ", ipControlador=" + ipControlador +
                '}';
    }

    public Controlador getInstance(){
        if (this == null){
            return new Controlador();
        }
        else{
            return this;
        }
    }
}
