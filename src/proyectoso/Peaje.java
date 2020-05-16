/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.Auto;
import Objects.Camion;
import Objects.IVehiculo;
import Objects.Omnibus;
import Utils.HandleFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
// uso solo para pruebas
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Seba-OS
 */
public class Peaje{

    private Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
    //private  PriorityQueue<IVehiculo>[] enEspera;
    
    public Semaphore semaforo = new Semaphore(1);
    public Queue<Thread> hilos;
    
    
    public static Peaje peaje;
   
    public static Peaje getInstance(){
        return peaje;
    }
  
    
    public Peaje(){
        
        casillas = new Casilla[5];
        casillas[0] = new Casilla(0, true,false);
        casillas[1] = new Casilla(1, true,false);
        casillas[2] = new Casilla(2, true,false);
        casillas[3] = new Casilla(3, true,false);
        casillas[4] = new Casilla(4, true,false);

        vehiculos = HandleFile.getInstance().cargaArchivo();
        
    

        
       

        
        peaje = this;
        hilos = new LinkedList();
        
    }
    
    public synchronized IVehiculo getHead(){
        return this.vehiculos.poll();
    }
    
    public void iniciar() throws InterruptedException {
        
        
        Thread aux;
        IVehiculo veh;
        casillas[0].setHabilitada(true);
        casillas[1].setHabilitada(true);
        
        while (!vehiculos.isEmpty()) {
            
                 
                if (vehiculos.peek() != null && Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                        
                        veh = vehiculos.poll();
                        
                        if (veh.getTelepeaje() || veh.getTipo().equals("emergencia")) {
                            Casilla vacia = siguienteCasilla();
                            if (vacia != null) {
                                ingresarVehiculoAEspera(vacia, veh);
                                continue;
                            }
                        }
                        
                        for (Casilla casilla : casillas) {
                            if (casilla.isHabilitado() && casilla.getEnEspera().size()  < 3) {
                                
                                ingresarVehiculoAEspera(casilla, veh);
                                continue;
                            }
                        }
                        /*
                        if (casillas[0].isHabilitado() && casillas[0].getEnEspera().size()  < 3) {
                            
                            ingresarVehiculoAEspera(casillas[0], veh);
                            continue;
                        }
                        
                        if (casillas[1].isHabilitado() && casillas[1].getEnEspera().size()  < 3) {
                            ingresarVehiculoAEspera(casillas[1], veh);
                            continue;
                        }
                        
                        if (casillas[2].isHabilitado() && casillas[2].getEnEspera().size()  < 3) {
                            ingresarVehiculoAEspera(casillas[2], veh);
                            continue;
                        }
                        
                        if (casillas[3].isHabilitado() && casillas[3].getEnEspera().size()  < 3) {
                            ingresarVehiculoAEspera(casillas[3], veh);
                            continue;
                        }
                        if (casillas[4].isHabilitado() && casillas[4].getEnEspera().size()  < 3) {
                            ingresarVehiculoAEspera(casillas[4], veh);
                            continue;
                        }*/
            }
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        HandleFile.getInstance().closeArchivoWriter();
    }
    
    
    public void ingresarVehiculoAEspera(Casilla a, IVehiculo veh){
        
        System.out.println("entra casilla " +a.getNumeroCasilla()+ " el vehiculo "+ veh.getMatricula());
        a.addVehiculoEnEspera(veh);
        Thread nuevoHilo = new Thread(a);
        hilos.add(nuevoHilo);
        nuevoHilo.start();
    }
    
    public Casilla siguienteCasilla(){
        
        Casilla ret = casillas[0];
        for (int i = 1; i < casillas.length; i++) {
            
            if (!casillas[i].isHabilitado()) {
                return casillas[i];
            }
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() ) {
                ret = casillas[i];
            } 
            
        }
        
        return ret;
    }
  
    
    
    
}
