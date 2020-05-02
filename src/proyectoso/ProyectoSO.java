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
import java.util.ArrayList;

/**
 *
 * @author Seba-OS
 */
public class ProyectoSO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ArrayList<IVehiculo> vehiculos = new ArrayList<>();
        
        vehiculos.add(new Camion());
        vehiculos.add(new Camion());
        vehiculos.add(new Auto());
        vehiculos.add(new Vehiculo_Emergencia());
        
        for (IVehiculo veh : vehiculos) {
            System.out.println(veh.getTipo());
        }
        
        
    }
    
}
