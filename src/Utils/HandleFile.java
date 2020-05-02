/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


/**
 *
 * @author Seba-OS
 */
public class HandleFile{
    
    private final String dir ;
    
    
    public HandleFile()
    {        
          dir = System.getProperty("user.dir");// + "\\":
    }

   
    
    private void cargaArchivo()
    {
        try {
        FileReader fr = new FileReader(dir);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        while((linea = br.readLine()) != null)
        {
           
        }
      } catch(Exception e) {
          System.out.println("Excepcion leyendo fichero " +": " + e);
        }
    }
    
    public boolean writeArchivo(String line)
    {
        File f = new File(dir+"archivo salida");
        try {
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea  = line;
            bw.write(linea);
            
            
            return true;
        } catch(Exception e) {
          System.out.println("Excepcion leyendo fichero" + e);
          
        }
        return false;
    }
    
    
}

