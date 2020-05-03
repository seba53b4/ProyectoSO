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
public class Vehiculo_Emergencia implements IVehiculo{
    /*
    private Integer tarifa;

    public Integer getTarifa() {
        return tarifa;
    }*/

    public Vehiculo_Emergencia(){
        
    }
    
    @Override
    public String getTipo() {
        return "emergencia";
    }

    @Override
    public boolean getTelepeaje() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
