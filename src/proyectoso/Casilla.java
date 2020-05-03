/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;
import java.util.LinkedList;
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
    private Queue<IVehiculo> enEspera; // 
    private boolean habilitado;
    private boolean telepeaje;
    private Semaphore accesoCasilla;
    
    public Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    
    public Casilla(int numCasilla, boolean tel){
        enEspera = new LinkedList<IVehiculo>() ;
        habilitado = true;
        numeroCasilla = numCasilla;
        telepeaje = tel;
        accesoCasilla = new Semaphore(1);
    }
    
    @Override
    public void run() {
        try {
            // Agarra un vehiculo y lo procesa
            while (!enEspera.isEmpty()) {
                
            
            accesoCasilla.acquire();
            System.out.println("Procesa casilla: " + this.numeroCasilla +" " +enEspera.poll().getTipo());
            // procesaminto
            accesoCasilla.release();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Casilla.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        
    }
    
    public void addVehiculoEnEspera(IVehiculo veh){
        this.enEspera.add(veh);
    }
    
    

    
}
