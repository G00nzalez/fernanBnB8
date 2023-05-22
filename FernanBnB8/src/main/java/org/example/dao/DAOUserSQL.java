package org.example.dao;

import org.example.models.Admin;
import org.example.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOUserSQL implements DAOUser{
    @Override
    public boolean insert(User u, DAOManager dao) {

        String sql = "INSERT INTO user VALUES (" +
                u.getId() + ",'" +
                u.getNombre() + "','" +
                u.getClave() + "','" +
                u.getEmail() +"',0);";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(User u, DAOManager dao) {
        String sql = "UPDATE user SET " +
                "id=" + u.getId() +
                ",nombre='" + u.getNombre() +
                "',clave='" + u.getClave() +
                "',email='" + u.getEmail() + "' " +
                "WHERE id="+u.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(User u, DAOManager dao) {
        String sql = "DELETE FROM user WHERE id=" +u.getId() +";";

//        System.out.println(sql);

        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public User read(int id, DAOManager daoManager) {
        String sql = "SELECT * FROM user WHERE id= "+id+";";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return u;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<User> readAll(DAOManager daoManager) {
        ArrayList<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user;";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()){
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    users.add(u);
                }
            }

            return users;

        } catch (SQLException e) {
            return null;
        }

    }

    public User readByEmail(String email, DAOManager daoManager) {
        String sql = "SELECT * FROM user WHERE email='"+email+"';";

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return u;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public User log(String email, String passw, DAOManager daoManager) throws Exception {
        daoManager.open();
        String sql = "SELECT * FROM user WHERE email='"+email+"' AND clave='"+passw+"';";
//        System.out.println(sql);

        try {
            Statement stmt = daoManager.getConn().prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));

                    return u;
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public boolean existEmail(String email, DAOManager daoManager) {
        String sql = "SELECT * FROM user WHERE email='"+email+"';";
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

    public boolean isActive(User user,DAOManager daoManager) {
        String sql = "SELECT activo FROM user WHERE email='"+user.getEmail()+"' AND clave='"+user.getClave()+"';";

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

    public boolean updateActivo(User u, DAOManager daoManager) {
        String sql = "UPDATE user SET " +
                "activo= 1 " +
                "WHERE id="+u.getId()+";";

//        System.out.println(sql);

        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}

