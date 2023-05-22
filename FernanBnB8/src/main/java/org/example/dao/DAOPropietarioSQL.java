package org.example.dao;

import org.example.models.Admin;
import org.example.models.Propietario;
import org.example.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOPropietarioSQL implements DAOPropietario{
    @Override
    public boolean insert(Propietario p, DAOManager dao) {

        String sql = "INSERT INTO propietario VALUES (" +
                p.getId() + ",'" +
                p.getNombre() + "','" +
                p.getClave() + "','" +
                p.getEmail() +"',0);";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(Propietario p, DAOManager dao) {
        String sql = "UPDATE propietario SET " +
                "id=" + p.getId() +
                ",nombre='" + p.getNombre() +
                "',clave='" + p.getClave() +
                "',email='" + p.getEmail() + "' " +
                "WHERE id="+ p.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Propietario p, DAOManager dao) {
        String sql = "DELETE FROM propietario WHERE id=" +p.getId() +";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Propietario read(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM propietario WHERE id= "+id+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Propietario p = new Propietario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return p;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Propietario> readAll(DAOManager daoManager) {
        ArrayList<Propietario> propietarios = new ArrayList<>();

        String sql = "SELECT * FROM propietario;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Propietario p = new Propietario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    propietarios.add(p);
                }
            }

            return propietarios;

        } catch (SQLException e) {
            return null;
        }

    }


    public Propietario readByEmail(String email, DAOManager daoManager) {
        String sql = "SELECT * FROM propietario WHERE email='"+email+"';";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Propietario p = new Propietario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return p;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public Propietario log(String email, String passw, DAOManager daoManager) throws Exception {
        daoManager.open();
        String sql = "SELECT * FROM propietario WHERE email='"+email+"' and clave='"+passw+"';";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Propietario p = new Propietario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return p;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public boolean existEmail(String email, DAOManager daoManager) {
        String sql = "SELECT * FROM propietario WHERE email='"+email+"';";
//        System.out.println(sql);

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean isActive(Propietario user, DAOManager daoManager) {
        String sql = "SELECT activo FROM propietario WHERE email='"+user.getEmail()+"' AND clave='"+user.getClave()+"';";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    if(rs.getInt("activo") == 0) return false;
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean updateActivo(Propietario p, DAOManager daoManager) {
        String sql = "UPDATE propietario SET " +
                "activo=1" +
                "WHERE id="+ p.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
