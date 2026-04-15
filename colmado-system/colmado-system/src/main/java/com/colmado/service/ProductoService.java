package com.colmado.service;

import com.colmado.dao.ProductoDAO;
import com.colmado.modelo.Producto;
import java.util.List;

public class ProductoService {
    private ProductoDAO dao = new ProductoDAO();

    public boolean guardar(Producto p) {
        if (p.getPrecio() <= 0 || p.getStock() < 0) {
            return false;
        }
        return dao.registrar(p);
    }

    public List<Producto> obtenerTodos() {
        return dao.listarProductos();
    }
}