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
// uso solo para pruebas
import java.util.Random;

/**
 *
 * @author Seba-OS
 */
public class Peaje extends Thread{

    private ArrayList<IVehiculo> vehiculos;//archivo.lenght];
    private ArrayList<Casilla> casillas;
   
    @Override
    public void run() {
        
        
        int iter = 0;
        Random generador = new Random(); // generador de numeros random para pruebas, los vehiculos caen en las casillas de manera random.
        
        for (IVehiculo veh : vehiculos) {
            casillas.get(generador.nextInt(5)).addVehiculoEnEspera(veh);
        }
        
        for (Casilla casilla : casillas) {
            casilla.start();
        }
        
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
    
    public Peaje(){
        
        casillas = new ArrayList<>();
        casillas.add(new Casilla(1, true));
        casillas.add(new Casilla(2, false));
        casillas.add(new Casilla(3, false));
        casillas.add(new Casilla(4, true));
        casillas.add(new Casilla(5, true));
        vehiculos = HandleFile.initHandeFile().cargaArchivo();
        
    }
    
    
    
}
