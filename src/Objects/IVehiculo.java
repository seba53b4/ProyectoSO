/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.util.Date;

/**
 *
 * @author Seba-OS
 */
public abstract interface IVehiculo extends Comparable<IVehiculo>{
    
    public abstract String getTipo();
    public boolean getTelepeaje();
    public String getMatricula();
    public Date getTime();
    public int getEspera();
    
}
