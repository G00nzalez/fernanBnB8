package org.example.dao;

import org.example.models.Propietario;
import org.example.models.Vivienda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOViviendaSQL implements DAOVivienda{
    @Override
    public boolean insert(Vivienda v, Propietario p, DAOManager dao) {

        String sql = "INSERT INTO vivienda VALUES (" +
                v.getId() + ",'" +
                v.getTitulo() + "','" +
                v.getDescripcion() + "','" +
                v.getLocalidad() + "','" +
                v.getProvincia() + "'," +
                v.getMaxOcupantes() + "," +
                v.getPrecioNoche() + "," +
                p.getId() +");";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(Vivienda v, DAOManager dao) {
        String sql = "UPDATE vivienda SET "+
                "titulo='"+v.getTitulo() + "'," +
                "descripcion='"+v.getDescripcion() + "'," +
                "localidad='"+v.getLocalidad() + "'," +
                "provincia='"+v.getProvincia() + "'," +
                "maxOcupantes="+v.getMaxOcupantes() + "," +
                "precioNoche="+v.getPrecioNoche() +" " +
                "WHERE id = "+v.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Vivienda v, DAOManager dao) {
        String sql = "DELETE FROM vivienda WHERE id=" +v.getId() +";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Vivienda read(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM vivienda WHERE id= "+id+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Vivienda v = new Vivienda(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getInt("maxOcupantes"),
                            rs.getInt("precioNoche"));

                    return v;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Vivienda> readAll(DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();

        String sql = "SELECT * FROM viviendas;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Vivienda v = new Vivienda(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getInt("maxOcupantes"),
                            rs.getInt("precioNoche"));

                    viviendas.add(v);
                }
            }

            return viviendas;

        } catch (SQLException e) {
            return viviendas;
        }
    }

    public ArrayList<Vivienda> readAllByPropietario(Propietario propietario, DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();

        String sql = "SELECT * FROM vivienda WHERE idPropietario="+propietario.getId()+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Vivienda v = new Vivienda(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getInt("maxOcupantes"),
                            rs.getInt("precioNoche"));

                    viviendas.add(v);
                }
            }

            return viviendas;

        } catch (SQLException e) {
            return viviendas;
        }
    }

    public Vivienda readAllByIdAndPropietario(Propietario propietario, int id, DAOManager daoManager) {
        return null;
    }

    public ArrayList<Vivienda> readByParameter(String busqueda, String parametro, DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();

        String sql = "SELECT * FROM vivienda WHERE "+busqueda+" LIKE '%"+parametro+"%';";
//        System.out.println(sql);

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Vivienda v = new Vivienda(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getInt("maxOcupantes"),
                            rs.getInt("precioNoche"));

                    viviendas.add(v);
                }
            }

            return viviendas;

        } catch (SQLException e) {
            return viviendas;
        }
    }

    public int readToIdPropietario(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM vivienda WHERE id ="+id+";";
//        System.out.println(sql);

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    return rs.getInt("idPropietario");
                }
            }

        } catch (SQLException e) {
            return -1;
        }
        return -1;
    }
}
