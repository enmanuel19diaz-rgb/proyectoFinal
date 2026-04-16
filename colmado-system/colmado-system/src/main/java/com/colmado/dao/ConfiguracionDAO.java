package com.colmado.dao;

import com.colmado.modelo.Configuracion;
import com.colmado.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDAO {

    // 1. Obtenemos tu conexión Singleton una sola vez
    private Connection con = ConexionDB.getInstancia().getConexion();

    public Configuracion obtenerConfiguracion() {
        Configuracion config = null;
        String sql = "SELECT * FROM configuracion WHERE id = 1";

        // 2. Solo cerramos el Statement y el ResultSet para liberar memoria,
        // pero MANTENEMOS la conexión viva para futuras consultas.
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                config = new Configuracion();
                config.setId(rs.getInt("id"));
                config.setNombre(rs.getString("nombre_negocio"));
                config.setRnc(rs.getString("rnc"));
                config.setDireccion(rs.getString("direccion"));
                config.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener configuración: " + e.getMessage());
        }
        return config;
    }

    public boolean actualizarConfiguracion(Configuracion config) {
        String sql = "UPDATE configuracion SET nombre_negocio=?, rnc=?, direccion=?, telefono=? WHERE id=1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, config.getNombre());
            ps.setString(2, config.getRnc());
            ps.setString(3, config.getDireccion());
            ps.setString(4, config.getTelefono());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar configuración: " + e.getMessage());
            return false;
        }
    }
}