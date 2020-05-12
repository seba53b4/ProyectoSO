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
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
// uso solo para pruebas
import java.util.Random;

/**
 *
 * @author Seba-OS
 */
public class Peaje{

    private Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
   
   
  
    
    public Peaje(){
        
        casillas = new Casilla[5];
        casillas[0] = new Casilla(0, true,true);
        casillas[1] = new Casilla(1, true,true);
        casillas[2] = new Casilla(2, true,false);
        casillas[3] = new Casilla(3, true,false);
        casillas[4] = new Casilla(4, true,false);
        vehiculos = HandleFile.initHandeFile().cargaArchivo();
        
    }
    
    public void iniciar() {
        
        
        int iter = 0;
        Random generador = new Random(); // generador de numeros random para pruebas, los vehiculos caen en las casillas de manera random.
        
        /*for (IVehiculo veh : vehiculos) {
            casillas[generador.nextInt(4)].addVehiculoEnEspera(veh);
        }*/
        
        IVehiculo veh;
        Casilla elegida;
        while (!vehiculos.isEmpty()) {
            if (Reloj.hora() >= vehiculos.peek().DAte){
                veh = vehiculos.poll();
                elegida = siguienteHabilitada();
                elegida.addVehiculoEnEspera(veh);
                if (!elegida.isAlive()) {
                    elegida.start();
                    elegida.setHabilitada(true);
                }
            }
            
            
            
            
            
        }
        /*while lsita de veh no vacia 
                if (reloj.tiempo >= veh.peek)
                    
                    if emeregencia 
                            ver si hay casilla libre */
                                    
        // aca deberia ir la logica de controlar las vias habilitadas y los eventos de emergencia. Podria ser con otra clase planificacdor o en esta misma clase.
        // A su vez las casillas no deberian finalizar si quedan sus listas enEspera vacias, si no terminan antes que se vuelva a mandar otros vehiculos.
        //Nota. 1- Todavia no se si esta clase deberia ser un thread..
        //      2- Falta hacer el Reloj
        
        
        
        for (Casilla casilla : casillas) {
            try {
                casilla.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Peaje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        HandleFile.initHandeFile().closeArchivoWriter();
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
