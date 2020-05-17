/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import Objects.Camion;
import Objects.IVehiculo;
import Objects.Auto;
import Objects.Vehiculo_Emergencia;
import Utils.HandleFile;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
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
        Scanner scanner = new Scanner(stream);
        String path = "";
        System.out.println("\n     Proyecto Obligatorio - Sistemas Operativos 2020\n\n");
        
        while (!input.equals("q")) {
            
            switch (input) {
                case "h":
                    System.out.println("\n------- Ayuda -------\n");
                    System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulacion");
                    System.out.println("h: Ayuda");
                    System.out.println("q: Salir");
                    System.out.println("\n---------------------\n");
                    break;
                case "q":
                    System.out.println("\nTermino de ejecucion \n");
                    break;
                case "ex":
                    if (input.contains("ex")) {
                        input = scanner.next();
                        File f = new File(dir+"//"+input);
                        if (f.exists()) {
                            HandleFile.getInstance(input);
                            Peaje peaje = new Peaje();
                            BancoDatos.initBancoDatos(115.0, 195.0, 195.0);
                            Reloj reloj = new Reloj(1, 21, 40, 0, 3, 5, 2020);
                            reloj.start();
                            peaje.iniciar();
                            
                            System.out.println("Cantidad de vehiculos: " + BancoDatos.getBancoDatos().getCantidadVehiculos());
                            System.out.println("Costo operativo: " + BancoDatos.getBancoDatos().getCostoOperativo());
                            System.out.println("Recaudacion: " + BancoDatos.getBancoDatos().getRecaudacion());
                            System.out.println("Promedio de espera: " + BancoDatos.getBancoDatos().getPromedioEspera());
                            System.out.println("\nEjecucion de archivo "+input + " finalizada.\n");
                            reloj.stop();
                        } else {
                            System.out.println("\n** Error en la direccion del archivo de entrada **\n");
                            
                        }
                        continue;
                        
                    }
                    break;
                    
                default:
                    if (!input.equals("q") || !input.equals("h") || !input.equals("ex")) {
                        cantidadErrores++;
                        System.out.println("\nEntrada errónea, intente de nuevo o ingrese h por ayuda.\n");
                        if (cantidadErrores == 3) {
                            cantidadErrores = 0;
                            input = "h";
                            continue;
                        }
                        
                    }
            }
            System.out.println("Ingrese operación:");
            input = scanner.next();
            
            
        }
        scanner.close();
    }
}
