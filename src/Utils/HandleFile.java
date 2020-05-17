/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


import Objects.IVehiculo;
import Objects.Vehiculo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Seba-OS
 */
public class HandleFile{
    
    private final String dir ;
    private static HandleFile hf;
    private final Queue<String> salida = new LinkedList<>();
    private FileWriter fw; 
    private FileReader fr;
    private final BufferedWriter bufferWriter;
    private final SimpleDateFormat formatoFecha;
    private final String entrada;
    
    public static HandleFile getInstance(){
        if (hf == null) {
            hf = new HandleFile("dummy");
        }
        return hf;
    }
    public static HandleFile getInstance(String entrada){
        if (hf == null) {
            hf = new HandleFile(entrada);
        }
        return hf;
    }
    
    
    public SimpleDateFormat getFormatoFecha(){
        return this.formatoFecha;
    }
    
    public HandleFile(String entrada)
    {        
          dir = System.getProperty("user.dir");
          this.entrada = entrada;
          File f = new File(dir+"//salida.csv");
          f.delete();
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            f.createNewFile();
        } catch (IOException ex) {
            System.out.println("Archivo de salida error");
        }
          try {
              fw = new FileWriter(f,true);
          } catch (IOException ex) {
              Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              fr = new FileReader(dir+"//"+entrada);
          } catch (IOException ex) {
              Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
          }
          bufferWriter = new BufferedWriter(fw);
          formatoFecha = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
          
          try {
            
            bufferWriter.write("Hilo;Casilla;Tipo;Telepeaje;Matricula;Demora Vehiculo;Hora Entrada;Hora Salida;Tiempo Espera"+" \n");
        } catch(Exception e) {
            System.out.println("Excepcion leyendo fichero" + e);
        }
    }

    public String getDir() {
        return dir;
    }

    
    public void closeArchivoWriter(){
        try {
            
            this.bufferWriter.close();
            this.fw.close();
        } catch (IOException ex) {
            Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Date parseDate(String date)
    {
        Date fechaDate = null;
        try {
            fechaDate = formatoFecha.parse(date);
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
        return fechaDate;
    }
    
    public Queue<IVehiculo> cargaArchivo()
    {
        Queue<IVehiculo> res = null;
        try {
            
            BufferedReader br = new BufferedReader(fr);
            
            res = new LinkedList<>();
            String[] line = new String[4];
            String linea;
            IVehiculo veh;
            br.readLine();
            while((linea = br.readLine()) != null)
            {
                if (!linea.contains("#")) {
                    
                    line = linea.split(";");
                    if (line[2].equals("1")) {
                        res.add(new Vehiculo(line[0],line[1],true,Integer.parseInt(line[3]),parseDate(line[4])));
                    } else {
                        res.add(new Vehiculo(line[0],line[1],false,Integer.parseInt(line[3]),parseDate(line[4])));
                    }
                }
                
            }
            br.close();
            this.fr.close();
            
        } catch(Exception e) {
            System.out.println("Excepcion leyendo fichero " +": " + e);
        }
        return res;
        
    }
    
    
    public synchronized boolean writeArchivo(String line)
    {
        
        try {
            
            bufferWriter.write(line+" \n");
            
            return true;
        } catch(Exception e) {
            System.out.println("Excepcion leyendo fichero" + e);
            
        }
        return false;
    }
    
    
}

