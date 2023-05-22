package org.example.dao;

import org.example.models.Propietario;
import org.example.models.Vivienda;

import java.util.ArrayList;

public interface DAOVivienda {
    public boolean insert(Vivienda v, Propietario p, DAOManager dao);
    public boolean update(Vivienda v, DAOManager dao);
    public boolean delete(Vivienda v, DAOManager dao);
    public Vivienda read(int id, DAOManager daoManager);
    public ArrayList<Vivienda> readAll(DAOManager daoManager);
}
