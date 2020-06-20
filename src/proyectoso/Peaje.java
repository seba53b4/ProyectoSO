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
    public Queue<Thread> hilosCasilla;
    public Queue<Thread> hilosEventos;
    public Queue<Evento> eventos;
   
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
        hilosCasilla = new LinkedList();
        hilosEventos = new LinkedList();
        
        
    }
    
    public void iniciar() throws InterruptedException {
        
        Thread aux;
        IVehiculo veh;
        
        casillas[0].setEsDefault(true);
        casillas[1].setEsDefault(true);
        casillas[0].setHabilitada(true);
        casillas[1].setHabilitada(true);
        
        Evento event;
        
        while (!vehiculos.isEmpty()) {
            
            
            if (!eventos.isEmpty() && Reloj.getInstance().getDate().compareTo(eventos.peek().getFechaEvento()) == 0) {
                SimpleDateFormat formato =  new SimpleDateFormat("hh:mm:ss a dd-MMM-aa");
                event = eventos.poll();
                Thread th = new Thread(event);
                hilosEventos.add(th);
                th.start();
                System.out.println("Ocurre un evento en la casilla " + event.getNumeroCasilla());
                casillaProcesoEventoAlta(event);
                
            }
            

            if (vehiculos.peek() != null && Reloj.getInstance().getDate().compareTo(vehiculos.peek().getTime()) == 0){
                veh = vehiculos.poll();
                if (veh.getTelepeaje() || veh.getTipo().equals("emergencia")) {
                    
                    Casilla vacia = siguienteCasillaEspecial();
                    if (vacia != null) {
                        ingresarVehiculoAEspera(vacia, veh);
                        continue;
                    }
                }
                
                boolean agregada = false;
                
                
                for (Integer i : getCasillasDefault()) {
                    
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
        }
        for (Thread hilo : hilosEventos) {
            hilo.join();
        }
        for (Thread hilo : hilosCasilla) {
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
    
    public void casillaProcesoEventoBaja(int numCasilla){
        
      
        if (numCasilla < 2) {
            casillas[numCasilla].setHabilitada(true);
            casillas[numCasilla].setEsDefault(true);
            for (Integer i : getCasillasDefault()) {
                if (i > 1) {
                    getCasillasDefault().remove(i);
                    getCasillasDefault().add(numCasilla);
                }
            }
 
        } 
        
    }
    public void casillaProcesoEventoAlta(Evento event){
        
        if (casillas[event.getNumeroCasilla()].esDefault()) {
            casillas[event.getNumeroCasilla()].setEsDefault(false); // quita el default a la casilla
            casillas[event.getNumeroCasilla()].setHabilitada(false);
            Casilla cs =  getSiguienteDefault(event.getNumeroCasilla()); // busca nueva casilla default
            cs.setEsDefault(true);
            cs.setHabilitada(true);
           
        } else {
            casillas[(event.getNumeroCasilla()+1)%4].setHabilitada(true); // Se habilita una casilla mas para agilizar el pasaje
        }
        if (casillas[event.getNumeroCasilla()].getCantidadEnEspera() != 0) {
            reordenarVehiculosEstancados(casillas[event.getNumeroCasilla()].clearFila(),event.getNumeroCasilla());
        }
    }
    
    public void estadoEventoCasilla(int i,boolean status){
        casillas[i].setBloqueada(status);
    }
    
    public Casilla getSiguienteDefault(int numCasilla){
        
        for (int i = 0;; i++) {
            i = i % 4;
            if (!casillas[i].isBloqueada() && !casillas[i].esDefault() && numCasilla != i) {
                casillas[i].setHabilitada(true);
                return casillas[i];
            }
        }
    }
    
    public void reordenarVehiculosEstancados(Queue<IVehiculo> estancados, int numCasilla){
        Casilla c1,c2,ret;
        for (IVehiculo estancado : estancados) {
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
                System.out.println("Replanifica e ingresa el auto " + estancado.getMatricula() + " en la casilla " + ret.getNumeroCasilla());
                if (!ret.isHabilitado()) {
                    ret.setHabilitada(true);
                }
                ingresarVehiculoAEspera(ret,estancado);
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
    
    public Casilla siguienteCasillaIzquierda(int numCasilla){
        
        for (int i = numCasilla-1; 0 < i; i--) {
            if (!casillas[i].isBloqueada()) {
                return casillas[i];
            }
        }
        return null;
    }
    
    public Casilla siguienteCasillaDerecha(int numCasilla){
         for (int i = numCasilla+1; i < casillas.length; i++) {
            if (!casillas[i].isBloqueada()) {
                return casillas[i];
            }
        }
        return null;
    }
    
    
    public void ingresarVehiculoAEspera(Casilla a, IVehiculo veh){
        
        System.out.println("[ENTRA] El vehiculo " + veh.getMatricula() + " ingresa a la lista de espera de la casilla " + a.getNumeroCasilla() );
        a.addVehiculoEnEspera(veh);
        Thread nuevoHilo = new Thread(a);
        hilosCasilla.add(nuevoHilo);
        nuevoHilo.start();
    }
    
    public Casilla siguienteCasillaEspecial(){
        
        // Si alguna default esta vacia
        for (Integer i : getCasillasDefault()) {
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
                casillas[i].setHabilitada(true);
                return casillas[i];
            }
        }
        
        if (ret.getNumeroCasilla() == 0 && ret.isBloqueada()) {
            ret = siguienteCasilla();
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
