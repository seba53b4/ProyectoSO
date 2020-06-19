/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import Utils.HandleFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyectoso.BancoDatos;
import proyectoso.Casilla;
import proyectoso.Peaje;
import proyectoso.Reloj;

/**
 *
 * @author Seba-OS
 */
public class Evento implements Runnable{
    
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

    @Override
    public void run() {
        
        SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
        
        synchronized(Reloj.getInstance()){
            try {
                HandleFile.getInstance().writeArchivo(Thread.currentThread().getName()+";Comienza evento en la casilla numero: "+ numeroCasilla + " a la hora "+formato.format(getFechaEvento()) 
                        +" hasta "+ formato.format(getFechaFinal()));
            } catch (Exception ex) {
                Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        System.out.println("*********************");
//        System.out.println("Ocurre un evento en la casilla " + getNumeroCasilla());
//        System.out.println("Se bloquea desde " + formato.format(getFechaEvento()) + " hasta " + formato.format(getFechaFinal()));
//        //System.out.println("Fecha evento iniciado "+ event.getFechaEvento()+ " estado casilla "+event.getNumeroCasilla()+" "+casillas[event.getNumeroCasilla()].isBloqueada());
//        //eventosPasados.add(event);
//        System.out.println("*********************");
        
        Peaje.getInstance().estadoEventoCasilla(numeroCasilla, false);
        
        while (Reloj.getInstance().getDate().compareTo(fechaFinal) != 0 ) {
                    
        }
        
        Peaje.getInstance().estadoEventoCasilla(numeroCasilla, true);
//      System.out.println("Finaliza el evento "+ toString());
        
         synchronized(Reloj.getInstance()){
            try {

               HandleFile.getInstance().writeArchivo(Thread.currentThread().getName()+";Finaliza evento en la casilla numero: "+ numeroCasilla + " a la hora "+ formato.format(getFechaFinal()) );

            } catch (Exception ex) {
                Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
                
    }
}
