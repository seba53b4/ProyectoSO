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
public class Vehiculo implements IVehiculo{

    private String tipo;
    private String matricula;
    private boolean telepeaje;
    private Date horaLlegada;
    
    public Vehiculo(String type,String mat,boolean telep,Date time ){
        tipo = type;
        matricula = mat;
        telepeaje = telep;
        horaLlegada = time;
    }
    
    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean getTelepeaje() {
        return telepeaje;
    }

    @Override
    public String getMatricula() {
        return matricula;
    }

    @Override
    public Date getTime() {
        return horaLlegada;
    }
    
}
