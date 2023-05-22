package org.example.dao;

import org.example.models.Propietario;
import org.example.models.Reserva;

import java.util.ArrayList;

public interface DAOReserva {
    public boolean insert(Reserva r, Propietario p, DAOManager dao);
    public boolean update(Reserva r, DAOManager dao);
    public boolean delete(Reserva r, DAOManager dao);
    public Reserva read(int id, DAOManager daoManager);
    public ArrayList<Reserva> readAll(DAOManager daoManager);
}
