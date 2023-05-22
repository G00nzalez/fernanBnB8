package org.example.models;

import java.io.Serializable;

public class Admin extends Persona implements Serializable {

    //Constructor
    public Admin(int id, String nombre, String clave, String email) {
        super(id, nombre, clave, email);
    }

    //Gets
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return  "Administrador id: " + id + '\n' +
                "Usuario: " + nombre + '\n' +
                "Clave: " + clave + '\n' +
                "Email: " + email+ '\n' +
                "=========================" +'\n';
    }
}
