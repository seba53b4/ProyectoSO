/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;
import Utils.HandleFile;
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
    private boolean habilitada;
    private boolean telepeaje;
    private Semaphore accesoCasilla;
    
    public synchronized Queue<IVehiculo> getEnEspera() { //
        return enEspera;
    }

    public boolean isHabilitado() {
        return habilitada;
    }

    public void setHabilitado(boolean available) {
        this.habilitada = available;
    }
    
    
    public Casilla(int numCasilla, boolean tel){
        enEspera = new LinkedList<IVehiculo>() ;
        habilitada = true;
        numeroCasilla = numCasilla;
        telepeaje = tel;
        accesoCasilla = new Semaphore(1);
    }
    
    @Override
    public void run() {
        try {
            // Agarra un vehiculo y lo procesa
            
            while (!enEspera.isEmpty()) {
                System.out.println("Ingresa  en casilla " + this.numeroCasilla +" el vehiculo: " +enEspera.peek().getTipo());
                
            accesoCasilla.acquire();
            IVehiculo aux = enEspera.poll();
            
            System.out.println("Procesa casilla " + this.numeroCasilla +" el vehiculo: " + aux.getTipo());
            
            BancoDatos.getBancoDatos().incCantidadVehiculos();
            BancoDatos.getBancoDatos().aumentarRecaudacion(aux.getTipo());
            BancoDatos.getBancoDatos().aumentarCostoOperativo(200.0);
            HandleFile.initHandeFile().writeArchivo("En casilla "+ this.numeroCasilla +" paso un " + aux.getTipo() );
            

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
