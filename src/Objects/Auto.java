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
public class Auto implements IVehiculo{

    
    public Auto(){
        
    }
    
    
    @Override
    public String getTipo() {
        return "auto";
    }

    @Override
    public boolean getTelepeaje() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
