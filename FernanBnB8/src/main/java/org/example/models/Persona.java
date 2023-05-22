package org.example.models;

public abstract class Persona {
    protected int id;
    protected String nombre;
    protected String clave;
    protected String email;
    protected boolean activado;

    public Persona(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        activado = false;
    }

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

    public abstract String toString();

}
