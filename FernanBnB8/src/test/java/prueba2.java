import org.example.controller.Controller;

import java.util.Scanner;

public class prueba2 {


    public static void main(String[] args) {

        int opBusqueda = 0;
//        do {
//
//            Scanner s = new Scanner(System.in);
//
//            System.out.print("=== BUSQUEDA DE ALOJAMIENTOS ===");
//            System.out.println("""
//
//                            1. Busqueda por título.
//                            2. Búsqueda por descripción.
//                            3. Búsqueda por localidad.
//                            4. Búsqueda por provincia.
//                            Introduce una opción: """);
//            opBusqueda = Integer.parseInt(s.nextLine());
//
//            switch (opBusqueda){
//                case 1 ->{
//                    System.out.println("op 1");
//                }
//                case 2 ->{
//                    System.out.println("op 2");
//                }
//                case 3 ->{
//                    System.out.println("op 3");
//                }
//                case 4 ->{
//                    System.out.println("op 4");
//                }
//                default -> System.out.println("Introduce un opción válida");
//            }
//
//        }while (opBusqueda < 1 || opBusqueda > 4);

//        Controller c = new Controller(null,null);
//        try {
//            System.out.println(c.generaIdVivienda());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        System.out.println(generaToken());
        System.out.println(generaToken());
        System.out.println(generaToken());

    }

    private static int generaToken() {
        return (int) (Math.random() * 1000000);
    }
}
