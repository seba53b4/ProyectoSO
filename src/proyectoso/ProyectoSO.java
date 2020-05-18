/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Utils.HandleFile;
import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author Seba-OS
 */
public class ProyectoSO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
        String dir = System.getProperty("user.dir");
        String input = "h";
        int cantidadErrores = 0;
        InputStream stream = System.in;
        HandleFile hf ;
        Scanner scanner = new Scanner(stream);
        String path = "";
        System.out.println("\n     Proyecto Obligatorio - Sistemas Operativos 2020\n\n");
        System.out.println("\n------- Ayuda -------\n");
        System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulación");
        System.out.println("h: Ayuda");
        System.out.println("q: Salir");
        System.out.println("\n---------------------\n");
        
        
        while (!input.equals("q")) {
            
            System.out.println("Ingrese operación:");
            input = scanner.next();
            
            switch (input) {
                case "h":
                    System.out.println("\n------- Ayuda -------\n");
                    System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulación");
                    System.out.println("h: Ayuda");
                    System.out.println("q: Salir");
                    System.out.println("\n---------------------\n");
                    break;
                case "q":
                    System.out.println("\nEjecución finalizada\n");
                    break;
                case "ex":
                    if (input.contains("ex")) {
                        input = scanner.next();
                        File f = new File(dir+"\\/src\\/testCasesEntrada\\/"+input);
                        if (f.exists()) {
                            
                            hf = new HandleFile(input);
                            
                            Peaje peaje = new Peaje();
                            BancoDatos.initBancoDatos(115.0, 195.0, 195.0);
                            Reloj reloj = new Reloj(1, 21, 40, 0, 3, 5, 2020);
                            reloj.start();
                            peaje.iniciar();
                            
                            System.out.println("Cantidad de vehículos: " + BancoDatos.getBancoDatos().getCantidadVehiculos());
                            System.out.println("Costo operativo: " + BancoDatos.getBancoDatos().getCostoOperativo());
                            System.out.println("Recaudación: " + BancoDatos.getBancoDatos().getRecaudacion());
                            System.out.println("Promedio de espera: " + BancoDatos.getBancoDatos().getPromedioEspera());
                            System.out.println("\nEjecución de archivo "+input + " finalizada.\n");
                            BancoDatos.getBancoDatos().clean();
                            reloj.stop();
                           // HandleFile.getInstance();
                        } else {
                            System.out.println("\n** Error en la dirección del archivo de entrada **\n");
                            
                        }
                       
                        
                    }
                    break;
                    
                default:
                        cantidadErrores++;
                        System.out.println("\nEntrada errónea, intente de nuevo o ingrese h por ayuda.\n");
                        if (cantidadErrores == 3) {
                            cantidadErrores = 0;
                            System.out.println("\n------- Ayuda -------\n");
                            System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulación");
                            System.out.println("h: Ayuda");
                            System.out.println("q: Salir");
                            System.out.println("\n---------------------\n");
                           
                        }
                        
                    
            }
            
            
            
        }
        scanner.close();
    }
}
