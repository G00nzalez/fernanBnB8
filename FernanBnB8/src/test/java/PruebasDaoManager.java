import org.example.dao.DAOManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PruebasDaoManager {

    public static void main(String[] args) {

        Properties p = new Properties();
        try {
            p.load(new FileReader("configuracion/config.properties"));

            DAOManager dao = DAOManager.getSinglentonInstance(p);

            dao.open();
            System.out.println("Conexión establecida.");

        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.print(e);;
        } catch (Exception e) {
            System.out.println(e);
        }


//        try {
//
//
////           daoAlumnoSQL.insert(a,dao);
////           daoAlumnoSQL.update(a,dao);
////           daoAlumnoSQL.delete(a,dao);
//            //Alumno b = daoAlumnoSQL.read("nombre","anto",dao);
//            //Alumno c = daoAlumnoSQL.read(a.getDni(),dao);
//
//            //b.mostrar();
//            //c.mostrar();
//
////            ArrayList<Alumno> alumnos = daoAlumnoSQL.read("nombre","a",dao);
////
////            for (Alumno al:
////                 alumnos) {
////                al.mostrar();
////            }
////
////            dao.close();
//        }

    }

}
