package com.colmado.modelo;

public class DetalleVenta {
    private int id_detalle;
    private int id_venta;
    private int id_producto;
    private int cantidad;
    private double precioUnit;

    public DetalleVenta(int id_producto, int cantidad, double precioUnit) {
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
    }

    // Setters necesarios para la lógica de negocio
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setId_venta(int id_venta) { this.id_venta = id_venta; }
    public void setId_detalle(int id_detalle) { this.id_detalle = id_detalle; }

    // Getters
    public int getId_producto() { return id_producto; }
    public int getCantidad()    { return cantidad; }
    public double getPrecioUnit() { return precioUnit; }
    public double getSubtotal()  { return cantidad * precioUnit; }
    public int getId_venta()    { return id_venta; }
}