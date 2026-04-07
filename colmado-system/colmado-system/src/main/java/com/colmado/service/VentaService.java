package com.colmado.service;

import com.colmado.modelo.Venta;
import com.colmado.modelo.DetalleVenta;
import com.colmado.dao.VentaDAO;
import java.util.List;
import java.util.ArrayList;

public class VentaService {
    private List<DetalleVenta> carrito;
    private VentaDAO ventaDAO;


    public VentaService(){
        this.carrito = new ArrayList<>();
        this.ventaDAO = new VentaDAO();
    }

    //Para agregar un producto
    public void agregarProducto(int id_venta, int cantidad, double precioUnit){
        DetalleVenta detalleVenta = new DetalleVenta(id_venta, cantidad, precioUnit);
        carrito.add(detalleVenta);
    }

    // Para eliminar un producto por indice
    public void eliminarProducto(int indice){
        if (indice >= 0 && indice < carrito.size()){
            carrito.remove(indice);
        }
    }

    //Para limpiar el carrito
    public void limpiarCarrito(){
        carrito.clear();
    }

    // Para mostrar nuestro carrito
    public List<DetalleVenta> getCarrito(){
        return carrito;
    }

    //Para sacar total

    public double getTotal(){
        double total = 0.00;

        for(DetalleVenta d: carrito){
            total += d.getSubtotal();
        }
        return total;
    }

    //Confirmar venta

    public boolean confirmarVenta(int id_cliente){
        if (carrito.isEmpty()){
            return false;
        }

        Venta venta = new Venta(id_cliente);
        for (DetalleVenta d: carrito){
            venta.addDetalle(d);
        }

        boolean guardado = ventaDAO.registrarVentas(venta);

        if (guardado){
            // Aqui aplicaremos un procedimiento para conectar con los productos que entregara Anderson
            // y de ahi manipulamos el stock
            limpiarCarrito();
        }
        return guardado;

    }

}
