package org.example.dao;

import org.example.models.Admin;
import org.example.models.Propietario;

import java.util.ArrayList;

public interface DAOPropietario {
    public boolean insert(Propietario p, DAOManager dao);
    public boolean update(Propietario p, DAOManager dao);
    public boolean delete(Propietario p, DAOManager dao);
    public Propietario read(int id, DAOManager daoManager);
    public ArrayList<Propietario> readAll(DAOManager daoManager);
}
