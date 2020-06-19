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
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Seba-OS
 */
public class Peaje{

    private Queue<IVehiculo> vehiculos;//archivo.lenght];
    private Casilla[] casillas;
    public static Peaje peaje;
    public Queue<Thread> hilos;
    public Queue<Thread> hilosEventos;
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
        hilosEventos = new LinkedList();
        
        eventosPasados = new LinkedList<>();
        
    }
    
    public void iniciar() throws InterruptedException {
        
        Thread aux;
        IVehiculo veh;
        
        casillas[0].esDefault();
        casillas[1].esDefault();
        casillas[0].setHabilitada(true);
        casillas[1].setHabilitada(true);
        
        Evento event;
        
        while (!vehiculos.isEmpty()) {
            
            //            if ((Reloj.getInstance().getDate().getHours() == 12 || Reloj.getInstance().getDate().getHours() == 18) && Reloj.getInstance().getDate().getSeconds() >= 0) {
            //                BancoDatos.getBancoDatos().setAumentarTarifasHoraPico();
            //            }
            //
            //            if ((Reloj.getInstance().getDate().getHours() == 13 || Reloj.getInstance().getDate().getHours() == 19) && Reloj.getInstance().getDate().getSeconds() > 0) {
            //                BancoDatos.getBancoDatos().setTarifaNormal();
            //            }
            
            if (!eventos.isEmpty() && Reloj.getInstance().getDate().compareTo(eventos.peek().getFechaEvento()) == 0) {
                SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                event = eventos.poll();
                Thread th = new Thread(event);
                hilosEventos.add(th);
                th.start();
                casillaProcesoEvento(event);
                
                
//                System.out.println("*********************");
//                System.out.println("Ocurre un evento en la casilla " + event.getNumeroCasilla());
//                System.out.println("Se bloquea desde " + formato.format(event.getFechaEvento()) + " hasta " + formato.format(event.getFechaFinal()));
//                //System.out.println("Fecha evento iniciado "+ event.getFechaEvento()+ " estado casilla "+event.getNumeroCasilla()+" "+casillas[event.getNumeroCasilla()].isBloqueada());
//                //eventosPasados.add(event);
//                System.out.println("*********************");
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
                
                boolean agregada = false;
                
                
                for (Integer i : getCasillasDefault()) {
                    System.out.println("evaluando casilla default: "+ i);
                    if (casillas[i].getCantidadEnEspera() < 3) {
                        ingresarVehiculoAEspera(menorCasilla(casillas[i], casillas[i+1]), veh);
                        agregada = true;
                        break;
                    }
                }
                if (!agregada) {
                    
                    Casilla cs = siguienteCasillaHabilitada();
                    if (cs  != null) {
                        ingresarVehiculoAEspera(cs, veh);
                    } else {
                        ingresarVehiculoAEspera(siguienteCasilla(), veh);
                    }
                }
                
                
            }
            /*
            if (!eventosPasados.isEmpty()) {
                    for (Evento eventPasado : eventosPasados) {
                        //System.out.println("RELOJ: " + Reloj.getInstance().getDate());
                        //System.out.println("Evento fecha final "+eventPasado.getNumeroCasilla() +" "+ eventPasado.getFechaFinal());
                        if (Reloj.getInstance().getDate().compareTo(eventPasado.getFechaFinal()) == 0) {
                            System.out.println("Finaliza el evento "+ eventPasado.toString());
                            casillas[eventPasado.getNumeroCasilla()].setBloqueada(false);
                            
                            if (eventPasado.getNumeroCasilla() < 2) {
                                casillas[eventPasado.getNumeroCasilla()].setHabilitada(true);
                            }
                            eventosPasados.remove(eventPasado);
                        }
                    }
                }*/
        }
        for (Thread hilo : hilosEventos) {
            hilo.join();
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }
        
        HandleFile.getInstance().closeArchivoWriter();
    }
    
    public Queue<Integer> getCasillasDefault(){
        Queue<Integer> q = new LinkedList<Integer>();
        
        for (Casilla casilla : casillas) {
            if (casilla.esDefault()) {
                q.add(casilla.getNumeroCasilla());
            }
            
        }
        
        return q;
    }
    
    
    public void casillaProcesoEvento(Evento event){
        
        if (casillas[event.getNumeroCasilla()].esDefault()) {
            Casilla cs =  getSiguienteDefault(event.getNumeroCasilla());
            cs.setEsDefault(true);
            cs.setHabilitada(true);
           
        } else {
            casillas[(event.getNumeroCasilla()+1)%4].setHabilitada(true); // Se habilita una casilla mas para agilizar el pasaje
        }
        if (casillas[event.getNumeroCasilla()].getCantidadEnEspera() != 0) {
            System.out.println("SE REORDENAN LOS VEHICULOS DE LA CASILLA " + event.getNumeroCasilla());
            for (IVehiculo ve : casillas[event.getNumeroCasilla()].getEnEspera()) {
                System.out.println(ve.getMatricula());
            }
            reordenarVehiculosEstancados(casillas[event.getNumeroCasilla()].clearFila(),event.getNumeroCasilla());
        }
    }
    
    
    public void estadoEventoCasilla(int i,boolean status){
        casillas[i].setBloqueada(status);
    }
    
    public Casilla getSiguienteDefault(int numCasilla){
        
        for (int i = numCasilla;; i++) {
            i = i % 4;
            if (!casillas[i].isBloqueada() && !casillas[i].esDefault()) {
                return casillas[i];
            }
            
        }
        
        
    }
    
    
    
    public void reordenarVehiculosEstancados(Queue<IVehiculo> estancados, int numCasilla){
        System.out.println("Cantidad de vehiculos estancados en casilla "+ numCasilla + "son " + estancados.size());
        Casilla c1,c2,ret;
        for (IVehiculo estancado : estancados) {
            System.out.println("SE EVALUA EL VEHICULO"+ estancado.getMatricula());
            switch (numCasilla) {
                case 0:
                    
                    c1 = siguienteCasillaDerecha(numCasilla);
                    c2 = siguienteCasillaDerecha(numCasilla+1);
                    
                    break;
                case 4:
                    
                    c1 = siguienteCasillaIzquierda(numCasilla);
                    c2 = siguienteCasillaIzquierda(numCasilla+1);
                    break;
                    
                default:
                    c1 = siguienteCasillaDerecha(numCasilla);
                    c2 = siguienteCasillaIzquierda(numCasilla);
            }
            ret = menorCasilla(c1,c2);
            if (ret != null) {
                ingresarVehiculoAEspera(menorCasilla(c1,c2),estancado);
            } else {
                ingresarVehiculoAEspera(siguienteCasillaHabilitada(),estancado);
            }
            
        }
        
    }
    
    public Casilla menorCasilla(Casilla a, Casilla b){
        
        if (a != null && b != null) {
            
            if (a.getCantidadEnEspera() < b.getCantidadEnEspera()) {
                return a;
            } else {
                return b;
            }
        } else if (a != null && b == null) {
            return a;
        } else if (b != null){
            return b;
        } else {
            return null;
        }
    }
    
    public Casilla siguienteCasillaDerecha(int numCasilla){
        
        for (int i = numCasilla; 0 < i; i--) {
            if (!casillas[i].isBloqueada()) {
                return casillas[i];
                
            }
        }
        return null;
    }
    
    public Casilla siguienteCasillaIzquierda(int numCasilla){
         for (int i = numCasilla; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada()) {
                return casillas[i];
                
            }
        }
        return null;
        
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
            if (casillas[i].getCantidadEnEspera() == 0 && !casillas[i].isBloqueada()) {
                return casillas[i];
            }
        }
        
        Casilla ret = casillas[0];
        for (int i = 0; i < casillas.length; i++) {
            
            if (casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() && !casillas[i].isBloqueada() ) {
                ret = casillas[i];
            } 
            if (!casillas[i].isHabilitado() && !casillas[i].isBloqueada()) {
                return casillas[i];
            }
        }
        return ret;
    }
    
    
    public Casilla siguienteCasillaHabilitada(){
        
        // Selección de pivote
        Casilla ret = null;
        for (int i = 0; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada() && casillas[i].isHabilitado()) {
                ret = casillas[i];
                break;
            }
        }
        // Selección de la casilla
        for (int i = 0; i < casillas.length; i++) {
            System.out.println("Pregunto la casilla " + casillas[i].getNumeroCasilla() + " está bloqueada? "+ casillas[i].isBloqueada());
            if (casillas[i].isBloqueada()) {
                continue;
            }
            if ( casillas[i].isHabilitado() && casillas[i].getCantidadEnEspera() < ret.getCantidadEnEspera() && casillas[i].getEnEspera().size()  < 3) {
                ret = casillas[i];
                
            } 
        }
        return ret;
    }
  
    public Casilla siguienteCasilla(){
        
        
        // Selección de pivote
        Casilla ret = null;
        for (int i = 0; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada() && casillas[i].isHabilitado()) {
                ret = casillas[i];
                break;
            }
        }
        // Selección de la casilla
        for (int i = 0; i < casillas.length; i++) {
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
