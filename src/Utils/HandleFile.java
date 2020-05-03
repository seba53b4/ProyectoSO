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
import java.io.IOException;
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
    private final Queue<String> salida = new LinkedList<String>();
    private FileWriter fw; 
    private BufferedWriter bw;
    
    
    public static HandleFile initHandeFile(){
        if (hf == null) {
            hf = new HandleFile();
        }
        return hf;
    }
    
    
    public HandleFile()
    {        
          dir = System.getProperty("user.dir");// + "\\":
          File f = new File(dir+"//salida.txt");
          try {
              fw = new FileWriter(f,true);
          } catch (IOException ex) {
              Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
          }
          bw = new BufferedWriter(fw);
          
    }

    public void closeArchivoWriter(){
        try {
            
            this.bw.close();
        } catch (IOException ex) {
            Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public synchronized boolean writeArchivo(String line)
    {
        
        try {
            
            String linea  = line;
            bw.write(linea+"\n");
            
            return true;
        } catch(Exception e) {
          System.out.println("Excepcion leyendo fichero" + e);
          
        }
        return false;
    }
    
    
}

