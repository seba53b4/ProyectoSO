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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seba-OS
 */
public class Peaje extends Thread{

    IVehiculo[] vehiculos = new IVehiculo[3];//archivo.lenght];
    
    ArrayList<Casilla> casillas;
   
    
    @Override
    public void run() {
        
        
        int iter = 0;
        
        for (IVehiculo veh : vehiculos) {
            
            System.out.println("Ingresa vehiculo desde peaje en casilla 1 " + veh.getTipo() );
            casillas.get(0).addVehiculoEnEspera(veh);
            casillas.get(1).addVehiculoEnEspera(veh);
            
         }
        
        for (Casilla casilla : casillas) {
            casilla.start();
        }
        
        
        for (Casilla casilla : casillas) {
            try {
                casilla.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Peaje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        HandleFile.initHandeFile().closeArchivoWriter();
        
        
        /*
        while (casillas.get(0).isAlive() && casillas.get(1).isAlive()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Peaje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
    
        
    }
    
    public Peaje(){
        
        casillas = new ArrayList<>();
        
        casillas.add(new Casilla(1, true));
        casillas.add(new Casilla(2, false));
        casillas.add(new Casilla(3, false));
        casillas.add(new Casilla(4, true));
        casillas.add(new Casilla(5, true));
        vehiculos[0] = new Auto();
        vehiculos[1] = new Camion();
        vehiculos[2] = new Omnibus();
        //vehiculos[3] = new Auto();
        
    }
    
    
    
}
