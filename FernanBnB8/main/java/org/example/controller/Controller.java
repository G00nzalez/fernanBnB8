package org.example.controller;


import org.example.comunicaciones.Comunications;
import org.example.dao.*;
import org.example.models.*;
import org.example.saves.Saves;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class Controller implements Serializable {



    //Atributos

    private Properties p;
    private Properties lastAccess;

    DAOManager daoManager;
    DaoAdminSQL daoAdminSQL;
    DAOPropietarioSQL daoPropietarioSQL;
    DAOUserSQL daoUserSQL;
    DAOViviendaSQL daoViviendaSQL;
    DAOReservaSQL daoReservaSQL;
    //Constructor
    public Controller(Properties p, Properties lastAccess) {
        this.p = p;
        this.lastAccess = lastAccess;

        daoManager = new DAOManager(p);
        daoAdminSQL = new DaoAdminSQL();
        daoPropietarioSQL = new DAOPropietarioSQL();
        daoUserSQL = new DAOUserSQL();
        daoViviendaSQL = new DAOViviendaSQL();
        daoReservaSQL = new DAOReservaSQL();
    }

    //Gets
    public ArrayList<User> getUsuarios() throws Exception {
        daoManager.open();
        return daoUserSQL.readAll(daoManager);
    }

    public ArrayList<Propietario> getPropietarios() throws Exception {
        daoManager.open();
        return daoPropietarioSQL.readAll(daoManager);
    }

    public ArrayList<Admin> getAdmins() throws Exception {
        daoManager.open();
        return daoAdminSQL.readAll(daoManager);
    }


    //MÉTODOS

    //Método para calcular cuantas reservas hay en las viviendas de un usuario
    public int totalReservas(Propietario propietario) throws Exception {
        daoManager.open();
        ArrayList<Reserva> reservas = daoReservaSQL.readAllByPropietario(propietario,daoManager);
        if (reservas == null) return 0;
        return reservas.size();
    }

    //Método para calcular cuantos usuarios se han registrado actualmente
    public int numeroUsuarios() throws Exception {
        daoManager.open();
        return (daoUserSQL.readAll(daoManager).size() + daoPropietarioSQL.readAll(daoManager).size() + daoAdminSQL.readAll(daoManager).size());
    }

    //Método para calcular cuantas viviendas se han registrado actualmente
    public int numeroViviendas() throws Exception {
        daoManager.open();
        return daoViviendaSQL.readAll(daoManager).size();
    }

    //Método para calcular cuantas reservas se han realizado actualmente
    public int numeroReservas() throws Exception {
        daoManager.open();
        ArrayList<Reserva> reservas = daoReservaSQL.readAll(daoManager);
        return reservas.size();
    }

    //Método para mostrar todas las viviendas registradas.
    public ArrayList<Vivienda> getAllViviendas() throws Exception {
        daoManager.open();
        return daoViviendaSQL.readAll(daoManager);
    }

    //Método que devuelve el total de reservas que tiene un usuario.
    public int totalReservasUsuario(User user) throws Exception {
        daoManager.open();
        return daoReservaSQL.readAllByUser(user,daoManager).size();
    }

    //Método que devuelve el total de viviendas que tiene un propietario.
    public int totalViviendas(Propietario propietario) throws Exception {
        daoManager.open();
        return daoViviendaSQL.readAllByPropietario(propietario,daoManager).size();
    }

    //Método para comprobar si el email introducido ya está registrado en algún otro usuario de cualquier tipo.
    public boolean existeEmail(String email) throws Exception {

        daoManager.open();

        //Buscamos el email introducido entre todos los usuarios.
        if (daoUserSQL.existEmail(email,daoManager)) return true;

        //Buscamos el email introducido entre todos los propietarios.
        if (daoPropietarioSQL.existEmail(email,daoManager)) return true;

        //Buscamos el email introducido entre todos los administradores.
        if (daoAdminSQL.existEmail(email,daoManager)) return true;

        return false;
    }

    //Método para agregar un nuevo usuario.
    public User addUsuario(String nombre, String passw, String email) {
        try {
            daoManager.open();
            User u = new User(generaIdUsuario(), nombre, passw, email);
            if (daoUserSQL.insert(u,daoManager)) return u;
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    //Método para generar ids para un usuario entre 0 y 1000
    private int generaIdAdmin() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000);
            try {
                daoManager.open();
                if (daoAdminSQL.read(id,daoManager) ==null) return id;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } while (repetido);

        return -1;
    }

    //Método para agregar un nuevo propietario.
    public Propietario addPropietario(String nombre, String passw, String email) {
        try {
            daoManager.open();
            Propietario p = new Propietario(generaIdPropietario(), nombre, passw, email);
            if (daoPropietarioSQL.insert(p,daoManager)) return p;
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    //Método para generar id única entre 1000 y 2000
    private int generaIdPropietario() throws Exception {
        int id;
        boolean repetido;

        daoManager.open();

        do {
            repetido = false;
            id = (int) (Math.random() * 1000)+1000;

            if (daoPropietarioSQL.read(id,daoManager) ==null) return id;

        } while (repetido);

        return -1;
    }

    //Método para agregar un nuevo administrador.
    public Admin addAdministrador(String nombre, String passw, String email) {
        try {
            daoManager.open();
            Admin admin = new Admin(generaIdAdmin(), nombre, passw, email);
            if (daoAdminSQL.insert(admin,daoManager)) return admin;
            else return null;
        } catch (Exception e) {
            return null;
        }

    }

    //Método para generar id única entre 2000 y 3000
    private int generaIdUsuario() throws Exception {
        int id;
        boolean repetido;

        daoManager.open();

        do {
            repetido = false;
            id = (int) (Math.random() * 1000)+2000;

            if (daoUserSQL.read(id,daoManager) ==null) return id;

        } while (repetido);

        return -1;
    }

    //Método para realizar el login el cual devuelve un objeto genérico comprobando el email entre todos los tipos de usuarios
    public Object login(String email, String passw) throws Exception {

        daoManager.open();
        //Comprobamos los usuarios
        User u = daoUserSQL.log(email,passw,daoManager);
        if (u != null) return u;

        //Comprobamos los propietarios
        Propietario p = daoPropietarioSQL.log(email,passw,daoManager);
        if (p != null) return p;

        //Comprobamos los admins
        Admin a = daoAdminSQL.log(email,passw,daoManager);
        if (a != null) return a;

        return null;
    }

    //Métodos que devuelve todos los usuarios.
    public ArrayList<User> getAllUsuarios() throws Exception {
        daoManager.open();
        return daoUserSQL.readAll(daoManager);
    }

    //Métodos que devuelve todos los propietarios.
    public ArrayList<Propietario> getAllPropietarios() throws Exception {
        daoManager.open();
        return daoPropietarioSQL.readAll(daoManager);
    }

    //Métodos que devuelve todos los administradores.
    public ArrayList<Admin> getAllAdmins() throws Exception {
        daoManager.open();
        return daoAdminSQL.readAll(daoManager);
    }

    //Método que nos devuelve todas las reservas de todos los usuarios.
    public ArrayList<Reserva> getReservas() throws Exception {
        daoManager.open();
        return daoReservaSQL.readAll(daoManager);
    }

    //Método que nos permite modificar el perfil según el tipo de usuario inyectado.
    public boolean modificaPerfil(Object temp) throws Exception {
        daoManager.open();
        //Comprobamos si el perfil a modificar es de tipo administrador
        if (temp instanceof Admin) {
            return daoAdminSQL.update((Admin) temp,daoManager);
        }

        //Comprobamos si el perfil a modificar es de tipo propietario
        if (temp instanceof Propietario) {
            return daoPropietarioSQL.update((Propietario) temp,daoManager);
        }
        //Comprobamos si el perfil a modificar es de tipo usuario
        if (temp instanceof User) {
            return daoUserSQL.update((User) temp,daoManager);
        }

        return false;

    }

    //Método que devuelve todas las viviendas de un propietario
    public ArrayList<Vivienda> buscaViviendasByPropietario(Propietario propietario) throws Exception {
        daoManager.open();
        return daoViviendaSQL.readAllByPropietario(propietario,daoManager);
    }

    //Método que devuelve una arraylist con todas las viviendas que tengan alguna reserva de un propietario
    public ArrayList<Vivienda> getViviendasReservadasPropietario(Propietario propietario) throws Exception {
        daoManager.open();

        ArrayList<Vivienda> viviendas = new ArrayList<>();
        viviendas = daoViviendaSQL.readAllByPropietario(propietario,daoManager);

        return viviendas;
    }

    //Método que genera id para una vivienda entre 5000 y 6000.
    public int generaIdVivienda() throws Exception {
        int id;
        boolean repetido;

        daoManager.open();

        do {
            repetido = false;
            id = (int) (Math.random() * 1000)+4000;

            if (daoPropietarioSQL.read(id,daoManager) ==null) return id;

        } while (repetido);

        return -1;
    }

    //Método que devuelve una vivienda según la id introducida.
    public Vivienda buscaViviendaId(int id) throws Exception {
        daoManager.open();

        return daoViviendaSQL.read(id,daoManager);

    }

    //Método que devuelve una vivienda buscada por id en un propietario concreto.
    public Vivienda buscaViviendaByIdAndPropietario(int id, Propietario propietario) throws Exception {
        daoManager.open();

        return daoViviendaSQL.readAllByIdAndPropietario(propietario,id, daoManager);
    }

    //Método que devuelve las reservas de un usuario.
    public ArrayList<Reserva> getReservasByUser(User user) throws Exception {
        daoManager.open();

        return daoReservaSQL.readAllByUser(user,daoManager);
    }

    //Método que permite modificar una vivienda. Si no se encuentra esa vivienda devuelve false.
    public boolean modificaVivienda(Vivienda viviendaNueva) throws Exception {
        daoManager.open();
        return daoViviendaSQL.update(viviendaNueva,daoManager);
    }

    //Método que devuelve todas las viviendas que contengan en su descripción
    public ArrayList<Vivienda> buscaViviendaByParametro(String busqueda,String parametro) throws Exception {
        daoManager.open();
        return daoViviendaSQL.readByParameter(busqueda, parametro,daoManager);
    }

    //Método que genera id para una reserva entre 6000 y 7000.
    public int generaIdReserva() throws Exception {
        int id;
        boolean repetido;

        daoManager.open();

        do {
            repetido = false;
            id = (int) (Math.random() * 1000)+5000;

            if (daoPropietarioSQL.read(id,daoManager) ==null) return id;

        } while (repetido);

        return -1;
    }

    //Método para añadir una nueva reserva. Si el user es null es una reserva de periodo de no disponibilidad
    public boolean addReserva(Reserva reserva, Propietario propietario) throws Exception {
//        v.getReservas().add(reserva);
        daoManager.open();
        return daoReservaSQL.insert(reserva, propietario,daoManager);
    }

    //Método para buscar un usuario por su Id.
    private User buscaUserById(int idUsuario) throws Exception {
        daoManager.open();
        return daoUserSQL.read(idUsuario,daoManager);
    }

    //Método que devuelve el propietario de una vivienda mediante su id.
    public Propietario buscaPropietarioIdVivienda(int id) throws Exception {
        daoManager.open();
        return daoPropietarioSQL.read(daoViviendaSQL.readToIdPropietario(id,daoManager),daoManager);

    }

    public void enviaCorreo(String asunto, String contenido, String email) {
        Comunications.enviarCorreo(asunto, contenido, email);
    }

    public void guardarDatos() {
        Saves.generarBackUp(this, "data");
    }

    public ArrayList<String> obtenerConfiguracion() {
        ArrayList<String> resultado = new ArrayList<>();
        // Obtenemos todas las claves
        for (String clave : p.stringPropertyNames()) {
            // Obtener el valor correspondiente a la clave
            String valor = p.getProperty(clave);

            // Mostramos la clave y el valor
            String contenido = clave + ", "+valor;
            resultado.add(contenido);
        }
        return resultado;
    }

    public void escribirLogUsuario(String nuevaReserva, User user, int idVivienda, Reserva r) {
        Saves.escribeLogUsuario("nueva reserva", user, r.getIdVivienda());
    }

    public void escribirLogAdmin(String inicioSesion, Admin admin) {
        Saves.escribeLogAdministrador("inicio sesion", admin);
    }

    public boolean generarBackUp(Controller c, String ruta) {
        return Saves.generarBackUp(c, ruta);
    }

    public boolean existeBackUp(String ruta) {
        return Saves.existeBackUp(ruta);
    }

    public Object recuperarBackUp(String ruta) {
        return Saves.recuperaBackUp(ruta);
    }

    public void escribirLogPropietario(String inicioSesion, Propietario propietario, int i) {
        Saves.escribeLogPropietario("inicio sesion", propietario, -1);
    }

    public void enviaCorreoCSV(String asunto, ArrayList<Vivienda> viviendas) {
        Comunications.enviarCorreoCVS(asunto, viviendas);
    }

    public void enviarReservaPDF(Reserva reserva) {
        Comunications.enviarCorreoPDF("Resumen de la reserva",reserva);
    }

    public void enviarResumenVivienda(Reserva r, User user) {
        Comunications.eniarResumenReserva(r, user);
    }

    public String mostrarUltimoAcceso(Persona p) {
        return (String) (lastAccess.get(p.getEmail()));
    }

    public boolean tieneDisponibilidad(LocalDate fecha, int noches, Vivienda v) throws Exception {
        daoManager.open();
        ArrayList<Reserva> reservas = daoReservaSQL.readAllByVivienda(v,daoManager);
        return v.tieneDisponibilidad(fecha, noches, reservas);
    }

    public boolean addVivienda(Propietario propietario, Vivienda temp) throws Exception {
        daoManager.open();
        return daoViviendaSQL.insert(temp,propietario,daoManager);
    }

    public boolean modificaReserva(Reserva reserva) throws Exception {
        daoManager.open();

        return daoReservaSQL.update(reserva,daoManager);
    }

    public boolean itsActive(Object user) {
        if (user instanceof User) return daoUserSQL.isActive((User) user,daoManager);
        if (user instanceof Propietario) return daoPropietarioSQL.isActive((Propietario) user, daoManager);
        if (user instanceof Admin) return daoAdminSQL.isActive((Admin) user, daoManager);
        return false;
    }

    public void modificaUsuario(Controller c, User user) throws Exception {
        daoManager.open();
        daoUserSQL.update(user,daoManager);
    }

    public void modificaPropietario(Controller c, Propietario user) throws Exception {
        daoManager.open();
        daoPropietarioSQL.update(user,daoManager);
    }

    public void modificaAdmin(Controller c, Admin user) throws Exception {
        daoManager.open();
        daoAdminSQL.update(user,daoManager);
    }

    public void modificaUsuarioActiva(Controller c, User user) throws Exception {
        daoManager.open();
        daoUserSQL.updateActivo(user,daoManager);
    }

    public void modificaAdminActiva(Controller c, Admin user) throws Exception {
        daoManager.open();
        daoAdminSQL.updateActivo(user,daoManager);
    }

    public void modificaPropietarioActiva(Controller c, Propietario user) throws Exception {
        daoManager.open();
        daoPropietarioSQL.updateActivo(user,daoManager);
    }
}
