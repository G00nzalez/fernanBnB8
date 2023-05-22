package org.example.dao;

import org.example.models.Admin;
import org.example.models.Propietario;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface DAOAdmin {
    public boolean insert(Admin a, DAOManager dao);
    public boolean update(Admin a, DAOManager dao);
    public boolean delete(Admin a, DAOManager dao);
    public Admin read(int id, DAOManager daoManager);
    public ArrayList<Admin> readAll(DAOManager daoManager);
}
