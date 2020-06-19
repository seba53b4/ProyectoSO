/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Utils.HandleFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * Encagada de simular el tiempo
 * 
 * @author Seba-OS
 */

public class Reloj extends Thread{
    
    private int segundos, horas, minutos, dia,mes,año, speed;
    private String ampm;
    private Date date;
    private Semaphore acceso = new Semaphore(1);
    public static SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private static Reloj reloj;
    //Calendar calentario;
    //Thread h1;
    
    public synchronized Date esperarTiempo(int esperaEnSegundos){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, esperaEnSegundos*1000);
        return cal.getTime();
    }
    
    
    
    public synchronized int getRazonCambio(){
        return speed;
    }
    
    public synchronized void getAccesoReloj() throws InterruptedException{
        acceso.acquire();
        
    }
    public synchronized void liberarAccesoReloj() throws InterruptedException{
        acceso.release();
        
    }
    
    
    public synchronized static Reloj getInstance(){
       return reloj;
    }
    
    public synchronized String getHora(){
        return formatoFecha.format(this.date);
    }
    
    public Reloj(int speed){
        this.segundos = 0;
        this.horas = 0;
        this.minutos = 0;
        this.speed = speed;
        this.ampm = "am";
        date = new Date();
        
        
    }
    public Reloj(int speed, int horas, int minutos, int segundos, int dia, int mes, int año){
        this.segundos = segundos;
        this.horas = horas;
        this.minutos = minutos;
        this.speed = speed;
        this.dia = dia;
        this.año = año;
        this.mes = mes;
        date = new Date(this.año,this.mes, this.dia, this.horas, this.minutos, this.segundos);
        //formatoFecha.format(this.date);
        if (this.horas < 12) {
            this.ampm = "am";
        } else {
            this.ampm = "pm";
        }
        reloj = this;
    }
    
    public Date ParseFecha(String fecha)
    {
        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            this.date = formatoFecha.parse(fecha);
           
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        return this.date;
    }
    
    @Override
    public void run(){
       
        while(true){
            try{
                Thread.sleep(this.speed);
            }catch(InterruptedException e){}
            
            segundos+=1;
            if(segundos > 59){
                minutos+=1;
                segundos = 0;
            }
            if(minutos>59){
                horas+=1;
                minutos = 0;
            }
            if(horas>23){
                dia+=1;
                Restaurar();
            }
            
            
            String fechaAux = ""+this.horas+":"+this.minutos+":"+this.segundos+" "+this.dia+"-"+this.mes+"-"+this.año;
            this.date = ParseFecha(fechaAux);
            //System.out.println("Reloj hora: "+ formatoFecha.format(this.date));
            
        }
    }
    private void Restaurar() {
        this.horas = 0;
        this.minutos = 0;
        this.segundos = 0;
    }
    
    
    public synchronized Date getDate(){
        return this.date;
    }

    
}
