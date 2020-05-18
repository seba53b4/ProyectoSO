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
        Double tarifaAuto = 115.0;
        Double tarifaCamion = 195.0;
        Double tarifaBus = 195.0;
        int segundos = 0 , horas = 21, minutos = 40, dia = 3 , mes = 5,año = 2020;
        int speed = 1;
        String[] entradas;
        Scanner scanner = new Scanner(stream);
        String path = "";
        System.out.println("\n     Proyecto Obligatorio - Sistemas Operativos 2020\n\n");
        System.out.println("\n------- Ayuda -------\n");
        System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulación");
        System.out.println("ex t 'numCaso': Selección de caso cargado de carpeta testCasesEntrada");
        System.out.println("ch speed 'Numero en milisegundos': Cambiar la velocidad del Reloj. ");
        System.out.println("ch tarifa 'tarifaAuto' 'tarifaCamion' 'tarifaOmnibus': Cambiar la tarifa de vehículos. ");
        System.out.println("ch date 'hora' 'minutos' 'segundos' 'dia' 'mes' 'año': Cambiar la fecha de simulación. ");
        System.out.println("show: Muestra datos de uso para la simulación");
        System.out.println("h: Ayuda");
        System.out.println("q: Salir");
        System.out.println("\n---------------------\n");
        while (!input.equals("q")) {
            System.out.println("Ingrese operación:");
            input = scanner.next();
            
            switch (input) {
                case "h":
                    System.out.println("\n------- Ayuda -------\n");
                    System.out.println("ex 'Nombre de archivo' : Se procede a ejecutar la simulación");
                    System.out.println("ex t 'Número de Caso': Selección de caso cargado de carpeta testCasesEntrada");
                    System.out.println("ch speed 'Numero en milisegundos': Cambiar la velocidad del Reloj.");
                    System.out.println("ch tarifa 'tarifaAuto' 'tarifaCamion' 'tarifaOmnibus': Cambiar la tarifa de vehículos.");
                    System.out.println("ch date 'hora' 'minutos' 'segundos' 'dia' 'mes' 'año': Cambiar la fecha de simulación.");
                    System.out.println("show: Muestra datos de uso para la simulación");
                    System.out.println("h: Ayuda");
                    System.out.println("q: Salir");
                    System.out.println("\n---------------------\n");
                    break;
                case "q":
                    System.out.println("\nEjecución finalizada\n");
                    break;
                case "show":
                    System.out.println("\n------- Informe de Datos -------\n");
                    System.out.println("Fecha: "+horas+":"+minutos+":"+segundos+" "+dia+"/"+mes+"/"+año);
                    System.out.println("Velocidad de Reloj: "+ speed);
                    System.out.println("Tarifa de Auto: "+ tarifaAuto);
                    System.out.println("Tarifa de Camion: "+ tarifaCamion);
                    System.out.println("Tarifa de Omnibus: "+ tarifaBus);
                    System.out.println("\n---------------------\n");
                    break;
                case "ch":
                        input = scanner.next();
                        switch (input) {
                            case "speed":
                                while (!input.equals("q")) {
                                    //System.out.println("\n Ingrese valor de velocidad del Reloj o ingrese q para volver atrás \n");
                                    input = scanner.next();
                                    if (HandleFile.isNumeric(input)) {
                                        speed = Integer.parseInt(input);
                                        System.out.println("\nValor ingresado correctamente\n");
                                        break;
                                    } else {
                                        System.out.println("\n Valor erróneo \n");
                                        break;
                                    }
                                }
                                input = "dummy";
                                break;
                            case "tarifa":

                                //System.out.println("\n Ingrese valor de velocidad del Reloj o ingrese q para volver atrás \n");
                                entradas = new String[5];
                                for (int i = 0; i < entradas.length; i++) {
                                    input = scanner.next();
                                    if (HandleFile.isNumeric(input)){
                                        entradas[i] = input;
                                    } else {
                                        System.out.println("\n Valor erróneo "+ input);
                                        input = "q";
                                        break;
                                    }
                                }
                                if (!input.equals("q")) {


                                    tarifaAuto =  Double.parseDouble(entradas[0]);
                                    tarifaCamion = Double.parseDouble(entradas[1]);
                                    tarifaBus = Double.parseDouble(entradas[2]);
                                    System.out.println("\nValores ingresados correctamente\n");
                                }


                                input = "dummy";
                                break;
                            case "date":

                                entradas = new String[6];
                                for (int i = 0; i < entradas.length; i++) {
                                    input = scanner.next();
                                    if (HandleFile.isNumeric(input)){
                                        entradas[i] = input;
                                    } else {
                                        System.out.println("\n Valor erróneo "+ input);
                                        input = "q";
                                        break;
                                    }
                                }

                                if (!input.equals("q")) {
                                    horas = Integer.parseInt(entradas[0]);
                                    minutos = Integer.parseInt(entradas[1]);
                                    segundos= Integer.parseInt(entradas[2]);
                                    dia = Integer.parseInt(entradas[3]);
                                    mes = Integer.parseInt(entradas[4]);
                                    año = Integer.parseInt(entradas[5]);
                                    System.out.println("\nValores ingresados correctamente\n");
                                }


                                input = "dummy";
                                break;
                            default:
                                System.out.println("\n** Error en la entrada del comando **\n");
                        }
                        break;
                case "ex":
                    if (input.contains("ex")) {
                        hf = new HandleFile();
                        hf.exportarTests();
                        boolean todoOk = true;
                        File[] tests = hf.getArchivosCarpeta("testCasesEntrada");
                        
                        input = scanner.next();
                        
                        if (input.equals("t")) {
                            System.out.println("\nTest Cases existentes en carpeta testCasesEntrada\n");
                            for (int i = 0; i < tests.length; i++) {
                                System.out.println(i+". "+tests[i].getName());
                            }
                            String msg = "\n Seleccione por el número el archivo elegido entre ";
                            if (tests.length == 0) {
                                System.out.println("\n No hay test en carpeta\n");
                            } else {
                                msg += "[0 .. "+ tests.length+"] \n";
                            }
                            
                            input = scanner.next();
                            if (HandleFile.isNumeric(input)) {
                                if (Integer.parseInt(input) < tests.length) {
                                    System.out.println("CARGA ARCHIVOOO:  "+tests[Integer.parseInt(input)].getName());
                                    hf = new HandleFile(tests[Integer.parseInt(input)].getName());
                                    System.out.println("\n Seleccionado correctamente el Test Case "+ tests[Integer.parseInt(input)].getName());
                                } else {
                                    System.out.println("\n ** ERROR: Número ingresado fuera de rango ** \n");
                                    todoOk = false;
                                }
                            }
                        } else {
                                System.out.println("entra ");
                                hf = new HandleFile(input);
                        }
                        if (todoOk) {
                            
                            System.out.println("**************************");
                            System.out.println("\n Ejecutando Simulación \n");
                            System.out.println("**************************\n");
                            Peaje peaje = new Peaje();
                            BancoDatos.initBancoDatos(tarifaAuto, tarifaCamion, tarifaBus);
                            Reloj reloj = new Reloj(speed, horas, minutos, segundos, dia, mes, año);
                            reloj.start();
                            peaje.iniciar();
                            System.out.println("Cantidad de vehículos: " + BancoDatos.getBancoDatos().getCantidadVehiculos());
                            System.out.println("Costo operativo: " + BancoDatos.getBancoDatos().getCostoOperativo());
                            System.out.println("Recaudación: " + BancoDatos.getBancoDatos().getRecaudacion());
                            System.out.println("Promedio de espera: " + BancoDatos.getBancoDatos().getPromedioEspera());
                            if (HandleFile.isNumeric(input)) {
                                System.out.println("\nEjecución de archivo "+ tests[Integer.parseInt(input)].getName()+ " finalizada.\n");
                            } else {
                                System.out.println("\nEjecución de archivo "+ input + " finalizada.\n");
                            }
                            BancoDatos.getBancoDatos().clean();
                            reloj.stop();
                        }
                    } else {
                        System.out.println("\n** Error en la dirección del archivo de entrada **\n");
                        
                    }
                    break;
                default:
                        cantidadErrores++;
                        System.out.println("\nEntrada errónea, intente de nuevo o ingrese h por ayuda.\n");
                        if (cantidadErrores == 3) {
                            System.out.println("\n------- Ayuda -------\n");
                            System.out.println("ex 'nombreArchivo' : Se procede a ejecutar la simulación");
                            System.out.println("ex t 'numCaso': Selección de caso cargado de carpeta testCasesEntrada");
                            System.out.println("ch speed 'Numero en milisegundos': Cambiar la velocidad del Reloj. ");
                            System.out.println("ch tarifa 'tarifaAuto' 'tarifaCamion' 'tarifaOmnibus': Cambiar la tarifa de vehículos. ");
                            System.out.println("ch date 'hora' 'minutos' 'segundos' 'dia' 'mes' 'año': Cambiar la fecha de simulación. ");
                            System.out.println("show: Muestra datos de uso para la simulación");
                            System.out.println("h: Ayuda");
                            System.out.println("q: Salir");
                            System.out.println("\n---------------------\n");
                           
                        }
                        
                    
            }
            
            
            
        }
        scanner.close();
    }
}
