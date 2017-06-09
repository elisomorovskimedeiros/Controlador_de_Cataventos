package model;

/**
 * Created by ifrs on 08/06/2017.
 */

public class Catavento {

    private boolean statusCatavento;
    private double tensao;
    private double giro;

    public boolean isStatus() {
        return statusCatavento;
    }

    public void setStatus(boolean status) {
        this.statusCatavento = status;
    }

    public double getTensao() {
        return tensao;
    }

    public void setTensao(double tensao) {
        this.tensao = tensao;
    }

    public double getGiro() {
        return giro;
    }

    public void setGiro(double giro) {
        this.giro = giro;
    }

    @Override
    public String toString() {
        return "Catavento{" +
                "status=" + statusCatavento +
                ", tensao=" + tensao +
                ", giro=" + giro +
                '}';
    }
}
