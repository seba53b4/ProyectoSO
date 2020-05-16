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
import java.util.concurrent.Semaphore;

/**
 *
 * @author Seba-OS
 */
public class Peaje{

    private Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
    private  PriorityQueue<IVehiculo>[] enEspera;
    public Semaphore semaforo = new Semaphore(1);
    public Queue<Thread> hilos;
    
    
    public static Peaje peaje;
   
    public static Peaje getInstance(){
        return peaje;
    }
  
    
    public Peaje(){
        
        casillas = new Casilla[5];
        casillas[0] = new Casilla(0, true,true);
        casillas[1] = new Casilla(1, true,true);
        casillas[2] = new Casilla(2, true,false);
        casillas[3] = new Casilla(3, true,false);
        casillas[4] = new Casilla(4, true,false);

        vehiculos = HandleFile.getInstance().cargaArchivo();
        
    

        
        enEspera = new PriorityQueue[5];
        
        for (int i = 0; i < enEspera.length; i++) {
            enEspera[i] = new PriorityQueue<>();
        }
        
       
        
        peaje = this;
        hilos = new LinkedList();
        
    }
    
    public synchronized IVehiculo getHead(){
        return this.vehiculos.poll();
    }
    
    public void iniciar() throws InterruptedException {
        
        casillas[0].setHabilitada(true);
        casillas[1].setHabilitada(true);
        casillas[2].setHabilitada(true);
        casillas[3].setHabilitada(true);
        casillas[4].setHabilitada(true);
        Thread aux;
//            System.out.println("Hora reloj: "+ Reloj.getInstance().getHora());
//            System.out.println("Hora proximo vehiculo: "+ HandleFile.getInstance().getFormatoFecha().format(vehiculos.peek().getTime()));
        IVehiculo veh;
        while (!vehiculos.isEmpty()) {
            synchronized (Reloj.getInstance()){
                 
                if (vehiculos.peek() != null && Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                      veh = vehiculos.poll();
//                    System.out.println("Hora proximo reloj: "+ Reloj.getInstance().getHora());
//                    System.out.println("Hora proximo vehiculo: "+ HandleFile.getInstance().getFormatoFecha().format(vehiculos.peek().getTime()));

                    
                        if (casillas[0].getEstado() && casillas[4].getCantEspera() < 3) {
                           System.out.println("entra casilla 0 el vehiculo "+ veh.getMatricula());
                            casillas[0].addVehiculoEnEspera(veh);
                            aux = new Thread(casillas[0]);
                            hilos.add(aux);
                            aux.start();
                            continue;
                        }
                    
                        if (casillas[1].getEstado() && casillas[4].getCantEspera() < 3) {
                           System.out.println("entra casilla 1 el vehiculo "+ veh.getMatricula());
                            casillas[1].addVehiculoEnEspera(veh);
                            aux = new Thread(casillas[1]);
                            hilos.add(aux);
                            aux.start();
                            continue;
                        }
                    //}
                        if (casillas[2].getEstado() && casillas[4].getCantEspera() < 3) {
                           System.out.println("entra casilla 2 el vehiculo "+ veh.getMatricula());
                            casillas[2].addVehiculoEnEspera(veh);
                            aux = new Thread(casillas[2]);
                            hilos.add(aux);
                            aux.start();
                            continue;
                        }
                    //}
                        if (casillas[3].getEstado() && casillas[4].getCantEspera() < 3) {
                           System.out.println("entra casilla 3 el vehiculo "+ veh.getMatricula());
                            casillas[3].addVehiculoEnEspera(veh);
                            aux = new Thread(casillas[3]);
                            hilos.add(aux);
                            aux.start();
                            continue;
                        }
                    //}
                        if (casillas[4].getEstado() && casillas[4].getCantEspera() < 3) {
                           System.out.println("entra casilla 4 el vehiculo "+ veh.getMatricula());
                            casillas[4].addVehiculoEnEspera(veh);
                            aux = new Thread(casillas[4]);
                            hilos.add(aux);
                            aux.start();
                            continue;
                        }
                    //}
                }
            }
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        HandleFile.getInstance().closeArchivoWriter();
    }
    
    public Casilla siguienteHabilitada(){
        
        Casilla ret = casillas[0];
        for (int i = 1; i < casillas.length; i++) {
            
            System.out.println("ret "+ ret.getCantidadEnEspera()+" casilla["+i+"] tiene "+casillas[i].getCantidadEnEspera()+ " ");
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() ) {
                ret = casillas[i];
            } 
            
        }
        
        System.out.println("Casilla elegida " + ret.getNumeroCasilla());
       
        
        return ret;
    }
  
    
    
    
}
