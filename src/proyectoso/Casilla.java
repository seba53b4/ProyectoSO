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
    private Semaphore accesoCasilla;
    private boolean bloqueada;
    private boolean esDefault;

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public synchronized boolean isBloqueada() {
        return bloqueada;
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
    
    public synchronized Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }
    public Queue<IVehiculo> clearFila(){
        Queue<IVehiculo> ret = this.enEspera;
        this.enEspera = new ConcurrentLinkedDeque<>();
        return ret;
    }

    public boolean isHabilitado() {
        return habilitada ;
    }
    
    public Casilla(int numCasilla, boolean tel,boolean av){
        enEspera = new ConcurrentLinkedDeque<IVehiculo>() ;
        habilitada = av;
        numeroCasilla = numCasilla;
        accesoCasilla = new Semaphore(1);
        bloqueada = false;
        if (numeroCasilla < 2) {
            esDefault = true;
        } else {
            esDefault = false;
        }
    }

    public void setEsDefault(boolean esDefault) {
        this.esDefault = esDefault;
    }

    public boolean esDefault() {
        return esDefault;
    }
    
    @Override
    public void run() {
        
        try {
            
            accesoCasilla.acquire();
            IVehiculo aux ;
            
            aux =  enEspera.peek();
            
            if (aux != null) {
                
                Date horaSalida;
                Date entradaReal = (Date) Reloj.getInstance().getDate().clone();
                //System.out.println(Thread.currentThread().getName() +" hora real de entrada " + HandleFile.getInstance().getFormatoFecha().format(entradaReal) +" de vehiculo "+ aux.getMatricula());
                horaSalida = Reloj.getInstance().esperarTiempo(aux.getEspera());
                //System.out.println(Thread.currentThread().getName() +" Hora salida estimada " + HandleFile.getInstance().getFormatoFecha().format(horaSalida)+" de vehiculo "+ aux.getMatricula());
                
                while (Reloj.getInstance().getDate().compareTo(horaSalida) != 0 && !isBloqueada()) {
                    
                }
                
                if (!isBloqueada()) {
                    
                    synchronized(Reloj.getInstance()){
                        try {
                            
                            System.out.println("[SALE] El vehiculo de tipo " + aux.getTipo() + " con matrícula: " + aux.getMatricula() + " se procesa en la casilla " + this.numeroCasilla );
                            //System.out.print("Quedan en espera de la casilla " + this.getNumeroCasilla() + " "+ enEspera.size() + " vehículos: ");
                            //for (IVehiculo ve : enEspera) {
                                //System.out.print(ve.getMatricula() + " ");
                            //}
                            //System.out.println("");
                            Long esperaVehiculo = new Long((Reloj.getInstance().getDate().getTime() - aux.getTime().getTime())/1000);
                            BancoDatos.getBancoDatos().registrar(aux, esperaVehiculo);
                            
                            SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                            HandleFile.getInstance().writeArchivo(Thread.currentThread().getName()+";"+ this.numeroCasilla +";" + aux.getTipo()+ ";"+aux.getTelepeaje()+";"
                                    +aux.getMatricula()+";"+ aux.getEspera()+" seg;"+formato.format(aux.getTime())+";"+formato.format(entradaReal)+";"+ formato.format(Reloj.getInstance().getDate())+";"+ esperaVehiculo);
                            
                        } catch (Exception ex) {
                            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    // Se dehabilita si no tiene mas vehiculos en espera, solo si la casilla no es Default
                    if (!this.esDefault && enEspera.isEmpty()) {
                        this.setHabilitada(false);
                        // dinamico por el uso
                        
                    }
                    
                    enEspera.pollFirst();
                }
                
            }
                accesoCasilla.release();
        } catch (InterruptedException ex) {
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
