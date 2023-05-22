package org.example.dao;

import org.example.models.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DaoAdminSQL implements DAOAdmin {
    @Override
    public boolean insert(Admin a, DAOManager dao) {

        String sql = "INSERT INTO admin VALUES (" +
                a.getId() + ",'" +
                a.getNombre() + "','" +
                a.getClave() + "','" +
                a.getEmail() + "'," + "1);";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(Admin a, DAOManager dao) {
        String sql = "UPDATE admin SET " +
                "id=" + a.getId() +
                ",nombre='" + a.getNombre() +
                "',clave='" + a.getClave() +
                "',email='" + a.getEmail() + "',activo=1 " +
                "WHERE id="+a.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Admin a, DAOManager dao) {
        String sql = "DELETE FROM admin WHERE id=" +a.getId() +";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Admin read(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM admin WHERE id= "+id+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Admin a = new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return a;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<Admin> readAll(DAOManager daoManager) {
        ArrayList<Admin> admins = new ArrayList<>();

        String sql = "SELECT * FROM admin;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    Admin a = new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    admins.add(a);
                }
            }

            return admins;

        } catch (SQLException e) {
            return null;
        }

    }

    //Método para leer un admin por email.
    public Admin readByEmail(String email, DAOManager daoManager) {
        String sql = "SELECT * FROM admin WHERE email='"+email+"';";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Admin a = new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return a;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public Admin log(String mail, String passw, DAOManager daoManager) throws Exception {
        daoManager.open();
        String sql = "SELECT * FROM admin WHERE email='"+mail+"' AND clave='"+passw+"';";
//        System.out.println(sql);

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    Admin a = new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return a;
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

    public boolean isActive(Admin user, DAOManager daoManager) {
        String sql = "SELECT activo FROM admin WHERE email='"+user.getEmail()+"' AND clave='"+user.getClave()+"';";

//        System.out.println(sql);

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

    public boolean updateActivo(Admin a, DAOManager daoManager) {

        String sql = "UPDATE admin SET " +
                "activo = 1" +
                "WHERE id="+a.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
