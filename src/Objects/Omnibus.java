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
public class Omnibus implements IVehiculo{

    public Omnibus(){
        
    }
    
    @Override
    public String getTipo() {
        return "omnibus";
    }
    
}