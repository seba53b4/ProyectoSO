/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;
import Utils.HandleFile;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Seba-OS
 */
public class Casilla implements Runnable{

    private int numeroCasilla;
    private PriorityQueue<IVehiculo> enEspera; //
    private boolean habilitada;

    public MonitorEntrada monitorEntrada;
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
    
    public MonitorEntrada getMonitor(){
        return monitorEntrada;
    }
    
    public boolean getEstado(){
        return monitorEntrada.getHabilitada();
    }
    
  


    public synchronized void setHabilitada(boolean habilitada) {
        this.monitorEntrada.setEstado(habilitada);
    }
    
    public synchronized Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }

    public boolean isHabilitado() {
        return habilitada ;
    }
    
    public Casilla(int numCasilla, boolean tel,boolean av){
        enEspera = new PriorityQueue<IVehiculo>() ;
        habilitada = av;
        numeroCasilla = numCasilla;
        telepeaje = tel;
        accesoCasilla = new Semaphore(0);
        monitorEntrada = new MonitorEntrada(false);
        
        cantEspera = 0;

    }
    
    
    
    
    @Override
    public void run() {
        try {

            
            
//            monitorEntrada.setEstado(false);
                    
//                    System.out.println("en espera semaforo " + accesoCasilla.getQueueLength() + " soy el hilo " + Thread.currentThread().getName());
//                    accesoCasilla.acquire();
                    synchronized(monitorEntrada){
                        monitorEntrada.evaluarEstado();
                    }
                    this.monitorEntrada.setEstado(false);
                    System.out.println("Thread "+ Thread.currentThread().getName()+ " entro en casilla "+ this.getNumeroCasilla());

                    IVehiculo aux =  enEspera.poll();//this.aProcesar;
                    //Thread.sleep(500);
                    
                    System.out.println("Hora entrada" + Reloj.getInstance().getHora());
                    int wait = Reloj.getInstance().getDate().getSeconds()+aux.getEspera();
                    Date horaSalida = Reloj.getInstance().getDate();
                    horaSalida.setSeconds(wait);

                    System.out.println("Hora de salida "+horaSalida.toString());


                    if (Reloj.getInstance().getDate().compareTo(horaSalida) == 0) {
                        System.out.println("ACAAAAAA paso ansu fati " + Reloj.getInstance().getHora());
                        //System.out.println("Ingresa  en casilla " + this.numeroCasilla +"con lista espera " +this.enEspera.size()+" el vehiculo: " +enEspera.peek().getTipo()+ "de mat: " + enEspera.peek().getMatricula());
                        System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo: " + aux.getTipo() + "de mat: " + aux.getMatricula());
                        BancoDatos.getBancoDatos().incCantidadVehiculos();
                        BancoDatos.getBancoDatos().aumentarRecaudacion(aux.getTipo());
                        BancoDatos.getBancoDatos().aumentarCostoOperativo(45.0);
                        // accesoCasilla.release();
                    synchronized(Reloj.getInstance()){
                        HandleFile.getInstance().writeArchivo("["+Thread.currentThread().getName()+"]"+" En casilla ["+ this.numeroCasilla +"] paso un " + aux.getTipo()+ " Matricula "
                                + "["+aux.getMatricula()+"] la hora "+ Reloj.getInstance().getHora());
                    }
                }


                //accesoCasilla.release();
            //                monitorEntrada.setEstado(true);
            //                synchronized(monitorEntrada){
            //                    monitorEntrada.evaluarEstado();
            //                }
                    //accesoCasilla.release();
                     synchronized(monitorEntrada){
                        monitorEntrada.evaluarEstado();
                    }
                    this.monitorEntrada.setEstado(true);
                    
            
            
        } catch (Exception ex) {
            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    
    public synchronized void addVehiculoEnEspera(IVehiculo veh){
        this.enEspera.add(veh);
    }
    
    public int getCantidadEnEspera(){
        return this.enEspera.size();
    }

    
}
