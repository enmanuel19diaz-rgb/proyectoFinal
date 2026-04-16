package com.colmado.service;

import com.colmado.dao.ProductoDAO;
import com.colmado.modelo.Producto;
import java.util.List;

public class InventarioService {

    private ProductoDAO productoDAO;

    public InventarioService() {
        this.productoDAO = new ProductoDAO();
    }

    // --- MÉTODOS QUE YA TENÍAS (Mantenidos) ---

    public boolean descontarStock(int idProducto, int cantidad) {
        Producto p = productoDAO.obtenerPorId(idProducto);
        if (p == null || p.getCantidad() < cantidad) return false;

        p.setCantidad(p.getCantidad() - cantidad);
        return productoDAO.actualizar(p);
    }

    public boolean hayStock(int idProducto, int cantidad) {
        Producto p = productoDAO.obtenerPorId(idProducto);
        return p != null && p.getCantidad() >= cantidad;
    }

    public boolean agregarStock(int idProducto, int cantidad) {
        Producto p = productoDAO.obtenerPorId(idProducto);
        if (p == null) return false;
        p.setCantidad(p.getCantidad() + cantidad);
        return productoDAO.actualizar(p);
    }

    // --- NUEVOS MÉTODOS PARA CONEXIÓN TOTAL ---

    public boolean registrarProductoNuevo(Producto p) {
        return productoDAO.agregar(p);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodos();
    }
}