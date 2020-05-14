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

    public static Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
   
   
  
    
    public Peaje(){
        
        casillas = new Casilla[5];
        casillas[0] = new Casilla(0, true,false);
        casillas[1] = new Casilla(1, true,false);
        
        casillas[2] = new Casilla(2, true,false);
        casillas[3] = new Casilla(3, true,false);
        casillas[4] = new Casilla(4, true,false);
        vehiculos = HandleFile.getInstance().cargaArchivo();
        
    }
    
    public void iniciar() throws InterruptedException {
        
        
        int iter = 0;
        Random generador = new Random(); // generador de numeros random para pruebas, los vehiculos caen en las casillas de manera random.
        
        /*for (IVehiculo veh : vehiculos) {
            casillas[generador.nextInt(4)].addVehiculoEnEspera(veh);
        }*/
        
        IVehiculo veh;
        Casilla elegida;
        
        
        while (!vehiculos.isEmpty()) {
            //System.out.println("Hora proximo reloj: "+ Reloj.getInstance().getHora());
            //System.out.println("Hora proximo vehiculo: "+ HandleFile.getInstance().getFormatoFecha().format(vehiculos.peek().getTime()));
            synchronized (Reloj.getInstance()){
                if (Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                    System.out.println("Hora proximo reloj: "+ Reloj.getInstance().getHora());
                    System.out.println("Hora proximo vehiculo: "+ HandleFile.getInstance().getFormatoFecha().format(vehiculos.peek().getTime()));
                    veh = vehiculos.poll();
                    elegida = siguienteHabilitada();
                    
                    elegida.addVehiculoEnEspera(veh);
                    
                    /*if (elegida.getEnEspera().isEmpty()) {
                        System.out.println("es vacia");
                    } else {
                        System.out.println("no es vacia");
                    }
                    if (!elegida.isHabilitado()) {
                        
                        elegida.setHabilitada(true);
                        elegida.start();
                        
                    }*/
                    
                    
                }
                    
            } 
            
        }
        for (Casilla casilla : casillas) {
            casilla.setHabilitada(true);
            casilla.start();
        }
        
        for (Casilla casilla : casillas) {
            casilla.setHabilitada(false);
            casilla.join();
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
