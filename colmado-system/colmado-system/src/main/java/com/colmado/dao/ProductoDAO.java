package com.colmado.dao;

import com.colmado.modelo.Producto;
import com.colmado.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean agregar(Producto p) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad, descripcion, categoria) VALUES (?,?,?,?,?)";
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getDescripcion());
            ps.setString(5, p.getCategoria());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId_producto(rs.getInt(1));
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, precio=?, cantidad=?, descripcion=?, categoria=? WHERE id_producto=?";
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getDescripcion());
            ps.setString(5, p.getCategoria());
            ps.setInt(6, p.getId_producto());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto=?";
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("descripcion"),
                        rs.getString("categoria")
                );
                p.setId_producto(rs.getInt("id_producto"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        }
        return lista;
    }

    public Producto obtenerPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto(
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("descripcion"),
                        rs.getString("categoria")
                );
                p.setId_producto(rs.getInt("id_producto"));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }
}