/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

/**
 *
 * @author Seba-OS
 */
public class BancoDatos {
    
    
    private static BancoDatos bancoDatos;
    
    private int cantidadVehiculos;
    private Double costoOperativo;

    private Double recaudacion;
    private final Double tarifaAuto;
    private final Double tarifaOmnibus;
    private final Double tarifaCamion;
    
    public static BancoDatos initBancoDatos(Double tarifaCar, Double tarifaTruck, Double tarifaBus){
        if (bancoDatos == null) {
            bancoDatos = new BancoDatos(tarifaCar, tarifaTruck, tarifaBus);
        }
        return bancoDatos;
    }
    
    public static BancoDatos getBancoDatos(){
        return bancoDatos;
    }
    
    public BancoDatos(Double tarifaCar, Double tarifaTruck, Double tarifaBus){
        costoOperativo = 0.0;
        recaudacion = 0.0;
        cantidadVehiculos = 0;
        tarifaAuto = tarifaCar;
        tarifaCamion = tarifaTruck;
        tarifaOmnibus = tarifaBus;
    }
    
    public int getCantidadVehiculos() {
        return cantidadVehiculos;
    }

    public Double getCostoOperativo() {
        return costoOperativo;
    }

    public Double getRecaudacion() {
        return recaudacion;
    }
    
    public synchronized void aumentarRecaudacion(String tipo){
        this.recaudacion += getTarifa(tipo);
    }
    
    
    public Double getTarifa(String tipo){
        
        switch (tipo.toLowerCase()) {
            case "auto": 
                     return tarifaAuto;
                     
            case "camion": 
                     return tarifaCamion;
                     
            case "omnibus": 
                     return tarifaOmnibus;
                     
            default: return 0.0;
        }
    }
    
    public synchronized void incCantidadVehiculos() {
        this.cantidadVehiculos = cantidadVehiculos + 1;
    }
    
    
    public synchronized void aumentarCostoOperativo(Double costo) {
        this.costoOperativo = costoOperativo + costo;
    }
    
    
    
    
    
}
