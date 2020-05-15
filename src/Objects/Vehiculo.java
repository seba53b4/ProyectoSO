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
    private int espera;
    
    public Vehiculo(String type,String mat,boolean telep,int espera,Date time ){
        tipo = type;
        matricula = mat;
        telepeaje = telep;
        horaLlegada = time;
        this.espera = espera;
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

    @Override
    public int compareTo(IVehiculo t) {
        
        if (this.getTipo().equals("emergencia") && !t.getTipo().equals("emergencia")){
            return -1;
        }
        else if (!this.getTipo().equals("emergencia") && t.getTipo().equals("emergencia")){
            return 1;
        }
        else{
            return 0;
        }
    }
    
}
