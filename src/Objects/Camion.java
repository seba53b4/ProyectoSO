/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author Seba-OS
 */
public class Camion implements IVehiculo{
    
    
    public Camion(){
        
    }
    
    @Override
    public String getTipo() {
        return "camion";
    }
    
}
