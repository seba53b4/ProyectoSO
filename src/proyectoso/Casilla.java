/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;
import Utils.HandleFile;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Seba-OS
 */
public class Casilla extends Thread{

    private int numeroCasilla;
    private PriorityQueue<IVehiculo> enEspera; //
    private boolean habilitada;
    
   

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
    private boolean telepeaje;
    private Semaphore accesoCasilla;
    
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
        accesoCasilla = new Semaphore(1);
        
    }
    
    @Override
    public void run() {
        try {
            // Agarra un vehiculo y lo procesa
            
                
                if (enEspera.isEmpty()) {
                    System.out.println("en espera vacia casilla"+ this.numeroCasilla);
                } else {
                    System.out.println("en espera con elem casilla "+ this.numeroCasilla+ " elem "+ enEspera.size());
                }
                while (!enEspera.isEmpty()) {
                    System.out.println("Ingresa  en casilla " + this.numeroCasilla +"con lista espera " +this.enEspera.size()+" el vehiculo: " +enEspera.peek().getTipo()+ "de mat: " + enEspera.peek().getMatricula());
                    accesoCasilla.acquire();
                    IVehiculo aux = enEspera.poll();
                    //sleep(500);
                    System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo: " + aux.getTipo() + "de mat: " + aux.getMatricula());
                    BancoDatos.getBancoDatos().incCantidadVehiculos();
                    BancoDatos.getBancoDatos().aumentarRecaudacion(aux.getTipo());
                    BancoDatos.getBancoDatos().aumentarCostoOperativo(45.0);
                    synchronized(Reloj.getInstance()){
                        System.out.println("hora al pasar"+ Reloj.getInstance().getHora());
                        HandleFile.getInstance().writeArchivo("["+Thread.currentThread().getName()+"]"+" En casilla ["+ this.numeroCasilla +"] paso un " + aux.getTipo()+ " Matricula "
                                + "["+aux.getMatricula()+"] la hora "+ Reloj.getInstance().getHora());
                    }
                    accesoCasilla.release();
                }
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    
    public void addVehiculoEnEspera(IVehiculo veh){
        this.enEspera.add(veh);
    }
    
    public int getCantidadEnEspera(){
        return this.enEspera.size();
    }

    
}
