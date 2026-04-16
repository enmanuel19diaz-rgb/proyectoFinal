package com.colmado.service;

import com.colmado.modelo.*;
import com.colmado.dao.VentaDAO;
import java.util.ArrayList;
import java.util.List;

public class VentaService {
    private List<DetalleVenta> carrito;
    private final VentaDAO ventaDAO;
    private final InventarioService inventarioService;

    public VentaService() {
        this.carrito = new ArrayList<>();
        this.ventaDAO = new VentaDAO();
        this.inventarioService = new InventarioService();
    }

    public void agregarProducto(int id, int cant, double precio) {
        for (DetalleVenta d : carrito) {
            if (d.getId_producto() == id) {
                d.setCantidad(d.getCantidad() + cant);
                return;
            }
        }
        carrito.add(new DetalleVenta(id, cant, precio));
    }

    public double getTotal() {
        return carrito.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    }

    public Venta confirmarVenta(int id_cliente) {
        if (carrito.isEmpty()) return null;

        Venta venta = new Venta(id_cliente);
        for (DetalleVenta d : carrito) {
            venta.addDetalle(d);
        }

        if (ventaDAO.registrarVentas(venta)) {
            // Descontamos usando el service de Anderson
            for (DetalleVenta d : carrito) {
                inventarioService.descontarStock(d.getId_producto(), d.getCantidad());
            }
            carrito.clear();
            return venta;
        }
        return null;
    }

    public List<DetalleVenta> getCarrito() { return carrito; }
}