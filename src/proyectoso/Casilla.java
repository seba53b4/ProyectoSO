/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;
import Utils.HandleFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Seba-OS
 */
public class Casilla implements Runnable{

    private int numeroCasilla;
    private ConcurrentLinkedDeque<IVehiculo> enEspera; //
    private boolean habilitada;

    
    public int cantEspera;
    private boolean telepeaje;
    private Semaphore accesoCasilla;
    private IVehiculo aProcesar;
    
    public synchronized void set_aProcesar(IVehiculo v){
        this.aProcesar = v;
    }
    
    public int getCantEspera() {
        return cantEspera;
    }
  
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
    
    public synchronized Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }

    public boolean isHabilitado() {
        return habilitada ;
    }
    
    public Casilla(int numCasilla, boolean tel,boolean av){
        enEspera = new ConcurrentLinkedDeque<IVehiculo>() ;
        habilitada = av;
        numeroCasilla = numCasilla;
        telepeaje = tel;
        accesoCasilla = new Semaphore(1);
        cantEspera = 0;
    }
    
    
    
    
    @Override
    public void run() {
        try {

            accesoCasilla.acquire();
            IVehiculo aux ;
            synchronized(enEspera){
                aux =  enEspera.remove();
            }
            Date horaSalida;
            Date entradaReal = (Date) Reloj.getInstance().getDate().clone(); 
            System.out.println(Thread.currentThread().getName() +" hora real de entrada " + HandleFile.getInstance().getFormatoFecha().format(entradaReal) +" de vehiculo "+ aux.getMatricula());
            int wait = aux.getTime().getSeconds()+aux.getEspera();
            horaSalida = (Date) Reloj.getInstance().getDate().clone();
            horaSalida.setSeconds(wait);
            System.out.println(Thread.currentThread().getName() +" Hora salida estimada " + HandleFile.getInstance().getFormatoFecha().format(horaSalida)+" de vehiculo "+ aux.getMatricula());
            
            //Thread.sleep(10 * aux.getEspera());
            
            while (Reloj.getInstance().getDate().compareTo(horaSalida) != 0 ) {

            }
            synchronized(Reloj.getInstance()){
                try {
                    
                    System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo: " + aux.getTipo() + " de mat: " + aux.getMatricula()+ " hora real salida: "+ Reloj.getInstance().getHora());
                    for (IVehiculo ve : enEspera) {
                        System.out.print("En espera "+ve.getMatricula());
                    }
                    
                    System.out.println("");
                    BancoDatos.getBancoDatos().incCantidadVehiculos();
                    BancoDatos.getBancoDatos().aumentarRecaudacion(aux.getTipo());
                    BancoDatos.getBancoDatos().aumentarCostoOperativo(45.0);
                    SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                    HandleFile.getInstance().writeArchivo(Thread.currentThread().getName()+";"+ this.numeroCasilla +";" + aux.getTipo()+ ";"
                            +aux.getMatricula()+";"+formato.format(entradaReal)+";"+ formato.format(Reloj.getInstance().getDate())+";"+ ((Reloj.getInstance().getDate().getTime() - entradaReal.getTime())/1000) );
                } catch (Exception ex) {
                    Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // Se dehabilita si no tiene mas vehiculos en espera y no son las primeras casilla 0 y casilla 1. 
            if (this.numeroCasilla > 1  && enEspera.isEmpty()) {
                this.setHabilitada(false);
            }
            accesoCasilla.release();
            
        } catch (Exception ex) {
            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    
    public void addVehiculoEnEspera(IVehiculo veh){
        synchronized(enEspera){
            this.enEspera.add(veh);
        }
    }
    
    public int getCantidadEnEspera(){
        return this.enEspera.size();
    }

    
}
