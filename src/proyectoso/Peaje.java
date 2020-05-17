/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.Evento;
import Objects.IVehiculo;
import Utils.HandleFile;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Seba-OS
 */
public class Peaje{

    private Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
    public Queue<Thread> hilos;
    public static Peaje peaje;
    public Queue<Evento> eventos;
    public LinkedList<Evento> eventosPasados;
    public static Peaje getInstance(){
        return peaje;
    }
  
    
    public Peaje(){
        
        casillas = new Casilla[5];
        casillas[0] = new Casilla(0, true,false);
        casillas[1] = new Casilla(1, true,false);
        casillas[2] = new Casilla(2, true,false);
        casillas[3] = new Casilla(3, true,false);
        casillas[4] = new Casilla(4, true,false);
        vehiculos = HandleFile.getInstance().cargaArchivo();
        eventos = HandleFile.getInstance().getEventos();
        peaje = this;
        hilos = new LinkedList();
        eventosPasados = new LinkedList<>();
        
    }
    
    public void iniciar() throws InterruptedException {
        
        Thread aux;
        IVehiculo veh;
        casillas[0].setHabilitada(true);
        casillas[1].setHabilitada(true);
        Evento event;
        while (!vehiculos.isEmpty()) {

                if (!eventos.isEmpty()){
                    for (Evento evento : eventos) {
                        if (!eventos.isEmpty() && Reloj.getInstance().getDate().compareTo(eventos.peek().getFechaEvento()) == 0) {
                            
                            event = eventos.poll();
                            casillas[event.getNumeroCasilla()].setBloqueada(true);
                            System.out.println("Fecha evento iniciado "+ event.getFechaEvento()+ " estado casilla "+event.getNumeroCasilla()+" "+casillas[event.getNumeroCasilla()].isBloqueada());
                            eventosPasados.add(event);
                        }
                    }
                }
            
            if (vehiculos.peek() != null && Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                veh = vehiculos.poll();
                // Evaluacion de vehiculos Telepeaje y Emergencia
                if (veh.getTelepeaje() || veh.getTipo().equals("emergencia")) {
                    Casilla vacia = siguienteCasillaEspecial();
                    if (vacia != null) {
                        System.out.println("Parte A ingresa aca el vehiculo " + veh.getMatricula());
                        ingresarVehiculoAEspera(vacia, veh);
                        continue;
                    }
                }
                
                for (int i = 0; i < casillas.length; i++) {
                    
                    
                    
                    if (!casillas[i].isBloqueada()) {
                        
                        if (casillas[i].isHabilitado() && casillas[i].getEnEspera().size()  < 3) {
                            System.out.println("Parte B ingresa aca el vehiculo " + veh.getMatricula());
                            ingresarVehiculoAEspera(casillas[i], veh);
                            break;
                        }
                        if ( i == 4) {
                            System.out.println("Parte C ingresa aca el vehiculo " + veh.getMatricula() + " estado casilla "+ casillas[i].isBloqueada());
                            ingresarVehiculoAEspera(siguienteCasilla(), veh);
                            break;
                        }
                    }
                }
            }
            
            if (!eventosPasados.isEmpty()) {
                    for (Evento eventPasado : eventosPasados) {
                        if (Reloj.getInstance().getDate().compareTo(eventPasado.getFechaFinal()) == 0) {
                            System.out.println("Evento iniciado finaliza "+ eventPasado.toString());
                            casillas[eventPasado.getNumeroCasilla()].setBloqueada(false);
                            eventosPasados.remove(eventPasado);
                        }
                    }
                }
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        HandleFile.getInstance().closeArchivoWriter();
    }
    
    
    public void ingresarVehiculoAEspera(Casilla a, IVehiculo veh){
        
        System.out.println("entra casilla " +a.getNumeroCasilla()+" estado "+ a.isBloqueada() +" el vehiculo "+ veh.getMatricula());
        a.addVehiculoEnEspera(veh);
        Thread nuevoHilo = new Thread(a);
        hilos.add(nuevoHilo);
        nuevoHilo.start();
    }
    
    public Casilla siguienteCasillaEspecial(){
        
        Casilla ret = casillas[0];
        for (int i = 1; i < casillas.length; i++) {
            
            if (!casillas[i].isHabilitado() && !casillas[i].isBloqueada()) {
                return casillas[i];
            }
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() && !casillas[i].isBloqueada() ) {
                ret = casillas[i];
            } 
        }
        return ret;
    }
    public Casilla siguienteCasilla(){
        
        //seleccion de pivote
        Casilla ret = null;
        for (int i = 0; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada()) {
                ret = casillas[i];
                break;
            }
        }
        for (int i = 1; i < casillas.length; i++) {
            System.out.println("Estado de casilla: "+i+ " status "+ casillas[i].isBloqueada());
            if (casillas[i].isBloqueada()) {
                continue;
            }
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera()) {
                ret = casillas[i];
                if (!casillas[i].isHabilitado()) {
                    casillas[i].setHabilitada(true);
                }
            } 
        }
        return ret;
    }
  
    
    
    
}
