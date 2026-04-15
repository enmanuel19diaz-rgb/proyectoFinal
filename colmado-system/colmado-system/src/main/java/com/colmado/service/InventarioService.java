package com.colmado.service;

import com.colmado.dao.ProductoDAO;
import com.colmado.modelo.Producto;
import java.util.List;

public class InventarioService {
    private ProductoDAO proDao = new ProductoDAO();

    public void controlStock() {
        List<Producto> lista = proDao.listarProductos();
        for (Producto p : lista) {
            if (p.getStock() <= 5) {
                System.out.println("Alerta: Stock bajo en " + p.getNombre());
            }
        }
    }
}