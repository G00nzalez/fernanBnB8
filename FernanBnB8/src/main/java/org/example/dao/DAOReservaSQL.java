package org.example.dao;

import org.example.models.Propietario;
import org.example.models.Reserva;
import org.example.models.User;
import org.example.models.Vivienda;

import javax.lang.model.type.ArrayType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOReservaSQL implements DAOReserva{
    @Override
    public boolean insert(Reserva r, Propietario p,DAOManager dao) {

        String sql = "INSERT INTO reserva VALUES (" +
                r.getId() + ",'" +
                r.getFechaInicio() + "','" +
                r.getNoches() + "'," +
                r.getPrecio() + "," +
                r.getOcupantes() + "," +
                r.getIdVivienda() + "," +
                r.getIdUsuario() + "," +
                p.getId()+");";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(Reserva r, DAOManager dao) {
        String sql = "UPDATE reserva SET "+
                "fechaInicio='"+r.getFechaInicio() + "'," +
                "noches="+r.getNoches() + "," +
                "precio="+r.getPrecio() + "," +
                "ocupantes=" + r.getOcupantes() + " " +
                "WHERE id = "+r.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Reserva r, DAOManager dao) {
        String sql = "DELETE FROM reserva WHERE id=" +r.getId() +";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Reserva read(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM reserva WHERE id= "+id+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Reserva r = new Reserva(
                            rs.getInt("id"),
                            rs.getDate("fechaInicio").toLocalDate(),
                            rs.getInt("noches"),
                            rs.getDouble("precio"),
                            rs.getInt("ocupantes"),
                            rs.getInt("idVivienda"),
                            rs.getInt("idUser"));

                    return r;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Reserva> readAll(DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();

        String sql = "SELECT * FROM reserva;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Reserva r = new Reserva(
                            rs.getInt("id"),
                            rs.getDate("fechaInicio").toLocalDate(),
                            rs.getInt("noches"),
                            rs.getDouble("precio"),
                            rs.getInt("ocupantes"),
                            rs.getInt("idVivienda"),
                            rs.getInt("idUser"));

                    reservas.add(r);
                }
            }

            return reservas;

        } catch (SQLException e) {
            return reservas;
        }
    }

    public ArrayList<Reserva> readAllByUser(User user, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva WHERE idUser= "+user.getId()+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Reserva r = new Reserva(
                            rs.getInt("id"),
                            rs.getDate("fechaInicio").toLocalDate(),
                            rs.getInt("noches"),
                            rs.getDouble("precio"),
                            rs.getInt("ocupantes"),
                            rs.getInt("idVivienda"),
                            rs.getInt("idUser"));

                    reservas.add(r);

                }

            }
            return reservas;

        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<Reserva> readAllByVivienda(Vivienda v, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva WHERE idVivienda= "+ v.getId()+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Reserva r = new Reserva(
                            rs.getInt("id"),
                            rs.getDate("fechaInicio").toLocalDate(),
                            rs.getInt("noches"),
                            rs.getDouble("precio"),
                            rs.getInt("ocupantes"),
                            rs.getInt("idVivienda"),
                            rs.getInt("idUser"));

                    reservas.add(r);

                }

            }
            return reservas;

        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<Reserva> readAllByPropietario(Propietario propietario, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva WHERE idVivienda=;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Reserva r = new Reserva(
                            rs.getInt("id"),
                            rs.getDate("fechaInicio").toLocalDate(),
                            rs.getInt("noches"),
                            rs.getDouble("precio"),
                            rs.getInt("ocupantes"),
                            rs.getInt("idVivienda"),
                            rs.getInt("idUser"));

                    reservas.add(r);

                }

            }
            return reservas;

        } catch (SQLException e) {
            return null;
        }
    }
}
