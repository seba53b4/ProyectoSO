/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seba-OS
 */
public class MonitorEntrada {
    
    private boolean habilitado;
    
    public MonitorEntrada(boolean av){
        habilitado = av;
        
    }
    public synchronized void setHabilitada(boolean bol){
        this.habilitado = bol;
    }
    public synchronized boolean getHabilitada(){
        return this.habilitado;
    }
    
    public synchronized void evaluarEstado(){
        
        if (!habilitado) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonitorEntrada.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public synchronized void setEstado(boolean habilitado){
         this.habilitado = habilitado;
         notify();
    }
    
    
}
