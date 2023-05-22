package org.example.dao;


import org.example.models.Admin;
import org.example.models.User;

import java.util.ArrayList;

public interface DAOUser {
    public boolean insert(User u, DAOManager dao);
    public boolean update(User u, DAOManager dao);
    public boolean delete(User u, DAOManager dao);
    public User read(int id, DAOManager daoManager);
    public ArrayList<User> readAll(DAOManager daoManager);
}
