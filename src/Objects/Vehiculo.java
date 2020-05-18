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
 * Poseen las características y métodos del vehículo.
 */
public class Vehiculo implements IVehiculo{

    private String tipo;
    private String matricula;
    private boolean telepeaje;
    private Date horaLlegada;
    private int espera;
    
    /**
     * Get tiempo de espera de lo que demora en la casilla al ser procesado.
     * @return espera
     */
    public int getEspera() {
        return espera;
    }
    
    /**
     * Crear un nuevo vehículo
     * @param type
     * @param mat
     * @param telep
     * @param espera
     * @param time 
     */
    public Vehiculo(String type,String mat,boolean telep,int espera,Date time ){
        tipo = type;
        matricula = mat;
        telepeaje = telep;
        horaLlegada = time;
        this.espera = espera;
    }
    
    /**
     * Obtener el tipo de vehículo
     * @return tipo
     */
    @Override
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene si el vehículo cuenta con telepeaje.
     * @return telepeaje
     */
    @Override
    public boolean getTelepeaje() {
        return telepeaje;
    }

    /**
     * Obtiene la matrícula del vehículo
     * @return matricula
     */
    @Override
    public String getMatricula() {
        return matricula;
    }

    /**
     * Obtiene la hora de llegada.
     * @return horaLlegada
     */
    @Override
    public Date getTime() {
        return horaLlegada;
    }

    /**
     * Método que compara dos tipos de vehículos.
     * @param t tipo de vehículo
     * @return 
     */
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
