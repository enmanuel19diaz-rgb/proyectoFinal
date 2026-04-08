package com.colmado.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionDB {
    private static ConexionDB instancia = null;
    private Connection conexion;

    private ConexionDB(){
        try{
            Properties props = new Properties();
            props.load(ConexionDB.class .getResourceAsStream("/config.properties"));
            this.conexion = DriverManager.getConnection(props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
            System.out.println("Conexion exitosa");

        }catch(Exception e){
            System.out.println(" Ha habido un error en la conexion a la base de datos " + e.getMessage());

        }
    }
    public static ConexionDB getInstancia(){
        if(instancia == null)
            instancia = new ConexionDB();

        return instancia;
    }
    public Connection getConexion(){
        return conexion;
    }
}
