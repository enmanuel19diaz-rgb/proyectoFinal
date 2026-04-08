package com.colmado.dao;


import com.colmado.util.ConexionDB;
import com.colmado.modelo.Usuario;

import java.sql.*;

public class UsuarioDAO {

    private Connection con;

    public UsuarioDAO(){
        this.con = ConexionDB.getInstancia().getConexion();
    }

   public Usuario login(String username, String password){
        String sql = "SELECT * FROM usuarios WHERE username=? and password=?";

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Usuario.rol.valueOf(rs.getString("rol").trim().toUpperCase())
                );
            }
        } catch (Exception e){
            System.out.println("error al hacer el login " + e.getMessage());
        }
        return null;
   }
}
