/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.IVehiculo;

/**
 *
 * @author Seba-OS
 * 
 * Clase que se encarga del monitoreo de indicadores del sistema.
 * Cantidad de vehículos procesados.
 * Costo operativo
 * Tiempos de espera
 * Recaudación total
 * Tarifas
 * 
 */
public class BancoDatos {
    
    private static BancoDatos bancoDatos;
    private int cantidadVehiculos;
    private Double costoOperativo;
    private Double sumaEsperas;
    private Double recaudacion;
    private Double tarifaAuto;
    private Double tarifaOmnibus;
    private Double tarifaCamion;
    
    private Double tarifaAutoNormal;
    private Double tarifaCamionNormal;
    private Double tarifaOmnibusNormal;
    
    /**
     * Crea un nuevo banco de datos con tarifas especificadas por parámetro.
     * @param tarifaCar
     * @param tarifaTruck
     * @param tarifaBus
     * @return 
     */
    public static BancoDatos initBancoDatos(Double tarifaCar, Double tarifaTruck, Double tarifaBus){
        if (bancoDatos == null) {
            bancoDatos = new BancoDatos(tarifaCar, tarifaTruck, tarifaBus);
        }
        return bancoDatos;
    }
    
    /**
     * Obtiene el banco de datos
     * @return bancoDatos
     */
    public static BancoDatos getBancoDatos(){
        return bancoDatos;
    }
    
    
    /**
     * Aumenta la tarifa en horas pico.
     */
    public synchronized void setAumentarTarifasHoraPico(){
        
        tarifaAuto += (tarifaAuto * 0.1);
        tarifaCamion += (tarifaCamion * 0.1);
        tarifaOmnibus += (tarifaOmnibus * 0.1);
        
    }   
    
    /**
     * Vuelve la tarifa a la normalidad.
     */
    public synchronized void setTarifaNormal(){
        
        tarifaAuto = tarifaAutoNormal;
        tarifaCamion = tarifaCamionNormal;
        tarifaOmnibus = tarifaOmnibusNormal;
        
    }
    
    /**
     * Crea un nuevo banco de datos
     * @param tarifaCar
     * @param tarifaTruck
     * @param tarifaBus 
     */
    public BancoDatos(Double tarifaCar, Double tarifaTruck, Double tarifaBus){
        costoOperativo = 0.0;
        recaudacion = 0.0;
        cantidadVehiculos = 0;
        tarifaAuto = tarifaCar;
        tarifaCamion = tarifaTruck;
        tarifaOmnibus = tarifaBus;
        sumaEsperas = 0.0;
        tarifaAutoNormal = tarifaCar;
        tarifaCamionNormal = tarifaTruck;
        tarifaOmnibusNormal = tarifaBus;
        bancoDatos = this;
        
    }
    
    /**
     * Limpia el banco de datos.
     */
    public void clean(){
        costoOperativo = 0.0;
        recaudacion = 0.0;
        cantidadVehiculos = 0;
        tarifaAuto = tarifaAutoNormal;
        tarifaCamion = tarifaCamionNormal;
        tarifaOmnibus = tarifaOmnibusNormal;
        sumaEsperas = 0.0;
    }
    
    /**
     * Obtiene la cantidad de vehículos procesados
     * @return cantidadVehiculos
     */
    public int getCantidadVehiculos() {
        return cantidadVehiculos;
    }

    /**
     * Obtiene el costoOperativo por los vehículos procesados en esta ejecución.
     * @return costoOperativo
     */
    public Double getCostoOperativo() {
        return costoOperativo;
    }

    /**
     * Obtiene el recaudacion por los vehículos procesados en esta ejecución.
     * @return recaudacion
     */
    public Double getRecaudacion() {
        return recaudacion;
    }
    
    /**
     * Aumenta la recaudación con respecto a las tarifas actuales.
     * @param tipo
     */
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
    
    /**
     * Registrar los datos del vehículo procesado
     * @param veh
     * @param espera 
     */
    public synchronized void registrar(IVehiculo veh, Long espera){
       
        incCantidadVehiculos();
        aumentarRecaudacion(veh.getTipo());
        aumentarCostoOperativo(45.0);
        aumentarSumaEsperas(espera.doubleValue());
    }
    
    /**
     * Inicializar la cantidad de vehículos
     */
    public synchronized void incCantidadVehiculos() {
        this.cantidadVehiculos = cantidadVehiculos + 1;
    }
    
    /**
     * Aumentar el costo operativo
     * @param costo 
     */
    public void aumentarCostoOperativo(Double costo) {
        this.costoOperativo = costoOperativo + costo;
    }
    
    /**
     * Aumentar la espera total
     * @param espera 
     */
    public synchronized void aumentarSumaEsperas(Double espera) {
        this.sumaEsperas += espera;
    }
    
    /**
     * Obtener el promedio de espera de los vehículos
     * @return promedio
     */
    public Double getPromedioEspera(){
        return sumaEsperas/this.cantidadVehiculos;
    }
    
    
    
}
