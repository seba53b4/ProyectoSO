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
 * 
 * Clase casilla que implementa Runnable. 
 * Encargada de procesar los vehículos que llegan al peaje.
 */
public class Casilla implements Runnable{

    private int numeroCasilla;
    private ConcurrentLinkedDeque<IVehiculo> enEspera; //
    private boolean habilitada;
    private Semaphore accesoCasilla;
    private boolean bloqueada;
    
    /**
     * Cuando hay un evento se bloquea esta casilla
     * @param bloqueada 
     */
    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }
    /**
     * Retorna true si la casilla esta bloqueada por un evento.
     * @return bloqueada
     */
    public synchronized boolean isBloqueada() {
        return bloqueada;
    }

    /**
     * Habilita la casilla. Se vuelve una casilla operativa.
     * @param habilitada 
     */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
    
    /**
     * Obtiene la lista de espera de la casilla.
     * @return enEspera
     */
    public synchronized Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }
    
     /**
     * Retorna true si la casilla esta hablilitada por un evento.
     * @return hablitada
     */
    public boolean isHabilitado() {
        return habilitada ;
    }
    
    /**
     * Crea una nueva casilla con los parámetros
     * @param numCasilla numero de la casilla
     * @param tieneTelepeaje boolean si tiene telepeaje
     * @param habilitada boolean si esta habilitada
     */
    public Casilla(int numCasilla, boolean tieneTelepeaje,boolean habilitada){
        enEspera = new ConcurrentLinkedDeque<IVehiculo>() ;
        habilitada = habilitada;
        numeroCasilla = numCasilla;
        accesoCasilla = new Semaphore(1);
        bloqueada = false;
    }
    
    /**
     * Método run de la casilla.
     * Esta clase provee la principal funcionalidad del sistema, 
     * el procesamiento de los vehículos.
     * Como esta operación es critica ya que el sistema soporta varios hilos
     * (herencia de thread), es clave el proteger la sección critica del 
     * sistema para evitar errores como la facturación y 
     * la solapación de vehículos.
     * esta clase define e implementa un semáforo que controla el acceso a la porción del código
     * considerada como sección critica, básicamente cuando un hilo esta haciendo uso
     * de esa porción del código, el semáforo imposibilita que otro hilo pueda ingresar.
     * 
     */
    @Override
    public void run() {
        
        try {
            accesoCasilla.acquire();
            IVehiculo aux ;
            
            aux =  enEspera.peek();
            
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
                    
                    System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo de tipo " + aux.getTipo() + " con matrícula: " + aux.getMatricula());
                    System.out.print("Quedan en espera de la casilla " + this.getNumeroCasilla() + " "+ enEspera.size() + " vehículos: ");
                    for (IVehiculo ve : enEspera) {
                        System.out.print(ve.getMatricula() + " ");
                    }
                    System.out.println("");
                    Long esperaVehiculo = new Long((Reloj.getInstance().getDate().getTime() - aux.getTime().getTime())/1000);
                    BancoDatos.getBancoDatos().registrar(aux, esperaVehiculo);
                            
                    SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                    HandleFile.getInstance().writeArchivo(Thread.currentThread().getName()+";"+ this.numeroCasilla +";" + aux.getTipo()+ ";"+aux.getTelepeaje()+";"
                            +aux.getMatricula()+";"+ aux.getEspera()+" seg;"+formato.format(aux.getTime())+";"+ formato.format(Reloj.getInstance().getDate())+";"+ esperaVehiculo);
                    
                } catch (Exception ex) {
                    Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // Se dehabilita si no tiene mas vehiculos en espera, solo si la casilla no son las primeras casilla 0 y casilla 1. 
            if (this.numeroCasilla > 1  && enEspera.isEmpty()) {
                this.setHabilitada(false);
            }
            enEspera.remove();
            accesoCasilla.release();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtiene el número de la casilla.
     * @return nroCasilla
     */
    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    
    /**
     * Ingresa un vehículo a la lista de espera
     * @param veh 
     */
    public void addVehiculoEnEspera(IVehiculo veh){
        synchronized(enEspera){
            this.enEspera.add(veh);
        }
    }
    
    /**
     * Obtiene la cantidad de vehículos en la lista de espera.
     * @return listaEnEspera
     */
    public int getCantidadEnEspera(){
        return this.enEspera.size();
    }

    
}
