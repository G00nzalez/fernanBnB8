package org.example;

import org.example.controller.Controller;
import org.example.models.*;
import org.example.saves.Saves;
import org.example.utils.Utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public final static String RUTA_PROPERTIES = "configuracion/config.properties";
    public final static String RUTA_LASTACCESS = "configuracion/lastaccess.properties";

    public static Scanner s = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        Properties lastAccess = new Properties();

        try {
            properties.load(new FileReader(RUTA_PROPERTIES));
            lastAccess.load(new FileReader(RUTA_LASTACCESS));

        } catch (IOException e) {
            System.out.println("ERROR");
        }


        Controller c = new Controller(properties, lastAccess);
        //mock(c);

        Object user = null;

        int op = -1;

        do {
            op = menuInicio();
            switch (op) {
                case 1 -> { //Loggear un usuario,
                    user = login(c);
                    if (user == null) System.out.println("Credenciales incorrectas");
                }

                case 2 -> { //Registrar un usuario nuevo,
                    user = registro(c);
                    if (user == null)
                        System.out.println("No se creó ningún usuario.");
                }

                case -1 -> {
                    System.out.println("Debe introducir un número para este menú");
                }

                case 3 -> { //Cerrar programa,
                    System.out.println("Cerrando programa");
                    Utils.cerrarPrograma();
                }
                default -> System.out.println("Opción incorrecta.");
            }

            // Si el usuario ya está loggeado
            if (user != null) {
                if (c.itsActive(user)) {
                    if (user instanceof User) menuUsuario(c, (User) user, properties);
                    if (user instanceof Propietario) menuPropietario(c, (Propietario) user, properties);
                    if (user instanceof Admin) menuAdmin(c, (Admin) user, properties);

                } else {
                    int tokenGenerado = generaToken();
                    Persona persona = (Persona) user;
                    c.enviaCorreo("Token", "Su token de verificación es: " + tokenGenerado, persona.getEmail());
                    System.out.print("Su usuario no está activado, introduzca el token: ");
                    int token = Integer.parseInt(s.nextLine());
                    if (token == tokenGenerado) {
                        if (user instanceof User) c.modificaUsuarioActiva(c, (User) user);
                        if (user instanceof Propietario) c.modificaPropietarioActiva(c, (Propietario) user);
                        if (user instanceof Admin) c.modificaAdminActiva(c, (Admin) user);
                    } else System.out.println("Token incorrecto");
                }
            }

            user = null;

        } while (op != 3);

    }

    //mock
    private static void mock(Controller c) {
        c.addAdministrador("admin", "admin", "admin@fernanbnb.com");
        c.addAdministrador("admin2", "admin", "admin2@fernanbnb.com");
        c.addUsuario("user", "user", "user@fernanbnb.com");
        c.addUsuario("user2", "user", "user2@fernanbnb.com");
        c.addPropietario("propietario", "propietario", "propietario@fernanbnb.com");
        c.addPropietario("propietario2", "propietario", "propietario2@fernanbnb.com");

    }

    //Registro de un nuevo usuario.
    private static Object registro(Controller c) throws Exception {
        int op = 0;
        String nombre, passw, email;

        System.out.println("===== REGISTRO =====");
        do {
            System.out.println("""
                    Seleccione su tipo de usuario
                    1. Usuario.
                    2. Propietario.
                    3. Cancelar.""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción incorrecta, debe introducir un número");
            }

        } while (op < 0 || op > 3 && op != 100);

        if (op != 3) {
            System.out.print("Nombre de usuario: ");
            nombre = s.nextLine();
            System.out.print("Contraseña: ");
            passw = s.nextLine();
            System.out.print("Email: ");
            email = s.nextLine();

            if (c.existeEmail(email)) return null;

            if (op == 1) return c.addUsuario(nombre, passw, email);
            if (op == 2) return c.addPropietario(nombre, passw, email);
            if (op == 100) return c.addAdministrador(nombre, passw, email);

        }

        return null;
    }

    //Log de usuario.
    private static Object login(Controller c) throws Exception {
        String passw, email;

        System.out.println("===== LOGGING =====");

        System.out.print("Introduzca su email: ");
        email = s.nextLine();
        System.out.print("Contraseña: ");
        passw = s.nextLine();


        if (c.itsActive(c.login(email, passw))) return c.login(email, passw);
        return null;

    }

    //Menu para los administradores.
    private static void menuAdmin(Controller c, Admin admin, Properties p) throws Exception {
        c.escribirLogAdmin("inicio sesion", admin);

        int op = 0;

        do {
            System.out.println("^^^^ MENU ADMINISTRADOR ^^^^");
            System.out.println("Bienvenido: " + admin.getNombre());
            System.out.println("Tiene " + c.numeroUsuarios() + " usuarios. Viviendas " + c.numeroViviendas() + ". Reservas " + c.numeroReservas());
            System.out.println("Último acceso: " + c.mostrarUltimoAcceso((Persona) admin));
            System.out.println("""
                    Menú de operaciones:
                    1. Ver todas las viviendas en alquiler.
                    2. Ver todos los usuarios.
                    3. Ver todas las reservas.
                    4. Ver perfil.
                    5. Modificar perfil.
                    6. Mostrar configuración.
                    7. Enviar listado de reservas por correo.
                    8. Realizar copia de seguridad.
                    9. Recuperar copia de seguridad.
                    10. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Ver todas las viviendas
                    ArrayList<Vivienda> mostrarViviendas = c.getAllViviendas();
                    if (mostrarViviendas.isEmpty()) System.out.println("Aún no se ha registrado ninguna vivienda");
                    else System.out.println(mostrarViviendas);
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Ver todos los usuarios
                    //Mostramos los usuarios normales.
                    ArrayList<User> usuarios = c.getAllUsuarios();
                    if (usuarios.isEmpty()) System.out.println("Aún no se ha registrado ningún usuario.");
                    else {
                        for (User u :
                                usuarios) {
                            System.out.println(u);
                        }
                    }
                    System.out.println();

                    //Mostramos los propietarios.
                    ArrayList<Propietario> propietarios = c.getAllPropietarios();
                    if (propietarios.isEmpty()) System.out.println("Aún no se ha registrado ningún propietario.");
                    else {
                        for (Propietario pr :
                                propietarios) {
                            System.out.println(pr);
                        }
                    }
                    System.out.println();
                    //Mostramos los admins
                    ArrayList<Admin> admins = c.getAllAdmins();
                    if (admins.isEmpty()) System.out.println("Aún no se ha registrado ningún administrador.");
                    else {
                        for (Admin a :
                                admins) {
                            System.out.println(a);
                        }
                    }
                    Utils.pulseParaContinuar();
                }
                case 3 -> { //Ver todas las reservas
                    ArrayList<Reserva> reservas = c.getReservas();
                    if (reservas.isEmpty()) System.out.println("No se ha realizado ninguna reserva aún.");
                    else {
                        for (Reserva r :
                                reservas) {
                            System.out.println(r);
                        }
                    }
                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Ver perfil
                    System.out.println(admin);
                    Utils.pulseParaContinuar();
                }
                case 5 -> { //Modificar perfil
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    Admin temp = new Admin(admin.getId(), nuevoNombre, nuevoPassw, nuevoEmail);

                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Mostrar configuración. También muestra la última conexión de cada usuario.
                    System.out.println("*** Configuración ***");
                    ArrayList<String> configuracion = c.obtenerConfiguracion();

                    for (int i = 0; i < configuracion.size(); i++) {
                        System.out.println(configuracion.get(i));
                        System.out.println("-----");
                    }

                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Enviar reservas al correo.
                    System.out.println("*** Envío de reservas al correo ***");
                    ArrayList<Propietario> propietarios = c.getPropietarios();
                    ArrayList<Vivienda> viviendas = new ArrayList<>();

                    //Obtenemos todas las viviendas registradas en el sistema.
                    for (Propietario propietario : propietarios
                    ) {
                        viviendas.addAll(propietario.getViviendas());
                    }

                    if (viviendas.isEmpty()) System.out.println("No existen viviendas registradas aún.");
                    else {
                        c.enviaCorreoCSV("Resumen de todas las viviendas", viviendas);
                        System.out.println("Resumen de todas las viviendas enviadas a su correo.");
                    }

                    Utils.pulseParaContinuar();
                }
                case 8 -> { //Realizar copia de seguridad.
                    System.out.println("*** Copia de Seguridad ***");

                    System.out.print("Introduzca la ruta donde desea generar la copia de seguridad (Esta ruta debe de ser escrita totalmente a mano, ej: 'C:/copias'): ");
                    String ruta = s.nextLine();

                    if (c.generarBackUp(c, ruta))
                        System.out.println("Copia de seguridad generada con éxito en: " + ruta + "/backup.out");
                    else System.out.println("No se pudo realizar la copia de seguridad.");

                    Utils.pulseParaContinuar();
                }
                case 9 -> { //Recuperar copia de seguridad
                    System.out.println("*** Recuperación de copia de seguridad ***");

                    System.out.print("Introduzca la ruta donde desea generar la copia de seguridad: ");
                    String ruta = s.nextLine();


                    if (c.existeBackUp(ruta)) {
                        if (c.recuperarBackUp(ruta) != null) {
                            c = (Controller) c.recuperarBackUp(ruta);
                            System.out.println("Copia recuperada con éxito.");
                        } else System.out.println("No se pudo recuperar la copia de seguridad");
                    } else System.out.println("Ruta incorrecta. Pruebe introduciendo otra.");

                    Utils.pulseParaContinuar();
                }
                case 10 -> { //Cerrar sesión
                    c.escribirLogAdmin("cerrar sesion", admin);
                    Saves.escribeUltimoAccesoUsuario(p, admin);
                    System.out.print("Cerrando sesión ");
                    Utils.cerrarPrograma();
                }

            }
        } while (op != 10);
    }

    //Menu para los propietarios
    private static void menuPropietario(Controller c, Propietario propietario, Properties properties) throws Exception {
        c.escribirLogPropietario("inicio sesion", propietario, -1);

        int op = 0;

        do {
            System.out.println("^^^^ MENU PROPIETARIO ^^^^");
            System.out.println("Bienvenido: " + propietario.getNombre() + ". Tiene " + c.totalViviendas(propietario) + " viviendas.");
            System.out.println("Tiene " + c.totalReservas(propietario) + " reservas en sus alojamientos.");
            System.out.println("Último acceso: " + c.mostrarUltimoAcceso(propietario));
            System.out.println("""
                    Menú de operaciones:
                    1. Ver mis viviendas en alquiler.
                    2. Editar mis viviendas.
                    3. Ver las reservas de mis viviendas.
                    4. Establecer un periodo de no disponibilidad para una vivienda.
                    5. Ver mi perfil.
                    6. Modificar mi perfil.
                    7. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Ver viviendas en alquiler
                    ArrayList<Vivienda> viviendas = c.buscaViviendasByPropietario(propietario);
                    if (!viviendas.isEmpty()) {
                        for (Vivienda v :
                                viviendas) {
                            System.out.println(v);
                        }
                    } else System.out.println("No hay viviendas registradas aún");
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Editar viviendas
                    int maxOcupantes = 0, id = -1;
                    double precioNoche = 0.0;

                    System.out.println("--- Editar una vivienda ---");
                    do {
                        System.out.print("Introduzca la id de la vivienda (pulse 0 para registrar una nueva vivienda): ");
                        try {
                            id = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (id < 0);

                    if (id == 0) System.out.println("--- Registro de nueva vivienda ---");
                    else {
                        System.out.print("¿Desea eliminar esa vivienda? s/n: ");
                        //Eliminación de una vivienda.
                        if (s.nextLine().equals("s")) {
                            Vivienda v = c.buscaViviendaId(id);

                            //Comprobación de sí la vivienda existe y contiene reservas.
                            if (v != null && v.getReservas().isEmpty()) {
                                propietario.getViviendas().remove(v);
                                System.out.println("Se ha eliminado la vivienda con id " + id);
                                c.escribirLogPropietario("eliminar vivienda", propietario, id);

                            } else
                                System.out.println("No se puede eliminar una vivienda que tenga reservas realizadas.");
                            break;
                        }
                    }
                    System.out.print("Introduce el título: ");
                    String titulo = s.nextLine();
                    System.out.print("Introduce la descripción: ");
                    String descripcion = s.nextLine();
                    System.out.print("Introduce la localidad: ");
                    String localidad = s.nextLine();
                    System.out.print("Introduce la provincia: ");
                    String provincia = s.nextLine();
                    do {
                        System.out.print("Introduce el número máximo de ocupantes: ");
                        try {
                            maxOcupantes = Integer.parseInt(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (maxOcupantes < 1);

                    do {
                        System.out.print("Introduce el precio por noche: ");
                        try {
                            precioNoche = Double.parseDouble(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (precioNoche < 1);

                    //Si la id = 0 significa que es una vivienda nueva, entonces hay que generarle una id nuevo. Si es!= 0, introducimos la id que
                    Vivienda temp;
                    if (id == 0) {
                        temp = new Vivienda(c.generaIdVivienda(), titulo, descripcion, localidad, provincia, maxOcupantes, precioNoche);
                    } else {
                        temp = c.buscaViviendaByIdAndPropietario(id, propietario);
                    }

                    if (temp == null) System.out.println("Vivienda no encontrada. Compruebe la id de su vivienda.");
                    else {

                        if (c.addVivienda(propietario, temp)) {
                            if (id == 0) {
                                System.out.println("Vivienda registrada con éxito");
                                c.escribirLogPropietario("insertar vivienda", propietario, temp.getId());
                            } else System.out.println("Vivienda modificada con éxito.");
                        } else {
                            if (id == 0) System.out.println("Error al registrar su vivienda.");
                            else System.out.println("Error al modificar su vivienda. ");
                        }
                    }
                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 3 -> { //Ver reservas en viviendas
                    ArrayList<Vivienda> viviendas = c.getViviendasReservadasPropietario(propietario);
                    if (viviendas.isEmpty()) System.out.println("No han realizado ninguna reserva aún.");
                    for (Vivienda v :
                            viviendas) {
                        System.out.println(v);
                    }
                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Establecer periodo de no disponibilidad
                    int id = -1;
                    System.out.println("=== PERIODO NO DISPONIBILIDAD ===");
                    do {
                        System.out.print("Introduce la id de la vivienda (0 para cancelar): ");
                        try {
                            id = Integer.parseInt(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                        if (id == 0) break;

                    } while (id < 5000 || id > 6000);

                    Vivienda v = c.buscaViviendaId(id);
                    if (v != null) {
                        int day = -1, month = -1, year = -1, noches = -1;
                        System.out.print("Introduzca el día de entrada: ");
                        try {
                            day = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el mes de entrada: ");
                        try {
                            month = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el año de entrada: ");
                        try {
                            year = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el número de noches: ");
                        try {
                            noches = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }

                        LocalDate fecha = LocalDate.of(year, month, day);

                        if (c.tieneDisponibilidad(fecha, noches, v)) {
                            //c.addReserva(v, null, new Reserva(c.generaIdReserva(), v.getId(), propietario.getId(), fecha, noches, v.getPrecioNoche(), -1));
                            c.escribirLogPropietario("indisponibilidad", propietario, v.getId());
                            c.guardarDatos();
                        } else System.out.println("No se ha podido realizar la operación por falta de días hábiles.");
                    } else System.out.println("Vivienda no encontrada. Compruebe que ha insertado bien la id.");
                }
                case 5 -> { //Ver perfil
                    System.out.println(propietario);
                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Modificar perfil
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    Propietario temp = new Propietario(propietario.getId(), nuevoNombre, nuevoPassw, nuevoEmail);
                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Cerrar
                    c.escribirLogPropietario("cerrar sesion", propietario, -1);
                    System.out.print("Cerrando sesión ");
                    Saves.escribeUltimoAccesoUsuario(properties, propietario);
                    c.guardarDatos();
                    Utils.cerrarPrograma();
                }
            }
        } while (op != 7);
    }

    //Menu para los usuarios.
    private static void menuUsuario(Controller c, User user, Properties p) throws Exception {
        Saves.escribeLogUsuario("inicio sesion", user, -1);
        Saves.escribeUltimoAccesoUsuario(p, user);
        Saves.escribeProperties(RUTA_LASTACCESS, user.getEmail() + "LastAccess", Utils.formateaFecha(LocalDate.now()));
        int op = 0;

        do {
            System.out.println("^^^^ MENU USUARIO ^^^^");
            System.out.println("Bienvenido: " + user.getNombre() + ". Tiene " + c.totalReservasUsuario(user) + " reservas pendientes.");
            System.out.println("Último acceso: " + c.mostrarUltimoAcceso(user));
            System.out.println("""
                    Menú de operaciones:
                    1. Búsqueda de alojamientos.
                    2. Ver mis reservas.
                    3. Modificar mis reservas.
                    4. Ver mi perfil.
                    5. Modificar mis datos.
                    6. Enviar reserva como PDF.
                    7. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Búsqueda de alojamientos.

                    int opBusqueda = 0;
                    String busqueda = "";
                    do {
                        System.out.print("=== BUSQUEDA DE ALOJAMIENTOS ===");
                        System.out.println("""

                                1. Busqueda por título.
                                2. Búsqueda por descripción.
                                3. Búsqueda por localidad.
                                4. Búsqueda por provincia.
                                Introduce una opción: """);

                        try {
                            opBusqueda = Integer.parseInt(s.nextLine());
                            switch (opBusqueda) {
                                case 1 -> { //Busqueda por titulo
                                    busqueda = "titulo";
                                }
                                case 2 -> { //Busqueda por descripcion
                                    busqueda = "descripcion";
                                }
                                case 3 -> { //Busqueda por localidad
                                    busqueda = "localidad";
                                }
                                case 4 -> { //Busqueda por provincia
                                    busqueda = "provincia";
                                }
                                default -> System.out.println("Introduce un opción válida");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Introduce un opción válida");
                        }


                    } while (opBusqueda < 1 || opBusqueda > 4);


                    System.out.print("Introduzca una descripción con la cual filtrar: ");
                    String parametro = s.nextLine();

                    ArrayList<Vivienda> viviendas = c.buscaViviendaByParametro(busqueda, parametro);
                    if (viviendas.isEmpty())
                        System.out.println("No se ha encontrado ninguna vivienda con esa busqueda.");
                    else {
                        for (int i = 0; i < viviendas.size(); i++) {
                            System.out.println((i + 1) + ") " + viviendas.get(i).getTitulo());
                        }

                        int reserva = -1;

                        do {
                            System.out.print("¿Qué vivienda quiere reserva? Introduzca 0 para cancelar la reserva: ");
                            try {
                                reserva = Integer.parseInt(s.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }

                            if (reserva > viviendas.size() + 1) System.out.println("Número no válido.");

                        } while (reserva == -1 || reserva > viviendas.size());

                        if (reserva == -1) System.out.println("Se ha cancelado su reserva.");
                        else {
                            int ocupantes = -1;
                            Vivienda v = viviendas.get(reserva);

                            do {
                                System.out.print("Introduzca el número de ocupantes: ");
                                try {
                                    ocupantes = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }

                            } while (ocupantes < 0);
                            if (ocupantes > v.getMaxOcupantes()) {
                                System.out.println("Ha introducido más ocupantes de los permitidos");
                                break;
                            } else {
                                int day = -1, month = -1, year = -1, noches = -1;
                                System.out.print("Introduzca el día de entrada: ");
                                try {
                                    day = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el mes de entrada: ");
                                try {
                                    month = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el año de entrada: ");
                                try {
                                    year = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el número de noches: ");
                                try {
                                    noches = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }

                                LocalDate fecha = LocalDate.of(year, month, day);
                                if (c.tieneDisponibilidad(fecha, noches, v)) {

                                    Reserva r = new Reserva(c.generaIdReserva(), fecha, noches, v.getPrecioNoche(), ocupantes, v.getId(), user.getId());
                                    Propietario propietario = c.buscaPropietarioIdVivienda(r.getIdVivienda());

                                    if (c.addReserva(r, propietario))
                                        System.out.println("Reserva realizada con éxito.");
                                    else System.out.println("Error al realizar la reserva.");

                                    c.escribirLogUsuario("nueva reserva", user, r.getIdVivienda(), r);
                                    c.enviarResumenVivienda(r, user);

                                    System.out.print("Enviando PDF");
                                    Utils.cerrarPrograma();
                                } else System.out.println("Reserva no disponible.");

                            }
                        }
                    }

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Ver reservas.
                    ArrayList<Reserva> reservas = c.getReservasByUser(user);

                    if (!reservas.isEmpty()) {

                        for (Reserva r :
                                reservas) {
                            System.out.println(r);
                        }
                    } else System.out.println("No se han realizado reservas todavía.");

                    Utils.pulseParaContinuar();

                }
                case 3 -> { //Modificar reservas.
                    ArrayList<Reserva> reservas = c.getReservasByUser(user);
                    int reservaSeleccionada = -1;
                    if (!reservas.isEmpty()) {

                        for (int i = 0; i < reservas.size(); i++) {
                            System.out.println("Reserva número: " + (i + 1) + '\n'
                                    + reservas.get(i) + '\n' +
                                    "=========");
                        }

                        do {
                            System.out.print("Indica que reserva quieres modificar: ");
                            try {
                                reservaSeleccionada = Integer.parseInt(s.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }
                        } while (reservaSeleccionada == -1 || reservaSeleccionada > reservas.size());

                        int id = -1;

                        System.out.println("=== MODIFICACIÓN DE RESERVA ===");

                        do {
                            System.out.print("Introduce la id de la vivienda (pulse 0 para borrar la reserva):  ");
                            try {
                                id = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico.");
                            }
                        } while (id == -1);

                        if (id == 0) {
                            if (user.deleteReserva(user.getReservas().get(reservaSeleccionada)))
                                System.out.println("Reserva eliminada con éxito.");
                            else System.out.println("Error al eliminar la reserva.");
                            break;
                        } else {
                            int day = -1, month = -1, year = -1;
                            try {
                                System.out.print("Introduce la nueva fecha de entrada(día): ");
                                day = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico");
                            }
                            try {
                                System.out.print("Introduce la nueva fecha de entrada(mes): ");
                                month = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico");
                            }
                            try {
                                System.out.print("Introduce la nueva fecha de entrada(año): ");
                                year = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico");
                            }
                            LocalDate fecha = LocalDate.of(year, month, day);
                            int numNoches = -1, ocupantes = -1;
                            try {
                                System.out.print("Introduce el número de noches: ");
                                numNoches = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico");
                            }
                            try {
                                System.out.print("Introduce el número de ocupantes: ");
                                ocupantes = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico");
                            }

                            Vivienda v = c.buscaViviendaId(id);
                            if (v == null) System.out.println("No se ha encontrado la vivienda.");
                            else {
                                Reserva reserva = new Reserva(id, fecha, numNoches, v.getPrecioNoche() * numNoches, ocupantes, v.getId(), user.getId());
                                if (c.modificaReserva(reserva)) System.out.println("Reserva modificada con éxito.");
                                else System.out.println("Error al modificar la reserva.");
                            }
                        }

                    } else System.out.println("No ha realizado reservas aún.");


                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Ver perfil
                    System.out.println(user);
                    Utils.pulseParaContinuar();

                }
                case 5 -> { //Modificar datos
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    User temp = new User(user.getId(), nuevoNombre, nuevoPassw, nuevoEmail);
                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Enviar reserva como PDF.
                    System.out.println("*** Enviar reserva al PDF ***");

                    ArrayList<Reserva> reservas = c.getReservasByUser(user);
                    int reservaSeleccionada = -1;
                    if (!reservas.isEmpty()) {

                        for (int i = 0; i < reservas.size(); i++) {
                            System.out.println("Reserva número: " + (i + 1) + '\n'
                                    + reservas.get(i) + '\n' +
                                    "=========");
                        }

                        do {
                            System.out.print("Indica que reserva quieres enviar a tu correo (Pulse 0 para cancelar): ");
                            try {
                                reservaSeleccionada = Integer.parseInt(s.nextLine()) - 1;
                                if (reservaSeleccionada == -1) {
                                    System.out.print("Operación cancelada");
                                    Utils.pulseParaContinuar();
                                    break;
                                } else {
                                    Reserva reserva = reservas.get(reservaSeleccionada);
                                    System.out.print("Generando pdf ");
                                    Utils.cerrarPrograma();
                                    c.enviarResumenVivienda(reserva, user);
                                    System.out.println("PDF enviado a su correo.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }
                        } while (reservaSeleccionada == -1 || reservaSeleccionada > reservas.size());

                    } else System.out.println("Usted no ha realizado ninguna reserva aún.");

                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Salir
                    System.out.print("Cerrando sesión ");
                    Saves.escribeLogUsuario("cerrar sesion", user, -1);
//                    c.guardarDatos();
                    Utils.cerrarPrograma();
                    System.out.println();
                }
            }

        } while (op != 7);
    }

    //Menu de inicio.
    private static int menuInicio() {
        System.out.println("""
                 _______  _______ .______      .__   __.      ___      .__   __. .______   .__   __. .______  \s
                |   ____||   ____||   _  \\     |  \\ |  |     /   \\     |  \\ |  | |   _  \\  |  \\ |  | |   _  \\ \s
                |  |__   |  |__   |  |_)  |    |   \\|  |    /  ^  \\    |   \\|  | |  |_)  | |   \\|  | |  |_)  |\s
                |   __|  |   __|  |      /     |  . `  |   /  /_\\  \\   |  . `  | |   _  <  |  . `  | |   _  < \s
                |  |     |  |____ |  |\\  \\----.|  |\\   |  /  _____  \\  |  |\\   | |  |_)  | |  |\\   | |  |_)  |\s
                |__|     |_______|| _| `._____||__| \\__| /__/     \\__\\ |__| \\__| |______/  |__| \\__| |______/ \s""");

        System.out.println("""
                                
                ======================
                1. Logging.
                2. Registrar usuario.
                3. Salir""");
        System.out.print("Introduzca una opción: ");
        try {
            return Integer.parseInt(s.nextLine());

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    //Método para generar un token aleatorio de 6 digitos
    private static int generaToken() {
        return (int) (Math.random() * 1000000);
    }
}