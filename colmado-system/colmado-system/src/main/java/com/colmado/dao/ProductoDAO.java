package com.colmado.dao;

import com.colmado.modelo.Producto;
import com.colmado.modelo.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    Connection con;
    Conexion conectar = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

  
    public boolean registrar(Producto prod) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, precio, stock) VALUES (?,?,?,?,?)";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, prod.getCodigo());
            ps.setString(2, prod.getNombre());
            ps.setString(3, prod.getCategoria());
            ps.setDouble(4, prod.getPrecio());
            ps.setInt(5, prod.getStock());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }


    public List<Producto> listarProductos() {
        List<Producto> listaProd = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto prod = new Producto();
                prod.setId(rs.getInt("id"));
                prod.setCodigo(rs.getString("codigo"));
                prod.setNombre(rs.getString("nombre"));
                prod.setCategoria(rs.getString("categoria"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setStock(rs.getInt("stock"));
                listaProd.add(prod);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.toString());
        }
        return listaProd;
    }
}