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
        accesoCasilla = new Semaphore(1, true);
        monitorEntrada = new MonitorEntrada(false);
        
        cantEspera = 0;

    }
    
    
    
    
    @Override
    public void run() {
        try {

            
//            if (!accesoCasilla.tryAcquire()) {
//                System.out.println("Entra el thread "+ Thread.currentThread().getName()+ " estaba trancadaso");
//            } else {
//                System.out.println("Entra el thread "+ Thread.currentThread().getName()+ " libre");
//            }
            accesoCasilla.acquire();
            IVehiculo aux ;
          
                aux =  enEspera.poll();
                Date horaSalida;
                
                    System.out.println(Thread.currentThread().getName() +" hora entrada " + HandleFile.getInstance().getFormatoFecha().format(aux.getTime()));
                    int wait = aux.getTime().getSeconds()+aux.getEspera();
                    horaSalida = (Date) aux.getTime().clone();
                    horaSalida.setSeconds(wait);
                    System.out.println(Thread.currentThread().getName() +" Hora salida " + HandleFile.getInstance().getFormatoFecha().format(horaSalida));
                
                while (Reloj.getInstance().getDate().compareTo(horaSalida) != 0) {
                    //System.out.println("espera date "+ Reloj.getInstance().getHora());
                    
                    if (Reloj.getInstance().getDate().compareTo(horaSalida) == 0) {
                        synchronized(Reloj.getInstance().getDate()){
                            System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo: " + aux.getTipo() + " de mat: " + aux.getMatricula()+ " hora: "+ Reloj.getInstance().getHora());
                            BancoDatos.getBancoDatos().incCantidadVehiculos();
                            BancoDatos.getBancoDatos().aumentarRecaudacion(aux.getTipo());
                            BancoDatos.getBancoDatos().aumentarCostoOperativo(45.0);
                            
                            HandleFile.getInstance().writeArchivo("["+Thread.currentThread().getName()+"]"+" En casilla ["+ this.numeroCasilla +"] paso un " + aux.getTipo()+ " Matricula "
                                    + "["+aux.getMatricula()+"] la hora "+ Reloj.getInstance().getHora());
                        }
                        break;
                    }
                
                
            }
            accesoCasilla.release();
                     
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
