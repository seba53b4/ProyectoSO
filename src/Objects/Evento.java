/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import Utils.HandleFile;
import java.util.Date;

/**
 *
 * @author Seba-OS
 */
public class Evento {
    private final Date fechaEvento;
    private final int numeroCasilla;
    private final Date fechaFinal;

    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    public Date getFechaEvento() {
        return fechaEvento;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }
    
    public Evento(int numCasilla,Date fechaIni,Date fechaFin){
        fechaEvento = fechaIni;
        fechaFinal = fechaFin;
        numeroCasilla = numCasilla;
    }
    
    public String toString(){
        return " \n ************** "
                + "\n Evento "
                + "\n Numero Casilla: "+numeroCasilla +" "
                + "\n Fecha Inicio: "+ HandleFile.getInstance().getFormatoFecha().format(fechaEvento)
                + "\n Fecha Final: " + HandleFile.getInstance().getFormatoFecha().format(fechaFinal)
                + "\n ************** ";
    }
}
