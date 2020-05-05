/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.Camion;
import Objects.IVehiculo;
import Objects.Auto;
import Objects.Vehiculo_Emergencia;
import Utils.HandleFile;
import java.util.ArrayList;

/**
 *
 * @author Seba-OS
 */
public class ProyectoSO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
        HandleFile.initHandeFile();
        Peaje peaje = new Peaje();
        BancoDatos.initBancoDatos(115.0, 195.0, 195.0);
        peaje.iniciar();
        
        System.out.println("Cantidad de vehiculos: " + BancoDatos.getBancoDatos().getCantidadVehiculos());
        System.out.println("Costo operativo: " + BancoDatos.getBancoDatos().getCostoOperativo());
        System.out.println("Recaudacion: " + BancoDatos.getBancoDatos().getRecaudacion());
        
        
       /* ArrayList<IVehiculo> vehiculos = new ArrayList<>();
        
        vehiculos.add(new Camion());
        vehiculos.add(new Camion());
        vehiculos.add(new Auto());
        vehiculos.add(new Vehiculo_Emergencia());
        
        for (IVehiculo veh : vehiculos) {
            System.out.println(veh.getTipo());
        }
        */
        
    }
    
}
