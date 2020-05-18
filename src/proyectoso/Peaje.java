/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.Evento;
import Objects.IVehiculo;
import Utils.HandleFile;
import java.text.SimpleDateFormat;
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
        
//
//            if (Reloj.getInstance().getDate().after(date)) {
//            
//            }

            if (!eventos.isEmpty()){
                    for (Evento evento : eventos) {
                        if (!eventos.isEmpty() && Reloj.getInstance().getDate().compareTo(eventos.peek().getFechaEvento()) == 0) {
                            SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                            event = eventos.poll();
                            casillas[event.getNumeroCasilla()].setBloqueada(true);
                            System.out.println("*********************");
                            System.out.println("Ocurre un evento en la casilla " + event.getNumeroCasilla());
                            System.out.println("Se bloquea desde " + formato.format(event.getFechaEvento()) + " hasta " + formato.format(event.getFechaFinal()));
                            //System.out.println("Fecha evento iniciado "+ event.getFechaEvento()+ " estado casilla "+event.getNumeroCasilla()+" "+casillas[event.getNumeroCasilla()].isBloqueada());
                            eventosPasados.add(event);
                            System.out.println("*********************");
                        }
                    }
                }
            
            if (vehiculos.peek() != null && Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                veh = vehiculos.poll();
                // Evaluacion de vehiculos Telepeaje y Emergencia
                if (veh.getTelepeaje() || veh.getTipo().equals("emergencia")) {
                    Casilla vacia = siguienteCasillaEspecial();
                    if (vacia != null) {
                        System.out.println("Ingresa el vehiculo " + veh.getMatricula() + " al peaje planificador.");
                        System.out.println("Es telepeaje o emergencia.");
                        System.out.println("Es telepeaje? " + veh.getTelepeaje());
                        System.out.println("Es emergencia? " + veh.getTipo());
                        ingresarVehiculoAEspera(vacia, veh);
                        continue;
                    }
                }
            

            for (int i = 0; i < casillas.length; i++) {
                
                
                    
                    if (!casillas[i].isBloqueada()) {
                        
                        if (casillas[i].isHabilitado() && casillas[i].getEnEspera().size()  < 3) {
                            ingresarVehiculoAEspera(casillas[i], veh);
                            break;
                        }
                        if ( i == 4) {

                            ingresarVehiculoAEspera(siguienteCasilla(), veh);
                            break;
                        }
                    }
                
            }
            
            if (!eventosPasados.isEmpty()) {
                    for (Evento eventPasado : eventosPasados) {
                        if (Reloj.getInstance().getDate().compareTo(eventPasado.getFechaFinal()) == 0) {
                            System.out.println("Finaliza el evento "+ eventPasado.toString());
                            casillas[eventPasado.getNumeroCasilla()].setBloqueada(false);
                            eventosPasados.remove(eventPasado);
                        }
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
        
        System.out.println("El vehiculo " + veh.getMatricula() + " ingresa a la lista de espera de la " + a.getNumeroCasilla());
        a.addVehiculoEnEspera(veh);
        Thread nuevoHilo = new Thread(a);
        hilos.add(nuevoHilo);
        nuevoHilo.start();
    }
    
    public Casilla siguienteCasillaEspecial(){
        
        // Si alguna de las primeras 2 estan vacias 
        for (int i = 0; i < 2; i++) {
            if (casillas[i].getCantidadEnEspera() == 0) {
                return casillas[i];
            }
        }
        
        Casilla ret = casillas[0];
        for (int i = 1; i < casillas.length; i++) {
            
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() && !casillas[i].isBloqueada() ) {
                ret = casillas[i];
            } 
            if (!casillas[i].isHabilitado() && !casillas[i].isBloqueada()) {
                return casillas[i];
            }
        }
        return ret;
    }
    public Casilla siguienteCasilla(){
        
        // Selección de pivote
        Casilla ret = null;
        for (int i = 0; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada()) {
                ret = casillas[i];
                break;
            }
        }
        // Selección de la casilla
        for (int i = 1; i < casillas.length; i++) {
            System.out.println("Pregunto la casilla " + casillas[i].getNumeroCasilla() + " está bloqueada? "+ casillas[i].isBloqueada());
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
