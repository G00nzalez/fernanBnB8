package org.example.models;

import org.example.controller.Controller;

import java.io.Serializable;
import java.util.ArrayList;

public class Propietario extends Persona implements Serializable {


    //Atributos

    private ArrayList<Vivienda> viviendas;

    public Propietario(int id, String nombre, String clave, String email) {
        super(id, nombre, clave, email);
        viviendas = new ArrayList<>();
    }

    //Constructor


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

    public ArrayList<Vivienda> getViviendas() {
        return viviendas;
    }

    //Métodos
    @Override
    public String toString() {
        return "***** PROPIETARIO " + nombre + " *****"+ '\n' +
                "Contraseña: " + clave + '\n' +
                "Email: " + email + '\n' +
                "===== VIVIENDAS =====" + '\n' + ((viviendas.size() == 0 ) ? "No ha registrado ninguna vivienda" : viviendas) +'\n';
    }

}
