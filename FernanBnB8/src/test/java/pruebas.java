import org.example.controller.Controller;
import org.example.dao.*;
import org.example.models.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class pruebas {

    public static void main(String[] args) {
//        ArrayList<String> csv = new ArrayList<>();
//
//        csv.add("IdVivienda;Titulo;Localidad;Provincia;MaxOcupantes;PrecioNoche");
//        csv.add("10;casa;Torredelcampo;Jaen;10;20");
//
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter("prueba.csv",true));
//            for (int i = 0; i < csv.size(); i++) {
//                bw.write(csv.get(i)+"\n");
//                System.out.println("Escritura");
//            }
//            bw.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        Properties p = new Properties();

        try {
            p.load(new FileReader("configuracion/config.properties"));
            System.out.println("cargado");

            Controller c = new Controller(p,null);

            DAOManager daoManager = DAOManager.getSinglentonInstance(p);
            daoManager.open();

            DaoAdminSQL daoAdminSQL = new DaoAdminSQL();
            DAOUserSQL daoUserSQL = new DAOUserSQL();
            DAOPropietarioSQL daoPropietarioSQL = new DAOPropietarioSQL();
            DAOViviendaSQL daoViviendaSQL = new DAOViviendaSQL();
            DAOReservaSQL daoReservaSQL = new DAOReservaSQL();

//            if(daoAdminSQL.insert(a,daoManager)) System.out.println("Correcto");
//            else System.out.println("MAL INSERT");
//
//            if (daoAdminSQL.delete(a,daoManager)) System.out.println("Borrado");
//            else System.out.println("SIN BORRAR");

//            Admin admin = daoAdminSQL.read(1,daoManager);

//            for (Admin admin: daoAdminSQL.readAll(daoManager)
//                 ) {
//
//                System.out.println(admin);
//            }

//            User u = new User(5000,"user","prueba","prueba");
//            User u2 = new User(5001,"user2","prueba","prueba");
//            daoUserSQL.insert(u,daoManager);
//            daoUserSQL.insert(u2,daoManager);
//            daoUserSQL.update(u,daoManager);
//            daoUserSQL.delete(u,daoManager);

//            mock(c);
//
//            System.out.println(daoPropietarioSQL.readByEmail("propietario@fernanbnb.com",daoManager));
//            System.out.println(daoUserSQL.readByEmail("user@fernanbnb.com",daoManager));
//            System.out.println(daoAdminSQL.readByEmail("admin@fernanbnb.com",daoManager));

//            Propietario propietario = daoPropietarioSQL.readByEmail("propietario@fernanbnb.com",daoManager);
//
//            Vivienda v = new Vivienda(6000,"casa","sin amueblar","Torredelcampo","Jaen",20,20);
//            daoViviendaSQL.insert(v,propietario,daoManager);
//            System.out.println(daoViviendaSQL.read(v.getId(), daoManager));
//            daoViviendaSQL.delete(v,daoManager);

//            Reserva r = new Reserva(7000, LocalDate.now(),10,500,5,6000,2967);
//            Vivienda v = new Vivienda(8000,"vivienda","vivienda","localidad", "provincia", 20,20);
//
//            daoReservaSQL.insert(r,daoManager);
//            Reserva r2 = new Reserva(7000, LocalDate.now(),20,1000,15,6000,2967);
//            System.out.println(daoReservaSQL.read(r2.getId(),daoManager));
//            daoReservaSQL.update(r2,daoManager);
//            System.out.println(daoReservaSQL.read(r2.getId(),daoManager));
//            daoReservaSQL.delete(r2,daoManager);
//
//            Admin a = daoAdminSQL.log("admin@fernanbnb.com","admin",daoManager);
//            System.out.println(a);
//
//            User u = daoUserSQL.log("user@fernanbnb.com","user",daoManager);
//            System.out.println(u);
//
//            Propietario propietario = daoPropietarioSQL.log("propietario@fernanbnb.com","propietario",daoManager);
//            System.out.println(propietario);

            System.out.println(c.generaIdVivienda());


        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo");
        } catch (IOException e) {
            System.out.println("No se cargo el archivo");
        } catch (Exception e) {
            System.out.println("Error al abrir el daoManager");
        }


        }

    private static void mock(Controller c) {
        c.addAdministrador("admin", "admin", "admin@fernanbnb.com");
        c.addAdministrador("admin2", "admin", "admin2@fernanbnb.com");
        c.addUsuario("user", "user", "user@fernanbnb.com");
        c.addUsuario("user2", "user", "user2@fernanbnb.com");
        c.addPropietario("propietario", "propietario", "propietario@fernanbnb.com");
        c.addPropietario("propietario2", "propietario", "propietario2@fernanbnb.com");

    }
}

