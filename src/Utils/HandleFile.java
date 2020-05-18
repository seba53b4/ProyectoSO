/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


import Objects.Evento;
import Objects.IVehiculo;
import Objects.Vehiculo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
 * Clase que provee funcionalidades necesarias para el manejo de archivos.
 * Levanta información de archivos.
 * Genera archivos de salida.
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
    private Queue<Evento> eventos;

    public Queue<Evento> getEventos() {
        return eventos;
    }
    
    public File[] getArchivosCarpeta(String entrada){
      File carpeta = new File(entrada);
      FileFilter filtro = new FileFilter() {
      @Override
      public boolean accept(File arch) {
        return arch.isFile();
      }};
      File[] archivos = carpeta.listFiles(filtro);
       
      return archivos;
    }
    
    public void exportarTests(){
        
        File f = new File(dir+"\\/src\\/testCasesEntrada");
        File[] tests = getArchivosCarpeta(dir+"\\/src\\/testCasesEntrada");
        File testFolder = new File("testCasesEntrada");
        
        if (!testFolder.exists()) {
            testFolder.mkdir();
            
        }
        /*if (tests.length != 0) {
            
            for (int i = 0; i < tests.length; i++) {
                try {
                    //System.out.println("Exportar test " +tests[i].getName() );
                    Files.copy(Paths.get(tests[i].getAbsolutePath()), Paths.get(testFolder.getAbsolutePath()+"/"+tests[i].getName()), StandardCopyOption.REPLACE_EXISTING);
                    //System.out.println("Path origen "+ Paths.get(tests[i].getAbsolutePath()));
                    //System.out.println("Path destino "+ Paths.get(testFolder.getAbsolutePath()));
                } catch (FileNotFoundException ex) {
                    System.out.println("Error no se encuentra archivo de tests "+ tests[i].getName());
                } catch (IOException ex) {
                    System.out.println("Error al copiar archivo de tests "+ tests[i].getName());
                }catch (Exception ex) {
                    System.out.println("Error exception "+ ex+" en "+ tests[i].getName());
                }
            }
        }*/
        
    }
    
    public SimpleDateFormat getFormatoFecha(){
        return this.formatoFecha;
    }
   
    public static HandleFile getInstance(){
        
        return HandleFile.hf;
    } 
    
    
    public HandleFile(){
        dir = System.getProperty("user.dir");
        this.entrada = "";
        fw = null;
        fr = null;
        bufferWriter= null;
        formatoFecha = null;
        eventos = null;
        hf = null;
    }
    public HandleFile(String entrada)
    {        
          dir = System.getProperty("user.dir");
          this.entrada = entrada;
          File dirSalida = new File(dir+"\\/testCasesSalida");
          if (!dirSalida.exists()) {
              dirSalida.mkdir();
          }
          File f = new File(dir+"\\/testCasesSalida\\/"+"//salida_"+entrada+".csv");
          f.delete();
          try {
              f.createNewFile();
          } catch (IOException ex) {
              Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              fw = new FileWriter(f,true);
          } catch (IOException ex) {
              Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              fr = new FileReader(dir+"/testCasesEntrada/"+entrada);
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
          
          eventos = new LinkedList<Evento>();
          
          hf = this;
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
                    if (line[0].equals("evento")) {
                        eventos.add(new Evento(Integer.parseInt(line[1]),parseDate(line[2]), parseDate(line[3])));
                    }else if (line[2].equals("1")) {
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
    
    public static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
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

