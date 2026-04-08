package com.colmado.util;

import com.colmado.dao.UsuarioDAO;
import com.colmado.modelo.Usuario;

import java.sql.Connection;
import java.sql.*;

public class testConexion {
    public static void main(String[] args) {
        /*Connection con = ConexionDB.getInstancia().getConexion();
        if(con != null){
            System.out.println("coneccion exitosa");
        } else{
            System.out.println("error al conectar");
        }*/

        UsuarioDAO us = new UsuarioDAO();
        Usuario sd = us.login("admin", "1234");

             if (sd != null) {
                 System.out.println("usuario ok " + sd);
             } else {
                 System.out.println("usuario incorrecto, no encontrado en la base de datos ");
             }

    }
}
